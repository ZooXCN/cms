package com.xuecheng.manage_cms.service;


import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    public QueryResponseResult findSiteList(){
        List<CmsSite> siteList = cmsSiteRepository.findAll();

        QueryResult<CmsSite> queryResult = new QueryResult<>();
        if (siteList!=null&&siteList.size()>0){
            queryResult.setList(siteList);
            queryResult.setTotal((long)siteList.size());
        }

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

}
