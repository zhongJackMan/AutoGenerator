package com.shusheng.mybatis.auto.generator.databaseconnect.service;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @Author shusheng
 * @createTime 2018/8/11/ 下午7:27
 */
@Component
public class AbstractConnectService {
    private final static Logger logger = LoggerFactory.getLogger(AbstractConnectService.class);

    /**
     * 根据类型获取对应的数据库链接
     * @param dataBaseDTO
     * @return
     */
    public Connection getDataBaseConnection(DatabaseDTO dataBaseDTO) {
        Connection connection = null;
        try {
            logger.info("start create connection");
            String driverName = DatabaseTypeEnum.getEnumByCode(dataBaseDTO.getDataBaseType()).getDriver();
            Driver driver = (Driver) Class.forName(driverName).newInstance();
            Properties info = new Properties();
            info.put("user", dataBaseDTO.getUserName());
            info.put("password", dataBaseDTO.getPassword());
            connection = driver.connect(dataBaseDTO.getUrl(), info);
            logger.info("create connection success");
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("create connection failed");
        }
        return connection;
    }

    /**
     * 创建事务
     * @param connection
     * @return
     */
    public boolean createTransaction(Connection connection) {
        try {
            logger.info("start create transaction");
            if(!connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            logger.info("create transaction success");

        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("create transaction failed");
            return false;
        }
        return true;
    }

    /**
     * 提交事务
     * @param connection
     * @return
     */
    public boolean commitTransaction(Connection connection) {
        try {
            logger.info("start commit transaction");
            if(!connection.getAutoCommit()) {
                connection.commit();
            }
            logger.info("commit transaction success");
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("commit transaction failed");
            return false;
        }
        return true;
    }

    /**
     * 创建事务
     * @param connection
     * @return
     */
    public boolean rollbackTransaction(Connection connection) {
        try {
            logger.info("start rollback transaction");
            if(!connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            logger.info("rollback transaction success");

        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("rollback transaction failed");
            return false;
        }
        return true;
    }

    /**
     * 释放数据库链接
     * @param connection
     */
    public void releaseConnection(Connection connection) {
        try {
            logger.info("start release connection");
            connection.close();
            logger.info("release connection success");
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            logger.info("release connection failed");
        }
    }

    /**
     * 测试链接是否可用
     * @param dto
     * @return
     */
    public boolean testConnection(DatabaseDTO dto) {
        try {
            Connection connection = DriverManager.getConnection(dto.getUrl(), dto.getUserName(), dto.getPassword());
            if(null == connection) {
                return false;
            }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }

        return true;
    }
}
