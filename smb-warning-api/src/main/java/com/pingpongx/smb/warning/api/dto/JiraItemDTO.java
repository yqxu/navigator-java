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
public class JiraItemDTO implements Serializable {
    private static final long serialVersionUID = 6852934966747107253L;

    private String field;

    private String toString;
}
