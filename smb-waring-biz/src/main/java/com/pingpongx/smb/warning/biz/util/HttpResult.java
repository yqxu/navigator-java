package com.pingpongx.smb.warning.biz.util;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpResult implements Serializable {
    private static final long serialVersionUID = 5601691471536258573L;

    private int code;

    private String body;
}
