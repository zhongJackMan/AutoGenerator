package com.shusheng.mybatis.auto.generator.databaseconnect.modal;

/**
 * 数据库实体
 * @Author shusheng
 * @createTime 2018/8/11/ 下午7:30
 */
public class DatabaseDTO {
    private String userName;
    private String password;
    private String url;
    private Integer dataBaseType;
    private String tableName;
    private String tableSchema;

    public String getUserName() {
        return userName;
    }

    public DatabaseDTO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DatabaseDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public DatabaseDTO setUrl(String url) {
        this.url = url;
        return this;
    }

    public Integer getDataBaseType() {
        return dataBaseType;
    }

    public void setDataBaseType(Integer dataBaseType) {
        this.dataBaseType = dataBaseType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }
}
