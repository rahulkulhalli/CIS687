package org.syr.cis687.service;

import org.syr.cis687.models.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    List<Location> getAllLocations();
    Location addLocation(Location location);
    Location updateLocationById(Long id, Location location);
    boolean deleteLocationById(Long id);
    Optional<Location> getLocationById(Long id);
}
