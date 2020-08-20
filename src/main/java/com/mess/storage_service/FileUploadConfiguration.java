package com.mess.storage_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class FileUploadConfiguration implements CommandLineRunner {

    @Autowired
    FileStorageService fileStorageService;

    @Override
    public void run(String... args) throws Exception {
        fileStorageService.init();
    }
}