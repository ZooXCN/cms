package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_course.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysdictionaryService {

    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;


    /**
     * 根据type查询SysDictionary
     * @param type
     * @return
     */
    public SysDictionary  findDictionaryByType(String type){
        return sysDictionaryRepository.findByDType(type);
    }
}
