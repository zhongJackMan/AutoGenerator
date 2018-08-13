package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * 实体类生成组件
 *
 * @Author shusheng
 * @createTime 2018/8/12/ 下午9:58
 */
@Component
public class DomainCreateComponent {
    private final static Logger logger = LoggerFactory.getLogger(DomainCreateComponent.class);
    private final static String DOMAIN_PATH = "/domain";
    @Autowired
    private FileComponent fileComponent;
    @Autowired
    private MysqlDataTypeTransferComponent mysqlDataTypeTransferComponent;

    public boolean createDomainFile(final List<TableInfoDTO> tableInfoDTOS, final File parentFile, final String tableName) {
        OutputStreamWriter writer = null;
        try {
            String domainPath = parentFile.getAbsolutePath() + DOMAIN_PATH;
            File domainFile = fileComponent.mkdir(domainPath);
            String filename = mysqlDataTypeTransferComponent.getClassName(tableName);
            String commonFileName = domainFile.getAbsolutePath() + "/" + filename;
            File DOFile = fileComponent.createFile(commonFileName + "DO.java");
            writer = new OutputStreamWriter(new FileOutputStream(DOFile), "UTF-8");
            /*
             * tempDO
             */
            this.writeFileTemplate(filename, tableInfoDTOS, "DO", writer);
            /*
             * tempDTO
             */
            File DTOFile = fileComponent.createFile(commonFileName + "DTO.java");
            writer = new OutputStreamWriter(new FileOutputStream(DTOFile), "UTF-8");
            this.writeFileTemplate(filename, tableInfoDTOS, "DTO", writer);
            /*
             * temp
             */
            File tempFile = fileComponent.createFile(commonFileName + ".java");
            writer = new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8");
            this.writeFileTemplate(filename, tableInfoDTOS, "", writer);
            /*
             * query
             */
            File queryFile = fileComponent.createFile(commonFileName + "Query.java");
            writer = new OutputStreamWriter(new FileOutputStream(queryFile), "UTF-8");
            getQueryDTO(tableInfoDTOS);
            this.writeFileTemplate(filename, tableInfoDTOS, "Query", writer);

        }catch(Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if(null != writer) {
                    writer.close();
                }
            }catch(Exception e) {
            }
        }
        return true;
    }

    /**
     * 模板
     *
     * @param fileName
     * @param tableInfoDTOS
     * @param suffix
     * @return
     * @throws Exception
     */
    private void writeFileTemplate(final String fileName, final List<TableInfoDTO> tableInfoDTOS,
                                   final String suffix, final OutputStreamWriter writer) throws Exception {
        /*
         * package;
         * public class $className {
         *
         * '/'**
         *  *
         *  * $comment
         *  *'/'
         *  private $dataType $column;
         *
         *  public $dataType get$Column() {
         *      return $column;
         *  }
         *
         *  public void set$Column($dataType $column) {
         *      this.$column = $column;
         *  }
         *
         */
        String gapTab = "    ";
        String lineSeparator = FileComponent.LINE_SEPARATOR;

        StringBuilder tempStr = new StringBuilder();
        StringBuilder getterStr = new StringBuilder();
        tempStr.append("package;");
        tempStr.append(lineSeparator);
        tempStr.append("public class ").append(fileName).append(suffix).append(" {");
        for(TableInfoDTO dto : tableInfoDTOS) {
            tempStr.append(lineSeparator);
            /*注释*/
            tempStr.append(gapTab).append("/**").append(lineSeparator)
                    .append(gapTab).append(" *").append(lineSeparator)
                    .append(gapTab).append(" * ").append(dto.getColumnComment())
                    .append(gapTab).append(" */").append(lineSeparator);
            /*属性*/
            String dataType = mysqlDataTypeTransferComponent.transfer(dto.getDataType().toLowerCase());
            String column = mysqlDataTypeTransferComponent.getFiledName(dto.getColumnName());
            tempStr.append(gapTab).append("private ").append(dataType)
                    .append(" ").append(column).append(";").append(lineSeparator);
            StringBuilder upColumn = new StringBuilder(column.toUpperCase()
                    .substring(0, 1));
            upColumn.append(column.substring(1));
            /*getter & setter*/
            getterStr.append(lineSeparator)
                    .append(gapTab).append("public ").append(dataType).append(" get")
                    .append(upColumn).append("() {").append(lineSeparator)
                    .append(gapTab).append(gapTab).append("return ").append(column).append(";").append(lineSeparator)
                    .append("}").append(lineSeparator).append(lineSeparator);
            getterStr.append(gapTab).append("public void set").append(upColumn).append("(").append(dataType)
                    .append(" ").append(column).append(" ) {").append(lineSeparator)
                    .append(gapTab).append(gapTab).append("this.")
                    .append(column).append(" = ").append(column).append(";").append(lineSeparator)
                    .append("}").append(lineSeparator);
        }
        getterStr.append(lineSeparator).append("}");
        writer.write(tempStr.toString());
        writer.write(getterStr.toString());
        writer.flush();
    }

    /**
     * 设置查询参数
     * @param tableInfoDTOS
     */
    private void getQueryDTO(final List<TableInfoDTO> tableInfoDTOS) {
        TableInfoDTO queryDTO = new TableInfoDTO();
        queryDTO.setColumnComment("每页查询条数").setColumnName("pageSize").setDataType("int");
        tableInfoDTOS.add(queryDTO);
        queryDTO = new TableInfoDTO();
        queryDTO.setColumnName("pageNo").setDataType("int").setColumnComment("查询分页参数");
        tableInfoDTOS.add(queryDTO);
        queryDTO.setColumnName("egtCreateTime").setDataType("datetime").setColumnComment("create_time >= egtCreateTime");
        tableInfoDTOS.add(queryDTO);
        queryDTO.setColumnName("eltCreateTime").setDataType("datetime").setColumnComment("create_time <= eltCreateTime");
        tableInfoDTOS.add(queryDTO);
        queryDTO.setColumnName("ltCreateTime").setDataType("datetime").setColumnComment("create_time < ltCreateTime");
        tableInfoDTOS.add(queryDTO);
        queryDTO.setColumnName("gtCreateTime").setDataType("datetime").setColumnComment("create_time > gtCreateTime");
        tableInfoDTOS.add(queryDTO);
    }

}
