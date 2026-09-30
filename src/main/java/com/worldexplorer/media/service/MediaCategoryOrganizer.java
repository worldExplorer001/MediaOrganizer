package com.worldexplorer.media.service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 第二阶段：文件名称分类流程
 */
public class MediaCategoryOrganizer {

    private final Path directory;
    private final MediaCategoryService service;

    /**
     * 创建媒体分类流程
     */
    public MediaCategoryOrganizer(Path directory) {
        this.directory = directory;
        this.service = new MediaCategoryService();
    }

    /**
     * 执行第二阶段
     */
    public void organize() throws IOException {

        printStartMessage();

        service.organizeDirectory(
                directory.resolve("Image")
        );

        service.organizeDirectory(
                directory.resolve("Video")
        );

        printCompleteMessage();
    }

    /**
     * 输出开始信息
     */
    private void printStartMessage() {

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ 🔄 Starting file name classification");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }

    /**
     * 输出完成信息
     */
    private void printCompleteMessage() {

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║ ✅ File name classification completed!!!");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
    }
}