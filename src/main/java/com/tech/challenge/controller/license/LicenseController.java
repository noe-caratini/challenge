package com.tech.challenge.controller.license;

import com.tech.challenge.model.licensetype.LicenseType;
import com.tech.challenge.service.LicenseService;
import com.tech.challenge.service.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/license")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/register")
    public ResponseEntity registerLicense(@RequestBody RegisterLicenseRequest request) {
        if (request.getCustomerId() == null ||
                request.getLicensedContentId() == null ||
                StringUtils.isBlank(request.getLicenseType())) {
            return ResponseEntity.badRequest().build();
        }

        try {
            LicenseType licenseType = LicenseType.fromCode(request.getLicenseType());

            licenseService.save(request.getCustomerId(), licenseType, request.getLicensedContentId());

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
