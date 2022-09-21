package com.pingpongx.smb.warning.biz.alert.threshold;

public enum InhibitionResultEnum {

    UnMatched(0,false),
    MatchedAndNeedInhibition(1,true),
    MatchedAndNeedThrow(2,false);
    InhibitionResultEnum(int level,boolean needInhibition){
        this.level = level;
        this.needInhibition = needInhibition;
    }
    int level;
    boolean needInhibition;

    public int getLevel() {
        return level;
    }

    public boolean isNeedInhibition() {
        return needInhibition;
    }
}
