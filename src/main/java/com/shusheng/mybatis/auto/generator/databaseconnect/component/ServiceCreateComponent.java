package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * service生成组件
 * @Author shusheng
 * @Date 18/8/13 下午2:24
 */
@Component("serviceCreateComponent")
public class ServiceCreateComponent extends AbstractComponentCreator {
    private final static Logger logger = LoggerFactory.getLogger(ServiceCreateComponent.class);
    private final static String SERVICE_PATH = "/service";
    @Autowired
    private MysqlDataTypeTransferComponent mysqlDataTypeTransferComponent;

    public boolean createService(final File parentFile, final String tableName, final TableInfoDTO tableInfoDTO) {
        return this.createComponent(parentFile, tableName, tableInfoDTO);
    }

    @Override
    protected String getBaseDirectory() {
        return SERVICE_PATH;
    }

    @Override
    protected String getInterfaceEndSuffix() {
        return "Service.java";
    }

    @Override
    protected String getInterfaceResourcePath() {
        return "java_template/ServiceInterface.txt";
    }

    @Override
    protected Map<String, String> getInterfaceTargetMap(String fileName, TableInfoDTO tableInfoDTO) throws Exception {
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

        targetMap.put("$FileName", fileName);

        targetMap.put("$FileNameQuery", fileName + "Query");
        targetMap.put("$fileNameQuery", param + "Query");
        return targetMap;
    }

    @Override
    protected String getImplementsEndSuffix() {
        return "ServiceImpl.java";
    }

    @Override
    protected String getImplementsResourcePath() {
        return "java_template/ServiceImplements.txt";
    }

    @Override
    protected Map<String, String> getImplementsTargetMap(String fileName, TableInfoDTO tableInfoDTO) {
        Map<String, String> targetMap = new HashMap<>();
        String className = fileName + "ServiceImpl";
        String interFaceName = fileName + "Service";
        String daoName = fileName + "DAO";
        targetMap.put("$FileNameServiceImpl", className);
        targetMap.put("$FileNameService", interFaceName);
        targetMap.put("$FileNameDAO", daoName);

        String param = fileName.toLowerCase().substring(0, 1) + fileName.substring(1);
        String paramDTO = param + "DTO";
        String typeDTO = fileName + "DTO";
        targetMap.put("$fileNameDTO", paramDTO);
        targetMap.put("$FileNameDTO", typeDTO);

        String paramDO = param + "DO";
        String typeDO = fileName + "DO";
        targetMap.put("$fileNameDO", paramDO);
        targetMap.put("$FileNameDO", typeDO);

        String paramDomain = param;
        String typeDomain = fileName;
        targetMap.put("$fileName", paramDomain);
        targetMap.put("$FileName", typeDomain);

        targetMap.put("$FileNameQuery", fileName + "Query");
        targetMap.put("$fileNameQuery", param + "Query");

        String daoParam = param + "DAO";
        targetMap.put("$fileNameDAO", daoParam);

        String dataType = mysqlDataTypeTransferComponent.transfer(tableInfoDTO.getDataType());
        targetMap.put("$DataType", dataType);

        return targetMap;
    }

}
