package com.aaroom.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private static int totalRows = 0;// 总行数

    private static int totalCells = 0;// 总列数

    /**
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @author: zfzhao
     * @description:导出Excel通用方法
     * @create: 2018-12-14 09:39
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, Object[][] values, HSSFWorkbook wb) {
        //创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        //在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        //在sheet中添加表头第0行
        HSSFRow row = sheet.createRow(0);
        //创建单元格，并设置值表头并且居中
        HSSFCellStyle style = wb.createCellStyle();
        //水平方向
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //垂直方向
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //声明列对象
        HSSFCell cell = null;
        //创建TITLE
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            //对多有列内容宽度进行自适应
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(i);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue((String) values[i][j]);
            }
        }
        return wb;
    }

    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void exportExcel(HSSFWorkbook wb, String sheetName, HttpServletResponse response) {
        try {
            setResponseHeader(response, sheetName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取列表数据
     * <按顺序放入实体成员变量>
     *
     * @param wb        工作簿
     * @param t         实体
     * @param beginLine 开始行数
     * @param totalcut  结束行数减去相应行数
     * @return List<T> 实体列表
     * @author: zfzhao
     * @description:导入Excel通用方法
     * @create: 2019-01-03 14:25
     */
    public static <T> List<T> readDateListT(Workbook wb, T t, int beginLine, int totalcut) {
        List<T> list = new ArrayList<T>();
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        totalRows = sheet.getPhysicalNumberOfRows();
        //得到Excel的列数
        if (totalRows >= 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        try {
            //循环Excel的行
            for (int r = beginLine - 1; r < totalRows - totalcut; r++) {
                Object newInstance = t.getClass().newInstance();
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                //成员变量的值
                Object entityMemberValue = "";
                //所有成员变量
                Field[] fields = t.getClass().getDeclaredFields();
                //列开始下标
                int startCell = 0;
                for (int f = 0; f < fields.length; f++) {
                    fields[f].setAccessible(true);
                    String fieldName = fields[f].getName();
                    Cell cell = row.getCell(startCell);
                    String cellValue = getCellValue(cell);
                    entityMemberValue = getEntityMemberValue(entityMemberValue, fields, f, cellValue);
                    //赋值
                    PropertyUtils.setProperty(newInstance, fieldName, entityMemberValue);
                    //列的下标加1
                    startCell++;
                }
                list.add((T) newInstance);
            }
            return list;
        } catch (Exception e) {
            log.error("===========导入模板数据格式有误===========");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * 根据Excel表格中的数据判断类型得到值
     *
     * @param cell
     * @return
     * @author: zfzhao
     * @create: 2019-01-03 15:32
     */
    private static String getCellValue(Cell cell) {
        String cellValue = "";

        if (null != cell) {
            //以下是判断数据的类型
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: //数字
                    if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                        Date theDate = cell.getDateCellValue();
                        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cellValue = dff.format(theDate);
                    } else  {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                        //处理金额保留两位小数
                        if (cellValue.substring(cellValue.indexOf("."),cellValue.length()-1).length() > 1) {
                            DecimalFormat df = new DecimalFormat("0.00");
                            cellValue = df.format(Double.valueOf(cellValue));
                            break;
                        }
                        DecimalFormat df = new DecimalFormat("0");
                        cellValue = df.format(cell.getNumericCellValue());
                    }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    cellValue = cell.getStringCellValue();
                    break;

                case HSSFCell.CELL_TYPE_BOOLEAN:
                    cellValue = cell.getBooleanCellValue() + "";
                    break;

                case HSSFCell.CELL_TYPE_FORMULA://公式
                    cellValue = cell.getCellFormula() + "";
                    break;

                case HSSFCell.CELL_TYPE_BLANK://空值
                    cellValue = "";
                    break;

                case HSSFCell.CELL_TYPE_ERROR://故障
                    cellValue = "非法字符";
                    break;

                default:
                    cellValue = "未知类型";
                    break;
            }

        }
        return cellValue;
    }

    /**
     * 根据实体成员变量的类型得到成员变量的值
     *
     * @param realValue
     * @param fields
     * @param f
     * @param cellValue
     * @return
     * @author: zfzhao
     * @create: 2019-01-03 15:50
     */
    private static Object getEntityMemberValue(Object realValue, Field[] fields, int f, String cellValue) throws ParseException {
        String type = fields[f].getType().getName();
        switch (type) {
            case "char":
            case "java.lang.Character":
            case "java.lang.String":
                realValue = cellValue;
                break;
            case "java.util.Date":
                realValue = StringUtils.isBlank(cellValue) ? null : strToDate(cellValue, "yyyy-MM-dd HH:mm:ss");
                break;
            case "java.lang.Integer":
                realValue = StringUtils.isBlank(cellValue) ? null : Integer.valueOf(cellValue);
                break;
            case "int":
            case "float":
            case "double":
            case "java.lang.Double":
            case "java.lang.Float":
            case "java.lang.Long":
            case "java.lang.Short":
            case "java.math.BigDecimal":
                realValue = StringUtils.isBlank(cellValue) ? null : cellValue;//new BigDecimal(cellValue);
                break;
            default:
                break;
        }
        return realValue;
    }

    /**
     * 将字符串转换成Date类型的时间
     *
     * @param s       日期类型的字符串
     * @param pattern
     * @return java.util.Date
     * @author: zfzhao
     * @create: 2019-01-03 15:55
     */
    public static Date strToDate(String s, String pattern) throws ParseException {
        if (s == null) {
            return null;
        }
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            date = sdf.parse(s);
        return date;
    }

    public static Workbook workbookType(String filePathOrName, InputStream in)
            throws IOException {
        //根据版本选择创建Workbook的方式
        Workbook wb = null;
        boolean isExcel2003 = isExcel2003(filePathOrName);
        if (isExcel2003) {
            wb = new HSSFWorkbook(in);
        } else {
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    //判断是否为2003的Excel
    public static boolean isExcel2003(String filePathOrName) {
        return filePathOrName.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @description:按规则导出Excel通用方法
     * @create: 2018-12-14 09:39
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[][] title, Object[][] values, HSSFWorkbook wb) {
        //创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        //在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        //创建单元格，并设置值表头并且居中
        HSSFCellStyle style = wb.createCellStyle();
        //水平方向
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //垂直方向
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        //声明列对象
        HSSFCell cell = null;
        //声明行对象
        HSSFRow row = null;
        //创建TITLE
        for (int i = 0; i < title.length; i++) {
            //在sheet中添加表头第0行
            row = sheet.createRow(i);
            for(int j = 0; j < title[0].length; j++){
                if(title[i][j]!=null){
                    cell = row.createCell(j);
                    cell.setCellValue(title[i][j]);
                    cell.setCellStyle(style);
                }
            }
        }
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 2);
            //对多有列内容宽度进行自适应
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(i);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue((String) values[i][j]);
            }
        }
        Region region1 = new Region(0, (short)0, 1, (short)0);
        Region region2 = new Region(0, (short)1, 1, (short)1);
        Region region3 = new Region(0, (short)2, 1, (short)2);
        Region region4 = new Region(0, (short)3, 0, (short)6);
        Region region5 = new Region(0, (short)7, 0, (short)10);
        Region region6 = new Region(0, (short)11, 0, (short)14);
        Region region7 = new Region(0, (short)15, 0, (short)18);
        Region region8 = new Region(0, (short)19, 0, (short)22);
        Region region9 = new Region(0, (short)23, 0, (short)26);
        Region region10 = new Region(0, (short)27, 0, (short)30);
        Region region11 = new Region(0, (short)31, 0, (short)34);
        Region region12 = new Region(0, (short)35, 0, (short)38);
        Region region13 = new Region(0, (short)39, 0, (short)42);
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        sheet.addMergedRegion(region5);
        sheet.addMergedRegion(region6);
        sheet.addMergedRegion(region7);
        sheet.addMergedRegion(region8);
        sheet.addMergedRegion(region9);
        sheet.addMergedRegion(region10);
        sheet.addMergedRegion(region11);
        sheet.addMergedRegion(region12);
        sheet.addMergedRegion(region13);
        return wb;
    }

}

