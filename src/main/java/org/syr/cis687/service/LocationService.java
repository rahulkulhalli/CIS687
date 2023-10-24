package org.syr.cis687.service;

import org.syr.cis687.models.Location;

import java.util.List;
import java.util.Optional;

public interface LocationService {
    public List<Location> getAllLocations();
    public Location addLocation(Location location);
    public Location updateLocationById(Long id, Location location);
    public boolean deleteLocationById(Long id);
    public Optional<Location> getLocationById(Long id);
}
