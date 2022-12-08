package com.pingpongx.smb.warning.biz.alert.model;

import com.pingpongx.smb.common.IdentityPath;

import java.io.Serializable;

public interface CountAble extends Serializable {
    IdentityPath<String> countPath();
}
