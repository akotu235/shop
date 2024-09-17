package io.github.akotu235.shop.service.files;

import io.github.akotu235.shop.exceptions.FileOperationException;
import io.github.akotu235.shop.properties.AppConfigurationProperties;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileService {
    private final String dataPath;

    public FileService(AppConfigurationProperties config) {
        this.dataPath = config.getDataPath();
    }

    public void saveImageAsJpg(MultipartFile file, String relativePath, String fileName) {
        FileUtils.saveImageAsJpg(file, getPath(relativePath), fileName);
    }

    public ResponseEntity<InputStreamResource> getImage(String relativePath) {
        try {
            return FileUtils.getImage(getPath(relativePath));
        } catch (IOException e) {
            throw new FileOperationException("error", e.getMessage());
        }
    }

    private String getPath(String relativePath) {
        return dataPath + File.separator + relativePath;
    }
}