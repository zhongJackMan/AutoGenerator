package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @Author shusheng
 * @createTime 2018/8/12/ 下午10:41
 */
@Component
public class FileComponent {

    public final static String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    /**
     * 创建文件
     * @param filePath
     * @return
     * @throws Exception
     */
    public File createFile(String filePath) throws Exception {
        File file = new File(filePath);
        if(file.exists()) {
            return file;
        }
        File parentFile = file.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdirs();
        }
        file.createNewFile();
        return file;
    }

    /**
     * 创建文件夹
     * @param filePath
     * @return
     * @throws Exception
     */
    public File mkdir(String filePath) throws Exception {
        File file = new File(filePath);
        if(file.exists()) {
            return file;
        }
        file.mkdirs();
        return file;
    }

    /**
     * 替换目标字符串
     * @param target
     * @param source
     * @param line
     */
    public String replaceTarget(final String target, final String source, final String line) {
        if(line.contains(target)) {
            return line.replace(target, source);
        }
        return line;
    }
}
