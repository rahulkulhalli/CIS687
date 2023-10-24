package org.syr.cis687.controller_impl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.syr.cis687.controller.ShuttleController;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.service_impl.ShuttleServiceImpl;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/shuttle")
public class ShuttleControllerImpl implements ShuttleController {

    private final ShuttleServiceImpl shuttleService;

    public ShuttleControllerImpl(ShuttleServiceImpl impl) {
        this.shuttleService = impl;
    }
    @Override
    public List<Shuttle> getAllShuttles() {
        return this.shuttleService.getAllShuttles();
    }

    @Override
    public Shuttle getShuttleById(Long id) {
        Optional<Shuttle> shuttle = this.shuttleService.getShuttleById(id);
        return shuttle.orElse(null);
    }

    @Override
    public Shuttle addShuttle(Shuttle shuttle) {
        return this.shuttleService.addShuttle(shuttle);
    }

    @Override
    public Shuttle updateShuttleById(Long id, Shuttle shuttle) {
        return this.shuttleService.updateShuttle(id, shuttle);
    }

    @Override
    public boolean deleteShuttleById(Long id) {
        return this.shuttleService.deleteShuttle(id);
    }
}
