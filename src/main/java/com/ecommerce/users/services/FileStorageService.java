package com.ecommerce.users.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileStorageService {
    String storeFile(MultipartFile file) throws IOException;
    String[] storeMultipleFiles(MultipartFile[] files) throws IOException;

    InputStream getFile(String fileId) throws IOException;
}
