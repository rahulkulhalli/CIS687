package org.syr.cis687.utils;

import org.syr.cis687.models.ETA;
import org.syr.cis687.models.Location;

import java.sql.Time;

public class LocationUtils {

    public static final int EARTH_RADIUS = 6371000; // Radius of the Earth in meters
    private static final double MILE_SCALE = 0.00062137273;

    public static double calculateHaversineDistance(Location location1, Location location2) {
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        // Haversine formula
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c * MILE_SCALE;
    }

    public static Location interpolate(Location startLocation, Location endLocation, double fraction) {

        // Clip fraction to (0., 1.)
        if (fraction < 0) {
            fraction = 0;
        } else if (fraction > 1) {
            fraction = 1;
        }

        double lat1 = Math.toRadians(startLocation.getLatitude());
        double lon1 = Math.toRadians(startLocation.getLongitude());
        double lat2 = Math.toRadians(endLocation.getLatitude());
        double lon2 = Math.toRadians(endLocation.getLongitude());

        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c * MILE_SCALE;
        double radius_scaled = EARTH_RADIUS * MILE_SCALE;

        double bearing = Math.atan2(Math.sin(deltaLon) * Math.cos(lat2), Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon));

        double interpolatedDistance = fraction * distance;

        double newLat = Math.asin(Math.sin(lat1) * Math.cos(interpolatedDistance
                / radius_scaled) + Math.cos(lat1)
                * Math.sin(interpolatedDistance / radius_scaled) * Math.cos(bearing));

        double newLon = lon1 + Math.atan2(Math.sin(bearing)
                * Math.sin(interpolatedDistance / radius_scaled) * Math.cos(lat1),
                Math.cos(interpolatedDistance / radius_scaled) - Math.sin(lat1) * Math.sin(newLat));

        Location returnLocation = new Location();
        returnLocation.setLatitude(Math.toDegrees(newLat));
        returnLocation.setLongitude(Math.toDegrees(newLon));

        return returnLocation;
    }

    public static final ETABuilder ETA = new ETABuilder();

    public static class ETABuilder {
        private Location from;
        private Location to;
        private Time departedTime = null;

        public ETABuilder from(Location from) {
            this.from = from;
            return this;
        }

        public ETABuilder to(Location to) {
            this.to = to;
            return this;
        }

        public ETABuilder withDepartedTime(Time departedTime) {
            this.departedTime = departedTime;
            return this;
        }

        public ETA calculate() {

            StringBuilder urlBuilder = new StringBuilder()
                    .append("https://maps.googleapis.com/maps/api/distancematrix/json")
                    .append("?destinations=")
                    .append(this.to.getLatitude())
                    .append(",")
                    .append(this.to.getLongitude())
                    .append("&origins=")
                    .append(this.from.getLatitude())
                    .append(",")
                    .append(this.from.getLongitude())
                    .append("&units=imperial");

            // Only add departedTime if it is valid.
            if (this.departedTime != null) {
                urlBuilder.append("&departure_time=")
                        .append(CommonUtils.convertTimeToEpoch(this.departedTime));
            }

            // Finally, add the key.
            urlBuilder.append("&key=")
                    .append(CommonUtils.getApiKey());

            // Build the string.
            String url = urlBuilder.toString();

            // Offload the work to the HttpUtils and return the parsed ETA Object.
            return HttpUtils.makePostRequest(url);
        }
    }
}
