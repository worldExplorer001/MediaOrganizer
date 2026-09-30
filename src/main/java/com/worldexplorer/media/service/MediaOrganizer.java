package com.worldexplorer.media.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 媒体文件整理总流程
 */
public class MediaOrganizer {

    private final Path sourceDirectory;

    /**
     * 创建媒体整理流程
     */
    public MediaOrganizer(Path sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    /**
     * 根据模式执行整理
     *
     * 1 = 原地整理
     * 2 = 复制整理
     */
    public void run(int mode) throws IOException {

        if (mode == 1) {
            organizeInPlace();
            return;
        }

        if (mode == 2) {
            copyToOrganizedDirectory();
            return;
        }

        throw new IllegalArgumentException(
                "Invalid mode. Please use 1 or 2."
        );
    }

    /**
     * 原地整理
     */
    private void organizeInPlace() throws IOException {

        FileTypeOrganizer fileTypeOrganizer =
                new FileTypeOrganizer(
                        sourceDirectory,
                        sourceDirectory,
                        1
                );

        fileTypeOrganizer.organize();

        MediaCategoryOrganizer mediaCategoryOrganizer =
                new MediaCategoryOrganizer(sourceDirectory);

        mediaCategoryOrganizer.organize();
    }

    /**
     * 复制整理
     *
     * 在原始文件夹内部创建：
     * MediaTest_Organized
     * MediaTest_Organized_2
     * MediaTest_Organized_3
     * ...
     */
    private void copyToOrganizedDirectory() throws IOException {

        String sourceName =
                sourceDirectory.getFileName().toString();

        Path targetDirectory =
                createTargetDirectory(sourceName);

        FileTypeOrganizer fileTypeOrganizer =
                new FileTypeOrganizer(
                        sourceDirectory,
                        targetDirectory,
                        2
                );

        fileTypeOrganizer.organize();

        MediaCategoryOrganizer mediaCategoryOrganizer =
                new MediaCategoryOrganizer(targetDirectory);

        mediaCategoryOrganizer.organize();
    }

    /**
     * 创建不重复的目标目录
     */
    private Path createTargetDirectory(
            String sourceName
    ) throws IOException {

        Path targetDirectory =
                sourceDirectory.resolve(
                        sourceName + "_Organized"
                );

        int number = 2;

        while (Files.exists(targetDirectory)) {

            targetDirectory =
                    sourceDirectory.resolve(
                            sourceName
                                    + "_Organized_"
                                    + number
                    );

            number++;
        }

        Files.createDirectories(targetDirectory);

        return targetDirectory;
    }
}