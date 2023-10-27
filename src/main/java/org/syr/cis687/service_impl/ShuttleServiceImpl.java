package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.repository.ShuttleRepository;
import org.syr.cis687.service.ShuttleService;
import org.syr.cis687.utils.CommonUtils;

import java.util.List;
import java.util.Optional;

@Service
public class ShuttleServiceImpl implements ShuttleService {

    @Autowired
    private ShuttleRepository repository;
    @Override
    public List<Shuttle> getAllShuttles() {
        return CommonUtils.convertIterableToList(repository.findAll());
    }

    @Override
    public Optional<Shuttle> getShuttleById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Shuttle addShuttle(Shuttle shuttle) {
        // first, check if a shuttle already exists.
        Shuttle dbShuttle = this.repository.findById(1L).orElse(null);

        if (dbShuttle != null) {
            // This won't do.
            return null;
        }

        return repository.save(shuttle);
    }

    @Override
    public Shuttle updateShuttle(Long id, Shuttle shuttle) {
        if (!repository.existsById(id)) {
            return null;
        }

        Optional<Shuttle> optShuttle = getShuttleById(id);
        if (optShuttle.isEmpty()) {
            return null;
        }

        Shuttle dbShuttleObj = optShuttle.get();

        dbShuttleObj.setAlias(shuttle.getAlias());
        dbShuttleObj.setMaxCapacity(shuttle.getMaxCapacity());
        dbShuttleObj.setCurrentCapacity(shuttle.getCurrentCapacity());
        dbShuttleObj.setCurrentLocation(shuttle.getCurrentLocation());
        dbShuttleObj.setOperatingState(shuttle.getOperatingState());
        dbShuttleObj.setCurrentState(shuttle.getCurrentState());
        dbShuttleObj.setPassengerList(shuttle.getPassengerList());

        // persist in db.
        addShuttle(dbShuttleObj);

        return dbShuttleObj;
    }

    @Override
    public boolean deleteShuttle(Long id) {
        try {
            return this.repository.deleteByIdAndReturnCount(id) > 0;
        } catch (Exception e) {
            return false;
        }
    }
}