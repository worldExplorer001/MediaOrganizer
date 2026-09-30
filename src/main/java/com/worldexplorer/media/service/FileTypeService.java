package com.worldexplorer.media.service;

import com.worldexplorer.media.classifier.FileTypeClassifier;
import com.worldexplorer.media.model.FileType;
import com.worldexplorer.media.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 第一阶段：文件类型整理功能
 */
public class FileTypeService {

    private final FileTypeClassifier classifier;

    /**
     * 创建文件类型功能服务
     */
    public FileTypeService() {
        this.classifier = new FileTypeClassifier();
    }

    /**
     * 处理一个文件
     */
    public void organize(
            Path file,
            Path targetDirectory,
            int mode
    ) throws IOException {

        FileType type = classifier.classify(file);

        String directoryName =
                getDirectoryName(type);

        Path typeDirectory =
                targetDirectory.resolve(directoryName);

        Path targetFile =
                typeDirectory.resolve(file.getFileName());

        if (Files.exists(targetFile)) {
            handleDuplicate(
                    file,
                    targetDirectory,
                    mode
            );
            return;
        }

        printFileInfo(
                file,
                directoryName,
                mode
        );

        if (mode == 1) {
            FileUtils.moveFile(file, targetFile);
            return;
        }

        if (mode == 2) {
            FileUtils.copyFile(file, targetFile);
            return;
        }

        throw new IllegalArgumentException(
                "Invalid mode. Please use 1 or 2."
        );
    }

    /**
     * 处理重复文件
     */
    private void handleDuplicate(
            Path file,
            Path targetDirectory,
            int mode
    ) throws IOException {

        Path duplicateDirectory =
                targetDirectory.resolve("Duplicate");

        Path targetFile =
                duplicateDirectory.resolve(file.getFileName());

        System.out.println(
                "♻️ " + file.getFileName()
                        + " | Type: Duplicate"
                        + " | "
                        + getOperationName(mode)
                        + " to: Duplicate"
        );

        if (Files.exists(targetFile)) {
            return;
        }

        if (mode == 1) {
            FileUtils.moveFile(file, targetFile);
            return;
        }

        if (mode == 2) {
            FileUtils.copyFile(file, targetFile);
        }
    }

    /**
     * 获取文件夹名称
     */
    private String getDirectoryName(FileType type) {

        switch (type) {

            case IMAGE:
                return "Image";

            case VIDEO:
                return "Video";

            case OTHER:
                return "Other";

            default:
                return "Other";
        }
    }

    /**
     * 输出文件处理信息
     */
    private void printFileInfo(
            Path file,
            String directoryName,
            int mode
    ) {

        String extension =
                FileUtils.getExtension(file);

        System.out.println(
                file.getFileName()
                        + " | Extension: " + extension
                        + " | Type: " + directoryName
                        + " | "
                        + getOperationName(mode)
                        + " to: " + directoryName
        );
    }

    /**
     * 获取操作名称
     */
    private String getOperationName(int mode) {

        if (mode == 1) {
            return "Move";
        }

        return "Copy";
    }
}