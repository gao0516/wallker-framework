package com.wallker.framework.tools.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.IURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import com.alibaba.fastjson.JSONObject;
import com.wallker.framework.tools.http.HttpHelper;

/**
 * @Filename: WordUtil
 * @Description: word 文件操作类
 * 直接使用Word.getHtmlContent() 获取word转html后内容
 * @Author: fu.zhao@PAAT.com
 * @Version: 1.0
 * @CreateTime: 2018/10/18 17:36
 * @History:
 */
public class WordUtil {

    public static void main(String[] args) throws Exception {
        //word资源文件地址
        String sourceFile = "D:\\第2届产品研发中心技能比拼大赛 V1.14.docx";
        //图片临时存储目录
        String temPath = "D:\\check_file";
        //图片上传地址
        String normalUrl = "http://fileserver.jieshui8.net";
        //图片显示地址
        String imageUrl = "http://image.jieshui8.net";
        //word文件后缀
        String suffix = "doc";
        File file = new File(sourceFile);
        InputStream fileInputStream = new FileInputStream(file);
        suffix = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
        //获取body中的内容 返回到前端
        String htmlContent = getHtmlContent(fileInputStream, suffix, normalUrl, imageUrl, temPath);
        System.out.println(htmlContent);
    }

    /***
     * word 转 html 并获取到内容
     * @param fileInputStream word资源文件流
     * @param suffix     word后缀
     * @param normalUrl  normalUrl地址
     * @param imageUrl  imageUrl显示地址
     * @param temPath  临时目录 存储文件目录  /tmp 此处格式固定
     * @return
     * @throws Exception
     */
    public static String getHtmlContent(InputStream fileInputStream, String suffix, String normalUrl, String imageUrl, String temPath) throws Exception {
        if (StringUtils.isNotEmpty(temPath)) {
            temPath = temPath + File.separator;
        }
        //word转换html操作
        String html = wordToHtml(fileInputStream, suffix, normalUrl, imageUrl, temPath);
        String htmlContent = null;
        //转成行内样式
        if (StringUtils.isNotEmpty(html)) {
            org.jsoup.nodes.Document doc = Jsoup.parse(html);
            htmlContent = doc.select("body").html();
        }
        //获取body中的内容 返回到前端
        return htmlContent;
    }


    /***
     * word 转换html操作
     * @param fileInputStream 原word文件流
     * @param suffix 原word文件后缀
     * @param normalUrl normalUrl地址
     * @param imageUrl  imageUrl显示地址
     * @param temPath 临时文件输出目录
     * @return
     * @throws Exception
     */
    private static String wordToHtml(InputStream fileInputStream, String suffix, String normalUrl, String imageUrl, String temPath) throws Exception {
        String docSuffix = "doc";
        String docXSuffix = "docx";
        String content = null;
        //图片的临时目录
        String picSrc = "";
        //html生成的目录地址
        String outFile = "";
        if (StringUtils.isEmpty(suffix)) {
            throw new Exception();
        }
        if (StringUtils.isNotEmpty(temPath)) {
            picSrc = temPath;
            outFile = temPath + UUID.randomUUID().toString() + "temporary.html";
        }
        //本地图片的地址
        List<String> localImgPaths = new ArrayList<String>();
        //内容中的图片地址
        List<String> contentImgPaths = new ArrayList<String>();
        if (docSuffix.equals(suffix)) {
            content = docToHtml(fileInputStream, outFile, picSrc, imageUrl, localImgPaths, contentImgPaths);
        } else if (docXSuffix.equals(suffix)) {
            content = docXToHtml(fileInputStream, outFile, picSrc, imageUrl, localImgPaths, contentImgPaths);
        } else {
            throw new Exception();
        }
        //获取图片的位置
        content = formatHtmlRowCss(content);
        //consoleLocalImg(localImglist);
        //consoleLocalImg(contentImglist);
        //上传本地图片到图片服务器
        List<String> serverImgPaths = saveFileToImgServer(normalUrl, imageUrl, localImgPaths);
        //将内容中的本地图片地址替换成服务器上图片地址的操作
        if (null != serverImgPaths && serverImgPaths.size() > 0 && null != contentImgPaths && contentImgPaths.size() > 0) {
            for (int i = 0; i < contentImgPaths.size(); i++) {
                String contentImg = contentImgPaths.get(i);
                String serverImg = serverImgPaths.get(i);
                //执行内容中服务上的图片替换本地的图片地址
                if (StringUtils.isNotEmpty(contentImg) && StringUtils.isNotEmpty(serverImg)) {
                    content = content.replaceAll(contentImg, serverImg);
                }
            }
        }
        return content;
    }


    /**
     * doc转html
     *
     * @param fileInputStream word文件流
     * @param outFile         输出的html临时文件
     * @param picSrc          写入的临时图片地址
     * @param htmlImgAction   临时图片显示的地址
     * @param localImgPaths   存储本地图片位置    （用于将本地图片上传到图片服务器上）
     * @param contentImgPaths 存储html中文件的地址（用于后续替换图片服务器的地址）
     * @return
     * @throws TransformerException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private static String docToHtml(InputStream fileInputStream, String outFile, final String picSrc, final String htmlImgAction,
                                    final List<String> localImgPaths, final List<String> contentImgPaths)
            throws TransformerException, IOException, ParserConfigurationException {
        String content = null;
        HWPFDocument wordDocument = new HWPFDocument(fileInputStream);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
                                      float heightInches) {
                localImgPaths.add(picSrc + suggestedName);
                contentImgPaths.add(htmlImgAction + "/" + suggestedName);
                return htmlImgAction + "/" + suggestedName;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);
        List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
        if (pics != null) {
            for (int i = 0; i < pics.size(); i++) {
                Picture pic = pics.get(i);
                try {
                    File file = new File(picSrc);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    pic.writeImageContent(new FileOutputStream(picSrc + pic.suggestFullFileName()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        content = new String(out.toByteArray());
        writeFile(content, outFile);
        //因上面可以直接获取到内容  所以这块调读取文件操作（内带删除该文件的操作） 并不取读取的内容
        fileString(new File(outFile));
        return content;
    }

    /**
     * docX转html word文件流
     *
     * @param fileInputStream
     * @param outFile         输出的html临时文件
     * @param picSrc          写入的临时图片地址
     * @param htmlImgAction   临时图片显示的地址
     * @param localImgPaths   存储本地图片位置    （用于将本地图片上传到图片服务器上）
     * @param contentImgPaths 存储html中文件的地址（用于后续替换图片服务器的地址）
     * @return
     * @throws Exception
     */
    private static String docXToHtml(InputStream fileInputStream, String outFile, final String picSrc, final String htmlImgAction,
                                     final List<String> localImgPaths, final List<String> contentImgPaths) throws Exception {
        String content = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            XWPFDocument document = new XWPFDocument(fileInputStream);
            XHTMLOptions options = XHTMLOptions.create();
            // 存放图片的文件夹
            options.setExtractor(new FileImageExtractor(new File(picSrc)));
            // html中图片的路径
            options.URIResolver(new IURIResolver() {
                public String resolve(String uri) {
                    localImgPaths.add(picSrc + uri);
                    contentImgPaths.add(htmlImgAction + "/" + uri);
                    return htmlImgAction + "/" + uri;
                }
            });
            //忽视样式
            //options.setIgnoreStylesIfUnused(false);
            //options.setFragment(true);
            File file = new File(outFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
            XHTMLConverter xhtmlConverter = (XHTMLConverter) XHTMLConverter.getInstance();
            xhtmlConverter.convert(document, outputStreamWriter, options);
            content = fileString(new File(file.getPath()));
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
        }
        return content;
    }


    /***
     * 输出html
     * @param content
     * @param path
     */
    private static void writeFile(String content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bw.write(content);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }


    /**
     * 读取该文件的内容
     *
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    private static String fileString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = result.toString();
        if (StringUtils.isNotEmpty(content)) {
            //删除文件
            file.delete();
        }
        return content;
    }


    /**
     * 将外联样式转为行内样式
     *
     * @param html
     * @return
     */
    private static String formatHtmlRowCss(String html) {
        org.jsoup.nodes.Document jsoup = Jsoup.parse(html);
        String allCss = jsoup.getElementsByTag("style").html();
        Map<String, String> styleMap = new HashMap<>();
        Pattern pattern = Pattern.compile("\\.(\\w+)\\{(?s)(.+?)\\}");
        Matcher matcher = pattern.matcher(allCss);
        while (matcher.find()) {
            styleMap.put(matcher.group(1), matcher.group(2));
        }
        return replaceClass(styleMap, html);
    }

    /**
     * 获取html中需要转换的标签或类
     *
     * @param styMap
     * @param html
     * @return
     */
    private static String replaceClass(Map<String, String> styMap, String html) {
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        Elements anyClass = doc.getElementsByAttribute("class");
        for (Element element : anyClass) {
            String aClass = element.attr("class");
            String[] classStrs = (aClass.split(" "));
            for (String classStr : classStrs) {
                element.attr("style", element.attr("style") + styMap.get(classStr));
            }
            element.removeAttr("class");
        }
        return doc.toString();
    }

    /**
     * 保存本地文件到文件服务器上
     *
     * @param normalUrl
     * @param imageUrl
     * @param localImgPaths
     * @return
     */
    private static List<String> saveFileToImgServer(String normalUrl, String imageUrl, List<String> localImgPaths) {
        List<String> serverImgList = new ArrayList<String>();
        if (null != localImgPaths && localImgPaths.size() > 0) {
            for (String localImg : localImgPaths) {
                //执行上传操作
                serverImgList.add(uploadImage(normalUrl, imageUrl, localImg));
            }
        }
        return serverImgList;
    }

    /***
     * 上传本地文件到图片服务器上操作
     * @param normalUrl
     * @param imageUrl
     * @param localImg
     * @return
     */
    private static String uploadImage(String normalUrl, String imageUrl, String localImg) {
        if (StringUtils.isNotEmpty(localImg)) {
            File file = new File(localImg);
            Map<String, String> map = new HashMap<>();
            try {
                JSONObject img = HttpHelper.postImage(normalUrl + "//file/uploadimg", file, "file", map);
                String ico = img.getString("fileId");
                //执行完上面步骤 然后删除本地的图片（包含一些其他文件）
                file.delete();
                if (StringUtils.isNotEmpty(ico)) {
                    return imageUrl + "/" + ico.substring(0, 3) + "/" + ico + ".jpg";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
