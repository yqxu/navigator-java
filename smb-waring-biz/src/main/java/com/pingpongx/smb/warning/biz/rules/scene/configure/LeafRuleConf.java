package com.pingpongx.smb.warning.biz.rules.scene.configure;

import lombok.Data;

import java.io.Serializable;

@Data
public class LeafRuleConf implements Serializable {
    String type;
    String attr;
    String operation;
    String expected;
    boolean not = false;
}
