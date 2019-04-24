package com.wallker.framework.tools.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.wallker.framework.tools.file.FileUtil;
import com.wallker.framework.tools.setting.AppSettings;
import com.wallker.framework.tools.utils.IDWorker;
import com.swetake.util.Qrcode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QRCodeUtil {

	private static Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);
	private static String QRCODE_PATH = "qrcode";

    /**
     * 生成不带logo的二维码
     * @param width 宽（正方形）
     * @param content	内容（必选）
     * @return 若为空，则生成失败，否则返回 维码显示url
     */
    public static String createQRCode(int width, String content) {
        String fileId = "";
        if (StringUtils.isBlank(fileId)) {//未生成二维码
            long fid = IDWorker.nextId();
            String fileName = String.format("%s/%s.png", getQRCodeSavePath(), fid);
            fileName = createQRCode(width, width, content, fileName, false);
            if (StringUtils.isBlank(fileName))
                return "";
            fileId = String.valueOf(fid);
        }
        String url = getQRCodeShowURL(fileId);
        return url;
    }

    /**
    * 生成二维码
    * @param width 宽
    * @param height 高
    * @param content	内容（必选）
    * @param fileName	存放路径及文件名（必选）
    * @param needLogo	是否需要logo
    * @return 若为空，则生成失败，否则返回 fileName
    */
    public static String createQRCode(int width, int height, String content, String fileName,
                                      boolean needLogo) {
        if (StringUtils.isBlank(fileName) && StringUtils.isBlank(content))
            return "";
        try {
            Qrcode qrcodeHandler = new Qrcode();
            qrcodeHandler.setQrcodeErrorCorrect('M');
            qrcodeHandler.setQrcodeEncodeMode('B');
            qrcodeHandler.setQrcodeVersion(7);
            byte[] contentBytes = content.getBytes("UTF-8");
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, width, height);
            // 输出内容 > 二维码 
            logger.info("contentBytes.length==={}", contentBytes.length);
            if (contentBytes.length > 0 && contentBytes.length < 120) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                int pixoff = 0;
                int step = (int) Math.ceil(width * 1.0 / codeOut.length);
                int size = codeOut.length * step;
                BufferedImage qrImg = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
                Graphics2D qrGs = qrImg.createGraphics();
                qrGs.setBackground(Color.WHITE);
                qrGs.setColor(Color.BLACK);
                qrGs.clearRect(0, 0, size, size);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            qrGs.fillRect(j * step + pixoff, i * step + pixoff, step, step);
                        }
                    }
                }
                File imgFile = new File(fileName);
                ImageIO.write(qrImg, "png", imgFile);
                qrImg.flush();
                qrGs.dispose();

                Image img = ImageIO.read(imgFile);
                int margin = 10;
                if (width >= 800) {
                    margin = 15;
                } else if (width <= 200) {
                    margin = 5;
                }
                int s = width - margin * 2;
                gs.drawImage(img, margin, margin, s, s, null);
            } else {
                return "";
            }
            String logoPath = getLogoFile();
            if (needLogo && !StringUtils.isBlank(logoPath)) {
                Image img = ImageIO.read(new File(logoPath));//实例化一个Image对象。
                if (img != null) {
                    int w = img.getWidth(null);
                    int x = (width - w) / 2;
                    int y = (width - w) / 2;
                    gs.drawImage(img, x, y, w, w, null);
                    gs.dispose();
                }
            }
            bufImg.flush();
            // 生成二维码QRCode图片  
            File imgFile = new File(fileName);
            ImageIO.write(bufImg, "png", imgFile);
        } catch (Exception e) {
            logger.debug("createQRCode error [{}]", e);
            return "";
        }
        return fileName;
    }

    /**
     * 二维码的服务器本地存储位置
     * ([配置目录（paat.file.image.savePath ）]/qrcode
     * @return
     */
    private static String getQRCodeSavePath() {
        AppSettings as = AppSettings.getInstance();
        String tmp = as.get("paat.file.tmpPath");
        FileUtil.localMkdir(tmp);
        tmp = String.format("%s/%s", tmp, QRCODE_PATH);
        FileUtil.localMkdir(tmp);
        return tmp;
    }

    /**
     * 二维码的服务器显示位置
     * ([配置目录（paat.file.image.url ）]/qrcode
     * @return
     */
    private static String getQRCodeShowURL(String fileId) {
        AppSettings as = AppSettings.getInstance();
        String url = as.get("paat.file.tmpPath");
        url = String.format("%s/%s/%s.png", url, QRCODE_PATH, fileId);
        return url;
    }

    /**
     * 取Logo的完整路径：logo100.png
     * ([配置目录（paat.file.resource.savePath ）]/logo100.png
     * @return
     */
    private static String getLogoFile() {
        AppSettings as = AppSettings.getInstance();
        String path = as.get("paat.file.tmpPath");
        path = String.format("%s/%s", path, "logo100.png");
        if (FileUtil.fileExists(path)){
            return path;
        }
        return null;
    }

    public static void main(String[] args) {
        String aa = QRCodeUtil.createQRCode(1024,
            "{\"invoiceId\":\"1\",\"invoiceNm\":\"上海XX建筑有限公司\",\"taxRegCreNo\":\"310113787239428\",\"registAddress\":\"宝山区月罗路559号M-297室\",\"linkTel\":\"021-51876681\",\"bankOpen\":\"招商银行东方支行\",\"bankAccount\":\"213082031410001\"}");
        //  		String aa = QRCodeUtil.createQRCode(280,"http://users.test.dtu.com/");
        System.out.println(aa);

        String imgurl = QRCodeUtil.createQRCode(280,"http://www.jieshui8.com/");
        System.out.println(imgurl);
    }
}
