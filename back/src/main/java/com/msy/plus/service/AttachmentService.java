package com.msy.plus.service;

import com.msy.plus.core.service.Service;
import com.msy.plus.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author wzp
* @date 2021/06/03
*/
public interface AttachmentService extends Service<Attachment> {

    void createAttachmentList(List<MultipartFile> fileList, String category, Integer iputuserID) throws Exception;

    Attachment createAttachment(MultipartFile file, String category, Integer iputuserID) throws Exception;

    List<Attachment> findFiles(String fileIds);

    void downLoad(String fileId, HttpServletResponse response);

    String findFileUrlByFileId(String fileId);

    List<String> findFileUrlByFileIds(String fileId);
}
