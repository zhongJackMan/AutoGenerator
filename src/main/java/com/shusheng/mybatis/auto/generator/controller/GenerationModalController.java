package com.shusheng.mybatis.auto.generator.controller;

import com.alibaba.fastjson.JSONObject;
import com.shusheng.mybatis.auto.generator.controller.modal.QueryVO;
import com.shusheng.mybatis.auto.generator.databaseconnect.component.DownloadFileComponent;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseTypeEnum;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.AbstractConnectService;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.TableInfoService;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shusheng
 * @createTime 2018/8/11/ 下午8:02
 */
@Controller
@RequestMapping(value = "/generation")
public class GenerationModalController {
    private final static Logger logger = LoggerFactory.getLogger(GenerationModalController.class);
    @Autowired
    private AbstractConnectService abstractConnectService;
    @Autowired
    private DownloadFileComponent downloadFileComponent;
    @Autowired
    private TableInfoService tableInfoService;

    /**
     * 初始化页面
     */
    @GetMapping(value = "/index")
    public String init(ModelMap map) {
        QueryVO query = new QueryVO();
        query.setDatabaseCode(DatabaseTypeEnum.MYSQL.getCode());
        query.setPageNo(0);
        query.setPageSize(20);
        map.put("query", query);
        return "index";
    }

    @PostMapping(value = "/list")
    public String GenerateModalClass(HttpServletRequest request, ModelMap modelMap) {
        try {
            Pair<DatabaseDTO, QueryVO> pair = getQueryDTO(request);
            String pageNo = request.getParameter("pageNo");
            String pageSize = request.getParameter("pageSize");
            if(StringUtils.isEmpty(pageNo) || StringUtils.isEmpty(pageSize)) {
                throw new Exception("分页参数不能为空!");
            }

            QueryVO query = pair.getRight();
            query.setPageNo(Integer.parseInt(pageNo));
            query.setPageSize(Integer.parseInt(pageSize));
            modelMap.put("query", query);

            List<Map<String, String>> result = tableInfoService.getTableNames(
                    abstractConnectService.getDataBaseConnection(pair.getLeft()),
                    pair.getLeft(),
                    Integer.parseInt(pageNo),
                    Integer.parseInt(pageSize));
            modelMap.put("code", 0);
            modelMap.put("data", result);


        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            modelMap.put("code", -1);
            modelMap.put("msg", "查询异常");
        }
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/columns/list")
    public String queryColumnsList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            Pair<DatabaseDTO, QueryVO> pair = getQueryDTO(request);
            List<TableInfoDTO> list = tableInfoService.getTableInfo(
                    abstractConnectService.getDataBaseConnection(pair.getLeft()),
                    pair.getLeft());
            jsonObject.put("code", 0);
            jsonObject.put("list", list);
        }catch(Exception e) {
            logger.error("查询异常", e);
            jsonObject.put("code", "-1");
            jsonObject.put("msg", "查询异常!");
        }
        return jsonObject.toJSONString();
    }

    @PostMapping(value = "/connection")
    @ResponseBody
    public String testConnection(HttpServletRequest request) {
        Map<String, Object> isSuccess = new HashMap<>();
        try {
            DatabaseDTO databaseDTO = getQueryDTO(request).getLeft();
            isSuccess.put("isSuccess", abstractConnectService.testConnection(databaseDTO));
            isSuccess.put("code", "0");
        }catch(Exception e) {
            logger.error(e.getMessage(), e);
            isSuccess.put("code", "-1");
            isSuccess.put("msg", "处理异常!");
        }
        return isSuccess.toString();
    }

    @PostMapping(value = "/file")
    public void downloadGeneratorFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        DatabaseDTO databaseDTO = getQueryDTO(request).getLeft();
        downloadFileComponent.downloadFile(databaseDTO, response);
    }

    @GetMapping(value = "/test")
    public String testGenerator(HttpServletResponse response) {
        DatabaseDTO databaseDTO = new DatabaseDTO();
        databaseDTO.setDataBaseType(DatabaseTypeEnum.MYSQL.getCode());
        databaseDTO.setUserName("tbj");
        databaseDTO.setPassword("tbj900900");
        databaseDTO.setTableName("crs_share_201802");
        databaseDTO.setTableSchema("crs");
        databaseDTO.setUrl("jdbc:mysql://db.tbj.com/information_schema");
        try {
            downloadFileComponent.downloadFile(databaseDTO, response);
        }catch (Exception e){}
        return null;
    }

    private Pair<DatabaseDTO, QueryVO> getQueryDTO(HttpServletRequest request) throws Exception {
        String url = request.getParameter("url");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        Integer database = Integer.parseInt(request.getParameter("databaseCode"));
        String tableName = request.getParameter("tableName");
        String modelName = request.getParameter("modelName");
        String tableSchema = request.getParameter("tableSchema");
        String port = request.getParameter("port");
        if(StringUtils.isEmpty(port)) {
            port = "3306";
        }

        DatabaseDTO databaseDTO = new DatabaseDTO();
        databaseDTO.setUserName(userName).setPassword(password)
                .setDataBaseType(database);
        databaseDTO.setModelName(modelName);
        databaseDTO.setTableName(tableName);
        databaseDTO.setTableSchema(tableSchema);
        if(DatabaseTypeEnum.MYSQL.getCode().equals(database)) {
            databaseDTO.setUrl("jdbc:mysql://" + url + ":" + port + "/information_schema");
        }

        QueryVO query = new QueryVO();
        query.setUrl(url);
        query.setPort(port);
        query.setPassword(password);
        query.setUserName(userName);
        query.setTableSchema(tableSchema);

        return new ImmutablePair<>(databaseDTO, query);
    }



}
