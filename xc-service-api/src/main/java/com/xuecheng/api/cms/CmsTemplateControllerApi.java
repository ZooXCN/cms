package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description="查找模板")
public interface CmsTemplateControllerApi {

    /**
     * 查询所有template列表
     * @return
     */
    @ApiOperation("查询cms_template站点数据")
    public QueryResponseResult findTemplateList();
}
