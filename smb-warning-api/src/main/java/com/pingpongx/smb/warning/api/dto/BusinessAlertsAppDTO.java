package com.pingpongx.smb.warning.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessAlertsAppDTO implements Serializable {
    private static final long serialVersionUID = 6255807400155015389L;

    private String appName;

    private String classify;
}
