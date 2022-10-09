package com.pingpongx.smb.warning.biz.alert.model;

import com.pingpongx.smb.warning.biz.moudle.IdentityPath;

import java.io.Serializable;

public interface CountAble extends Serializable {
    IdentityPath<String> countPath();
}
