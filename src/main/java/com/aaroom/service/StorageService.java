package com.aaroom.service;


import com.aliyun.oss.OSSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.Date;

@Service
public class StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageService.class);

    public static final long DAY = 86400 * 1000L;

    public static final long DAY2 = 86400 * 2 * 1000L;

    public static final long MONTH = 86400 * 1000 * 30L;

    public static final String bucketName = "aaroom-pic";

    @Value("${aliyun.oss.AccessKeyId}")
    private String accessId;

    @Value("${aliyun.oss.AccessKeySecret}")
    private String accessKey;

    @Value("${aliyun.oss.endPoint}")
    private String endpoint;


    /**
     * 文件上传，删除服务器本地缓存
     * @param file
     * @param fileName
     */
    public void uploadFile(File file, String fileName){
        OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);

        try {
            ossClient.putObject(bucketName, fileName, file);
            file.deleteOnExit();
        } catch (Exception oe) {
            oe.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }


    /**
     * 文件访问之前，赋予签名，以获得访问权限
     * @param fileName
     * @return
     */
    public String generatePresignUrl(String fileName){
        OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);
        URL url  = null;
        try{
            url = ossClient.generatePresignedUrl(bucketName, fileName, new Date(new Date().getTime() + DAY));
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return url.toString();
    }

    /**
     * 文件重命名，因为阿里云没有相应方法，所以这里变成复制一个新的文件，然后删除旧文件
     * @param originName
     * @param destname
     */
    public void renameFile(String originName, String destname) {
        OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);

        try {
            ossClient.copyObject(bucketName, originName, bucketName, destname);
            ossClient.deleteObject(bucketName, originName);
        } catch (Exception oe) {
            oe.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    //oss删除文件
    public void deleteFile(String originName){
        OSSClient ossClient = new OSSClient(endpoint, accessId, accessKey);
        try{
        ossClient.deleteObject(bucketName, originName);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

}
