package org.liquiz.stevens.service;

import org.liquiz.stevens.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${app.upload.dir.jspfile:${user.dir}}")
    public String uploadDirJspFile;

    @Value("${app.upload.dir.answerfile:${user.dir}}")
    public String uploadDirAnsFile;

    @Value("${app.upload.dir:${user.dir}}")
    public String uploadDir;

    public Path uploadAnswerFile(MultipartFile file, String classId) {

        try {
            String dir = uploadDir + File.separator + "data" + File.separator + classId;
            File classDir = new File(dir);
            if(!classDir.exists())
                classDir.mkdir();
            Path copyLocation = Paths
                    .get(dir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return copyLocation;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }
    }

    public Path uploadJspFile(MultipartFile file, String classId) {
        try {
            String dir = uploadDir + File.separator + "src" + File.separator + "main" + File.separator + "webapp" +
                    File.separator + "WEB-INF" + File.separator + "jsp" + File.separator + "quizzes" + File.separator + classId;
            File classDir = new File(dir);
            if(!classDir.exists())
                classDir.mkdir();
            Path copyLocation = Paths
                    .get(dir +  File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return copyLocation;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }
    }
}
