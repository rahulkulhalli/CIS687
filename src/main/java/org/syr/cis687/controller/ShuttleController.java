package org.syr.cis687.controller;

import org.springframework.web.bind.annotation.*;
import org.syr.cis687.models.Shuttle;

import java.util.List;

public interface ShuttleController {
    @GetMapping(path = "/getAllShuttles")
    public List<Shuttle> getAllShuttles();

    @GetMapping(path = "/getShuttleById/{id}")
    public Shuttle getShuttleById(@PathVariable("id") Long id);

    @PostMapping(path = "/addShuttle")
    public Shuttle addShuttle(@RequestBody Shuttle shuttle);

    @PutMapping(path = "/updateShuttle/{id}")
    public Shuttle updateShuttleById(@PathVariable("id") Long id, @RequestBody Shuttle shuttle);

    @DeleteMapping(path = "/deleteShuttle/{id}")
    public boolean deleteShuttleById(@PathVariable("id") Long id);
}
