package com.ecommerce.users.services;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.codec.digest.DigestUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public String storeFile(MultipartFile file) throws IOException {
        // Read the file into a byte array (this might be necessary if InputStream doesn't support reset)
        byte[] fileBytes = file.getBytes();

        // Calculate SHA-256 hash of the uploaded file for comparison
        String fileSha256 = DigestUtils.sha256Hex(fileBytes);

        // Query the existing files in the database by their SHA-256 hash
        GridFSFile existingFile = gridFsTemplate.findOne(new Query(Criteria.where("metadata.sha256").is(fileSha256)));

        // If the file exists, return the existing file's ID
        if (existingFile != null) {
            return existingFile.getObjectId().toString();
        }

        // Store the file and include the SHA-256 hash as metadata
        InputStream inputStream = new ByteArrayInputStream(fileBytes);
        DBObject metaData = new BasicDBObject();
        metaData.put("sha256", fileSha256);
        String fileId = gridFsTemplate.store(inputStream, file.getOriginalFilename(), file.getContentType(), metaData).toString();

        return fileId;
    }


    @Override
    @Transactional
    public String[] storeMultipleFiles(MultipartFile[] files) throws IOException {
        List<String> fileIds = new ArrayList<>();
        for (MultipartFile file : files) {
            fileIds.add(storeFile(file));
        }
        return fileIds.toArray(new String[fileIds.size()]);
    }

    @Override
    public InputStream getFile(String fileId) throws IOException {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(fileId)));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);

        if (gridFSFile == null) {
            throw new IOException("File not found with id: " + fileId);
        }

        GridFsResource resource = gridFsTemplate.getResource(gridFSFile);

        return resource.getInputStream();
    }
}
