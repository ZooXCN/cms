package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsTemplateRepositoryTest {

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Test
    public void testFindTemplateList(){
        List<CmsTemplate> templateList = cmsTemplateRepository.findAll();
        System.out.println(templateList);
    }
}
