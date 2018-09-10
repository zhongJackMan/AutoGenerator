package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.AbstractConnectService;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author shusheng
 * @createTime 2018/8/12/ 下午9:54
 */
@Component
public class DownloadFileComponent {
    /**
     * 文件存储路径
     */
    private final static String LOCAL_PATH = "/data/temp";
    @Autowired
    private AbstractConnectService abstractConnectService;
    @Autowired
    @Qualifier("TableInfoForMysqlService")
    private TableInfoService tableInfoService;
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private DomainCreateComponent domainCreateComponent;
    @Autowired
    private DaoCreateComponent daoCreateComponent;
    @Autowired
    private ServiceCreateComponent serviceCreateComponent;

    /**
     * 下载文件
     * @param databaseDTO
     * @param response
     */
    public void downloadFile(final DatabaseDTO databaseDTO, final HttpServletResponse response) throws Exception {

        Connection connection = abstractConnectService.getDataBaseConnection(databaseDTO);
        if(null == connection) {
            return;
        }
        final List<TableInfoDTO> tableInfoDTOS = tableInfoService.getTableInfo(connection, databaseDTO);
        if(CollectionUtils.isEmpty(tableInfoDTOS)) {
            return;
        }
        StringBuilder parentFilePath = new StringBuilder(LOCAL_PATH);
        parentFilePath.append("/").append(databaseDTO.getTableName());
        String className = StringUtils.isEmpty(databaseDTO.getModelName()) ?
                databaseDTO.getTableName() : databaseDTO.getModelName();
        final File parentFile = fileComponent.mkdir(parentFilePath.toString());
        domainCreateComponent.createDomainFile(tableInfoDTOS, parentFile, className);
        daoCreateComponent.createDaoFile(parentFile, className, tableInfoDTOS.get(0),
                tableInfoDTOS);
        serviceCreateComponent.createService(parentFile, className, tableInfoDTOS.get(0));

        setResponse(response, databaseDTO.getTableName() + ".zip");

        /**
         * 压缩文件
         */
        ZipOutputStream writeOut = new ZipOutputStream(response.getOutputStream());
        zipFile(writeOut, parentFile, "");
        writeOut.close();
        fileComponent.deleteFile(parentFile);
    }

    private void zipFile(ZipOutputStream zipOutputStream, File file, String parentPath) throws Exception {


        if(!file.exists()) {
            return;
        }
        if(file.isDirectory()) {
            StringBuilder tempPath = new StringBuilder(parentPath);
            tempPath.append(file.getName());
            tempPath.append("/");

            File[] files = file.listFiles();
            if(0 == files.length) {
                return;
            }
            for(File temp : files) {
                zipFile(zipOutputStream, temp, tempPath.toString());
            }
        }else if(file.isFile()) {
            StringBuilder tempPath = new StringBuilder(parentPath);
            tempPath.append(file.getName());
            ZipEntry zipEntry = new ZipEntry(tempPath.toString());
            zipOutputStream.putNextEntry(zipEntry);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = null;
            while(null != (line = reader.readLine())) {
                zipOutputStream.write(line.getBytes("UTF-8"));
                zipOutputStream.write(FileComponent.LINE_SEPARATOR.getBytes());
            }
            zipOutputStream.flush();
            zipOutputStream.closeEntry();
            reader.close();
        }
    }

    private void setResponse(HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
    }

	public static void main(String[] args) {
		String path = new DownloadFileComponent().getClass().getClassLoader()
						.getResource("java_template/DO.text").getPath();
		System.out.println(path);

	}

}
