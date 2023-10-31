# CIS687

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

<hr/>

## Implementation

This project is implemented completely in Java Spring-Boot. The backend used a MySQL database. This project has NOT been designed to be deployed in higher environments. However, best practices are followed.

<hr/>

### Project Members:
- Rahul Kulhalli
- Aman Velani
- Manav
- Neeraj

<hr/>

### TODOs:
- Fix the bug in the ETA calculation
- Write unit tests
- Incorporate Spring Security
- Fix the location db bug
- Add code docs
- Create the presentation
- Publish the Postman API collection here