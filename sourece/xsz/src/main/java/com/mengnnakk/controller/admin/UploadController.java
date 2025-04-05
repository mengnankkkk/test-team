package com.mengnnakk.controller.admin;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.configuration.property.SystemConfig;
import com.mengnnakk.service.FileUpload;
import com.mengnnakk.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
