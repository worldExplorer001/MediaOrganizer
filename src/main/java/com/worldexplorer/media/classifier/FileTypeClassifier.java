package com.worldexplorer.media.classifier;

import com.worldexplorer.media.model.FileType;
import com.worldexplorer.media.util.FileUtils;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 根据文件扩展名判断文件类型
 */
public class FileTypeClassifier {

    private static final Set<String> IMAGE_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp",
            "webp", "heic", "heif", "tif", "tiff"
    ));

    private static final Set<String> VIDEO_EXTENSIONS = new HashSet<>(Arrays.asList(
            "mp4", "mov", "avi", "mkv", "wmv",
            "flv", "webm", "m4v", "3gp"
    ));

    /**
     * 根据扩展名判断文件类型
     */
    public FileType classify(Path file) {

        String extension = FileUtils.getExtension(file);

        if (IMAGE_EXTENSIONS.contains(extension)) {
            return FileType.IMAGE;
        }

        if (VIDEO_EXTENSIONS.contains(extension)) {
            return FileType.VIDEO;
        }

        return FileType.OTHER;
    }
}