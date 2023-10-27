package org.syr.cis687.utils;

import org.syr.cis687.models.ETA;
import org.syr.cis687.models.Location;

import java.sql.Time;

public class LocationUtils {

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
