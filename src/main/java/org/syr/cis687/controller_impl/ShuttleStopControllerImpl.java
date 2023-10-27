package org.syr.cis687.controller_impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.syr.cis687.controller.ShuttleStopController;
import org.syr.cis687.enums.OpType;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.models.ShuttleStop;
import org.syr.cis687.service_impl.ShuttleStopServiceImpl;
import org.syr.cis687.utils.CommonUtils;

@Controller
@RequestMapping(path = "/shuttleStop")
public class ShuttleStopControllerImpl implements ShuttleStopController {

    private final ShuttleStopServiceImpl service;

    public ShuttleStopControllerImpl(ShuttleStopServiceImpl impl) {
        this.service = impl;
    }

    @Override
    @PostMapping(path = "/addShuttleStop")
    public ResponseEntity<ApiResponse> addShuttleStop(@RequestBody ShuttleStop stop) {
        return CommonUtils.validateAndReturn(this.service.addShuttleStop(stop), OpType.INSERT);
    }

    @Override
    @GetMapping(path = "/getShuttleStop")
    public ResponseEntity<ApiResponse> getShuttleStop() {
        return CommonUtils.validateAndReturn(this.service.getShuttleStop(), OpType.FIND_ONE);
    }

    @Override
    @DeleteMapping(path = "/deleteShuttleStop")
    public ResponseEntity<ApiResponse> deleteShuttleStop(Long id) {
        return CommonUtils.validateAndReturn(this.service.deleteShuttleStop(id), OpType.DELETE);
    }

    @Override
    @PutMapping(path = "/updateShuttleStop")
    public ResponseEntity<ApiResponse> updateShuttleStop(Long id, ShuttleStop stop) {
        return CommonUtils.validateAndReturn(this.service.updateShuttleStop(id, stop), OpType.UPDATE);
    }
}
