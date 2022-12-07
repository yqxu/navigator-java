package com.pingpongx.smb.monitor.dal.entity.constant;

public enum FMMainPages {

    SHOU_YE("首页"),
    WAI_MAO_SHOU_KUAN("外贸收款"),
    WAI_MAO_SHOU_KUAN_ZHANG_HU_GUAN_LI("外贸收款账户管理"),
    ZI_JING_HUI_JI("资金轨迹"),
    HE_TONG_DING_DANG("合同订单"),
    ZHANG_DAN_SHOU_KUAN("账单收款"),
    WAI_MAO_SHOU_KUAN_TI_XIAN("外贸收款提现"),
    REN_MING_BI_ZHANG_HU_TI_XIAN("人民币账户提现"),
    FA_QI_FU_KUAN("发起付款"),
    SHOU_KUAN_REN_GUAN_LI("收款人管理"),
    WAI_MAO_SHOU_KUAN_MING_XI("外贸收款明细"),
    REN_MING_BI_ZHANG_HU_MING_XI("人民币账户明细"),
    HUI_LV_FENG_XIAN("汇率风险"),
    WALLET_DETAIL("人民币账户详情"),

    ;

    private final String pageName;
    FMMainPages(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

}
