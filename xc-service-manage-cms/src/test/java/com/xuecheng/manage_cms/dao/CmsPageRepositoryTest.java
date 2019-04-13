package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 查询所有
     */
    @Test
    public void testFindAll(){
        List<CmsPage> cmsPageList = cmsPageRepository.findAll();
        System.out.println(cmsPageList);
    }

    /**
     * 分页查询
     */
    @Test
    public void testFindByPage(){
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(pageable);
        System.out.println(cmsPages);
    }

    /**
     * 修改
     */
    @Test
    public void testUpdate(){
        //根据id查询要修改的对象
        Optional<CmsPage> optionalPage = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        if (optionalPage.isPresent()){//非空判断
            CmsPage cmsPage = optionalPage.get();
            cmsPage.setPageAliase("ddd");
            CmsPage page = cmsPageRepository.save(cmsPage);
            System.out.println(page);
        }
    }

    @Test
    public void testFindByPageName(){
        CmsPage cmsPage = cmsPageRepository.findByPageName("10101.html");
        System.out.println(cmsPage);
    }

    @Test
    public void testFindByCondition(){

        //分页
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        CmsPage cmsPage = new CmsPage();
        //条件查询
        //按照站点id
        //cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //按照模板id
        //cmsPage.setTemplateId("5a962bf8b00ffc514038fafa");
        //按照别名
        cmsPage.setPageAliase("轮播");

        //模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains()) ;
        Example<CmsPage> example = Example.of(cmsPage,matcher);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> content = cmsPages.getContent();
        System.out.println(content);
    }
}
