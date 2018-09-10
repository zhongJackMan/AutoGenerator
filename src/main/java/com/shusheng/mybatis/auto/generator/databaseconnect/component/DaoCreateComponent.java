package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dao生成组件
 *
 * @Author shusheng
 * @createTime 2018/8/13/ 下午11:16
 */
@Component("daoCreateComponent")
public class DaoCreateComponent extends AbstractComponentCreator {

    private final static ThreadLocal<List<TableInfoDTO>> localList = new ThreadLocal<>();
    private final static ThreadLocal<String> localTableName = new ThreadLocal<>();
    private final static String GAP_TAB = "\t";
    private final static String LINE_SEPARATOR = FileComponent.LINE_SEPARATOR;
    @Autowired
    private MysqlDataTypeTransferComponent mysqlDataTypeTransferComponent;

    public boolean createDaoFile(final File parenFile, final String tableName,
                                 final TableInfoDTO tableInfoDTO, final List<TableInfoDTO> tableInfoDTOS) {
        localList.set(tableInfoDTOS);
        localTableName.set(tableName);
        this.createComponent(parenFile, tableName, tableInfoDTO);
        return true;
    }

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
        targetMap.put("$FileName", className);
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
        return ".xml";
    }

    @Override
    protected String getImplementsResourcePath() {
        return "java_template/MybatisXML.xml";
    }

    @Override
    protected Map<String, String> getImplementsTargetMap(String fileName, TableInfoDTO tableInfoDTO) throws Exception {
        Map<String, String> targetMap = new HashMap<>();
        List<TableInfoDTO> list = localList.get();
        targetMap.put("$nameSpace", "");
        targetMap.put("$tableName", localTableName.get());
        targetMap.put("$dataType", mysqlDataTypeTransferComponent.transfer(tableInfoDTO.getDataType()));
        targetMap.put("$FileNameQuery", fileName + "Query");
        targetMap.put("$FileNameDO", fileName + "DO");
        targetMap.put("$FileName", fileName);
        StringBuilder mapStr = new StringBuilder();
        StringBuilder baseSqlStr = new StringBuilder();
        StringBuilder selectWhereStr = new StringBuilder();
        StringBuilder updateStr = new StringBuilder();
        StringBuilder insertStr = new StringBuilder();
        boolean isLast = false;
        int i = 1;
        for(TableInfoDTO dto : list) {
            if(i == list.size()) {
                isLast = true;
            }
            String column = dto.getColumnName();
            String property = mysqlDataTypeTransferComponent.getFiledName(column);
            getMapStr(mapStr, column, property);
            getBaseSql(baseSqlStr, column, isLast);
            getSelectWhere(selectWhereStr, column, property);
            getUpdateStr(updateStr, column, property, isLast);
            getInsertValueStr(insertStr, property, isLast);
            i++;
        }
        targetMap.put("$mapStr", mapStr.toString());
        targetMap.put("$sqlStr", baseSqlStr.toString());
        targetMap.put("$selectWhereStr", selectWhereStr.toString());
        targetMap.put("$updateColumnsStr", updateStr.toString());
        targetMap.put("$insertValueStr", insertStr.toString());
        return targetMap;
    }

    private void getMapStr(StringBuilder str, String column, String property) {
        str.append(GAP_TAB).append(GAP_TAB)
                .append("<result column = \"").append(column).append("\" property = \"")
                .append(property).append("\" />")
                .append(LINE_SEPARATOR);
    }

    private void getBaseSql(StringBuilder str, String column, boolean isLast) {
        str.append(GAP_TAB).append(GAP_TAB).append(GAP_TAB)
                .append(column);
        if(!isLast) {
            str.append(",");
        }
        str.append(LINE_SEPARATOR);
    }

    private void getSelectWhere(StringBuilder str, String column, String property) {
        str.append(GAP_TAB).append(GAP_TAB)
                .append("<if test = \"").append(property).append(" != null and ")
                .append(property).append(" != ''\">").append(LINE_SEPARATOR)
                .append(GAP_TAB).append(GAP_TAB).append(GAP_TAB)
                .append("and ").append(column).append(" = ").append("#{").append(property).append("}")
                .append(LINE_SEPARATOR).append(GAP_TAB).append(GAP_TAB).append("</if>").append(LINE_SEPARATOR);
    }

    private void getUpdateStr(StringBuilder str, String column, String property, boolean isLast) {
        str.append(GAP_TAB).append(GAP_TAB)
                .append("<if test = \"").append(property).append(" != null and ")
                .append(property).append(" != ''\">").append(LINE_SEPARATOR)
                .append(GAP_TAB).append(GAP_TAB).append(GAP_TAB)
                .append(column).append(" = ").append("#{").append(property).append("}");
        if(!isLast) {
            str.append(",");
        }
        str.append(LINE_SEPARATOR).append(GAP_TAB).append(GAP_TAB).append("</if>").append(LINE_SEPARATOR);
    }

    private void getInsertValueStr(StringBuilder str, String property, boolean isLast) {
        str.append(GAP_TAB).append(GAP_TAB).append(GAP_TAB)
                .append("#{").append(property).append("}");
        if(!isLast) {
            str.append(",");
        }
        str.append(LINE_SEPARATOR);
    }
}
