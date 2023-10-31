package org.syr.cis687.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.syr.cis687.models.ShuttleStop;
import org.syr.cis687.repository.ShuttleStopRepository;
import org.syr.cis687.service.ShuttleStopService;

import java.util.Optional;

@Service
public class ShuttleStopServiceImpl implements ShuttleStopService {

    @Autowired
    private ShuttleStopRepository repository;

    @Override
    public ShuttleStop getShuttleStop() {
        return repository.findById(1L).orElse(null);
    }

    @Override
    public ShuttleStop addShuttleStop(ShuttleStop stop) {
        return repository.save(stop);
    }

    @Override
    public Boolean deleteShuttleStop(Long id) {
        return this.repository.deleteByIdAndReturnCount(id) > 0;
    }

    @Override
    public ShuttleStop updateShuttleStop(Long id, ShuttleStop shuttleStop) {
        // Check if this id exists.
        if (!this.repository.existsById(id)) {
            return null;
        }

        Optional<ShuttleStop> optStop = this.repository.findById(id);

        if (optStop.isEmpty()) {
            return null;
        }

        ShuttleStop stop = optStop.get();

        // Update attributes.
        stop.setShuttleStopLocation(shuttleStop.getShuttleStopLocation());

        // Persist in db.
        return addShuttleStop(stop);
    }
}
