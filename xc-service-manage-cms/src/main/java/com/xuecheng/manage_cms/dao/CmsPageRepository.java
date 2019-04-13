package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    /**
     * 根据pageName来查询CmsPage对象
     * @param pageName
     * @return
     */
    public CmsPage findByPageName(String pageName);

    /**
     * 根据pageName、siteId和pageWebPath联合索引，查询是否存在页面
     * @param pageName
     * @param siteId
     * @param pageWebPath
     * @return
     */
    public CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);


}
