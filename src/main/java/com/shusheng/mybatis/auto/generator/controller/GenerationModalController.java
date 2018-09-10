package com.shusheng.mybatis.auto.generator.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shusheng.mybatis.auto.generator.databaseconnect.component.DownloadFileComponent;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseTypeEnum;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.AbstractConnectService;
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

    /**
     * 初始化页面
     */
    @GetMapping(value = "/temp")
    public String init(ModelMap map) {
        return "index";
    }

    @GetMapping(value = "/modal")
    public String GenerateModalClass(HttpServletRequest request) {
        return "temp";
    }

    @PostMapping(value = "/connection")
    @ResponseBody
    public String testConnection(HttpServletRequest request) {
        Map<String, Object> isSuccess = new HashMap<>();
        try {
            DatabaseDTO databaseDTO = getQueryDTO(request);
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
        DatabaseDTO databaseDTO = getQueryDTO(request);
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

    private DatabaseDTO getQueryDTO(HttpServletRequest request) throws Exception {
        String url = request.getParameter("url");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        Integer database = Integer.parseInt(request.getParameter("databaseCode"));
        String tableName = request.getParameter("tableName");
        String modelName = request.getParameter("modelName");
        String tableSchema = request.getParameter("tableSchema");

        DatabaseDTO databaseDTO = new DatabaseDTO();
        databaseDTO.setUserName(userName).setPassword(password)
                .setDataBaseType(database);
        databaseDTO.setModelName(modelName);
        databaseDTO.setTableName(tableName);
        databaseDTO.setTableSchema(tableSchema);
        if(DatabaseTypeEnum.MYSQL.getCode().equals(database)) {
            databaseDTO.setUrl("jdbc:mysql://" + url + "/information_schema");
        }

        return databaseDTO;
    }
}
