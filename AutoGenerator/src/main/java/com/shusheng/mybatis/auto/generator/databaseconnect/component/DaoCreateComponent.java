package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Dao生成组件
 * @Author shusheng
 * @createTime 2018/8/13/ 下午11:16
 */
@Component
public class DaoCreateComponent extends AbstractComponentCreator {
    @Autowired
    private MysqlDataTypeTransferComponent mysqlDataTypeTransferComponent;

    @Override
    protected String getBaseDirectory() {
        return "/dao";
    }

    @Override
    protected String getInterfaceEndSuffix() {
        return "Dao.java";
    }

    @Override
    protected String getInterfaceResourcePath() {
        return "java_template/DaoInterface.txt";
    }

    @Override
    protected Map<String, String> getInterfaceTargetMap(String fileName, TableInfoDTO tableInfoDTO) throws Exception {
        Map<String, String> targetMap = new HashMap<>();
        String className = fileName + "Dao";
        targetMap.put("$FileNameDao", className);

        String param = fileName.toLowerCase().substring(0, 1) + fileName.substring(1);
        String paramDO = param + "DO";
        targetMap.put("$fileNameDO", paramDO);

        String typeDO = fileName + "DO";
        targetMap.put("$FileNameDO", typeDO);

        String dataType = mysqlDataTypeTransferComponent.transfer(tableInfoDTO.getDataType());
        targetMap.put("$DataType", dataType);

        targetMap.put("$FileNameDO", typeDO);

        targetMap.put("$FileNameQuery", fileName + "Query");
        targetMap.put("$fileNameQuery", param + "Query");
        return targetMap;
    }

    @Override
    protected String getImplementsEndSuffix() {
        return null;
    }

    @Override
    protected String getImplementsResourcePath() {
        return null;
    }

    @Override
    protected Map<String, String> getImplementsTargetMap(String fileName, TableInfoDTO tableInfoDTO) {
        return null;
    }

    @Override
    protected String getXMLTemplatePath() {
        return null;
    }

    @Override
    protected boolean isWriteToJAVA() {
        return false;
    }
}
