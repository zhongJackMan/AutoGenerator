package com.shusheng.mybatis.auto.generator.databaseconnect.modal;

/**
 * 数据库枚举类型
 * @Author shusheng
 * @createTime 2018/8/11/ 下午7:31
 */
public enum DatabaseTypeEnum {
    MYSQL(0, "com.mysql.jdbc.Driver"),
    ORACLE(1, "oracle"),
    SQLSERVER(2, "sqlServer");

    private Integer code;
    private String driver;

    private DatabaseTypeEnum(Integer code, String driver) {
        this.code = code;
        this.driver = driver;
    }

    public static DatabaseTypeEnum getEnumByCode(final Integer code) {
        for(DatabaseTypeEnum temp : DatabaseTypeEnum.values()) {
            if(temp.code.equals(code)) {
                return temp;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
