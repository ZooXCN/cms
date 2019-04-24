package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.system.SysDictionary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeachplanMapper teachplanMapper;
    @Autowired
    SysDictionaryRepository sysDictionaryRepository;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }
    @Test
    public void testTeachplanMapper(){
        TeachplanNode teachplanNode = teachplanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(teachplanNode);
    }

    /**
     * 我的课程，分页测试
     */
    @Test
    public void testPageHelper(){

        int pageNum = 1;
        int pageSize = 10;
        PageHelper.startPage(pageNum,pageSize);
        CourseListRequest courseListRequest = new CourseListRequest();
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        long total = courseListPage.getTotal();
        System.out.println(total);
    }


    @Test
    public void testCategoryList(){
        CategoryNode list = categoryMapper.findList();
        System.out.println(list);
    }


    /**
     * 课程等级查询测试
     */
    @Test
    public  void testSysDictionary(){
        SysDictionary sysDictionary = sysDictionaryRepository.findByDType("200");
        System.out.println(sysDictionary);
    }

    /**
     *
     * 查询基本课程信息
     */
    @Test
    public void testCourseInfo(){
        Optional<CourseBase> optional = courseBaseRepository.findById("297e7c7c62b888f00162b8a7dec20000");
        CourseBase courseBase = optional.get();
        System.out.println(courseBase);
    }

    /**
     * 查询课程营销课程
     */
    @Test
    public void testCourseMarket(){
        Optional<CourseMarket> optional = courseMarketRepository.findById("402885816243d2dd016243f24c030002");
        if (optional.isPresent()){
            CourseMarket courseMarket = optional.get();
            System.out.println(courseMarket);
        }

    }

}
