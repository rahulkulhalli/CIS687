package org.syr.cis687.controller_impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.ShuttleController;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.Shuttle;
import org.syr.cis687.service_impl.ShuttleServiceImpl;
import org.syr.cis687.utils.CommonUtils;

import java.util.Optional;

@Controller
@RequestMapping(path = "/shuttle")
public class ShuttleControllerImpl implements ShuttleController {

    private final ShuttleServiceImpl shuttleService;

    public ShuttleControllerImpl(ShuttleServiceImpl impl) {
        this.shuttleService = impl;
    }

    @Override
    @GetMapping(path = "/getAllShuttles")
    public ResponseEntity<ApiResponse> getAllShuttles() {
        return CommonUtils.validateAndReturn(this.shuttleService.getAllShuttles(), OpType.FIND_ALL);
    }

    @Override
    @GetMapping(path = "/getShuttleById")
    public ResponseEntity<ApiResponse> getShuttleById(@RequestParam Long id) {
        Optional<Shuttle> shuttle = this.shuttleService.getShuttleById(id);
        if (shuttle.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return CommonUtils.validateAndReturn(shuttle.get(), OpType.FIND_ONE);
    }

    @Override
    @PostMapping(path = "/addShuttle")
    public ResponseEntity<ApiResponse> addShuttle(@RequestBody Shuttle shuttle) {
        return CommonUtils.validateAndReturn(this.shuttleService.addShuttle(shuttle), OpType.INSERT);
    }

    @Override
    @PutMapping(path = "/updateShuttle")
    public ResponseEntity<ApiResponse> updateShuttleById(@RequestParam Long id, @RequestBody Shuttle shuttle) {
        return CommonUtils.validateAndReturn(this.shuttleService.updateShuttle(id, shuttle), OpType.UPDATE);
    }

    @Override
    @DeleteMapping(path = "/deleteShuttle")
    public ResponseEntity<ApiResponse> deleteShuttleById(@RequestParam Long id) {
        return CommonUtils.validateAndReturn(this.shuttleService.deleteShuttle(id), OpType.DELETE);
    }
}