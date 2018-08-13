package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * service生成组件
 * @Author shusheng
 * @Date 18/8/13 下午2:24
 */
@Component
public class ServiceCreateComponent {
    private final static Logger logger = LoggerFactory.getLogger(ServiceCreateComponent.class);
    private final static String SERVICE_PATH = "/service";
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private MysqlDataTypeTransferComponent mysqlDataTypeTransferComponent;

    public boolean createService(final File parentFile, final String tableName, final TableInfoDTO tableInfoDTO) {
        try {
            String fileName = mysqlDataTypeTransferComponent.getClassName(tableName);
        }catch(Exception e) {

        }
        return true;
    }

    /**
     * 创建接口
     * @param parentFile
     * @param fileName
     */
    private void createInterface(final File parentFile, final String fileName,
                                 final TableInfoDTO tableInfoDTO) throws Exception {
        String directoryPath = parentFile.getAbsolutePath() + SERVICE_PATH;
        File directory = fileComponent.mkdir(directoryPath);
        String interfacePath = directory.getAbsolutePath() + "/" + fileName + "service.java";
        File interfaceFile = fileComponent.createFile(interfacePath);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(interfaceFile), "UTF-8");
        writeToInterfaceFile(fileName, writer, tableInfoDTO);
    }

    /**
     * 创建实现类
     * @param parentFile
     * @param fileName
     */
    private void createImplements(final File parentFile, final String fileName,
                                  final TableInfoDTO tableInfoDTO) throws Exception {
        String directoryPath = parentFile.getAbsolutePath() + SERVICE_PATH + "/impl";
        File directory = fileComponent.mkdir(directoryPath);
        String implementsPath = directory.getAbsolutePath() + "/" + fileName + "serviceImpl.java";

    }

    /**
     * 写入接口文件
     * @param fileName
     * @param writer
     */
    private void writeToInterfaceFile(final String fileName, final OutputStreamWriter writer,
                                      final TableInfoDTO tableInfoDTO) throws Exception {

        String templateFilePath = this.getClass().getClassLoader()
                .getResource("java_template/ServiceInterface.txt").getPath();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(new File(templateFilePath)), "UTF-8"));
        String line = null;
        Map<String, String> targetMap = getTargetMap(fileName, tableInfoDTO);
        while(null != (line = reader.readLine())) {
            Iterator<Map.Entry<String, String>> iterator = targetMap.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if(line.contains(entry.getKey())) {
                    fileComponent.replaceTarget(entry.getKey(), entry.getValue(), line);
                }
            }
            writer.write(line);
            writer.flush();
        }
        writer.close();
    }

    /**
     * 写入实现文件
     * @param fileName
     * @param writer
     * @param tableInfoDTO
     * @throws Exception
     */
    private void writeToImplementsFile(final String fileName, final OutputStreamWriter writer,
                                       final TableInfoDTO tableInfoDTO) throws Exception {
        String templateFilePath = this.getClass().getClassLoader().getResource("").getPath();
    }

    private Map<String, String> getTargetMap(final String fileName, final TableInfoDTO tableInfoDTO) throws Exception{
        Map<String, String> targetMap = new HashMap<>();
        String className = fileName + "Service";
        targetMap.put("$FileNameService", className);

        String param = fileName.toLowerCase().substring(0, 1) + fileName.substring(1);
        String paramDO = param + "DTO";
        targetMap.put("$fileNameDTO", paramDO);

        String typeDO = fileName + "DTO";
        targetMap.put("$FileNameDTO", typeDO);

        String dataType = mysqlDataTypeTransferComponent.transfer(tableInfoDTO.getDataType());
        targetMap.put("$DataType", dataType);

        targetMap.put("$FileNameDTO", typeDO);

        targetMap.put("$FileNameQuery", fileName + "Query");
        targetMap.put("$fileNameQuery", param + "Query");
        return targetMap;
    }

}
