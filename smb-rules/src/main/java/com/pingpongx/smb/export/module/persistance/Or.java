package com.pingpongx.smb.export.module.persistance;


import java.io.Serializable;
import java.util.List;

public class Or implements Serializable {
    List<And> orRules;

    public List<And> getOrRules() {
        return orRules;
    }

    public void setOrRules(List<And> orRules) {
        this.orRules = orRules;
    }
}
