package com.mengnnakk.controller.admin;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.configuration.property.SystemConfig;
import com.mengnnakk.service.FileUpload;
import com.mengnnakk.service.UserService;
import com.mengnnakk.viewmodel.admin.file.UeditorConfigVM;
import com.mengnnakk.viewmodel.admin.file.UploadResultVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

@RequestMapping("/api/admin/upload")
@RestController
public class UploadController extends BaseApiController {
    private final FileUpload fileUpload;
    private final SystemConfig systemConfig;
    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    private static final String IMAGE_UPLOAD = "imgUpload";
    private static final String IMAGE_UPLOAD_FILE = "upFile";

    @Autowired
    public UploadController(FileUpload fileUpload, SystemConfig systemConfig, UserService userService) {
        this.fileUpload = fileUpload;
        this.systemConfig = systemConfig;
        this.userService = userService;
    }
    @ResponseBody
    @RequestMapping("/configAndUpload")
    public Object upload(HttpServletRequest request , HttpServletResponse response){
        String action = request.getParameter("action");
        if (action.equals(IMAGE_UPLOAD)){//如果触发了上传图片的话
            try {
                //Spring 提供的用于解析 multipart/form-data 类型请求的对象。
                MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
                //multipartFile：获取上传的文件。
                MultipartFile multipartFile = multipartHttpServletRequest.getFile(IMAGE_UPLOAD_FILE);
                long attachSize = multipartFile.getSize();
                String imgName = multipartFile.getOriginalFilename();
                String filePath;
                //上传文件
                try(InputStream inputStream = multipartFile.getInputStream()) {
                    filePath = fileUpload.uploadFile(inputStream,attachSize,imgName);
                }
                //获取文件的扩展名
                String imageType = imgName.substring(imgName.lastIndexOf("."));
                UploadResultVM uploadResultVM = new UploadResultVM();
                uploadResultVM.setOriginal(imgName);
                uploadResultVM.setName(imgName);
                uploadResultVM.setUrl(filePath);
                uploadResultVM.setSize(multipartFile.getSize());
                uploadResultVM.setType(imageType);
                uploadResultVM.setState("SUCCESS");
                return uploadResultVM;
            }catch (IOException e){//上传失败，记录日志
                logger.error(e.getMessage(),e);
                return Collections.singletonMap("state","error");//前端正常解析
            }
        }else {//处理Ueditor请求
            UeditorConfigVM ueditorConfigVM = new UeditorConfigVM();
            ueditorConfigVM.setImageActionName(IMAGE_UPLOAD);
            ueditorConfigVM.setImageFieldName(IMAGE_UPLOAD_FILE);
            ueditorConfigVM.setImageMaxSize(2048000L);
            ueditorConfigVM.setImageAllowFiles(Arrays.asList(".png", ".jpg", ".jpeg", ".gif", ".bmp"));
            ueditorConfigVM.setImageCompressEnable(true);
            ueditorConfigVM.setImageCompressBorder(1600);
            ueditorConfigVM.setImageInsertAlign("none");
            ueditorConfigVM.setImageUrlPrefix("");
            ueditorConfigVM.setImagePathFormat("");
            return ueditorConfigVM;
        }
    }


}
