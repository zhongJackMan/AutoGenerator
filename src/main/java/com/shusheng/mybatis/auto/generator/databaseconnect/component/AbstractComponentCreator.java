package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author shusheng
 * @Date 18/8/13 下午7:37
 */
@Component
public abstract class AbstractComponentCreator {
    private final static Logger logger = LoggerFactory.getLogger(AbstractComponentCreator.class);
    @Autowired
    private MysqlDataTypeTransferComponent mysqlDataTypeTransferComponent;
    @Autowired
    private FileComponent fileComponent;

    /**
     * 获取文件目录
     *
     * @return
     */
    protected abstract String getBaseDirectory();

    /**
     * 获取接口文件后缀
     * Service.java
     *
     * @return
     */
    protected abstract String getInterfaceEndSuffix();

    /**
     * 获取接口模板文件路径
     *
     * @return
     */
    protected abstract String getInterfaceResourcePath();

    /**
     * 获取接口文件替换关键字
     *
     * @param fileName
     * @param tableInfoDTO
     * @return
     */
    protected abstract Map<String, String> getInterfaceTargetMap(String fileName, TableInfoDTO tableInfoDTO) throws Exception;

    /**
     * 获取实习类文件的后缀
     *
     * @return
     */
    protected abstract String getImplementsEndSuffix();

    /**
     * 获取实现类模板文件路径
     *
     * @return
     */
    protected abstract String getImplementsResourcePath();

    /**
     * 获取实现类文件替换关键字
     *
     * @param fileName
     * @param tableInfoDTO
     * @return
     */
    protected abstract Map<String, String> getImplementsTargetMap(String fileName, TableInfoDTO tableInfoDTO) throws Exception;

    protected boolean createComponent(final File parentFile, final String tableName, final TableInfoDTO tableInfoDTO) {
        try {
            String fileName = mysqlDataTypeTransferComponent.getClassName(tableName);
            String directoryPath = parentFile.getAbsolutePath() + getBaseDirectory();
            fileComponent.mkdir(directoryPath);
            /*
             * 创建接口
             */
            createInterface(fileName, directoryPath, tableInfoDTO);
            /*
             * 创建实现类
             */
            createImplements(fileName, directoryPath + "/impl", tableInfoDTO);

        }catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return true;
    }

    private void createInterface(final String fileName, final String directoryPath, final TableInfoDTO tableInfoDTO)
            throws Exception {

        String interfacePath = directoryPath + "/" + fileName + getInterfaceEndSuffix();
        File interfaceFile = fileComponent.createFile(interfacePath);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(interfaceFile), "UTF-8");
        writeToInterfaceFile(fileName, writer, tableInfoDTO);
    }

    private void createImplements(final String fileName, final String directoryPath, final TableInfoDTO tableInfoDTO)
            throws Exception {
        String implementsPath = directoryPath + "/" + fileName + getImplementsEndSuffix();
        File implementsFile = fileComponent.createFile(implementsPath);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(implementsFile), "UTF-8");
        writeToImplementsFile(fileName, writer, tableInfoDTO);
    }

    private void writeToInterfaceFile(final String fileName, final OutputStreamWriter writer,
                                      final TableInfoDTO tableInfoDTO) throws Exception {
        BufferedReader reader = getBufferedReader(getInterfaceResourcePath());
        Map<String, String> targetMap = getInterfaceTargetMap(fileName, tableInfoDTO);
        writeToFile(writer, reader, targetMap);
    }

    private void writeToImplementsFile(final String fileName, final OutputStreamWriter writer,
                                       final TableInfoDTO tableInfoDTO) throws Exception {
        BufferedReader reader = getBufferedReader(getImplementsResourcePath());
        Map<String, String> targetMap = getImplementsTargetMap(fileName, tableInfoDTO);
        writeToFile(writer, reader, targetMap);
    }

    private void writeToFile(final OutputStreamWriter writer, final BufferedReader reader,
                             final Map<String, String> targetMap) throws Exception {
        String line = null;

        while(null != (line = reader.readLine())) {
            if(line.contains("$")) {
                Iterator<Map.Entry<String, String>> iterator = targetMap.entrySet().iterator();
                while(iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    line = fileComponent.replaceTarget(entry.getKey(), entry.getValue(), line);
                }
            }
            writer.write(line);
            writer.write(FileComponent.LINE_SEPARATOR);
            writer.flush();
        }
        writer.close();
    }

    private BufferedReader getBufferedReader(final String resourcePath) throws Exception {
        String templateFilePath = this.getClass().getClassLoader()
                .getResource(resourcePath).getPath();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(templateFilePath)), "UTF-8"));
        return reader;
    }


    public static void main(String[] args) {
        String line = "public class $FileName";
        String target = "$FileName";
        String source = "TestClass";
        if(line.contains(target)) {
            String temp = line.replace(target, source);
            System.out.println(temp);
        }
        System.out.println(line);
    }
}
