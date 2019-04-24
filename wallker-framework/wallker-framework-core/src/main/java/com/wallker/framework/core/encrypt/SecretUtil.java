package com.wallker.framework.core.encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

public class SecretUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecretUtil.class);

    // 定义加密算法，有DES、DESede(卄1�7ES)、Blowfish
    private static final String Algorithm = "DESede";

    private static final String PASSWORD_CRYPT_KEY = "2015Moanker";

    /*
     * 根据字符串生成密钥字节数组1�7
     *
     * @param keyStr 密钥字符丄1�7
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr) throws UnsupportedEncodingException {
        final byte[] key = new byte[24]; // 声明丄1�7丄1�7位的字节数组，默认里面都昄1�7        
        final byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组

		/*
         * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里弄1�7始拷贝，目标数组，拷贝多少位)
		 */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组丄1�7
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组丄1�7
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    /**
     * 解密函数
     *
     * @param src      密文的字节数组1�7
     * @param cryptKey 密钥
     * @return
     */
    public static byte[] decryptMode(byte[] src, String cryptKey) {
        try {
            final SecretKey deskey = new SecretKeySpec(build3DesKey(cryptKey), Algorithm);
            final Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey); // 初始化为解密模式
            return c1.doFinal(src);
        } catch (final Exception e) {
            logger.warn("解密失败!", e);
        }
        return null;
    }


    /**
     * 解密函数
     *
     * @param src 密文的字节数组1�7
     * @return
     */
    public static byte[] decryptMode(byte[] src) {
        return decryptMode(src, PASSWORD_CRYPT_KEY);
    }

    /**
     * 解密函数
     *
     * @param str 密文的字节数组1�7
     * @return
     */
    public static String decryptMode(String str) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        try {
            return decryptMode(str, PASSWORD_CRYPT_KEY);
        } catch (final Exception e) {
            logger.warn("解密失败!", e);
        }
        return null;
    }

    /**
     * 解密函数
     *
     * @param str 密文的字节数组1�7
     * @return
     */
    public static String decryptMode(String str, String key) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        try {
//			final SecretKey deskey = new SecretKeySpec(build3DesKey(key), Algorithm);
//			final Cipher c1 = Cipher.getInstance(Algorithm);
//			c1.init(Cipher.DECRYPT_MODE, deskey); // 初始化为解密模式
//			return new String(c1.doFinal(Base64.decode(str)));
            return new String(decryptMode(Base64.decode(str), key));
        } catch (final Exception e) {
            logger.warn("解密失败!", e);
        }
        return null;

    }

    /**
     * 加密方法
     *
     * @param src 源数据的字节数组
     * @param key 密钥
     * @return
     */
    public static byte[] encryptMode(byte[] src, String key) {
        try {
            final SecretKey deskey = new SecretKeySpec(build3DesKey(key), Algorithm); // 生成密钥
            final Cipher c1 = Cipher.getInstance(Algorithm); // 实例化负责加寄1�7/解密的Cipher工具籄1�7
            c1.init(Cipher.ENCRYPT_MODE, deskey); // 初始化为加密模式
            return c1.doFinal(src);
        } catch (final Exception e) {

        }
        return null;
    }


    /**
     * 加密方法
     *
     * @param src 源数据的字节数组
     * @return
     */
    public static byte[] encryptMode(byte[] src) {
        return encryptMode(src, PASSWORD_CRYPT_KEY);
    }

    /**
     * 加密方法
     *
     * @param str 源数据的字符丄1�7
     * @return
     */
    public static String encryptMode(String str) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        // 加密
        final byte[] secretArr = SecretUtil.encryptMode(str.getBytes(), PASSWORD_CRYPT_KEY);
        return Base64.encode(secretArr);

    }

    /**
     * 加密方法
     *
     * @param str 源数据的字符丄1�7
     * @param key 密钥
     * @return
     */
    public static String encryptMode(String str, String key) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        try {
            return Base64.encode(encryptMode(str.getBytes(), key));
        } catch (final Exception e) {
            // e
        }
        return null;
    }


    public static void main(String[] args) {
        final String msg = "3DES加密解密案例";
        System.out.println("【加密前】：" + msg);

        // 加密
        final String secretArr = SecretUtil.encryptMode(msg, "123456789");
        System.out.println("【加密后】：" + secretArr);

        // 解密
        final String myMsgArr = SecretUtil.decryptMode(secretArr, "123456789");
        System.out.println("【解密后】：" + new String(myMsgArr));

    }

}
