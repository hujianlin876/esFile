package com.esfile.common.util;
import java.io.File;
/**
 * 文件工具类
 */
public class FileUtil {
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }
}
