package com.wallker.framework.tools.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.wallker.framework.tools.date.DateUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.util.CollectionUtils;

/**
 * @Description: 
 * @Copyright: qqsscs.com
 * @History:<br>
 *<li>Author: feng.gao</li>
 *<li>Date: 2016年8月17日</li>
 */
public class ExcelUpload {

    public static final String EXCEL_TYPE = "application/vnd.ms-excel";

    /**
     * 通过BeanUtils方法读取excel内容(推荐)
     * @param file 要读取的excel对象
     * @param t excel对应的泛型对象
     * @return 存放excel内容的list对象
     */
	public static <T> List<T> readExcel(File file, InputStream fis, Class<T> t, ExcelInterface[] excelInterfaces) {
		List<T> list = null;
		/** 取得工作薄对象 **/
		Workbook wBook = fis == null ? readBook(file) : readBook(fis);
		if (wBook == null ) {
			return null;
		}
		if(wBook.getSheetAt(0) != null){
			list = new ArrayList<T>();
			/** 取得工作表索引为sheetNum的工作表 **/
			Sheet sheet = wBook.getSheetAt(0);
			/** 对每行进行遍历 **/
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				T tObj = null;
				if (row == null || row.getCell(0) == null || row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK) {
					continue;
				}
				try {
					/** 创建泛型对象 **/ 
					tObj = t.newInstance();
					/** 对每个单元格进行循环 **/ 
					for (int cellNum = 0; cellNum <= row.getLastCellNum(); cellNum++) {
						/** 取得当前单元格 **/ 
						Cell cell = row.getCell(cellNum);
						if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
							/** 取得表头信息 **/ 
							String bookTitle = sheet.getRow(0).getCell(cellNum).getStringCellValue();
							/** 取得表头信息在枚举类中对应的字段名 **/ 
							String fieldName = getKeyByName(excelInterfaces, bookTitle);
							try {
								Class<?> type = PropertyUtils.getPropertyType(tObj, fieldName);
								if (Date.class.equals(type)) {
									BeanUtils.setProperty(tObj, fieldName,
											DateUtil.convertString2Date(cell.getStringCellValue()));
								}
								if (Number.class.isAssignableFrom(type)) {
									BeanUtils.setProperty(tObj, fieldName, new BigDecimal(cell.getNumericCellValue()));
								}
								if (String.class.equals(type)) {
									if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
										BeanUtils.setProperty(tObj, fieldName, new BigDecimal(cell.getNumericCellValue())
												.stripTrailingZeros().toPlainString());
										continue;
									}
									/** 使用BeanUtils设置泛型对象的字段值 **/ 
									BeanUtils.setProperty(tObj, fieldName, cell.getStringCellValue());
								}
							} catch (NoSuchMethodException e) {
								continue;
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							}
						}
					}
					/** 将设置好的泛型对象存入到list中 **/ 
					list.add(tObj);
					
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			/** 关闭  Workbook**/
			wBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

    /**
     * 导出excel
     * @param path 导出的路径地址
     * @param excelInterfaces 字段与名称映射枚举
     * @param list 需要导出的信息列表
     */
    public static <T> HSSFWorkbook writeExcel(String path, String sheeName,
                                              ExcelInterface[] excelInterfaces, List<T> list) {
        // 创建工作薄对象
        HSSFWorkbook wBook = new HSSFWorkbook();
        Cell cell = null;
        Row row = null;
        // 创建工作表
        Sheet sheet = wBook.createSheet(sheeName);
        // 第一行
        row = sheet.createRow(0);
        // 对泛型对象字段进行遍历
        for (int i = 0; i < excelInterfaces.length; i++) {
            // 创建单元格
            cell = row.createCell(i);
            // 设置单元格值的类型为String类型
            cell.setCellType(Cell.CELL_TYPE_STRING);
            // 设置表头
            cell.setCellValue(excelInterfaces[i].getName());
        }
        // 传入的对象列表不为null和不为空的请款
        if (!CollectionUtils.isEmpty(list)) {
            HSSFCellStyle contextstyle = wBook.createCellStyle();
            // 对传入对象列表进行循环
            for (int j = 0; j < list.size(); j++) {
                // 创建剩下的行
                row = sheet.createRow(j + 1);
                // 对泛型对象字段进行遍历
                for (int x = 0; x < excelInterfaces.length; x++) {

                    String fieldName = excelInterfaces[x].getKey();
                    // 设置单元格内容
                    if (StringUtils.isEmpty(fieldName)) {
                        cell.setCellValue("");
                        continue;
                    }
                    // 创建单元格
                    cell = row.createCell(x);
                    // 设置单元格值的类型为String类型
                    try {
                        Class<?> type = PropertyUtils.getPropertyType(list.get(j), fieldName);
                        String fieldValue = BeanUtils.getProperty(list.get(j), fieldName);
                        if (Double.class.isAssignableFrom(type)
                            || BigDecimal.class.isAssignableFrom(type)) {
                            contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));//保留两位小数点
                            // 设置单元格格式
                            cell.setCellStyle(contextstyle);
                            if (StringUtils.isEmpty(fieldValue)) {
                                fieldValue = "0";
                            }
                            Double cellValue = new BigDecimal(fieldValue)
                                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(cellValue);
                        }else if(Integer.class.isAssignableFrom(type)){
                            if(StringUtils.isNotBlank(fieldValue)){
                                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                                cell.setCellValue(Integer.parseInt(fieldValue));
                            }
                        } else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cell.setCellValue(fieldValue);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(path)) {
            // 通过传入的路径创建文件对象
            File file = new File(path);
            try {
                // 文件不存在的情况
                if (!file.exists()) {
                    // 创建文件对象
                    file.createNewFile();
                }
                // 通过文件创建输出流
                FileOutputStream fos = new FileOutputStream(file);
                // 将工作薄写入到输出流中
                wBook.write(fos);
                // 清除输出流的缓存
                fos.flush();
                // 关闭输出流
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return wBook;
    }

    /**
     * 读取工作薄对象
     */
    public static Workbook readBook(File file) {
        Workbook wBook = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            wBook = WorkbookFactory.create(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wBook;
    }

    /**
     * 读取工作薄对象
     */
    public static Workbook readBook(InputStream fis) {
        Workbook wBook = null;
        try {
            wBook = WorkbookFactory.create(fis);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wBook;
    }

    /**
     * 根据Excel的Title获取对应的字段名
     */
    private static String getKeyByName(ExcelInterface[] excelInterfaces, String name) {
        for (ExcelInterface excelInterface : excelInterfaces) {
            if (excelInterface.getName().equals(name)) {
                return excelInterface.getKey();
            }
        }
        return null;
    }

}
