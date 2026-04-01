package io.github.fantasticname.dormitory.repair.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传工具类
 * 
 * @author FantasticName
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
    private static final String UPLOAD_DIR = "uploads/";

    /**
     * 上传文件
     * 
     * @param file 上传的文件
     * @return 文件路径
     */
    public static String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 确保上传目录存在
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;
        String filePath = UPLOAD_DIR + filename;

        // 保存文件
        try {
            file.transferTo(new File(filePath));
            logger.info("文件上传成功: {}", filePath);
            return filePath;
        } catch (IOException e) {
            logger.error("文件上传失败", e);
            return null;
        }
    }

    /**
     * 删除文件
     * 
     * @param filePath 文件路径
     * @return 是否删除成功
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            logger.info("文件删除{}", deleted ? "成功" : "失败");
            return deleted;
        }
        return false;
    }

}