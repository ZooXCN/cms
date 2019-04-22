package com.xuecheng.manage_cms;


import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GenerateHtmlTest {

    @Autowired
    private PageService pageService;

    @Test
    public void testGenerateHtml() throws IOException {
        String pageHtml = pageService.getPageHtml("5b319c39f73c661c80b0b8af");
        System.out.println(pageHtml);
    }

}
