package com.worldexplorer.media;

import com.worldexplorer.media.service.MediaOrganizer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 程序入口
 */
public class Main {

    public static void main(String[] args) {

        // ==================== 基础配置 ====================

        // 待处理文件夹地址
        String directoryPath =
                "H:\\互联网_待AI处理\\测验数据\\侧式数据2";

        // 1 = 原地整理
        // 2 = 复制整理
        int mode = 1;

        // ==================== 创建整理流程 ====================

        Path sourceDirectory =
                Paths.get(directoryPath);

        MediaOrganizer organizer =
                new MediaOrganizer(sourceDirectory);

        try {
            organizer.run(mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}