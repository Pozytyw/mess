package com.mess.storage_service;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service("fileStorageService")
public class FileStorageServiceImpl implements FileStorageService {
    private final String name = "images";
    private final Path path = Paths.get(name);


    @Override
    public void init() {
        try {
            if(!Files.exists(path))
                Files.createDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile multipartFile, String filename) {
        try {
            String strPath = filename + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            Path path = this.path.resolve(strPath);
            Files.deleteIfExists(path);//remove is file already exist
            Files.copy(multipartFile.getInputStream(), path);//save file
            return "/" + this.path + "/" + strPath;
        } catch (IOException e) {
            throw new RuntimeException("Could not store the file. Error:"+e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        Path file = path.resolve(filename);
        try {
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                throw new RuntimeException("Could not read the file.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error:"+e.getMessage());
        }
    }

    @Override
    public Stream<Path> load() {
        try {
            return Files.walk(this.path,1)
                    .filter(path -> !path.equals(this.path))
                    .map(this.path::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files.");
        }
    }

    @Override
    public String getPath() {
        return this.name;
    }
}