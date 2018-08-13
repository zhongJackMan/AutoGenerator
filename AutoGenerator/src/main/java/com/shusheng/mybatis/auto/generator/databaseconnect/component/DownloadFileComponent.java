package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.DatabaseDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.AbstractConnectService;
import com.shusheng.mybatis.auto.generator.databaseconnect.service.TableInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.*;

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


    private final static ThreadPoolExecutor tpe;
    static {
        tpe = new ThreadPoolExecutor(5, 10, 1L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(1000), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    tpe.getQueue().put(r);
                } catch (InterruptedException e) {
                }
            }
        });
        tpe.allowCoreThreadTimeOut(true);
    }
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
        final File parentFile = fileComponent.mkdir(parentFilePath.toString());
        ExecutorCompletionService<Boolean> es = new ExecutorCompletionService<>(tpe);
        domainCreateComponent.createDomainFile(tableInfoDTOS, parentFile, databaseDTO.getTableName());
//        es.submit(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                return domainCreateComponent.createDomainFile(tableInfoDTOS, parentFile, databaseDTO.getTableName());
//            }
//        });
    }

}
