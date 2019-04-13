package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description="cms页面管理接口，提供页面的增、删、改、查")
public interface CmsSiteControllerApi {

    /**
     * 查询cms_site站点数据
     * @return
     */
    @ApiOperation("查询cms_site站点数据")
    public QueryResponseResult findSiteList();
}
