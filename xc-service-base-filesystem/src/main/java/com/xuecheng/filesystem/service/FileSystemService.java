package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.Map;

@Service
public class FileSystemService {

    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;

    @Autowired
    private FileSystemRepository fileSystemRepository;


    /**
     * 上传文件
     * @param file  文件
     * @param filetag 文件标签
     * @param businesskey 业务key
     * @param metadata 元数据
     * @return
     */
    @Transactional
    public UploadFileResult upload(MultipartFile file,
                                   String filetag,
                                   String businesskey,
                                   String metadata){

        if (file == null){//上传文件为空
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }

        //上传文件到fastDFS,返回文件id
        String fileId = this.fdfs_upload(file);
        //上传文件
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFilePath(fileId);
        fileSystem.setFiletag(filetag);
        if (StringUtils.isNotEmpty(metadata)){
            try {
                Map map = JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //名称
        fileSystem.setFileName(file.getOriginalFilename());
        //大小
        fileSystem.setFileSize(file.getSize());
        //文件类型
        fileSystem.setFileType(file.getContentType());
        //上传文件
        fileSystemRepository.save(fileSystem);

        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);

    }


    /**
     * 上传文件到fastDFS
     * @param file
     * @return 返回文件fileId
     */
    private String fdfs_upload(MultipartFile file){
        //初始化fastDFS配置
        this.initFdfsConfig();

        //创建trackerClient
        TrackerClient trackerClient = new TrackerClient();
        //连接tracker服务器
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            //连接storage服务器
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String exc = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = storageClient1.upload_file1(bytes, exc, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //加载fastDFS配置
    private void initFdfsConfig(){
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
        } catch (Exception e) {
            e.printStackTrace();
            //初始化文件系统错误
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }

}




