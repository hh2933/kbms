package com.shuxin.commons.utils;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

public class ImportErrorExcelUtils {

	 private static final Logger logger = LogManager.getLogger(ImportErrorExcelUtils.class);

	/**
	 * 导入数据错误信息提示表格导出
	 * @param request
	 * @param response
	 * @param staff
	 */
	public static void creatErrorExcel(HttpServletResponse response, 
			List<Map<String, String>> errorlist) {
		InputStream in = null;
		OutputStream out = null;
		try {
			String templateFileName = Thread.currentThread()
					.getContextClassLoader().getResource("").getPath()
					+ "excleTemplate/importErrorInfo.xls";
			String destFileName = java.net.URLEncoder.encode("导入错误信息", "UTF-8")
					+ ".xls";

			// List<Map<String, String>> staff = new ArrayList<Map<String,
			// String>>();

			Map<String, Object> beans = new HashMap<String, Object>();
			beans.put("errorList", errorlist);
			XLSTransformer transformer = new XLSTransformer();

			// 设置响应
			response.setHeader("Content-Disposition", "attachment;filename="
					+ destFileName);
			response.setContentType("application/vnd.ms-excel");

			in = new BufferedInputStream(new FileInputStream(templateFileName));
			Workbook workbook = transformer.transformXLS(in, beans);
			out = response.getOutputStream();
			// 将内容写入输出流并把缓存的内容全部发出去
			workbook.write(out);
			out.flush();
		} catch (Exception e) {
			// e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
