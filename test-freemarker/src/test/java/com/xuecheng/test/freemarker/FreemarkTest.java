package com.xuecheng.test.freemarker;


import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemarkTest {

    /**
     * 基于test.ftl模板文件生成html文件
     */
    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //定义模板
        //1、获取classPath路径
        String classPath = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(classPath+"/templates/"));
        Template template = configuration.getTemplate("test1.ftl", "utf-8");
        //定义数据模型
        Map map = getMap();
        //生成静态页面
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //System.out.println(content);
        //将content写入到输入流中
        InputStream inputStream = IOUtils.toInputStream(content);
        //指定文件输出路径
        FileOutputStream outputStream = new FileOutputStream(new File("D:/test.html"));
        //将输入流中的数据复制到输出流中，生成静态页面test.html
        IOUtils.copy(inputStream,outputStream);

        System.out.println("html页面生成完成......");
        //释放资源
        inputStream.close();
        outputStream.close();

    }

    @Test
    public void testGenerateHtml1() throws IOException, TemplateException {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //定义模板
        //1、将字符串定义为模板
        String templateString=""+"<html>\n"+
                "<head></head>\n"+
                "<body>\n"+"名称：${name}\n"+
                "</body>\n"+"</html>";
        //2、定义一个模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateString);
        //3、在配置configuration中配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template = configuration.getTemplate("template", "utf-8");
        //定义数据模型
        Map map = getMap();
        //生成静态模板
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("D:/template.html"));
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();

    }

    public Map getMap(){
        Map map = new HashMap();
        //向数据模型放数据
        map.put("name", "黑马程序员");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setMoney(1000.86f);
        stu1.setBirthday(new Date());
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setMoney(200.1f);
        stu2.setAge(19);
        //stu2.setBirthday(new Date());
        List<Student> friends = new ArrayList<>();
        friends.add(stu1);
        stu2.setFriends(friends);
        stu2.setBestFriend(stu1);
        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //向数据模型放数据
        map.put("stus", stus);
        //准备map数据
        HashMap<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);
        //向数据模型放数据
        map.put("stu1", stu1);
        //向数据模型放数据
        map.put("stuMap", stuMap);

        return map;
    }
}
