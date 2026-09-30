package com.worldexplorer.media.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

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

        // 从根目录开始递归扫描
        scanDirectory(sourceDirectory);

        printCompleteMessage();
    }

    /**
     * 递归扫描目录
     */
    private void scanDirectory(Path directory)
            throws IOException {

        try (Stream<Path> paths = Files.list(directory)) {

            paths.forEach(path -> {

                try {

                    // 如果是文件夹，继续向下扫描
                    if (Files.isDirectory(path)) {

                        // 复制模式下，不扫描刚创建的目标目录
                        if (mode == 2 &&
                                path.toAbsolutePath().normalize()
                                        .equals(
                                                targetDirectory
                                                        .toAbsolutePath()
                                                        .normalize()
                                        )) {
                            return;
                        }

                        scanDirectory(path);
                        return;
                    }

                    // 如果是文件，立即执行原来的分类逻辑
                    service.organize(
                            path,
                            targetDirectory,
                            mode
                    );

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
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