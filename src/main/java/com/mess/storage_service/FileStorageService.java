package com.mess.storage_service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {

    void init();

    String save(MultipartFile multipartFile, String filename);

    Resource load(String filename);

    Stream<Path> load();

    String getPath();
}