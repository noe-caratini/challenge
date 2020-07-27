package com.tech.challenge.controller.license;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterLicenseRequest {
    private Long customerId;
    private String licenseType;
    private Long licensedContentId;
}
