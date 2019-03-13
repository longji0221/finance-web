package com.cx.finance.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExportUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExportUtil.class);
    /**
     * CSV文件列分隔符·
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_RN = "\r\n";

    /**
     * @param dataList 集合数据
     * @param colNames 表头部数据
     * @param mapKey   查找的对应数据
     * @param os       返回结果
     */
    public static boolean doExport(List<Map<String, Object>> dataList, String colNames, String mapKey, OutputStream os) {
        try {
            StringBuffer buf = new StringBuffer();

            String[] colNamesArr = null;
            String[] mapKeyArr = null;

            colNamesArr = colNames.split(",");
            mapKeyArr = mapKey.split(",");

            // 完成数据csv文件的封装
            // 输出列头
            for (int i = 0; i < colNamesArr.length; i++) {
                buf.append(colNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);

            if (null != dataList) { // 输出数据
                for (int i = 0; i < dataList.size(); i++) {
                    for (int j = 0; j < mapKeyArr.length; j++) {
                        buf.append(dataList.get(i).get(mapKeyArr[j]) == null ? "" : dataList.get(i).get(mapKeyArr[j]))
                           .append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            // 写出响应
            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;
        } catch (Exception e) {
            logger.error("doExport错误...", e);
        }
        return false;
    }

    /**
     * @param map 集合数据
     * @param os  表头部数据
     */
    public static boolean doListExport(Map<String, Object> map, OutputStream os) {
        try {
            StringBuffer buf = new StringBuffer();

            String fTitle = (String) map.get("fTitle");
            String fMapKey = (String) map.get("fMapKey");
            List<Map<String, Object>> fDataList = (List<Map<String, Object>>) map.get("fDataList");
            String[] fColNamesArr = fTitle.split(",");
            String[] fMapKeyArr = fMapKey.split(",");

            // 完成数据csv文件的封装，输出列头
            for (int i = 0; i < fColNamesArr.length; i++) {
                buf.append(fColNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);
            if (null != fDataList) {// 输出数据
                for (int i = 0; i < fDataList.size(); i++) {
                    for (int j = 0; j < fMapKeyArr.length; j++) {
                        buf.append(fDataList.get(i).get(fMapKeyArr[j])).append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            buf.append(CSV_RN);

            //第二层
            String tTitle = (String) map.get("tTitle");
            String tMapKey = (String) map.get("tMapKey");
            List<Map<String, Object>> tDataList = (List<Map<String, Object>>) map.get("tDataList");
            String[] tColNamesArr = tTitle.split(",");
            String[] tMapKeyArr = tMapKey.split(",");
            // 完成数据csv文件的封装
            // 输出列头
            for (int i = 0; i < tColNamesArr.length; i++) {
                buf.append(tColNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);
            if (null != fDataList) { // 输出数据
                for (int i = 0; i < tDataList.size(); i++) {
                    for (int j = 0; j < tMapKeyArr.length; j++) {
                        buf.append(tDataList.get(i).get(tMapKeyArr[j])).append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            buf.append(CSV_RN);

            //第三层
            String sTitle = (String) map.get("sTitle");
            String sMapKey = (String) map.get("sMapKey");
            List<Map<String, Object>> sDataList = (List<Map<String, Object>>) map.get("sDataList");
            String[] sColNamesArr = sTitle.split(",");
            String[] sMapKeyArr = sMapKey.split(",");
            // 完成数据csv文件的封装
            // 输出列头
            for (int i = 0; i < sColNamesArr.length; i++) {
                buf.append(sColNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);
            if (null != sDataList) { // 输出数据
                for (int i = 0; i < sDataList.size(); i++) {
                    for (int j = 0; j < sMapKeyArr.length; j++) {
                        buf.append(sDataList.get(i).get(sMapKeyArr[j])).append(CSV_COLUMN_SEPARATOR);
                    }
                    buf.append(CSV_RN);
                }
            }
            // 写出响应
            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;
        } catch (Exception e) {
            logger.error("doExport错误...", e);
        }
        return false;
    }

    /**
     * @author chenglvpeng  2018/08/06增加
     * @param map
     * @param os
     * @return
     */
    public static boolean doListExport2(Map<String, Object> map, OutputStream os) {
        try {
            StringBuffer buf = new StringBuffer();

            String fTitle = (String) map.get("fTitle");
            String prefix = (String) map.get("prefix");
            List<String> fDataList = (List<String>) map.get("fDataList");
            String[] fColNamesArr = fTitle.split(",");

            // 完成数据csv文件的封装，输出列头
            for (int i = 0; i < fColNamesArr.length; i++) {
                buf.append(fColNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
            }
            buf.append(CSV_RN);
            if (null != fDataList) {// 输出数据
                for (int i = 0; i < fDataList.size(); i++) {
                        int index=i+1;
                        StringBuilder sb=new StringBuilder();
                        buf.append(index).append(CSV_COLUMN_SEPARATOR);
                        sb.append(prefix).append(fDataList.get(i));
                        buf.append(sb.toString()).append(CSV_COLUMN_SEPARATOR);

                    buf.append(CSV_RN);
                }
            }
            buf.append(CSV_RN);


            // 写出响应
            os.write(buf.toString().getBytes("GBK"));
            os.flush();
            return true;
        } catch (Exception e) {
            logger.error("doExport错误...", e);
        }
        return false;
    }


    /**
     * @throws UnsupportedEncodingException setHeader
     */
    public static void responseSetProperties(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String fn = fileName + sdf.format(new Date()).toString() + ".csv";
        // 读取字符编码
        String utf = "UTF-8";

        // 设置响应
        response.setContentType("application/ms-txt.numberformat:@");
        response.setCharacterEncoding(utf);
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fn, utf));
    }

}