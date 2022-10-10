package com.pingpongx.smb.warning.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pingpongx.business.dal.core.BaseRepository;
import com.pingpongx.smb.warning.dal.dataobject.SmbQaLibrary;
import com.pingpongx.smb.warning.dal.mapper.SmbQaLibraryMapper;
import org.springframework.stereotype.Repository;

/**
 * @author tangsh
 * @date 2022/09/26
 */
@Repository
public class SmbQaLibraryRepository extends BaseRepository<SmbQaLibraryMapper, SmbQaLibrary> {

    public SmbQaLibrary getOneQa(String keyWords) {
        LambdaQueryWrapper<SmbQaLibrary> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(SmbQaLibrary::getKeyWord, keyWords);
        queryWrapper.last("LIMIT 1");
        return getOne(queryWrapper);
    }

}
