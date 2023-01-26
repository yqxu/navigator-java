package com.pingpongx.smb.warning.biz.rules.scene.configure;

import com.pingpongx.smb.export.module.persistance.Or;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import lombok.Data;

import java.io.Serializable;
@Data
public class Scene implements Serializable {
    String identity;
    Or rulesOf;
    ThresholdAlertConf countWith;
}
