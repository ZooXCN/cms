package com.xuecheng.manage_cms.client;


import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 配置FeignClient接口
 * FeignClient 中指定cms服务名称，Feign将会从Eureka注册服务中获取cms服务列表，通过负载均衡算法进行调用服务
 */
@FeignClient(value = XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient {
    @GetMapping("/cms/page/get/{id}")//指定url 那么Feign将根据url地址去调用指定的服务
    public CmsPage findById(@PathVariable("id") String id);

    //一键发布页面
    @PostMapping("/cms/page/postPageQuick")
    public CmsPostPageResult postPageQuick(CmsPage cmsPage);

}
