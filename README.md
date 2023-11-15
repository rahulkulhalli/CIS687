# WhereMyShuttleAt

Our implementation for the CIS 687 Project

<hr/>

## Overview
The use-case for this project is to design a real-time university shuttle simulator. The following are the assumptions:
- There will only be one active shuttle
- The shuttle travels at a constant speed of 18 mph (can be configured via an API)
- A maximum of 30 students can board the shuttle (can be configured via an API)
- The shuttle waits for *min(15 minutes, time to fill the shuttle)* before departing
- The shuttle drops every student at their given address. There is no preconfigured route that the shuttle takes
- The shuttle operates on a daily schedule
- If a student inside the shuttle invokes the /ETA endpoint, they will receive the estimated time to reach their address. This is calculated as the sum of the ETAs of all the students who boarded before this student and the ETA from the last student's address to this student's address
- If a student outside the shuttle invokes the /ETA endpoint, they will receive the estimated time that the shuttle will require to drop all the current students and travel back to the shuttle stop
- The location interpolation is performed using arithmetic interpolation, without considering the curvature of the earth.
- Distances are designed as crow-flight distances, i.e., point-to-point distances. These distances consider the curvature of the earth and are calculated using the [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula).

<hr/>

## Implementation

This project is implemented completely in Java Spring-Boot. The backend used a MySQL database. This project has NOT been designed to be deployed in higher environments. However, best practices are followed.

<hr/>

## Setup
1. To set this code up locally, clone the repository:
`git clone git@github.com:rahulkulhalli/CIS687.git`
2. Next, ensure you have MySQL installed. You may download that from [here](https://dev.mysql.com/downloads/)
3. Once MySQL is set up, create a new database called `shuttle_db`. Ensure that `root` has access to this database
4. Navigate to `src/main/resources/application.properties` and change the `spring.datasource.username` and
   `spring.datasource.password` properties to your credentials
5. Open the project in IntelliJ and build Maven to install the dependencies
6. If everything works as expected, running the `Cis687Application.java` file should start the server!
<hr />

### Project Members:
- Rahul Kulhalli
- Aman Velani
- Manav Nisar
- Neeraj Patil

<hr/>

### TODOs:
- ~Fix the bug in the ETA calculation~
- ~Write unit tests~
- ~Incorporate Spring Security~ [Aman]
- ~Fix the location db bug~
- Add code docs
- Create the presentation
