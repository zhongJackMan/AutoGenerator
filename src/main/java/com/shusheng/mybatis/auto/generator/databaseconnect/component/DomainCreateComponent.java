package com.shusheng.mybatis.auto.generator.databaseconnect.component;

import com.shusheng.mybatis.auto.generator.databaseconnect.modal.TableInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.tools.jconsole.Tab;

import java.io.*;
import java.util.ArrayList;
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
            List<TableInfoDTO> queryDTOs = getQueryDTO(tableInfoDTOS);
            this.writeFileTemplate(filename, queryDTOs, "Query", writer);
            /*
             * orderBy
             */
            File orderByFile = fileComponent.createFile(domainFile.getAbsolutePath() + "/OrderBy.java");
            writer = new OutputStreamWriter(new FileOutputStream(orderByFile), "UTF-8");
            this.writeToOrderBy(writer);

        }catch(Exception e) {
            logger.error(e.getMessage(), e);
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
                    .append(gapTab).append(" * ").append(dto.getColumnComment()).append(lineSeparator)
                    .append(gapTab).append(" */").append(lineSeparator);
            /*属性*/
            String dataType = mysqlDataTypeTransferComponent.transfer(dto.getDataType().toLowerCase());
            if(null == dataType) {
                dataType = dto.getDataType();
            }
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
                    .append(gapTab).append("}").append(lineSeparator).append(lineSeparator);
            getterStr.append(gapTab).append("public void set").append(upColumn).append("(").append(dataType)
                    .append(" ").append(column).append(" ) {").append(lineSeparator)
                    .append(gapTab).append(gapTab).append("this.")
                    .append(column).append(" = ").append(column).append(";").append(lineSeparator)
                    .append(gapTab).append("}").append(lineSeparator);
        }
        getterStr.append(lineSeparator).append("}");
        writer.write(tempStr.toString());
        writer.write(getterStr.toString());
        writer.flush();
        writer.close();
    }

    /**
     * 写入OrderBy.java
     * @param writer
     * @throws Exception
     */
    private void writeToOrderBy(OutputStreamWriter writer) throws Exception {
        String templatePath = this.getClass().getClassLoader()
                .getResource("java_template/OrderBy.txt").getPath();
        File file = fileComponent.createFile(templatePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8"));
        String line = null;
        while(null != (line = reader.readLine())) {
            writer.write(line);
            writer.write(FileComponent.LINE_SEPARATOR);
            writer.flush();
        }
        writer.close();
    }

    /**
     * 设置查询参数
     * @param tableInfoDTOS
     */
    private List<TableInfoDTO> getQueryDTO(final List<TableInfoDTO> tableInfoDTOS) {
        List<TableInfoDTO> queryDTOs = new ArrayList<>(tableInfoDTOS.size());
        for(TableInfoDTO dto : tableInfoDTOS) {
            TableInfoDTO queryDTO = new TableInfoDTO();
            BeanUtils.copyProperties(dto, queryDTO);
            queryDTOs.add(queryDTO);
        }
        TableInfoDTO queryDTO = new TableInfoDTO();
        queryDTO.setColumnComment("每页查询条数").setColumnName("pageSize").setDataType("int");
        queryDTOs.add(queryDTO);

        TableInfoDTO pageNo = new TableInfoDTO();
        pageNo.setColumnName("pageNo").setDataType("int").setColumnComment("查询分页参数");
        queryDTOs.add(pageNo);

        TableInfoDTO egtCreateTime = new TableInfoDTO();
        egtCreateTime.setColumnName("egtCreateTime").setDataType("datetime").setColumnComment("create_time >= egtCreateTime");
        queryDTOs.add(egtCreateTime);

        TableInfoDTO eltCreateTime = new TableInfoDTO();
        eltCreateTime.setColumnName("eltCreateTime").setDataType("datetime").setColumnComment("create_time <= eltCreateTime");
        queryDTOs.add(eltCreateTime);

        TableInfoDTO ltCreateTime = new TableInfoDTO();
        ltCreateTime.setColumnName("ltCreateTime").setDataType("datetime").setColumnComment("create_time < ltCreateTime");
        queryDTOs.add(ltCreateTime);

        TableInfoDTO gtCreateTime = new TableInfoDTO();
        gtCreateTime.setColumnName("gtCreateTime").setDataType("datetime").setColumnComment("create_time > gtCreateTime");
        queryDTOs.add(gtCreateTime);

        TableInfoDTO orderBy = new TableInfoDTO();
        orderBy.setColumnName("orderBy").setDataType("List<OrderBy>").setColumnComment("排序字段集合");
        queryDTOs.add(orderBy);
        return queryDTOs;
    }

}
