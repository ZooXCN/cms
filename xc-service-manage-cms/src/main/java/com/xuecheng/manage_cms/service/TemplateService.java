package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    public QueryResponseResult findTemplateList(){

        List<CmsTemplate> templateList = cmsTemplateRepository.findAll();
        QueryResult<CmsTemplate> queryResult = new QueryResult<>();
        if (templateList!=null&&templateList.size()>0){
            queryResult.setList(templateList);
            queryResult.setTotal((long)templateList.size());
        }
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

}
