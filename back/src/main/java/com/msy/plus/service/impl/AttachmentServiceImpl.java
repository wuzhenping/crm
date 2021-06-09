package com.msy.plus.service.impl;

import com.msy.plus.core.key.CodeGenerator;
import com.msy.plus.core.service.AbstractService;
import com.msy.plus.entity.Attachment;
import com.msy.plus.service.AttachmentService;
import com.msy.plus.util.File2Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author wzp
* @date 2021/06/03
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class AttachmentServiceImpl extends AbstractService<Attachment> implements AttachmentService {

    @Resource
    private com.msy.plus.mapper.AttachmentMapper attachmentMapper;

    private static final Logger logger = LogManager.getLogger(AttachmentService.class);

    @Value("${file.local-path}")
    private String uploadPath;

    @Value("${file.max}")
    private String maxSizeStr = "0";

    @Override
    public void createAttachmentList(List<MultipartFile> fileList, String category, Integer iputuserID) throws Exception {
        if(null == fileList || fileList.size() == 0){
            return;
        }
        for (MultipartFile file : fileList) {
            this.createAttachment(file,category,iputuserID);
        }
    }

    @Override
    public Attachment createAttachment(MultipartFile file, String category, Integer iputuserID) throws Exception {
        Attachment attachment = new Attachment();
        // 最大文件大小
        long maxSize = Long.parseLong(maxSizeStr);
//        String savePath = Config.get("file.upload.path") + "/";
        String savePath = uploadPath + "/";
        if (null != category && !"".equals(category)) {
            savePath += category + "/";
        }
        logger.info("上传文件的路径:" + savePath);
        // 定义允许上传的文件扩展名
        String fileType = "doc,docx,xls,xlsx,ppt,pptx,htm,html,xml,sql,txt,zip,rar,gz,bz2,pdf,gif,jpg,jpeg,png,bmp,tif,swf,flv,swf,mp3,mp4,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb,";
        fileType += fileType.toUpperCase();
        // 检查目录
        File uploadDir = new File(savePath);
        if (!uploadDir.isDirectory()) {
            uploadDir.mkdirs();
        }

        // 检查目录写权限
        if (!uploadDir.canWrite()) {
            throw new Exception("上传目录没有写权限");
        }

        if (file != null && null != file.getOriginalFilename()) {// 判断上传的文件是否为空

            String path = null;// 文件路径
            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称

            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;

            // 检查文件大小
            Long fileSize = file.getSize();
            if (fileSize > maxSize) {
                throw new Exception("上传文件大小超过限制");
            }

            if (type != null) {// 判断文件类型是否为空

                if (Arrays.asList(fileType.split(",")).contains(type)) {

                    // 项目在容器中实际发布运行的根路径
                    // String realPath = request.getSession().getServletContext().getRealPath("/");

                    // 自定义的文件名称
                    String trueFileName = CodeGenerator.getInstance().getAttachmentId();

                    // 设置存放图片文件的路径
                    path = savePath + trueFileName + "." + type;
                    logger.info("存放图片文件的路径:" + path);
                    File2Utils.writeByteArrayToFile(path, file.getBytes());

                    // 转存文件到指定的路径
//                    file.transferTo(new File(path));
                    logger.debug("文件成功上传到指定目录下");

                    attachment.setId(trueFileName);
                    attachment.setCategory(category);
                    attachment.setOrifilename(fileName);
                    attachment.setInputuser(iputuserID);
                    attachment.setUrl(path);
                    attachment.setInputtime(new Date());
                    attachmentMapper.insert(attachment);

                } else {
                    logger.error("文件类型受限制,请按要求重新上传");
                    throw new Exception("文件类型受限制,请按要求重新上传");
                }

            } else {
                logger.error("文件不能为空");
                throw new Exception("文件不能为空");
            }

        } else {
            logger.error("没有找到相对应的文件");
            throw new Exception("没有找到相对应的文件");
        }
        return attachment;

    }

    @Override
    public List<Attachment> findFiles(String fileIds) {
        return this.listByIds(fileIds);
    }

    @Override
    public void downLoad(String fileId, HttpServletResponse response) {
        Attachment attachment = this.getById(fileId);
        String fileName = attachment.getOrifilename();

        File file = this.getFile(attachment);
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);


            fileName = URLEncoder.encode(fileName, "utf-8");
            fos = response.getOutputStream();
            // 设置response的Header
            // response.reset();
            response.setContentType("text/html;charset=UTF-8");
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + file.length());
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != fis){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fos){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private File getFile(Attachment attachment) {
        if (null == attachment) {
            return null;
        }
        String filePath = attachment.getUrl();
        File file = new File(filePath);
        return file;
    }

    @Override
    public String findFileUrlByFileId(String fileId) {
        Attachment attachment = this.getById(fileId);
        if (null == attachment) {
            return null;
        }
        return attachment.getUrl();
    }

    @Override
    public List<String> findFileUrlByFileIds(String fileIds) {
        List<Attachment> attachmentList = this.listByIds(fileIds);
        if (null == attachmentList || attachmentList.size() == 0) {
            return null;
        }
        List<String> urlList = new ArrayList<>();
        for (Attachment attachment : attachmentList) {
            if (null != attachment.getUrl()){
                urlList.add(attachment.getUrl());
            }
        }
        return urlList;
    }
}
