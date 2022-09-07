package com.pingpongx.smb.warning.dal.dataobject;

import com.pingpongx.business.dal.core.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 应用
 * </p>
 *
 * @author PingPong Code Generator
 * @since 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BusinessAlertsApp extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 应用名称
     */
    @TableField("appName")
    private String appName;

    /**
     * 所属分类 SMB-B2B,SMB-福贸
     */
    private String classify;


}
