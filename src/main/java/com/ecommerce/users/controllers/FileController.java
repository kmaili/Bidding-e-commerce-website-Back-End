package com.ecommerce.users.controllers;


import com.ecommerce.users.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> saveFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileId = fileStorageService.storeFile(file);
        Map<String, String> response = new HashMap<>();
        response.put("fileId", fileId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("upload_multi")
    public ResponseEntity<Map<String, String[]>> saveFileMulti(@RequestParam("files") MultipartFile[] files) throws IOException {
        String[] fileIds = fileStorageService.storeMultipleFiles(files);
        Map<String, String[]> response = new HashMap<>();
        response.put("filesIds", fileIds);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/download/{fileId}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileId) {
        try {
            InputStream fileInputStream = fileStorageService.getFile(fileId);

            // Create a response entity with the input stream
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM) // Set appropriate content type
                    .body(new InputStreamResource(fileInputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
