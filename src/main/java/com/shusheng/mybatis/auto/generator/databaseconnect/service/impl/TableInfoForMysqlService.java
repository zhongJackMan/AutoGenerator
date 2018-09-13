package com.shusheng.mybatis.auto.generator.databaseconnect.service.impl;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.TableInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shusheng
 * @createTime 2018/8/12/ 下午10:15
 */
@Service("TableInfoForMysqlService")
public class TableInfoForMysqlService implements TableInfoService {
    private final static Logger logger = LoggerFactory.getLogger(TableInfoForMysqlService.class);

    @Override
    public List<TableInfoDTO> getTableInfo(Connection connection, DatabaseDTO databaseDTO) {
        List<TableInfoDTO> resultList = null;
        try {
            StringBuilder executeSql = new StringBuilder("select COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT from COLUMNS");
            executeSql.append(" where table_name = ? and table_schema = ?");
            logger.info("the sql is : " + executeSql.toString());
            PreparedStatement preparedStatement = connection.prepareStatement(executeSql.toString());
            preparedStatement.setString(1, databaseDTO.getTableName());
            preparedStatement.setString(2, databaseDTO.getTableSchema());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultList = new ArrayList<>();
            while (resultSet.next()) {
                TableInfoDTO dto = new TableInfoDTO();
                resultList.add(dto);
                dto.setColumnName(resultSet.getString("COLUMN_NAME"))
                        .setDataType(resultSet.getString("DATA_TYPE"))
                        .setColumnComment(resultSet.getString("COLUMN_COMMENT"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return resultList;
    }

    @Override
    public List<Map<String, String>> getTableNames(Connection connection, DatabaseDTO databaseDTO, Integer pageNo, Integer pageSize) {
        List<Map<String, String>> result = null;
        try {
            StringBuilder executeSql = new StringBuilder("select TABLE_NAME, TABLE_COMMENT from TABLES");
            executeSql.append(" where TABLE_SCHEMA = ? limit ?, ?");
            logger.error("the query sql is : " + executeSql.toString());
            PreparedStatement preparedStatement = connection.prepareStatement(executeSql.toString());
            preparedStatement.setString(1, databaseDTO.getTableSchema());
            preparedStatement.setInt(2, pageNo);
            preparedStatement.setInt(3, pageSize);
            ResultSet resultSet = preparedStatement.executeQuery();

            result = new ArrayList<>();
            while(resultSet.next()) {
                Map<String, String> map = new HashMap<>(1);
                result.add(map);
                map.put("tableName", resultSet.getString("TABLE_NAME"));
                map.put("tableComment", resultSet.getString("TABLE_COMMENT"));
            }
        }catch(Exception e) {
            logger.error("query table error", e);
        }
        return result;
    }
}
