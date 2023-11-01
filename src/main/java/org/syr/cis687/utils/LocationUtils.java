package org.syr.cis687.utils;

import org.syr.cis687.models.ETA;
import org.syr.cis687.models.Location;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.models.Student;

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
        if (fraction >= 1.0) {
            return endLocation;
        }

        double lat1 = Math.toRadians(startLocation.getLatitude());
        double lon1 = Math.toRadians(startLocation.getLongitude());
        double lat2 = Math.toRadians(endLocation.getLatitude());
        double lon2 = Math.toRadians(endLocation.getLongitude());

        double dLon = lon2 - lon1;

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);

        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2),
                Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        lat3 = Math.toDegrees(lat3);
        lon3 = Math.toDegrees(lon3);

        Location location = new Location();
        location.setLatitude(lat3);
        location.setLongitude(lon3);

        return location;
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

            // Determine if the student is in the shuttle.
            if (CommonUtils.isStudentOnShuttle(this.shuttle, this.student.getOrgId())) {
                // how many people before this student?
                double distanceToBeCovered = 0.0;
                double timeRequired = 0.0;

                // Check if the student is at the beginning.
                if (this.shuttle.getPassengerList().get(0).getOrgId().equals(this.student.getOrgId())) {
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

                for (int i = 0; i < this.shuttle.getPassengerList().size(); i++) {
                    Student currStudent = this.shuttle.getPassengerList().get(i);

                    // We know the target student is not at the first position for sure.
                    if (i == 0) {
                        // distance(shuttle, currStudent)
                        distanceToBeCovered = calculateHaversineDistance(
                                this.shuttle.getCurrentLocation(), currStudent.getAddress()
                        );
                    } else {
                        Student previousStudent = this.shuttle.getPassengerList().get(i-1);
                        distanceToBeCovered += calculateHaversineDistance(
                                currStudent.getAddress(),  previousStudent.getAddress()
                        );

                        if (currStudent.getOrgId().equals(this.student.getOrgId())) {
                            break;
                        }
                    }
                }

                timeRequired = (distanceToBeCovered / this.shuttle.getCurrentSpeed()) * 60;
                etaObj.setEstimatedTime(String.format("%f minutes", timeRequired));
                etaObj.setEstimatedDistance(String.format("%f miles", distanceToBeCovered));

            } else {
                // iterate over ALL Students in the shuttle and sum their pairwise distance
                // and add the distance from the last student's address to the shuttle stop.
                double distance = 0.0;
                int trailing = 0;

                for (int pointer = 1; pointer < this.shuttle.getPassengerList().size(); pointer++) {
                    Student trailingStudent = this.shuttle.getPassengerList().get(trailing);
                    Student currentStudent = this.shuttle.getPassengerList().get(pointer);

                    // compute pairwise distance.
                    distance += calculateHaversineDistance(trailingStudent.getAddress(), currentStudent.getAddress());
                    trailing++;
                }

                // lastly, distance from last student to shuttle stop.
                distance += calculateHaversineDistance(
                        this.shuttle.getPassengerList().get(this.shuttle.getPassengerList().size()-1).getAddress(),
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
