package com.shusheng.mybatis.auto.generator.databaseconnect.service;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @Author shusheng
 * @createTime 2018/8/12/ 下午10:10
 */
public interface TableInfoService {

    /**
     * 获取对应的表信息
     * @param connection
     * @param databaseDTO
     * @return
     */
    List<TableInfoDTO> getTableInfo(final Connection connection, final DatabaseDTO databaseDTO);

    /**
     * 查询库所有的表
     * @param connection
     * @param databaseDTO
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Map<String, String>> getTableNames(final Connection connection, final DatabaseDTO databaseDTO,
                                              final Integer pageNo, final Integer pageSize);
}

