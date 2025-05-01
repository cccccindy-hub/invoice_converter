package com.nnroad.common.utils;

import com.alibaba.druid.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件模板配置
 *
 * @author Hrone
 */
public class MailTemplate {

    private static final Logger log = LoggerFactory.getLogger(MailTemplate.class);

    /**
     * 获取指定模板拼接的内容
     *
     * @param tempFile
     * @param params
     * @return
     * @throws FileNotFoundException
     */
    public static String getContentByTemplate(String tempFile, String... params) throws IOException {
        ClassPathResource resource = new ClassPathResource(tempFile);
        InputStream is = resource.getInputStream();
        return MessageFormat.format(Utils.read(is), params);
    }


    /**
     * 获取目录下所有文件
     *
     * @param strPath
     * @return
     */
    public static List<File> getFileList(String strPath) {
        List<File> fileList = new ArrayList<>();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else {
                    String strFileName = files[i].getAbsolutePath();
                    log.debug("file name : {}", strFileName);
                    fileList.add(files[i]);
                }
            }
        }
        return fileList;

    }

}
