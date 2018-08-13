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
            File DOFile = fileComponent.createFile(domainFile.getAbsolutePath() + "/" +  filename + "DO" + ".java");
            writer = new OutputStreamWriter(new FileOutputStream(DOFile), "UTF-8");
            /*
             * tempDO
             */
            this.writeFileTemplate(filename, tableInfoDTOS, "DO", writer);
            /*
             * tempDTO
             */
            File DTOFile = fileComponent.createFile(domainFile.getAbsolutePath() + "/" + filename + "DTO" + ".java");
            writer = new OutputStreamWriter(new FileOutputStream(DTOFile), "UTF-8");
            this.writeFileTemplate(filename, tableInfoDTOS, "DTO", writer);
            /*
             * temp
             */
            File tempFile = fileComponent.createFile(domainFile.getAbsolutePath() + "/" + filename + ".java");
            writer = new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8");
            this.writeFileTemplate(filename, tableInfoDTOS, "", writer);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            try {
                if(null != writer) {
                    writer.close();
                }
            }catch (Exception e){}
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
        StringBuilder tempStr = new StringBuilder();
        StringBuilder getterStr = new StringBuilder();
        tempStr.append("package;");
        tempStr.append(FileComponent.LINE_SEPARATOR);
        tempStr.append("public class ").append(fileName).append(suffix).append(" {");
        for (TableInfoDTO dto : tableInfoDTOS) {
            tempStr.append(FileComponent.LINE_SEPARATOR);
            /*注释*/
            tempStr.append("/**").append(FileComponent.LINE_SEPARATOR)
                    .append(" *").append(FileComponent.LINE_SEPARATOR)
                    .append(" * ").append(dto.getColumnComment())
                    .append(" */").append(FileComponent.LINE_SEPARATOR);
            /*属性*/
            String dataType = mysqlDataTypeTransferComponent.transfer(dto.getDataType().toLowerCase());
            String column = mysqlDataTypeTransferComponent.getFiledName(dto.getColumnName());
            tempStr.append("private ").append(dataType)
                    .append(" ").append(column).append(";").append(FileComponent.LINE_SEPARATOR);
            StringBuilder upColumn = new StringBuilder(column.toUpperCase()
                    .substring(0, 1));
            upColumn.append(column.substring(1));
            /*getter & setter*/
            getterStr.append(FileComponent.LINE_SEPARATOR).append("public ").append(dataType)
                    .append(" get").append(upColumn).append("() {").append(FileComponent.LINE_SEPARATOR)
                    .append("return ").append(column).append(";").append(FileComponent.LINE_SEPARATOR).append("}")
                    .append(FileComponent.LINE_SEPARATOR).append(FileComponent.LINE_SEPARATOR);
            getterStr.append("public void set").append(upColumn).append("(").append(dataType)
                    .append(" ").append(column).append(" ) {").append(FileComponent.LINE_SEPARATOR).append("this.")
                    .append(column).append(" = ").append(column).append(";").append(FileComponent.LINE_SEPARATOR)
                    .append("}").append(FileComponent.LINE_SEPARATOR);
        }
        getterStr.append(FileComponent.LINE_SEPARATOR).append("}");
        writer.write(tempStr.toString());
        writer.write(getterStr.toString());
        writer.flush();
    }

}
