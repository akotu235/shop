package io.github.akotu235.shop.service.files;

import io.github.akotu235.shop.exceptions.FileOperationException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

class FileUtils {

    static void saveImageAsJpg(MultipartFile file, String directoryPath, String fileName) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                throw new FileOperationException("error.files.read-image");
            }
            BufferedImage rgbImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = rgbImage.createGraphics();
            graphics.drawImage(bufferedImage, 0, 0, Color.WHITE, null);
            graphics.dispose();
            createDirectory(directoryPath);
            File outputFile = new File(directoryPath + File.separator + fileName);
            try (ImageOutputStream ios = ImageIO.createImageOutputStream(outputFile)) {
                ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                writer.setOutput(ios);
                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.9f);
                writer.write(null, new javax.imageio.IIOImage(rgbImage, null, null), param);
                writer.dispose();
            }
        } catch (IOException e) {
            throw new FileOperationException("error", e.getMessage());
        }
    }

    static ResponseEntity<InputStreamResource> getImage(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileOperationException("error.files.read-image");
        }
        String contentType = Files.probeContentType(file.toPath());
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(file.length());
        return new ResponseEntity<>(new InputStreamResource(fileInputStream), headers, HttpStatus.OK);
    }

    private static void createDirectory(String path) {
        File directory = new File(path);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (!created) {
                throw new FileOperationException("error.files.create-directory", path);
            }
        }
    }
}