package com.nnroad.common.utils.poi;

import com.nnroad.common.annotation.Excel;
import com.nnroad.common.annotation.Excel.ColumnType;
import com.nnroad.common.annotation.Excel.Type;
import com.nnroad.common.annotation.Excels;
import com.nnroad.common.config.NNRoadConfig;
import com.nnroad.common.core.domain.AjaxResult;
import com.nnroad.common.core.domain.TreeEntity;
import com.nnroad.common.core.text.Convert;
import com.nnroad.common.exception.BusinessException;
import com.nnroad.common.utils.DateUtils;
import com.nnroad.common.utils.DictUtils;
import com.nnroad.common.utils.MessageUtils;
import com.nnroad.common.utils.StringUtils;
import com.nnroad.common.utils.reflect.ReflectUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Excel相关处理
 *
 * @author Hrone
 */
public class NnroadExcelUtil<T>
{
    private static final Logger log = LoggerFactory.getLogger(NnroadExcelUtil.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Type type;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Object[]> fields;

    /**
     * 统计列表
     */
    private Map<Integer, Double> statistics = new HashMap<Integer, Double>();

    /**
     * 数字格式
     */
    private static final DecimalFormat DOUBLE_FORMAT = new DecimalFormat("######0.00");

    /**
     * 实体对象
     */
    public Class<T> clazz;

    private Function<List<Object[]>, List<Object[]>> sortMethod=null;

    public NnroadExcelUtil(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public NnroadExcelUtil(Class<T> clazz, Function<List<Object[]>, List<Object[]>> sortMethod) {
        this.clazz = clazz;
        this.sortMethod = sortMethod;
    }

    public NnroadExcelUtil(Class<T> clazz, boolean flag, Function<List<Field>, List<Field>> sortMethod) {

    }

    public void init(List<T> list, String sheetName, Type type)
    {
        if (list == null)
        {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
    }

    /**
     * 对excel表单默认第一个索引名转换成list
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(InputStream is) throws Exception
    {
        return importExcel(StringUtils.EMPTY, is);
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * 如果指定的sheet不存在直接报错
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(String sheetName, InputStream is, int headerSatrt, int headerEnd, String...requireHeader) throws Exception
    {
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }

        if (sheet == null)
        {
            throw new IOException(MessageFormat.format(MessageUtils.message("excel.sheet_not_exist"), sheetName));
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0)
        {
            // 定义一个map用于存放excel列的序号和field.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // 获取表头
            Row heard = sheet.getRow(headerEnd);
            if(headerEnd != 0){
                for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
                {
                    Cell cell = heard.getCell(i);
                    if (StringUtils.isNotNull(cell) && StringUtils.isNotEmpty(cell.toString()))
                    {
                        // 标题不为空，则正常，不需要往前取一行
                    }
                    else
                    {
                        // 标题为空，则需要往前取一行的列
                        Cell cellTemp = sheet.getRow(headerSatrt).getCell(i);
                        heard.createCell(i).setCellValue(cellTemp!=null?cellTemp.toString():"");
                    }
                }

            }
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                if (StringUtils.isNotNull(cell))
                {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                }
                else
                {
                    cellMap.put(null, i);
                }
            }
            for (String head: requireHeader) {
                if(!cellMap.containsKey(head)){
                    throw new IOException(MessageFormat.format(MessageUtils.message("excel.sheet_head_not_exist"), sheetName, headerSatrt+1, headerEnd+1));
                }
            }
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            for (int col = 0; col < allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
                {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    String excelHeader= MessageUtils.message(attr.name());
                    if(cellMap.keySet().contains(excelHeader)){
                        Integer column = cellMap.get(excelHeader);
                        fieldsMap.put(column, field);
                    } else {
                        // 不需要报错，因为payment_notice文件的列可能缺失顺序也不一定
//                        throw new IOException(MessageFormat.format(MessageUtils.message("excel.col_not_exist"), sheetName ,cellMap.keySet().toString(),excelHeader));
//                        throw new IOException("文件中sheet[" + sheetName + "]的所有列名为" + cellMap.keySet() +",不存在列名为'" + excelHeader +"'");
                    }
                }
            }
            for (int i = headerEnd + 1; i < rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                T entity = null;
                for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet())
                {
                    Object val = this.getCellValue(row, entry.getKey());

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(entry.getKey());
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                            if (StringUtils.isNotEmpty(dateFormat))
                            {
                                val = DateUtils.parseDateToStr(dateFormat, (Date) val);
                            }
                            else
                            {
                                val = Convert.toStr(val);
                            }
                        }
                    }
                    else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val)))
                    {
                        val = Convert.toInt(val);
                    }
                    else if (Long.TYPE == fieldType || Long.class == fieldType)
                    {
                        val = Convert.toLong(val);
                    }
                    else if (Double.TYPE == fieldType || Double.class == fieldType)
                    {
                        val = Convert.toDouble(val);
                    }
                    else if (Float.TYPE == fieldType || Float.class == fieldType)
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (BigDecimal.class == fieldType)
                    {
                        val = Convert.toBigDecimal(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if (val instanceof String)
                        {
                            val = DateUtils.parseDate(val);
                        }
                        else if (val instanceof Double)
                        {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    }
                    else if (Boolean.TYPE == fieldType || Boolean.class == fieldType)
                    {
                        val = Convert.toBool(val, false);
                    }
                    if (StringUtils.isNotNull(fieldType))
                    {
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(Convert.toStr(val), attr.readConverterExp(), attr.separator());
                        }
                        else if (StringUtils.isNotEmpty(attr.dictType()))
                        {
                            val = reverseDictByExp(Convert.toStr(val), attr.dictType(), attr.separator());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                if(entity != null){
                    list.add(entity);
                }
            }
        }


        return list;
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(String sheetName, InputStream is) throws Exception
    {
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException(MessageFormat.format(MessageUtils.message("excel.sheet_not_exist"), sheetName));
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0)
        {
            // 定义一个map用于存放excel列的序号和field.
            Map<String, Integer> cellMap = new HashMap<String, Integer>();
            // 获取表头
            Row heard = sheet.getRow(0);
            for (int i = 0; i < heard.getPhysicalNumberOfCells(); i++)
            {
                Cell cell = heard.getCell(i);
                if (StringUtils.isNotNull(cell))
                {
                    String value = this.getCellValue(heard, i).toString();
                    cellMap.put(value, i);
                }
                else
                {
                    cellMap.put(null, i);
                }
            }
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            for (int col = 0; col < allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
                {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    String excelHeader= MessageUtils.message(attr.name());
                    if(cellMap.keySet().contains(excelHeader)){
                        Integer column = cellMap.get(excelHeader);
                        fieldsMap.put(column, field);
                    } else {
                        // 不需要报错，因为payment_notice文件的列可能缺失顺序也不一定
//                        throw new IOException(MessageFormat.format(MessageUtils.message("excel.col_not_exist"), sheetName ,cellMap.keySet().toString(),excelHeader));
//                        throw new IOException("文件中sheet[" + sheetName + "]的所有列名为" + cellMap.keySet() +",不存在列名为'" + excelHeader +"'");
                    }
                }
            }
            for (int i = 1; i < rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                T entity = null;
                for (Map.Entry<Integer, Field> entry : fieldsMap.entrySet())
                {
                    Object val = this.getCellValue(row, entry.getKey());

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(entry.getKey());
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                            if (StringUtils.isNotEmpty(dateFormat))
                            {
                                val = DateUtils.parseDateToStr(dateFormat, (Date) val);
                            }
                            else
                            {
                                val = Convert.toStr(val);
                            }
                        }
                    }
                    else if ((Integer.TYPE == fieldType || Integer.class == fieldType) && StringUtils.isNumeric(Convert.toStr(val)))
                    {
                        val = Convert.toInt(val);
                    }
                    else if (Long.TYPE == fieldType || Long.class == fieldType)
                    {
                        val = Convert.toLong(val);
                    }
                    else if (Double.TYPE == fieldType || Double.class == fieldType)
                    {
                        val = Convert.toDouble(val);
                    }
                    else if (Float.TYPE == fieldType || Float.class == fieldType)
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (BigDecimal.class == fieldType)
                    {
                        val = Convert.toBigDecimal(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if (val instanceof String)
                        {
                            val = DateUtils.parseDate(val);
                        }
                        else if (val instanceof Double)
                        {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    }
                    else if (Boolean.TYPE == fieldType || Boolean.class == fieldType)
                    {
                        val = Convert.toBool(val, false);
                    }
                    if (StringUtils.isNotNull(fieldType))
                    {
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(Convert.toStr(val), attr.readConverterExp(), attr.separator());
                        }
                        else if (StringUtils.isNotEmpty(attr.dictType()))
                        {
                            val = reverseDictByExp(Convert.toStr(val), attr.dictType(), attr.separator());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        return list;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public AjaxResult exportExcel(List<T> list, String sheetName)
    {
        this.init(list, sheetName, Type.EXPORT);
        return exportExcel();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public AjaxResult importTemplateExcel(String sheetName)
    {
        this.init(null, sheetName, Type.IMPORT);
        return exportExcel();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @return 结果
     */
    public AjaxResult exportExcel()
    {
        OutputStream out = null;
        try
        {
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, index);

                // 产生一行
                Row row = sheet.createRow(0);
                int column = 0;
                // 写入各个字段的列头名称

                for (Object[] os : fields)
                {
                    Excel excel = (Excel) os[1];
                    this.createCell(excel, row, column++);
                }
                if (Type.EXPORT.equals(type))
                {
                    fillExcelData(index, row);
                    addStatisticsRow();
                }
            }
            String filename = encodingFilename(sheetName);
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AjaxResult.success(filename);
        }
        catch (Exception e)
        {
            log.error("export Excel error :{}", e.getMessage());
            throw new BusinessException("export failed");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     */
    public void fillExcelData(int index, Row row)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            T vo = (T) list.get(i);
            int column = 0;
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                this.addCell(excel, row, vo, field, column++);
            }
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb)
    {
        // 写入各条记录,每条记录对应excel表中的一行
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font totalFont = wb.createFont();
        totalFont.setFontName("Arial");
        totalFont.setFontHeightInPoints((short) 10);
        style.setFont(totalFont);
        styles.put("total", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("data3", style);

        return styles;
    }

    /**
     * 创建单元格
     */
    public Cell createCell(Excel attr, Row row, int column)
    {
        // 创建列
        Cell cell = row.createCell(column);
        // 表头国际化
//        String excelHeader= MessageUtils.message(attr.name());
        String excelHeader=attr.name();
        // 写入列信息
        cell.setCellValue(excelHeader);
        setDataValidation(attr, row, column);
        cell.setCellStyle(styles.get("header"));
        return cell;
    }

    /**
     * 设置单元格信息
     *
     * @param value 单元格值
     * @param attr 注解相关
     * @param cell 单元格信息
     */
    public void setCellVo(Object value, Excel attr, Cell cell)
    {
        if (ColumnType.STRING == attr.cellType())
        {
            cell.setCellValue(StringUtils.isNull(value) ? attr.defaultValue() : value + attr.suffix());
        }
        else if (ColumnType.NUMERIC == attr.cellType())
        {
            cell.setCellValue(StringUtils.contains(Convert.toStr(value), ".") ? Convert.toDouble(value) : Convert.toInt(value));
        }
    }

    /**
     * 创建表格样式
     */
    public void setDataValidation(Excel attr, Row row, int column)
    {
        if (attr.name().indexOf("注：") >= 0)
        {
            sheet.setColumnWidth(column, 6000);
        }
        else
        {
            // 设置列宽
            sheet.setColumnWidth(column, (int) ((attr.width() + 0.72) * 256));
            row.setHeight((short) (attr.height() * 20));
        }
        // 如果设置了提示信息则鼠标放上去提示.
        if (StringUtils.isNotEmpty(attr.prompt()))
        {
            // 这里默认设了2-101列提示.
            setXSSFPrompt(sheet, "", attr.prompt(), 1, 100, column, column);
        }
        // 如果设置了combo属性则本列只能选择不能输入
        if (attr.combo().length > 0)
        {
            // 这里默认设了2-101列只能选择不能输入.
            setXSSFValidation(sheet, attr.combo(), 1, 100, column, column);
        }
    }

    /**
     * 添加单元格
     */
    public Cell addCell(Excel attr, Row row, T vo, Field field, int column)
    {
        Cell cell = null;
        try
        {
            // 设置行高
            row.setHeight((short) (attr.height() * 20));
            // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
            if (attr.isExport())
            {
                // 创建cell
                cell = row.createCell(column);
                int align = attr.align().value();
                cell.setCellStyle(styles.get("data" + (align >= 1 && align <= 3 ? align : "")));

                // 用于读取对象中的属性
                Object value = getTargetValue(vo, field, attr);
                String dateFormat = attr.dateFormat();
                String readConverterExp = attr.readConverterExp();
                String separator = attr.separator();
                String dictType = attr.dictType();
                if (StringUtils.isNotEmpty(dateFormat) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(DateUtils.parseDateToStr(dateFormat, (Date) value));
                }
                else if (StringUtils.isNotEmpty(readConverterExp) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(convertByExp(Convert.toStr(value), readConverterExp, separator));
                }
                else if (StringUtils.isNotEmpty(dictType) && StringUtils.isNotNull(value))
                {
                    cell.setCellValue(convertDictByExp(Convert.toStr(value), dictType, separator));
                }
                else if (value instanceof BigDecimal && -1 != attr.scale())
                {
                    cell.setCellValue((((BigDecimal) value).setScale(attr.scale(), attr.roundingMode())).toString());
                }
                else
                {
                    // 设置列类型
                    setCellVo(value, attr, cell);
                }
                addStatisticsData(column, Convert.toStr(value), attr);
            }
        }
        catch (Exception e)
        {
            log.error("export Excel failed :{}", e);
        }
        return cell;
    }

    /**
     * 设置 POI XSSFSheet 单元格提示
     *
     * @param sheet 表单
     * @param promptTitle 提示标题
     * @param promptContent 提示内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     */
    public void setXSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
                              int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createCustomConstraint("DD1");
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        dataValidation.createPromptBox(promptTitle, promptContent);
        dataValidation.setShowPromptBox(true);
        sheet.addValidationData(dataValidation);
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    public void setXSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol)
    {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 处理Excel兼容性问题
        if (dataValidation instanceof XSSFDataValidation)
        {
            dataValidation.setSuppressDropDownArrow(true);
            dataValidation.setShowErrorBox(true);
        }
        else
        {
            dataValidation.setSuppressDropDownArrow(false);
        }

        sheet.addValidationData(dataValidation);
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @param separator 分隔符
     * @return 解析后值
     * @throws Exception
     */
    public static String convertByExp(String propertyValue, String converterExp, String separator) throws Exception
    {
        StringBuilder propertyString = new StringBuilder();
        try
        {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource)
            {
                String[] itemArray = item.split("=");
                if (StringUtils.containsAny(separator, propertyValue))
                {
                    for (String value : propertyValue.split(separator))
                    {
                        if (itemArray[0].equals(value))
                        {
                            propertyString.append(itemArray[1] + separator);
                            break;
                        }
                    }
                }
                else
                {
                    if (itemArray[0].equals(propertyValue))
                    {
                        return itemArray[1];
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @param separator 分隔符
     * @return 解析后值
     * @throws Exception
     */
    public static String reverseByExp(String propertyValue, String converterExp, String separator) throws Exception
    {
        StringBuilder propertyString = new StringBuilder();
        String[] convertSource = converterExp.split(",");
        for (String item : convertSource)
        {
            String[] itemArray = item.split("=");
            if (StringUtils.containsAny(separator, propertyValue))
            {
                for (String value : propertyValue.split(separator))
                {
                    if (itemArray[1].equals(value))
                    {
                        propertyString.append(itemArray[0] + separator);
                        break;
                    }
                }
            }
            else
            {
                if (itemArray[1].equals(propertyValue))
                {
                    return itemArray[0];
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 解析字典值
     *
     * @param dictValue 字典值
     * @param dictType 字典类型
     * @param separator 分隔符
     * @return 字典标签
     */
    public static String convertDictByExp(String dictValue, String dictType, String separator) throws Exception
    {
        return DictUtils.getDictLabel(dictType, dictValue, separator);
    }

    /**
     * 反向解析值字典值
     *
     * @param dictLabel 字典标签
     * @param dictType 字典类型
     * @param separator 分隔符
     * @return 字典值
     */
    public static String reverseDictByExp(String dictLabel, String dictType, String separator) throws Exception
    {
        return DictUtils.getDictValue(dictType, dictLabel, separator);
    }

    /**
     * 合计统计信息
     */
    private void addStatisticsData(Integer index, String text, Excel entity)
    {
        if (entity != null && entity.isStatistics())
        {
            Double temp = 0D;
            if (!statistics.containsKey(index))
            {
                statistics.put(index, temp);
            }
            try
            {
                temp = Double.valueOf(text);
            }
            catch (NumberFormatException e)
            {
            }
            statistics.put(index, statistics.get(index) + temp);
        }
    }

    /**
     * 创建统计行
     */
    public void addStatisticsRow()
    {
        if (statistics.size() > 0)
        {
            Cell cell = null;
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            Set<Integer> keys = statistics.keySet();
            cell = row.createCell(0);
            cell.setCellStyle(styles.get("total"));
            cell.setCellValue("合计");

            for (Integer key : keys)
            {
                cell = row.createCell(key);
                cell.setCellStyle(styles.get("total"));
                cell.setCellValue(DOUBLE_FORMAT.format(statistics.get(key)));
            }
            statistics.clear();
        }
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename)
    {
        // 取系统时间
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateNowStr = sdf.format(nowDate);
        filename = filename + "_" + dateNowStr+ ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = NNRoadConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 获取bean中的属性值
     *
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception
    {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            if (target.indexOf(".") > -1)
            {
                String[] targets = target.split("[.]");
                for (String name : targets)
                {
                    o = getValue(o, name);
                }
            }
            else
            {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (StringUtils.isNotNull(o) && StringUtils.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            o = field.get(o);
        }
        return o;
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField()
    {
        this.fields = new ArrayList<Object[]>();
        List<Field> tempFields = new ArrayList<>();
        tempFields.addAll(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
        tempFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        for (Field field : tempFields)
        {
            // 单注解
            if (field.isAnnotationPresent(Excel.class))
            {
                putToField(field, field.getAnnotation(Excel.class));
            }

            // 多注解
            if (field.isAnnotationPresent(Excels.class))
            {
                Excels attrs = field.getAnnotation(Excels.class);
                Excel[] excels = attrs.value();
                for (Excel excel : excels)
                {
                    putToField(field, excel);
                }
            }
        }
        if(sortMethod==null) {
            this.fields = this.fields.stream().sorted(Comparator.comparing(objects -> ((Excel) objects[1]).sort())).collect(Collectors.toList());
        } else {
            this.fields = sortMethod.apply(fields);
        }
    }

    /**
     * 放到字段集合中
     */
    private void putToField(Field field, Excel attr)
    {
        if (attr != null && (attr.type() == Type.ALL || attr.type() == type))
        {
            this.fields.add(new Object[] { field, attr });
        }
    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * 创建工作表
     *
     * @param sheetNo sheet数量
     * @param index 序号
     */
    public void createSheet(double sheetNo, int index)
    {
        this.sheet = wb.createSheet();
        this.styles = createStyles(wb);
        // 设置工作表的名称.
        if (sheetNo == 0)
        {
            wb.setSheetName(index, sheetName);
        }
        else
        {
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (StringUtils.isNotNull(cell))
            {
                if(cell.getCellType() == CellType.FORMULA){
                    switch (cell.getCachedFormulaResultType()) {
                        case STRING:
                            val = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            short format = cell.getCellStyle().getDataFormat();
                            if (DateUtil.isCellDateFormatted(cell)) {
                                SimpleDateFormat sdf = null;
                                if (format == 20 || format == 32) {
                                    sdf = new SimpleDateFormat("HH:mm");
                                } else if (format == 14 || format == 31 || format == 57 || format == 58) {
                                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    double value = cell.getNumericCellValue();
                                    Date date = DateUtil.getJavaDate(value);
                                    val = sdf.format(date);
                                }  else {
                                    // 日期
                                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                                }
                                try {
                                    // 日期
                                    val = sdf.format(cell.getDateCellValue());
                                } catch (Exception e) {
                                    try {
                                        throw new Exception("exception on get date data !".concat(e.toString()));
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                }finally{
                                    sdf = null;
                                }
                            }
                            else {
                                NumberFormat nf = NumberFormat.getInstance();
                                nf.setGroupingUsed(false);
                                val = String.valueOf(nf.format(cell.getNumericCellValue()));
                            }

                            break;
                        case BOOLEAN:
                            val = String.valueOf(cell.getBooleanCellValue());
                            break;
                        default:
                            val = cell.getCellFormula();
                    }
                }else if(cell.getCellType() == CellType.NUMERIC)
                {
                    short format = cell.getCellStyle().getDataFormat();
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = null;
                        if (format == 20 || format == 32) {
                            sdf = new SimpleDateFormat("HH:mm");
                        } else if (format == 14 || format == 31 || format == 57 || format == 58) {
                            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                            double value = cell.getNumericCellValue();
                            Date date = DateUtil
                                    .getJavaDate(value);
                            val = sdf.format(date);
                        }else {
                            // 日期
                            sdf = new SimpleDateFormat("yyyy-MM-dd");
                        }
                        try {
                            // 日期
                            val = sdf.format(cell.getDateCellValue());
                        } catch (Exception e) {
                            try {
                                throw new Exception("exception on get date data !".concat(e.toString()));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }finally{
                            sdf = null;
                        }
                    }
                    else {
                        Double value = cell.getNumericCellValue();
                        BigDecimal bd1 = new BigDecimal(Double.toString(value));
                        val = bd1.toPlainString().replaceAll("0+?$", "").replaceAll("[.]$", "");
                    }
                }
                else if (cell.getCellType() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellType() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellType() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }

    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @param isRequire
     * @param fileName
     * @param skipKeyWords
     * @return 转换后集合
     */
    public List<Map> importExcelCustomize(String sheetName, InputStream is, Map<String, Integer> mtcMap, Integer maxLevel, Integer startLine, Boolean isRequire, String fileName, List<String> skipKeyWords) throws Exception
    {
        StringBuffer sbf =new StringBuffer();
        int start = startLine==0?0:startLine-1;
        int max = maxLevel ;
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<Map> list = new ArrayList<Map>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName)){
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        } else {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null) {
            if (isRequire){
                throw new BusinessException(fileName +" "+ MessageFormat.format(MessageUtils.message("excel.sheet_not_exist"), sheetName));
            }else {
                return new ArrayList<>();
            }

        }

        int rows = sheet.getPhysicalNumberOfRows();
        int lastRowNum = sheet.getLastRowNum();


        if (rows > 0)
        {
            Map<Integer, String> fieldsMap = new HashMap<Integer, String>();
            //表头各字段名称 （title，序号）
            Map<String, Integer> cellMap = new HashMap<>();
            //表头各字段名称
            cellMap=getCellMap(sheet,maxLevel,start);
            for (String key : mtcMap.keySet()){
                String dbColumnName = key;
                Set<String> titles = cellMap.keySet();
                for (String title : titles) {
                    String name= key.replaceAll("\r|\n", "");
                    String realtitle= title.replaceAll("\r|\n", "");
                    if (name.equals(realtitle)){
                        Integer column = cellMap.get(name);
                        fieldsMap.put(column, key);
                    }
                }
            }

            //用rows判断会出现对中间有空行情况的误判
            //label1: for (int i = start+max; i < rows; i++)
            label1: for (int i = start+max; i <= lastRowNum; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                if (isEmptyRow(row)){
                    continue;
                }
                Object cellValue = getCellValue(row, 0);
                if (ObjectUtils.isNotEmpty(cellValue)){
                    //如果一行的第一列带有sum字样直接跳过
                    if (row!=null ){
                        if (skipKeyWords!=null &&skipKeyWords.size()>0){
                            for (String skipKeyWord : skipKeyWords) {
                                if (cellValue.toString().replace("\n","").toLowerCase(Locale.ROOT).equals(skipKeyWord.toLowerCase(Locale.ROOT))){
                                    continue label1;
                                }
                            }
                        }


                    }
                }
                Map entity = new HashMap();
                for (Map.Entry<Integer, String> entry : fieldsMap.entrySet()){
                    Object val = this.getCellValue(row, entry.getKey());
                    Integer dbType = mtcMap.get(entry.getValue());
                    Pattern allNumber = Pattern.compile("^(-|\\+)?\\d+(\\.\\d+)?$");
                    Pattern isInt = Pattern.compile("^[-\\+]?[\\d]*$");
                    //字符
                    if (null == dbType) {
                        String realVal = Convert.toStr(val);
                        if(realVal!=null && !realVal.contains("!#REF!") && !realVal.contains("#N/A"))
                            entity.put(entry.getValue(), realVal);
                    }
                    else if (0 == dbType){
                        String s = Convert.toStr(val);
                        if (StringUtils.isEmpty(s)){
                            val =null;
                        } else if (StringUtils.endsWith(s, ".0")){
                            val = StringUtils.substringBefore(s, ".0");
                        } else {
                            val = Convert.toStr(val);
                            Cell cell = row.getCell(entry.getKey());
                            if (cell!=null){
                                CellType cellType = cell.getCellType();
                                //公式
                                if (cell.getCellType() ==  CellType.FORMULA){
                                    try {
                                        String resltvalue = cell.getStringCellValue();
                                    }catch (Exception e){
                                        val = "";
                                    }
                                }
                            }

                        }
                    }
                    //数字
                    else if (1 == dbType ){
                        if (StringUtils.isEmpty(Convert.toStr(val))){
                            val = null;
                        } else if (allNumber.matcher(Convert.toStr(val)).matches()){
                            val = Convert.toStr(val);
                        }else {
                            sbf.append(fileName + " [ " +sheetName +" ] :" +MessageUtils.message("html.client.employee.lineError",i+1)+MessageUtils.message("html.client.employee.columnError",entry.getValue()) +"\\n");
                            //val = "0";
                            val = null;
                        }
                    }
                    //日期
                    else if (2 == dbType){
                        if (val instanceof String)
                        {
                            if (StringUtils.isEmpty(val.toString())){
                                val= null;
                            }else {
                                val = DateUtils.parseDate(val);
                            }
                        }
                        else if (val instanceof Double)
                        {
                            val = DateUtil.getJavaDate((Double) val);
                        }
                    }
                    //dbtype不为空
                    if (StringUtils.isNotNull(dbType)) {

                        try {
                            if (val!=null){
                                String realVal = val.toString();
                                if (realVal.contains("!#REF!")||realVal.contains("#N/A")) {
                                    realVal = null;
                                }
                                entity.put(entry.getValue(), realVal);
                            }
                        }catch (Exception e){
                            System.out.println(e);
                            sbf.append(fileName + " [ " +sheetName +" ] :" +MessageUtils.message("html.client.employee.lineError",i+1)+MessageUtils.message("html.client.employee.columnError",entry.getValue()) +"\\n");
                        }
                    }
                }
                list.add(entity);
            }
        }
        String error = sbf.toString();
        if (StringUtils.isNotEmpty(error)){
            throw new BusinessException(error);
        }
        return list;
    }
    /**
     * 读取excel表格头数据转化为表头数组
     * @param sheetName 表名
     * @param is excel文件流
     * @param startLine 开始行数
     * @return String[][] 表头数组
     */
    public  String[][] getHeaders(String sheetName, InputStream is, Integer startLine ) throws IOException {
        int start = startLine==0?0:startLine-1;
        this.type = Type.IMPORT;
        this.wb = WorkbookFactory.create(is);
        List<TreeEntity> tree =new ArrayList<>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new BusinessException(MessageFormat.format(MessageUtils.message("excel.sheet_not_exist"), sheetName));
        }
        int rows = sheet.getPhysicalNumberOfRows();
        Row firstRow = sheet.getRow(start);
        int cellCount = firstRow.getPhysicalNumberOfCells();
        String[][] strings=new String[rows][cellCount];
        for (int j = start; j < rows; j++) {
            Row head = sheet.getRow(j);
            int rowNum =j;
            for (int i = 0; i < cellCount; i++) {
                //判断是否是合并的单元格
                CellRangeAddress merge = isMerge(sheet, j, i);
                if (merge==null){
                    String cellValue = this.getCellValue(head, i).toString();
                    cellValue=cellValue.replaceAll("\r|\n", "");
                    if (j>0&& StringUtils.isEmpty(cellValue)){
                        cellValue = strings[j-1][i];
                    }
                    strings[j][i]=cellValue;

                }else {
                    //合并行的单元格
                    if (merge.getLastRow()==merge.getFirstRow() && merge.getFirstRow()  ==rowNum  && i >= merge.getFirstColumn() && i <= merge.getLastColumn()){
                        String cellValue = this.getCellValue(head, merge.getFirstColumn()).toString();
                        cellValue=cellValue.replaceAll("\r|\n", "");
                        strings[j][i]=cellValue;
                        //合并列的单元格
                    }else if (merge.getFirstColumn()==merge.getLastColumn() && merge.getFirstColumn() ==i && rowNum >= merge.getFirstRow() && rowNum <= merge.getLastRow()){

                        if (j==merge.getFirstRow()){
                            String cellValue = this.getCellValue(sheet.getRow(merge.getFirstRow()), i).toString();
                            cellValue=cellValue.replaceAll("\r|\n", "");
                            strings[j][i]=cellValue;
                        }else {
                            strings[j][i]="";
                        }
                    }
                }
            }
        }
        return strings ;
    }
    public static boolean isEmptyRow(Row row) {
        if (row == null || row.toString().isEmpty()) {
            return true;
        } else {
            boolean isEmpty = true;
            // 从第一个不为空的列开始 到 最后一个 不为空的列(有格式就算一列 )
            for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                if (cell != null && cell.getCellType() != CellType.BLANK){
                    isEmpty = false;
                    break;
                }
            }
            return isEmpty;
        }
    }
    /**
     * 获取多级表头名字
     * @param sheet 表格
     * @param maxLevel 表头最大层级
     * @param startLine 开始行数
     * @return  获取多级表头名字 上下级拼接
     */


    private Map<String, Integer> getCellMap(Sheet sheet, Integer maxLevel, Integer startLine) {
        Map<String, Integer> fieldsMap = new HashMap<>();
        //获取所有表头
        List<Row> heads = new ArrayList<Row>();
        for (Integer i = 0; i < maxLevel; i++) {
            Row row = sheet.getRow(i + startLine);
            heads.add(row);
        }
        // 获取表头长度
        Row firstRow = heads.get(0);
        if(firstRow == null){
            return fieldsMap;
        }
        int cellCount = firstRow.getPhysicalNumberOfCells();
        //表头二维数组  前行后列
        String[][] strings=new String[maxLevel][cellCount];
        for (int j = 0; j < heads.size(); j++) {
            Row head = heads.get(j);
            //获取该行行数
            int rowNum = head.getRowNum();
            //获取该行列数
            //int cellCounts = head.getPhysicalNumberOfCells();
            for (int i = 0; i < cellCount; i++) {
                //判断是否是合并的单元格
                CellRangeAddress merge = isMerge(sheet, rowNum, i);
                //未合并的单元格
                if (merge==null){
                    String cellValue = this.getCellValue(head, i).toString();
                    cellValue=cellValue.replaceAll("\r|\n", "");
                    strings[j][i]=cellValue;
                }else {
                    //合并行的单元格
                    if (merge.getLastRow()==merge.getFirstRow() && merge.getFirstRow()  ==rowNum  && i >= merge.getFirstColumn() && i <= merge.getLastColumn()){
                        String cellValue = this.getCellValue(head, merge.getFirstColumn()).toString();
                        cellValue=cellValue.replaceAll("\r|\n", "");
                        strings[j][i]=cellValue;
                        //合并列的单元格
                    }else if (merge.getFirstColumn()==merge.getLastColumn() && merge.getFirstColumn() ==i && rowNum >= merge.getFirstRow() && rowNum <= merge.getLastRow()){

                        if ((j+startLine)==merge.getFirstRow()){
                            String cellValue = this.getCellValue(sheet.getRow(merge.getFirstRow()), i).toString();
                            cellValue=cellValue.replaceAll("\r|\n", "");
                            strings[j][i]=cellValue;
                        }else {
                            strings[j][i]="";
                        }
                    }
                }
            }
        }
        //靠列遍历 每一列多级菜单拼接
        for (int i = 0; i < cellCount; i++) {
            String title ="";
            for (int j = 0; j < maxLevel; j++) {
                title +=strings[j][i];
            }
            fieldsMap.put(title,i);
        }
        return fieldsMap;
    }

    /**
     * 该格子是否是合并的单元格
     * @param sheet 表格
     * @param row 行
     * @param column 列
     * @return  合并后的单元格返回该单元格所有行列信息  未合并的返回null
     */

    public  CellRangeAddress isMerge(Sheet sheet,int row ,int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return range;
                }
            }
        }
        return null;
    }

    /**
     * 读取表格特定位置的数值
     * @param sheetName 表格名称
     * @param inputStream 写入流
     * @param rowNum 行数
     * @param colNum 列数
     * @return  读取表格特定位置的数值  未合并的返回null
     */
    public Object getValueByRowNumColNum(String sheetName,InputStream inputStream, int rowNum, int colNum) throws Exception{
        this.wb = WorkbookFactory.create(inputStream);
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException(MessageFormat.format(MessageUtils.message("excel.sheet_not_exist"), sheetName));
        }
        Row row = sheet.getRow(rowNum);
        return getCellValue(row,colNum);
    }
    /**
     * 读取表格特定位置的数值
     * @param sheetName 表格名称
     * @param inputStream 写入流
     * @param address  Map<String, CellAddress> 字段名称 和 行和列位置。
     * @return  读取表格特定位置的数值  返回map集合
     */
     public Map<String,Object> getObjectByConfigWithRowCol(String sheetName, InputStream inputStream, Map<String, CellAddress> address, Boolean requireFlag) throws IOException {
        Map<String,Object> obj = new HashMap<>();
         this.wb = WorkbookFactory.create(inputStream);
         Sheet sheet = null;
         if (StringUtils.isNotEmpty(sheetName)) {
             // 如果指定sheet名,则取指定sheet中的内容.
             sheet = wb.getSheet(sheetName);
         } else {
             // 如果传入的sheet名不存在则默认指向第1个sheet.
             sheet = wb.getSheetAt(0);
         }

         if (sheet == null) {
             if (requireFlag){
                 throw new IOException(MessageFormat.format(MessageUtils.message("excel.sheet_not_exist"), sheetName));
             }else {
                 return null;
             }
         }
         for (String key : address.keySet()) {
             CellAddress cellAddress = address.get(key);
             Row row = sheet.getRow(cellAddress.getRow()-1);
             Object cellValue = getCellValue(row, cellAddress.getColumn()-1);
             obj.put(key,cellValue);
         }
         return obj;
     }
}