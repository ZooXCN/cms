package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="课程管理接口",description="课程管理接口，提供课程的增、删、改、查")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);


    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    /**
     * 分页查询我的课程
     * @param page 当前页
     * @param size 每页显示条数
     * @param courseListRequest 扩展属性
     * @return
     */
    @ApiOperation("查询我的课程列表")
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    /**
     * 添加课程基础信息
     * @param courseBase
     * @return
     */
    @ApiOperation("添加课程基础信息")
    public AddCourseResult addCourseBase(CourseBase courseBase);


    /**
     * 根据courseId查询基本课程信息
     * @param courseId
     * @return
     */
    @ApiOperation("查询课程基础信息")
    public CourseBase getCourseBaseById(String courseId);

    /**
     * 修改课程信息
     * @param courseId
     * @param courseBase
     * @return
     */
    @ApiOperation("修改课程基础信息")
    public ResponseResult updateCourseBase(String courseId,CourseBase courseBase);


    /**
     * 查询课程营销信息
     * @param courseId
     * @return
     */
    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(String courseId);


    /**
     * 更新课程营销信息
     * @param id
     * @param courseMarket
     * @return
     */
    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(String id,CourseMarket courseMarket);


    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(String courseId,String pic);



}