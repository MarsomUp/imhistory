package com.weina.imhistory.common;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/8.
 */
@Configuration
public class OssTools {
    private final static Logger log = LoggerFactory.getLogger(OssTools.class);

    private String endpoint = "http://oss-cn-zhangjiakou.aliyuncs.com";

    private String accessKeyId = "LTAIR8o7lZv5pdJK";

    private String accessKeySecret = "SK9kbJqWP4jceiscdEyhKY070BwMFf";

    private String bucketName = "wmioss";
    //文件存储目录
    private String filedir = "web-upload/simditor/images/";
    private String filedirW = "chat_history/";

    private OSSClient ossClient;

    public OssTools() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 初始化
     */
    public void init() {
        ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 销毁
     */
    public void destory() {
        ossClient.shutdown();
    }

    /**
     * 上传图片
     *
     * @param url
     */
    public void uploadImg2Oss(String url) throws Exception {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            String[] split = url.split("/");
            this.uploadFile2OSS(fin, split[split.length - 1]);
        } catch (FileNotFoundException e) {
            throw new Exception("图片上传失败");
        }
    }


    public String uploadImg2Oss(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        Calendar now = Calendar.getInstance();
        String name = RandomUtils.getStringRandom(4) + "_" + RandomUtils.createCode(6) + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return "";
    }

    public String getImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedirW + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 获得图片路径
     *
     * @param fileUrl
     * @return
     */
    public String getSimpleImgUrl(String fileUrl) {
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            Calendar now = Calendar.getInstance();
            String name = now.get(Calendar.YEAR) + "";
            name += (now.get(Calendar.MONTH) + 1) + "/" ;
            name += now.get(Calendar.DAY_OF_MONTH) +"/";
            return this.getSimpleUrl(this.filedirW +name+ split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器  如果同名文件会覆盖服务器上的
     *
     * @param instream 文件流
     * @param fileName 文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名, 返回文件路劲
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ossFileUrl = "";
        try {
            //创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            //上传文件
            Calendar now = Calendar.getInstance();
            String name = now.get(Calendar.YEAR) + "/";
            name += getMonth(now) + "/";
            name += getDay(now) + "/";
            String key = filedirW + name + fileName;
            PutObjectResult putResult = ossClient.putObject(bucketName, key, instream, objectMetadata);
            String ret = putResult.getETag();
            if (ret != null && ret != "") {
                ossFileUrl = key;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ossFileUrl;
    }

    /**
     * 获取月份，显示两位数
     * @param calendar
     * @return
     */
    private String getMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        if (month < 10) {
            return "0"+month;
        }
        return String.valueOf(month);
    }

    /**
     * 获取日期，显示两位数
     * @param calendar
     * @return
     */
    private String getDay(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if (day < 10) {
            return "0"+day;
        }
        return String.valueOf(day);
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getcontentType(String FilenameExtension) {
        if ("bmp".equalsIgnoreCase(FilenameExtension)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(FilenameExtension)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(FilenameExtension) ||
                "jpg".equalsIgnoreCase(FilenameExtension) ||
                "png".equalsIgnoreCase(FilenameExtension)) {
            return "image/jpeg";
        }
        if ("html".equalsIgnoreCase(FilenameExtension)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(FilenameExtension)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(FilenameExtension)) {
            return "application/vnd.visio";
        }
        if ("pptx".equalsIgnoreCase(FilenameExtension) ||
                "ppt".equalsIgnoreCase(FilenameExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("docx".equalsIgnoreCase(FilenameExtension) ||
                "doc".equalsIgnoreCase(FilenameExtension)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(FilenameExtension)) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param key
     * @return
     */
    public String getUrl(String key) {
        // 设置URL过期时间为10年  3600l* 1000*24*365*10
        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    public String getSimpleUrl(String key) {
        Date expiration = new Date(System.currentTimeMillis() + (3600L * 1000 * 24 * 365 * 10));
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
        if (url != null) {
            String ur = url.toString();
            String substring = ur.substring(ur.lastIndexOf("."));
            if (substring.indexOf("?") > 1) {
                substring = substring.substring(substring.indexOf("?"));
                ur = ur.replace(substring, "");
            }
            return ur;
        }
        return null;
    }

}

