package org.syr.cis687.service;

import org.syr.cis687.models.ShuttleStop;

public interface ShuttleStopService {

    // For now - we only have one shuttle stop.
    ShuttleStop getShuttleStop();

    ShuttleStop addShuttleStop(ShuttleStop shuttle);

    Boolean deleteShuttleStop(Long id);

    ShuttleStop updateShuttleStop(Long id, ShuttleStop shuttleStop);
}
