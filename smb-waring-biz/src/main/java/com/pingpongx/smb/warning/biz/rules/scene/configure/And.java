package com.pingpongx.smb.warning.biz.rules.scene.configure;

import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class And implements Serializable {
    List<ConfiguredLeafRule> andRules;
}
