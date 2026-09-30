package com.worldexplorer.media.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 文件操作工具
 */
public class FileUtils {

    /**
     * 获取文件扩展名
     */
    public static String getExtension(Path file) {

        String fileName = file.getFileName().toString();
        int index = fileName.lastIndexOf(".");

        if (index == -1) {
            return "";
        }

        return fileName.substring(index + 1).toLowerCase();
    }

    /**
     * 移动文件
     */
    public static void moveFile(
            Path source,
            Path target
    ) throws IOException {

        Files.createDirectories(target.getParent());
        Files.move(source, target);
    }

    /**
     * 复制文件
     */
    public static void copyFile(
            Path source,
            Path target
    ) throws IOException {

        Files.createDirectories(target.getParent());
        Files.copy(source, target);
    }
}