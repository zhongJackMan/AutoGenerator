package com.shusheng.mybatis.auto.generator.controller;

import com.shusheng.mybatis.auto.generator.databaseconnect.component.DownloadFileComponent;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseTypeEnum;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.AbstractConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author shusheng
 * @createTime 2018/8/11/ 下午8:02
 */
@Controller
@RequestMapping(value = "/generation")
public class GenerationModalController {
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
    public String testConnection(HttpServletRequest request) {

        return null;
    }

    @PostMapping(value = "/file")
    public void downloadGeneratorFile(HttpServletRequest request, HttpServletResponse response) {
        DatabaseDTO databaseDTO = getQueryDTO(request);
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

    private DatabaseDTO getQueryDTO(HttpServletRequest request) {
        String url = request.getParameter("url");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String database = request.getParameter("databaseCode");
        DatabaseDTO databaseDTO = new DatabaseDTO();
        databaseDTO.setUrl(url).setUserName(userName).setPassword(password)
                .setDataBaseType(Integer.parseInt(database));
        return databaseDTO;
    }
}
