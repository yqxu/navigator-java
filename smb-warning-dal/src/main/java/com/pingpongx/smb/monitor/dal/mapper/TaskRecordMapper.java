package com.pingpongx.smb.monitor.dal.mapper;

import com.pingpongx.business.dal.core.DefaultMapper;
import com.pingpongx.smb.monitor.dal.entity.dataobj.TaskRecord;
import org.springframework.stereotype.Repository;

/**
 * 1.相同点
 * @Mapper和@Repository都是作用在dao层接口，使得其生成代理对象bean，交给spring 容器管理，对于mybatis来说，都可以不用写mapper.xml文件
 *
 * 2.不同点
 * @Repository需要在Spring中配置扫描地址，然后生成Dao层的Bean才能被注入到Service层中
 * @Mapper不需要配置扫描地址，通过xml里面的namespace里面的接口地址，生成了Bean后注入到Service层中
 */
@Repository
public interface TaskRecordMapper extends DefaultMapper<TaskRecord> {

}
