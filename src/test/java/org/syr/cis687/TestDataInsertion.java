package org.syr.cis687;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.syr.cis687.models.DriverDetails;
import org.syr.cis687.repository.*;

@Component
public class TestDataInsertion implements CommandLineRunner {

    // Declare the repositories.
    private final DriverRepository driverRepository;
    private final StudentRepository studentRepository;
    private final EmergencyDetailsRepository detailsRepository;
    private final ShuttleRepository shuttleRepository;
    private final LocationRepository locationRepository;

    private final PassengerRepository passengerRepository;

    @Autowired
    public TestDataInsertion(
            DriverRepository d,
            StudentRepository s,
            EmergencyDetailsRepository e,
            ShuttleRepository sh,
            LocationRepository l,
            PassengerRepository p
    ) {
        this.driverRepository = d;
        this.studentRepository = s;
        this.detailsRepository = e;
        this.shuttleRepository = sh;
        this.locationRepository = l;
        this.passengerRepository = p;
    }

    @Override
    public void run(String... args) throws Exception {
        // Simple DriverDetails.
        DriverDetails d = new DriverDetails();
        d.setId(10L);
        d.setFirstName("Test1");
        d.setLastName("LN1");
        d.setContactNumber("+1777222");
        d.setOrgId("XYZ123");
        d.setEmailId("xyz@abc.edu");

        this.driverRepository.save(d);
    }
}
