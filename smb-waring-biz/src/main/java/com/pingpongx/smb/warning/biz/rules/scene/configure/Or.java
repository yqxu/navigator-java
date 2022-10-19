package com.pingpongx.smb.warning.biz.rules.scene.configure;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class Or implements Serializable {
    List<And> orRules;
}
