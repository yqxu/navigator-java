package com.pingpongx.smb.warning.biz.moudle.dingding;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @Auther: jiangkun
 * @Date: 2022/06/22/10:33 上午
 * @Description:
 * @Version:
 */
@Data
public class DingAlert implements Serializable {

    private static final long serialVersionUID = -4520584367899683195L;
    private String aliuid;
    private String alert_instance_id;
    private String alert_id;
    private String alert_type;
    private String alert_name;
    private String region;
    private String project;
    private Long project_id;
    private Long next_eval_interval;
    private Long fire_time;
    private Long alert_time;
    private List<FireResults> fire_results;
    private Integer fire_results_count;
    private Integer resolve_time;
    private String status;
    private Integer severity;

}
