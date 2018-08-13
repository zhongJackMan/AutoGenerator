package com.shusheng.mybatis.auto.generator.databaseconnect.modal;

/**
 * 表信息
 * @Author shusheng
 * @createTime 2018/8/12/ 下午10:10
 */
public class TableInfoDTO {

    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 字段类型
     */
    private String dataType;
    /**
     * 注释
     */
    private String columnComment;

    public String getColumnName() {
        return columnName;
    }

    public TableInfoDTO setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public String getDataType() {
        return dataType;
    }

    public TableInfoDTO setDataType(String dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public TableInfoDTO setColumnComment(String columnComment) {
        this.columnComment = columnComment;
        return this;
    }
}
