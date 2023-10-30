package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.Location;
import org.syr.cis687.repository.LocationRepository;
import org.syr.cis687.service.LocationService;
import org.syr.cis687.utils.CommonUtils;

import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository repository;
    @Override
    public List<Location> getAllLocations() {
        return CommonUtils.convertIterableToList(repository.findAll());
    }

    @Override
    public Location addLocation(Location location) {
        return repository.save(location);
    }

    @Override
    public Location updateLocationById(Long id, Location location) {
        Optional<Location> optionalLoc = getLocationById(id);
        if (optionalLoc.isPresent()) {
            Location loc = optionalLoc.get();
            loc.setLatitude(location.getLatitude());
            loc.setLongitude(location.getLongitude());
//            loc.setLastUpdated(location.getLastUpdated());

            // persist to db.
            return addLocation(loc);
        }

        return null;
    }

    @Override
    public boolean deleteLocationById(Long id) {
        try {
            return this.repository.deleteByIdAndReturnCount(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<Location> getLocationById(Long id) {
        return repository.findById(id);
    }
}
