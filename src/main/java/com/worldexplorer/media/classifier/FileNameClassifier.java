package com.worldexplorer.media.classifier;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据文件名结构提取分类关键词
 */
public class FileNameClassifier {

    /**
     * 提取关键词
     */
    public List<String> extractKeywords(Path file) {

        String name =
                removeDuplicateMark(
                        removeExtension(
                                file.getFileName().toString()
                        )
                );

        List<String> keywords =
                new ArrayList<>();

        // ① 提取【】内容
        extractBracket(name, keywords);

        // ② 提取下划线结构
        extractUnderscoreParts(name, keywords);

        // ③ 提取日期前面的文字
        extractDatePrefix(name, keywords);

        // ④ 提取文字 + 长数字
        extractNumberPrefix(name, keywords);

        return keywords;
    }

    /**
     * 提取下划线结构
     */
    private void extractUnderscoreParts(
            String name,
            List<String> keywords
    ) {

        if (!name.contains("_")) {
            return;
        }

        String[] parts =
                name.split("_");

        for (String part : parts) {

            part = part.trim();

            if (part.isEmpty()) {
                continue;
            }

            // 日期、时间、数字不作为关键词
            if (isDate(part) ||
                    isTime(part) ||
                    part.matches("\\d+")) {
                continue;
            }

            // 应用包名直接保留
            if (isPackageName(part)) {
                addKeyword(part, keywords);
                continue;
            }

            extractText(
                    part,
                    keywords
            );
        }
    }

    /**
     * 提取日期前面的文字
     */
    private void extractDatePrefix(
            String name,
            List<String> keywords
    ) {

        Matcher matcher =
                Pattern.compile(
                        "^(.+?)[-_]?\\d{4}-\\d{2}-\\d{2}"
                ).matcher(name);

        if (!matcher.find()) {
            return;
        }

        String prefix =
                matcher.group(1)
                        .replaceAll(
                                "[-_\\s]+$",
                                ""
                        )
                        .trim();

        addKeywordIfValid(
                prefix,
                keywords
        );
    }

    /**
     * 提取文字 + 长数字
     */
    private void extractNumberPrefix(
            String name,
            List<String> keywords
    ) {

        Matcher matcher =
                Pattern.compile(
                        "^([A-Za-z\\u4e00-\\u9fa5]+)\\d{8,}"
                ).matcher(name);

        if (!matcher.find()) {
            return;
        }

        addKeywordIfValid(
                matcher.group(1),
                keywords
        );
    }

    /**
     * 提取【】内容
     */
    private void extractBracket(
            String name,
            List<String> keywords
    ) {

        int start =
                name.indexOf("【");

        int end =
                name.indexOf("】", start);

        if (start >= 0 && end > start) {

            addKeyword(
                    name.substring(
                            start + 1,
                            end
                    ).trim(),
                    keywords
            );
        }
    }

    /**
     * 提取普通文字
     */
    private void extractText(
            String text,
            List<String> keywords
    ) {

        text = text.trim();

        text = text.replaceAll(
                "\\d{4}-\\d{2}-\\d{2}",
                ""
        );

        text = text.replaceAll(
                "\\d{2}:?\\d{2}:?\\d{2}",
                ""
        );

        text = text.replaceAll(
                "\\d+",
                ""
        );

        text = text.trim();

        addKeywordIfValid(
                text,
                keywords
        );
    }

    /**
     * 判断是否为日期
     */
    private boolean isDate(
            String value
    ) {

        return value.matches(
                "\\d{4}-\\d{1,2}-\\d{1,2}"
        );
    }

    /**
     * 判断是否为时间
     */
    private boolean isTime(
            String value
    ) {

        return value.matches(
                "\\d{2}:?\\d{2}:?\\d{2}"
        );
    }

    /**
     * 判断是否为应用包名
     */
    private boolean isPackageName(
            String value
    ) {

        return value.matches(
                "[a-zA-Z_][a-zA-Z0-9_]*(\\.[a-zA-Z0-9_]+)+"
        );
    }

    /**
     * 添加有效关键词
     */
    private void addKeywordIfValid(
            String keyword,
            List<String> keywords
    ) {

        if (isValidKeyword(keyword)) {
            addKeyword(
                    keyword,
                    keywords
            );
        }
    }

    /**
     * 判断关键词
     */
    private boolean isValidKeyword(
            String value
    ) {

        if (value == null ||
                value.isEmpty()) {
            return false;
        }

        if (value.matches("\\d+")) {
            return false;
        }

        if (value.matches(
                "(?i)[0-9a-f]{16,64}"
        )) {
            return false;
        }

        return value.matches(
                ".*[A-Za-z\\u4e00-\\u9fa5].*"
        );
    }

    /**
     * 添加不重复关键词
     */
    private void addKeyword(
            String keyword,
            List<String> keywords
    ) {

        if (!keywords.contains(keyword)) {
            keywords.add(keyword);
        }
    }

    /**
     * 删除重复文件标记
     */
    private String removeDuplicateMark(
            String name
    ) {

        return name.replaceFirst(
                "\\s*\\(\\d+\\)$",
                ""
        ).trim();
    }

    /**
     * 删除扩展名
     */
    private String removeExtension(
            String fileName
    ) {

        int index =
                fileName.lastIndexOf(".");

        if (index <= 0) {
            return fileName;
        }

        return fileName.substring(
                0,
                index
        );
    }
}