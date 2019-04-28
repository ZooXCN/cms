package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class PageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;


    /**
     * 保存页面到服务器的物理路径
     * @param pageId
     */
    public void savePageToServerPath(String pageId){

        //根据pageId查询cmsPage
        CmsPage cmsPage = this.findCmsPageById(pageId);
        if (cmsPage==null){//页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //通过cmsPage来获取htmlFieldId
        String htmlFileId = cmsPage.getHtmlFileId();

        //从gridFs中查询html文件
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream==null){//当inputSteam为空，输出Logger日志信息
            LOGGER.error("getFileById InputStream is null,htmlFileId:{}",htmlFileId);
            return;
        }
        //从cmsPage中获取siteId
        String siteId = cmsPage.getSiteId();
        //获取站点的物理路径
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        if (cmsSite==null){//站点信息不存在
            ExceptionCast.cast(CmsCode.CMS_SITEINFO_NOTEXISTS);
        }
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();
        //获取页面的物理路径
        String pagePath = sitePhysicalPath +cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        //将html文件保存到服务器的物理路径下
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {//释放资源
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据文件id获取文件内容
     * @param fileId
     * @return
     */
    public InputStream getFileById(String fileId){

        //获取文件对象
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //获取下载流对象
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(fsFile,downloadStream);
        try {
            InputStream inputStream = gridFsResource.getInputStream();
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * 根据页面id查询页面信息
     * @param pageId
     * @return
     */
    public CmsPage findCmsPageById(String pageId){

        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }
    /**
     * 根据站点id查询页面信息
     * @param siteId
     * @return
     */
    public CmsSite findCmsSiteById(String siteId){
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (optional.isPresent()){
            CmsSite cmsSite = optional.get();
            return cmsSite;
        }
        return null;
    }
}
