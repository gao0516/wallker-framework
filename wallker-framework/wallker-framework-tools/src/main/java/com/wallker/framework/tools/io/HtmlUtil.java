package com.wallker.framework.tools.io;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wallker.framework.tools.enums.SrcTypeEnum;

/**
 * @Filename: HtmlUtil
 * @Description: 解析html工具类
 * @Author: fu.zhao@PAAT.com
 * @Version: 1.0
 * @CreateTime: 2018/9/11 16:47
 * @History:
 */
public class HtmlUtil {

	/**
	 * 获取富文本内容中 img video ember 标签的src集合
	 *
	 * @param html
	 * @param srcTypeEnum
	 *            根据id
	 * @return
	 */
	public static List<DocumentData> getContentSrcs(String html, SrcTypeEnum srcTypeEnum) {
		List<DocumentData> DocumentDataList = null;
		if (!StringUtils.isEmpty(html)) {
			DocumentDataList = new ArrayList<DocumentData>();
			Document doc = Jsoup.parse(html);
			if (doc != null) {
				String enumTypeName = srcTypeEnum.getName();
				Elements data = new Elements();
				if (!StringUtils.isEmpty(enumTypeName)) {
					String[] typeNames = enumTypeName.split(";");
					for (String typeName : typeNames) {
						Elements childData = doc.select(typeName);
						for (Element element : childData) {
							data.add(element);
						}
					}
				}
				if (data != null && data.size() > 0) {
					// 获取src
					for (Element e : data) {
						DocumentData documentData = new DocumentData();
						if (e.hasAttr("src")) {
							String src = e.attr("src");
							documentData.setSrc(src);
						}
						// 获取自定标签属性义文件的大小
						if (e.hasAttr("data-file-size")) {
							String fileSize = e.attr("data-file-size");
							documentData.setFileSize(Integer.parseInt(fileSize));
						}
						if (e.hasAttr("data-file-name")) {
							String fileName = e.attr("data-file-name");
							documentData.setFileName(fileName);
						}
						if (e.hasAttr("data-file-format")) {
							String fileFormat = e.attr("data-file-format");
							documentData.setFileFormat(fileFormat);
						}
						if (e.hasAttr("data-file-id")) {
							String fileId = e.attr("data-file-id");
							documentData.setFileId(fileId);
						}
						DocumentDataList.add(documentData);
					}
				}
			}
		}
		return DocumentDataList;
	}

	public static void main(String[] args) {
		String html = "<p>KindEditor"
				+ "<img src=\"http://image.jieshui8.net/029/029aa6082e15e6f232270c99cd749dd5.jpg\"  data-file-size=\"1M\" data-ke-src=\"http://image.jieshui8.net/029/029aa6082e15e6f232270c99cd749dd5.jpg\" alt=\"\">dadasda"
				+ "<img src=\"http://image.jieshui8.net/00d/00dbe662e45202671db28d3a78bf8b45_200_200.jpg\" data-file-size=\"1M\" data-ke-src=\"http://image.jieshui8.net/00d/00dbe662e45202671db28d3a78bf8b45_200_200.jpg\" alt=\"\">sdada</p>\n"
				+ "<p><img class=\"lazy\" src=\"http://image.jieshui8.com/fb2/fb2aa3f0842a94b73ab82335f7d1dcf8.jpg\" data-file-size=\"4M\" alt=\"工资收入型税务筹划\" data-original=\"http://image.jieshui8.com/fb2/fb2aa3f0842a94b73ab82335f7d1dcf8.jpg\" style=\"display: block;\"></p>\n"
				+ "<p><span style=\"background-color:#E53333;\">&nbsp;dasdasdadada</span><em><span style=\"color: rgb(229, 102, 0); background-color: rgb(229, 51, 51);\">\u200B</span><span style=\"background-color:#E53333;\">\u200B&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;</span></em><span style=\"background-color: rgb(255, 153, 0);\">sdasdasdaxxasdadadasdasda</span><span style=\"background-color: rgb(255, 153, 0);\">\u200B&nbsp; sdas dad a</span><span style=\"background-color: rgb(255, 153, 0);\">\u200B</span>"
				+ "<img src=\"http://image.jieshui8.net/00b/00b9d6c65be5868d52f7d013671e08e4.jpg\" data-file-size=\"5M\" data-ke-src=\"http://image.jieshui8.net/00b/00b9d6c65be5868d52f7d013671e08e4.jpg\" alt=\"\"></p>\n"
				+ "<p><img src=\"http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg\" "
				+ "_src=\"http://img.zcool.cn/community/0117e2571b8b246ac72538120dd8a4.jpg@1280w_1l_2o_100sh.jpg\">"
				+ "<br><img src=\"http://image.jieshui8.net/6cc/6ccfd07d1dcc37ea29cdd617d8aabc8a.jpg\" data-file-size=\"6M\"></p>"
				+ "<p><video type=\"application/x-shockwave-flash\" class=\"edui-faked-video\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" src=\"https://media.w3.org/2010/05/sintel/trailer.mp4\"\" +\n"
				+ "\" +\n"
				+ "\"\\\"width=\\\\\\\"420\\\\\\\" height=\\\\\\\"280\\\\\\\" style=\\\\\\\"float:none\\\\\\\" vmode=\\\\\\\"transparent\\\\\\\" play=\\\\\\\"true\\\\\\\"\\\" +\\n\" +\n"
				+ "\"\"loop=\"false\" menu=\"false\" allowscriptaccess=\"never\" allowfullscreen=\"true\"><br/><embed type=\"application/x-shockwave-flash\" class=\"edui-faked-video\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" src=\"https://media.w3.org/2010/05/sintel/trailer.mp4\"\" +\n"
				+ "\"width=\\\"420\\\" height=\\\"280\\\" style=\\\"float:none\\\" vmode=\\\"transparent\\\" play=\\\"true\\\"\" +\n"
				+ "\"loop=\"false\" menu=\"false\" allowscriptaccess=\"never\" allowfullscreen=\"true\">";
		List<DocumentData> documentDataList = getContentSrcs(html, SrcTypeEnum.IMG);
		for (DocumentData documentData : documentDataList) {
			System.out.println(documentData);
		}
	}

}
