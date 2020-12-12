package org.liquiz.stevens.service;

import org.apache.log4j.Logger;
import org.liquiz.stevens.controller.QuizController;
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
    private static final Logger
        LOG = Logger.getLogger(QuizController.class);
    @Value("${app.upload.dir.jspfile:${user.dir}}")
    public String uploadDirJspFile;

    @Value("${app.upload.dir:}")
    public String uploadDir;

    public Path uploadFile(MultipartFile file,
                           String courseId,
                           boolean jsp, String dir){
        try {
            if(!jsp)
                dir =  ".." + File.separator + "data" + File.separator + courseId;//  uploadDir + File.separator + "data" + File.separator + classId;System.getProperty("user.dir") +
            File classDir = new File(dir);
            if(!classDir.exists()) {
                if(!classDir.mkdirs())
                    throw new FileStorageException("Could not store file" + file.getOriginalFilename() + ". Please contact support!");
            }
            Path copyLocation = Paths
                    .get(dir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            LOG.info(copyLocation.toAbsolutePath().toString());
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return copyLocation;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }
    }

    public Path uploadAnswerFile(MultipartFile file, String courseId) {

        try {
            String dir =  ".." + File.separator + "webapps" + File.separator + "liquiz-test" + File.separator + "WEB-INF"
                    + File.separator + "data" + File.separator + courseId;//  uploadDir + File.separator + "data" + File.separator + classId;System.getProperty("user.dir") +
            File classDir = new File(dir);
            if(!classDir.exists()) {
                if(!classDir.mkdir())
                    throw new FileStorageException("Could not store file" + file.getOriginalFilename() + ". Please contact support!");
            }
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
            String dir = ".." + File.separator + "webapps" + File.separator + "liquiz-test" + File.separator +
                    "WEB-INF" + File.separator + "jsp" + File.separator + "quizzes" + File.separator + classId;
            // uploadDir + File.separator + "src" + File.separator + "main" + File.separator + "webapp" +
            //                    File.separator + "WEB-INF" + File.separator + "jsp" + File.separator + "quizzes" + File.separator + classId; System.getProperty("user.dir") +
            File classDir = new File(dir);
            if(!classDir.exists()) {
                if(!classDir.mkdir())
                    throw new FileStorageException("Could not store file" + file.getOriginalFilename() + ". Please contact support!");
            }
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
