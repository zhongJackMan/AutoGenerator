package com.shusheng.mybatis.auto.generator.controller.modal;

/**
 * @Author shusheng
 * @Date 18/9/13 下午2:53
 */
public class QueryVO {
    private String url;
    private String userName;
    private String password;
    private String tableSchema;
    private String port;
    private Integer databaseCode;
    private Integer pageNo;
    private Integer pageSize;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Integer getDatabaseCode() {
        return databaseCode;
    }

    public void setDatabaseCode(Integer databaseCode) {
        this.databaseCode = databaseCode;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
