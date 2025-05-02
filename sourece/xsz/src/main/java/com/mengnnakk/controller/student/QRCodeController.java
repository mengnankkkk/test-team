package com.mengnnakk.controller.student;

import com.mengnnakk.service.ClassSignService;
import com.mengnnakk.utility.QRCodeUtils;
import com.qiniu.http.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qr")
public class QRCodeController {
    @Autowired
    private ClassSignService classSignService;
    @GetMapping(value = "/generate", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generate(@RequestParam String taskId)throws Exception{
        String code = classSignService.getCode(taskId);
        if (code==null){
            return ResponseEntity.badRequest().build();
        }
        String qrcontent = "sign://task?taskId="+taskId+"&code="+code;
        byte[] image = QRCodeUtils.generateQRCode(qrcontent,300,300);
        return ResponseEntity.ok(image);
    }
}
