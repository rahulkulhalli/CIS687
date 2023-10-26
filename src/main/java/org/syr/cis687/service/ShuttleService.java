package org.syr.cis687.service;

import org.syr.cis687.models.Shuttle;

import java.util.List;
import java.util.Optional;

public interface ShuttleService {
    List<Shuttle> getAllShuttles();
    Optional<Shuttle> getShuttleById(Long id);
    Shuttle addShuttle(Shuttle shuttle);
    Shuttle updateShuttle(Long id, Shuttle shuttle);
    boolean deleteShuttle(Long id);
}
