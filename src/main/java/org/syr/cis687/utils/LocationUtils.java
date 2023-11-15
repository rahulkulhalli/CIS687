package org.syr.cis687.utils;

import org.syr.cis687.models.ETA;
import org.syr.cis687.models.Location;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.models.Student;

import java.util.List;

public class LocationUtils {

    // Earth's radius in miles.
    public static final double EARTH_RADIUS = 3959.0; // Radius of the Earth in miles

    /**
     * A utility function to calculate the Haversine distance between two LatLng coordinates.
     * Considers the earth's curvature while computing the distance.
     * @param location1 Location A
     * @param location2 Location B
     * @return Distance(A, B)
     */
    public static double calculateHaversineDistance(Location location1, Location location2) {
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    /**
     * A utility function to perform arithmetic interpolation between two locations.
     * @param start The start location
     * @param end The destination
     * @param fraction Fraction of distance traveled
     * @return A new `Location` object with the interpolated coordinates.
     */
    public static Location interpolate(Location start, Location end, double fraction) {
        if (fraction > 1.0) {
            fraction = 1.0;
        }

        double lat = start.getLatitude() + (end.getLatitude() - start.getLatitude()) * fraction;
        double lng = start.getLongitude() + (end.getLongitude() - start.getLongitude()) * fraction;

        Location newLocation = new Location();
        newLocation.setLatitude(lat);
        newLocation.setLongitude(lng);

        return newLocation;
    }

    public static boolean isLocationClose(Location location1, Location location2) {
        // Define a tolerance for comparing double values.
        double tolerance = 0.0001;
        return calculateHaversineDistance(location1, location2) < tolerance;
    }

    public static final ETABuilder ETA_BUILDER = new ETABuilder();

    public static class ETABuilder {
        private Shuttle shuttle;
        private Student student;
        private Location stopLocation;

        public ETABuilder withShuttle(Shuttle shuttle) {
            this.shuttle = shuttle;
            return this;
        }

        public ETABuilder forStudent(Student student) {
            this.student = student;
            return this;
        }

        public ETABuilder withShuttleStopLocation(Location location) {
            this.stopLocation = location;
            return this;
        }

        public ETA calculate() {

            ETA etaObj = new ETA();

            List<Student> passengers = this.shuttle.getPassengerList();

            // If there are no passengers on the shuttle.
            if (passengers.isEmpty()) {
                if (this.shuttle.getHasDepartedFromStop()) {
                    double distance = calculateHaversineDistance(
                            this.shuttle.getCurrentLocation(),
                            this.stopLocation
                    );
                    double time = (distance / this.shuttle.getCurrentSpeed()) * 60.0;
                    etaObj.setEstimatedTime(String.format("%f minutes", time));
                    etaObj.setEstimatedDistance(String.format("%f miles", distance));
                } else {
                    etaObj.setEstimatedDistance("Shuttle is on the stop");
                    etaObj.setEstimatedTime("Shuttle is on the stop");
                }
                return etaObj;
            }

            // Determine if the student is in the shuttle.
            if (CommonUtils.isStudentOnShuttle(this.shuttle, this.student.getOrgId())) {
                // how many people before this student?
                double distanceToBeCovered = 0.0;
                double timeRequired = 0.0;

                // Check if the student is at the beginning.
                if (passengers.get(0).getOrgId().equals(this.student.getOrgId())) {
                    // compute the distance from the shuttle to the address and break.
                    double distance = calculateHaversineDistance(
                            this.shuttle.getCurrentLocation(), this.student.getAddress()
                    );

                    // Estimated time = distance/speed (convert to minutes)
                    double time = (distance / this.shuttle.getCurrentSpeed()) * 60;

                    etaObj.setEstimatedTime(String.format("%f minutes", time));
                    etaObj.setEstimatedDistance(String.format("%f miles", distance));
                    return etaObj;
                }

                // If student is [s1, s2, s3, THIS],
                // calculate distance(shuttle, s1) + distance(s1, s2) + distance(s2, s3) + distance(s3, THIS)
                int studentIndex = CommonUtils.getStudentIndex(this.shuttle, this.student.getOrgId());

                /*
                Example: This is the 4th student.
                Calculate (shuttle-0) + (0-1) + (1-2) + (2-3) + (3-4)
                 */

                // This gives you shuttle -> 1st student distance.
                distanceToBeCovered += calculateHaversineDistance(
                        this.shuttle.getCurrentLocation(),
                        passengers.get(0).getAddress()
                );

                // i == 1??
                for (int i = 1; i <= studentIndex; i++) {
                    Student trailing = passengers.get(i);
                    Student current = passengers.get(i-1);

                    distanceToBeCovered += calculateHaversineDistance(
                            trailing.getAddress(), current.getAddress()
                    );
                }

                timeRequired = (distanceToBeCovered / this.shuttle.getCurrentSpeed()) * 60;
                etaObj.setEstimatedTime(String.format("%f minutes", timeRequired));
                etaObj.setEstimatedDistance(String.format("%f miles", distanceToBeCovered));

            } else {
                // iterate over ALL Students in the shuttle and sum their pairwise distance
                // and add the distance from the last student's address to the shuttle stop.
                double distance = 0.0;

                for (int pointer = 1; pointer < passengers.size(); pointer++) {
                    Student trailingStudent = passengers.get(pointer-1);
                    Student currentStudent = passengers.get(pointer);

                    // compute pairwise distance.
                    distance += calculateHaversineDistance(
                            trailingStudent.getAddress(), currentStudent.getAddress()
                    );
                }

                // lastly, distance from last student to shuttle stop.
                distance += calculateHaversineDistance(
                        passengers.get(passengers.size()-1).getAddress(),
                        this.stopLocation
                );

                double time = (distance / this.shuttle.getCurrentSpeed()) * 60;
                etaObj.setEstimatedDistance(String.format("%f miles", distance));
                etaObj.setEstimatedTime(String.format("%f minutes", time));
            }

            return etaObj;
        }
    }
}
