package org.syr.cis687.controller_impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.syr.cis687.controller.ETAController;
import org.syr.cis687.models.ApiResponse;
import org.syr.cis687.service_impl.ETAServiceImpl;
import org.syr.cis687.utils.CommonUtils;

@Controller
@RequestMapping(path = "/ETA")
public class ETAControllerImpl implements ETAController {

    private final ETAServiceImpl service;

    public ETAControllerImpl(ETAServiceImpl impl) {
        this.service = impl;
    }

    @GetMapping(path = "/getETA")
    @Override
    public ResponseEntity<ApiResponse> calculateETA(@RequestParam String SUID) {
        if (SUID == null) {
            return CommonUtils.getBadResponse(null, "Input SUID is NULL!");
        }

        return this.service.calculateETA(SUID);
    }
}
