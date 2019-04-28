package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.*;

@Api(value="cms页面管理接口",description="cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {

    /**
     * 根据指定条件查询页面
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value="当前页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value="每页记录数",required=true,paramType="path",dataType="int")})
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);


    /**
     * 新增页面
     * @param cmsPage
     * @return
     */
    @ApiOperation("新增页面")
    public CmsPageResult add(CmsPage cmsPage);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @ApiOperation("查询页面")
    public CmsPage findById(String id);

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
    @ApiOperation("修改页面")
    public CmsPageResult update(String id,CmsPage cmsPage);

    /**
     * 删除页面
     * @param id
     * @return
     */
    @ApiOperation("删除页面")
    public ResponseResult delete(String id);

    /**
     * 发布页面
     * @param pageId
     * @return
     */
    @ApiOperation("发布页面")
    public ResponseResult post(String pageId);


    /**
     * 一键发布页面
     * @param cmsPage
     * @return
     */
    @ApiOperation("一键发布页面")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
