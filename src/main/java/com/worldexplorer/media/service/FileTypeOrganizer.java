package com.worldexplorer.media.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * 第一阶段：文件类型整理流程
 */
public class FileTypeOrganizer {

    private final Path sourceDirectory;
    private final Path targetDirectory;
    private final int mode;
    private final FileTypeService service;

    /**
     * 创建文件类型整理流程
     *
     * @param mode 1 = 移动，2 = 复制
     */
    public FileTypeOrganizer(
            Path sourceDirectory,
            Path targetDirectory,
            int mode
    ) {
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.mode = mode;
        this.service = new FileTypeService();
    }

    /**
     * 执行第一阶段
     */
    public void organize() throws IOException {

        printStartMessage();

        List<Path> files =
                service.readFiles(sourceDirectory);

        for (Path file : files) {
            service.organize(
                    file,
                    targetDirectory,
                    mode
            );
        }

        printCompleteMessage();
    }

    /**
     * 输出开始信息
     */
    private void printStartMessage() {

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ 🔄 Starting file type classification");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * 输出完成信息
     */
    private void printCompleteMessage() {

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ ✅ File type classification completed!!!");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
}