package org.syr.cis687.service;

import org.syr.cis687.models.Shuttle;

import java.util.List;
import java.util.Optional;

public interface ShuttleService {
    public List<Shuttle> getAllShuttles();
    public Optional<Shuttle> getShuttleById(Long id);
    public Shuttle addShuttle(Shuttle shuttle);
    public Shuttle updateShuttle(Long id, Shuttle shuttle);
    public boolean deleteShuttle(Long id);
}
