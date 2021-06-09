package com.msy.plus.controller;

import com.msy.plus.core.jwt.JwtUtil;
import com.msy.plus.core.response.Result;
import com.msy.plus.core.response.ResultGenerator;
import com.msy.plus.entity.Attachment;
import com.msy.plus.service.AttachmentService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
* @author wzp
* @date 2021/06/03
*/

@Slf4j
@Validated
@Api(tags={"附件接口"})
@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    @Resource private AttachmentService attachmentService;
    @Resource private JwtUtil jwtUtil;

//    @Operation(description = "多个附件添加")
//    @PostMapping(value = "/uploads/{category}", consumes = "multipart/*", headers = "content-type=multipart/form-data")
//    public Result uploads(@PathVariable String category, @ApiParam(value = "上传的附件") List<MultipartFile> multipartFileList,
//                      @RequestHeader Map<String, String> headers) throws Exception {
//        String header = jwtUtil.getJwtProperties().getHeader();
//        Integer iputuserID= Integer.valueOf(jwtUtil.getId(headers.get(header)).get());
//        attachmentService.createAttachmentList(multipartFileList, category, iputuserID);
//        return ResultGenerator.genOkResult();
//    }

    @Operation(description = "附件添加")
    @PostMapping(value = "/upload/{category}", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    public Result upload(@PathVariable String category, @ApiParam(value = "上传的附件", required = true) MultipartFile file,
                      @RequestHeader Map<String, String> headers) throws Exception {
        String header = jwtUtil.getJwtProperties().getHeader();
        Integer iputuserID= Integer.valueOf(jwtUtil.getId(headers.get(header)).get());
        Attachment attachment = attachmentService.createAttachment(file, category, iputuserID);
        return ResultGenerator.genOkResult(attachment);
    }

    @ApiOperation(value = "获取附件信息", notes = "获取附件信息接口")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileId", value = "附件ID", required = true, dataType = "string"),})
    @GetMapping(value = "/get/{fileId}")
    public Result get(@PathVariable String fileId) {
        Attachment attachment = attachmentService.getById(fileId);
        return ResultGenerator.genOkResult(attachment);
    }

    @ApiOperation(value = "查看附件信息集", notes = "查看附件信息集")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileIds", value = "附件ID集", dataType = "string"),})
    @GetMapping(value = "/findFiles/{fileIds}")
    public Result findFiles(@PathVariable String fileIds){
        List<Attachment> url = attachmentService.findFiles(fileIds);
        return ResultGenerator.genOkResult(url);
    }

    @ApiOperation(value = "下载附件", produces = "application/octet-stream")
    @ApiImplicitParams({@ApiImplicitParam(name = "fileId", value = "附件ID", required = true, dataType = "string"),})
    @GetMapping(value = "/downLoad/{fileId}", produces = {"application/octet-stream"}, consumes = {"*"})
    public Result downLoad(@PathVariable String fileId, HttpServletResponse response){
        attachmentService.downLoad(fileId, response);
        return ResultGenerator.genOkResult();
    }

    @ApiOperation(value = "获取文件路径", notes = "获取文件路径接口")
    @ApiImplicitParam(name = "fileId", value = "文件ID", dataType = "string")
    @GetMapping(value = "/findFileUrlByFileId/{fileId}")
    public Result findFileUrlByFileId(@PathVariable String fileId){
        String url = attachmentService.findFileUrlByFileId(fileId);
        return ResultGenerator.genOkResult(url);
    }

    @ApiOperation(value = "获取文件路径集", notes = "获取文件路径集接口")
    @ApiImplicitParam(name = "fileIds", value = "文件ID", dataType = "string")
    @GetMapping(value = "/findFileUrlByFileIds/{fileIds}")
    public Result findFileUrlByFileIds(@PathVariable String fileId){
        List<String> urlList = attachmentService.findFileUrlByFileIds(fileId);
        return ResultGenerator.genOkResult(urlList);
    }

}
