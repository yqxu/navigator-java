package com.pingpongx.smb.warning.biz.rules.scene.configure;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import com.pingpongx.smb.warning.biz.rules.ConfiguredLeafRule;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class LeafRuleConf implements Serializable {
    String type;
    String attr;
    String operation;
    String excepted;
}
