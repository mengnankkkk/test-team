package com.mengnnakk.service;

import java.io.InputStream;
import java.util.Set;

public interface FileUpload {
    String uploadFile(InputStream inputStream,long size,String exName);

}