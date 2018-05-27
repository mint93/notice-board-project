package com.noticeboardproject.storage;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file, String userName);

    Path load(String filename, String userName);

    Resource loadAsResource(String filename, String userName);

    void delete(String filename, String userName);
    
    void deleteAllFilesForUser(String userName);

	Byte[] loadAsByteArray(String filename, String userName);

}