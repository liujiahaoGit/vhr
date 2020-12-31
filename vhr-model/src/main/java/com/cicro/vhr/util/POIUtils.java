package com.cicro.vhr.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.NoSuchElementException;

public class POIUtils {

    /*
     * @methodName: export
     * @description excel 导出操作
     * @param:  data:实体类数据
     *          tClass:映射的实体类class
     *          excelName:导出的excel名称
     *          sheetName: sheet名称
     *          isCreateHeadRows :是否有表头行
     *          style : 表格自定义样式
     * @return: ResponseEntity
     * @createdAt 11:23 2020/8/5 0005
     * @version 1.0.0
     **/
    public static <R> ResponseEntity<byte[]> export(List<R> data, Class<R> tClass, String excelName, String sheetName
        , boolean isCreateHeadRows, Class<? extends AbstractExcelExportStyler> style) throws UnsupportedEncodingException {

        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName(sheetName);
        exportParams.setCreateHeadRows(isCreateHeadRows);
        exportParams.setTitle(sheetName); //表格名称
        if (style!=null){
            exportParams.setStyle(style);
        }else {
            exportParams.setStyle(ExcelExportStylerImpl.class); //设置表格的style样式  自定义Style,并实现相应的接口
        }

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, tClass, data);
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        try {

            workbook.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", new String(String.format("%s.xls", excelName).getBytes(
            "UTF-8"), "ISO-8859-1"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(fos.toByteArray(), headers, HttpStatus.CREATED);
    }

    /*
     * @methodName: importExcel
     * @description excel 导入操作
     * @param: file 需要导入的文件
     *         titleRows 表格标题行数,默认0
     *         headerRows 表头行数,默认1
     *         pojoClass 映射的实体类class
     * @return: List 导入解析成功的数据
     * @createdAt 11:30 2020/8/5 0005
     * @version 1.0.0
     **/
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows,
                                          Class<T> pojoClass) {
        if (file == null) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows); //标题行有几行
        params.setHeadRows(headerRows); //表头行有几行
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params); //导入成功的数据
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }
}
