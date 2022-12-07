package com.pingpongx.smb.export.spi;

public interface AttrExtractor {
    Object getAttr(Object data,String attrPath);
}
