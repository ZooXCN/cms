package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    public TeachplanNode findTeachplanList(String courseId) {

        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        return teachplanNode;
    }


    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    public ResponseResult addTeachplan(Teachplan teachplan) {

        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //获取课程courseId
        String courseid = teachplan.getCourseid();
        //获取parentId
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            //获取课程的根节点
            parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan parentNode = optional.get();
        String grade = parentNode.getGrade();
        //新节点
        Teachplan teachplanNew = new Teachplan();
        //将页面提交的teachplan信息拷贝到teachplanNew对象中
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setParentid(parentid);
        teachplanNew.setCourseid(courseid);
        if (grade.equals("1")) {//根据父节点来设置级别
            teachplanNew.setGrade("2");
        } else {
            teachplan.setGrade("3");
        }
        teachplanRepository.save(teachplanNew);
        return new ResponseResult(CommonCode.SUCCESS);

    }

    /**
     * 查询课程的根节点，如果查询不到需要自动添加根节点
     *
     * @param courseId
     * @return
     */
    @Transactional
    public String getTeachplanRoot(String courseId) {
        //获取课程
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();

        //查询课程的根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() <= 0) {
            //没有根节点，要自动添加一个根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");//父节点
            teachplan.setGrade("1");//设置等级
            teachplan.setPname(courseBase.getName());//设置课程名称
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");//未发布
            //保存课程
            teachplanRepository.save(teachplan);
            return teachplan.getId();

        }
        //如果查询到，将返回根节点的id
        return teachplanList.get(0).getId();
    }


    /**
     * 分页查询我的课程
     *
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {

        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }

        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }
        //实现分页pageHelper
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> list = courseListPage.getResult();
        long total = courseListPage.getTotal();
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setList(list);
        queryResult.setTotal(total);

        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);

    }

    /**
     * 添加基础课程
     *
     * @param courseBase
     * @return
     */
    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {

        if (courseBase == null) {//创建课程详情页面出错
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }

        //新增课程默认是未发布的状态
        courseBase.setStatus("202001");
        //保存基础课程
        courseBaseRepository.save(courseBase);
        return new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
    }

    /**
     * 查询基础课程
     *
     * @param courseid
     * @return
     */
    public CourseBase getCoursebaseById(String courseid) {

        if (courseid == null) {//课程id为空
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        Optional<CourseBase> optional = courseBaseRepository.findById(courseid);
        if (optional.isPresent()) {
            CourseBase courseBase = optional.get();
            return courseBase;
        }
        return null;
    }

    /**
     * 修改课程
     *
     * @param courseId
     * @param courseBase
     * @return
     */
    @Transactional
    public ResponseResult updateCourseBase(String courseId, CourseBase courseBase) {

        CourseBase coursebase1 = this.getCoursebaseById(courseId);
        if (coursebase1 == null) {//课程基础信息不存在
            ExceptionCast.cast(CourseCode.COURSE_BASEPAGE_ISNULL);
        }
        coursebase1.setName(courseBase.getName());
        coursebase1.setUsers(courseBase.getUsers());
        coursebase1.setDescription(courseBase.getDescription());
        coursebase1.setGrade(courseBase.getGrade());
        coursebase1.setStudymodel(courseBase.getStudymodel());
        coursebase1.setSt(courseBase.getSt());
        coursebase1.setMt(courseBase.getMt());
        //保存修改的课程信息
        courseBaseRepository.save(coursebase1);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据课程id查询课程营销信息
     *
     * @param courseid
     * @return
     */
    public CourseMarket getCourseMarketById(String courseid) {

        Optional<CourseMarket> optional = courseMarketRepository.findById(courseid);
        if (optional.isPresent()) {
            CourseMarket courseMarket = optional.get();
            return courseMarket;
        }

        return null;
    }

    /**
     * 保存课程营销信息
     *
     * @param id
     * @param courseMarket
     * @return
     */
    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket courseMarket1 = this.getCourseMarketById(id);
        if (courseMarket1 == null) {//如果courseMarket为空，创建一个新的课程营销信息
            courseMarket1 = new CourseMarket();
            courseMarket1.setId(id);//设置课程id
            //将基本课程营销信息复制到new的对象中
            BeanUtils.copyProperties(courseMarket,courseMarket1);
            //保存课程营销信息
            courseMarketRepository.save(courseMarket1);
        }else {//market信息存在，进行信息的修改操作

            courseMarket1.setCharge(courseMarket.getCharge());
            courseMarket1.setValid(courseMarket.getValid());
            courseMarket1.setQq(courseMarket.getQq());
            courseMarket1.setStartTime(courseMarket.getStartTime());
            courseMarket1.setEndTime(courseMarket.getEndTime());
            courseMarket1.setPrice(courseMarket.getPrice());
            courseMarket1.setPrice_old(courseMarket.getPrice_old());
            courseMarketRepository.save(courseMarket1);

        }

        return courseMarket1;
    }
}
