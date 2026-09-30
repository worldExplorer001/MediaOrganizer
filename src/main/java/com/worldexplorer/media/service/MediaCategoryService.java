package com.worldexplorer.media.service;

import com.worldexplorer.media.classifier.FileNameClassifier;
import com.worldexplorer.media.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 第二阶段：根据文件名关键词进行分类
 */
public class MediaCategoryService {

    private final FileNameClassifier classifier;

    public MediaCategoryService() {
        this.classifier = new FileNameClassifier();
    }

    /**
     * 整理 Image 或 Video
     */
    public void organizeDirectory(
            Path sourceDirectory
    ) throws IOException {

        if (!Files.exists(sourceDirectory)) {
            return;
        }

        List<Path> files =
                readFiles(sourceDirectory);

        if (files.isEmpty()) {
            return;
        }

        Map<String, Integer> keywordCount =
                countKeywords(files);

        for (Path file : files) {

            List<String> keywords =
                    classifier.extractKeywords(file);

            String category =
                    findCategory(
                            keywords,
                            keywordCount
                    );

            if (category == null) {

                moveFile(
                        file,
                        sourceDirectory.resolve("Other")
                );

                continue;
            }

            // 有应用包名时进行二级分类
            String packageName =
                    findPackageName(keywords);

            if (packageName != null) {

                moveFile(
                        file,
                        sourceDirectory
                                .resolve(category)
                                .resolve(packageName)
                );

                continue;
            }

            moveFile(
                    file,
                    sourceDirectory.resolve(category)
            );
        }
    }

    /**
     * 寻找第一级分类
     */
    private String findCategory(
            List<String> keywords,
            Map<String, Integer> keywordCount
    ) {

        // 来源关键词优先
        for (int i = keywords.size() - 1;
             i >= 0;
             i--) {

            String keyword =
                    keywords.get(i);

            if (keyword.startsWith("来自") &&
                    keywordCount.getOrDefault(
                            keyword,
                            0
                    ) > 1) {

                return keyword;
            }
        }

        // 普通重复关键词
        for (String keyword : keywords) {

            if (keywordCount.getOrDefault(
                    keyword,
                    0
            ) > 1) {

                return keyword;
            }
        }

        return null;
    }

    /**
     * 寻找应用包名
     */
    private String findPackageName(
            List<String> keywords
    ) {

        for (String keyword : keywords) {

            if (keyword.matches(
                    "[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z0-9_]+)+"
            )) {

                return keyword;
            }
        }

        return null;
    }

    /**
     * 统计关键词
     */
    private Map<String, Integer> countKeywords(
            List<Path> files
    ) {

        Map<String, Integer> countMap =
                new HashMap<>();

        for (Path file : files) {

            List<String> keywords =
                    classifier.extractKeywords(file);

            for (String keyword : keywords) {

                countMap.put(
                        keyword,
                        countMap.getOrDefault(
                                keyword,
                                0
                        ) + 1
                );
            }
        }

        return countMap;
    }

    /**
     * 读取文件
     */
    private List<Path> readFiles(
            Path directory
    ) throws IOException {

        List<Path> files =
                new ArrayList<>();

        try (Stream<Path> stream =
                     Files.list(directory)) {

            stream.filter(Files::isRegularFile)
                    .forEach(files::add);
        }

        return files;
    }

    /**
     * 移动文件
     */
    private void moveFile(
            Path file,
            Path targetDirectory
    ) throws IOException {

        Path targetFile =
                targetDirectory.resolve(
                        file.getFileName()
                );

        System.out.println(
                file.getFileName()
                        + " | Move to: "
                        + targetDirectory
        );

        if (Files.exists(targetFile)) {
            return;
        }

        FileUtils.moveFile(
                file,
                targetFile
        );
    }
}