package com.wallker.framework.core.encrypt;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import com.alibaba.fastjson.JSONObject;

/** 
 * RSA公钥/私钥/签名工具包
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 *
 * @author Wallker.Gao
 * @date 2019-03-05
 * @version 1.0
 */
public class RSAUtils {

    /** 加密算法RSA **/
    public static final String KEY_ALGORITHM = "RSA";

    /** 签名算法  **/
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    /** 获取公钥的key **/
    private static final String PUBLIC_KEY = "RSAPublicKey";

    /** 获取私钥的key **/
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /** RSA最大加密明文大小  **/
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** RSA最大解密密文大小 **/
    private static final int MAX_DECRYPT_BLOCK = 128;

    /** 
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    public static Map<String, String> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, String> keyMap = new HashMap<>(2);
        String pubKey = Base64.encode(publicKey.getEncoded());
        String priKey = Base64.encode(privateKey.getEncoded());
//        keyMap.put(PUBLIC_KEY, publicKey);
//        keyMap.put(PRIVATE_KEY, privateKey);
        keyMap.put(PUBLIC_KEY, pubKey);
        keyMap.put(PRIVATE_KEY, priKey);
        return keyMap;
    }

    /** 
     * 用私钥对信息生成数字签名
     * @param data 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
    	
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return Base64.encode(signature.sign());
    }

    /** 
     * 校验数字签名
     * @param data 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @param sign 数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(Base64.decode(sign));
    }

    /** 
     * 私钥解密
     * @param encryptedData 已加密数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /** 
     * 公钥解密
     * @param encryptedData 已加密数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /** 
     * 公钥加密
     * @param data 源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** 
     * 私钥加密
     *
     * @param data 源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        byte[] keyBytes = Base64.decode(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }

    /** 
     * 获取私钥
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encode(key.getEncoded());
    }

    /** 
     * 获取公钥
     * @param keyMap 密钥对
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)
            throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encode(key.getEncoded());
    }
    
    public static void main(String args []){
    	try {
			//Map<String, Object> keyMap = RSAUtils.genKeyPair();
			//System.out.println("publicKey:"+ keyMap.get(PUBLIC_KEY));
			//System.out.println("privateKey:"+ keyMap.get(PRIVATE_KEY));
			String publicKey  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoPGmlqkV2gIl0yLEwNKS3UDC6DJwB6v3NDN+EyH+kxTX6srNIynu46yI8tOWsTRZ/p7zjzyd8zJWRv5ekhrWi7hEtUfWwDisFBlNbe9n/4wieQwume3D9OsuDVl5EjUZvYhjSIh0lzVcnWzqvXr6PfqYgEV1irBYeuJvtl8pgfQIDAQAB";
			String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKg8aaWqRXaAiXTIsTA0pLdQMLoMnAHq/c0M34TIf6TFNfqys0jKe7jrIjy05axNFn+nvOPPJ3zMlZG/l6SGtaLuES1R9bAOKwUGU1t72f/jCJ5DC6Z7cP06y4NWXkSNRm9iGNIiHSXNVydbOq9evo9+piARXWKsFh64m+2XymB9AgMBAAECgYEAlXqIDBBMA8yVVVR0SvabW90+TjCh9PpMY4KKnzpeX5JyMbvRnF18rIMPOo0ofH1J/kaViHwpA7PrOULYg/DFNPkrS3RpVpln9Q5MiBDTXRaPsHFczHBxZFdTxmqwgmQXcK63DLwNTH+rj/bZtsPfzQbc7I1fAAkeoTFcBEBYq+kCQQDr6z/X0OQ96BSfmZ/SzsqOt9Iu8xhKS2/ORRDy+N1xxj6h8rbwfvVIpGGY795k5pn69d2ogymhGUGxNGwathHnAkEAto5UY228cMmmeJFYs3u2JtzVmu1Cznm9QM6pnQB+JocN3EKmWfDxtSwdFh+aDvYR9LgYptNSudY6EJHe7541+wJBAMcX2eH8pJFl6PmjgNA7uY9lq9f5SPdx4CH4AUy7Hs5kWfj7xs0Ap2ktaDvTuh/2x92VXgFW52vPz7OHJPAJ1OMCQFUjI7ALKWrgYEayYk+yTkzpiKTsKc/pbp2ew/N4m1yUd55VZwdjGxaHMDl1G4AGunEKulRzEY2hnwrDPNk7S6kCQQDPpe2F4XWSKstwTmt7cQ/GotiVu6hTyjTF94pUZlc+WdcZQzszqjx4/0itsUY2Mw8zI64MvGakyydRfIzr5RHo";
			String text = "\"version\":\"1.0\",\"serviceId\":\"payOrder\"";
			
			/** 公钥加密  **/
			byte[] encryptPub    = encryptByPublicKey(text.getBytes(), publicKey);
			String encryptPubStr = new String(Base64.encode(encryptPub));
			System.out.println("encryptPubStr:"+ encryptPubStr);
			
			/** 私钥解密 **/
			byte[] decryptPri = decryptByPrivateKey(Base64.decode(encryptPubStr), privateKey);
			String decryptPriStr = new String(decryptPri);
			System.out.println("decryptPriStr:"+ decryptPriStr);
			
			/** 私钥加密 **/
			byte[] encryptPri    = encryptByPrivateKey(text.getBytes() ,privateKey);
			String encryptPriStr = new String(Base64.encode(encryptPri));
			System.out.println("encryptPriStr:"+ encryptPriStr);
			
			/** 公钥解密 **/
			byte[] decryptPub = decryptByPublicKey(Base64.decode(encryptPriStr) , publicKey);
			String decryptPubStr = new String(decryptPub);
			System.out.println("decryptPubStr:"+ decryptPubStr);
			
			Map<String, String> map = new HashMap<>();
			map.put("version", "1.0");
			map.put("serviceId", "payOrder");
			map.put("sendId", "1810000001");
			map.put("productCode", "JSB");
			map.put("userId", "1950");
			map.put("accessToken", "1taceai124csdfat");
			String compMap = Tools.createLinkString(map);
			System.out.println("compMap:"+ compMap);
			
			String [] strArray = compMap.split("&");
			Map<String, String> result = new HashMap<>();
			String array = null;
			String key = null;
			String value = null;
			for(int i=0; i<strArray.length; i++ ){
				array = strArray[i];
				key = array.substring(0, array.indexOf("="));
				value = array.substring(array.indexOf("=")+1);
				result.put(key, value);
			}
			System.out.println("map  result:" +  result);
			System.out.println("json result:" +  JSONObject.toJSONString(result));
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    }
    
    
}
