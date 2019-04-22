package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听mq，接收页面发布的消息
 */
@Component
public class ConsumerPostPage {

    @Autowired
    private PageService pageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg){
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        //获取消息中的页面id
        String pageId = (String) map.get("pageId");
        CmsPage cmsPage = pageService.findCmsPageById(pageId);
        if (cmsPage==null){//cmsPage不存在
            LOGGER.error("receive postPage msg,cmsPage is null,pageId:{}"+pageId);
            return;
        }

        //调用Service方法将页面从GridFs中下载到服务器
        pageService.savePageToServerPath(pageId);

    }
}
