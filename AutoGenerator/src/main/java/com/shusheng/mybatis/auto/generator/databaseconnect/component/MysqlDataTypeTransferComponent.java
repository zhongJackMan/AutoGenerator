package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import org.springframework.stereotype.Component;


/**
 * @Author shusheng
 * @createTime 2018/8/12/ 下午11:00
 */
@Component
public class MysqlDataTypeTransferComponent {

    public String transfer(final String mysqlDataType) throws Exception {

        switch (mysqlDataType) {
            case "char":
            case "text":
            case "varchar":
                return "String";
            case "smallint":
            case "integer":
            case "int":
                return "Integer";
            case "float":
                return "Float";
            case "double":
                return "Double";
            case "bigint":
            case "numeric":
                return "BigDecimal";
            case "tinyint":
                return "Byte";
            case "datetime":
            case "timestamp":
                return "Date";
            case "boolean":
                return "Boolean";
            default:
                throw new Exception("unSupport dataType transfer");
        }
    }

    /**
     * 将mysql的表明转换成驼峰模式
     * @param tableName
     * @return
     */
    public String getClassName(final String tableName) {
        if(tableName.contains("_")) {
            return getUpperName(tableName, "_");
        }
        if(tableName.contains("-")) {
            return getUpperName(tableName, "-");
        }
        return tableName;
    }

    public String getFiledName(final String columnName) {
        if(columnName.contains("_")) {
            return getLowerName(columnName, "_");
        }
        if(columnName.contains("-")) {
            return getLowerName(columnName, "-");
        }
        return columnName;
    }

    private String getUpperName(final String tableName, final String splitStr) {
        String[] tempArray = tableName.split(splitStr);
        StringBuilder tempStr = new StringBuilder();
        for(String temp : tempArray) {
            tempStr.append(temp.toUpperCase().substring(0, 1))
                    .append(temp.substring(1));
        }
        return tempStr.toString();
    }

    private String getLowerName(final String name, final String splitStr) {
        String[] tempArray = name.split(splitStr);
        StringBuilder tempStr = new StringBuilder();
        int i = 0;
        for(String temp : tempArray) {
            if(i == 0) {
                tempStr.append(temp.toLowerCase().substring(0, 1));
            }else {
                tempStr.append(temp.toUpperCase().substring(0, 1));
            }
            tempStr.append(temp.substring(1));
            i++;
        }
        return tempStr.toString();
    }
}
