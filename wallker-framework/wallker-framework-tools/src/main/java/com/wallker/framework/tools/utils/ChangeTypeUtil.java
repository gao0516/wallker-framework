package com.wallker.framework.tools.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeTypeUtil {

	private final static Logger logger = LoggerFactory.getLogger(ChangeTypeUtil.class);

	public final static String BASE_CHAR = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
    public final static String BASE_NUMBER = "0123456789";

	public static Map<String, Object> objectToMap(Object obj, boolean keepNullVal) {
		logger.info("=====[objectToMap]=====obj:{}", obj);
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Field[] declaredFields = obj.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
				if (keepNullVal == true) {
					map.put(field.getName(), field.get(obj));
				} else {
					if (field.get(obj) != null && !"".equals(field.get(obj).toString())) {
						map.put(field.getName(), field.get(obj));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 生成指定长度随机字符
	 * 
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		Random random = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int num = random.nextInt(62);
			buf.append(BASE_CHAR.charAt(num));
		}
		return buf.toString();
	}
	

    public static String randomNumber(int length) {
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(10);
            buf.append(BASE_NUMBER.charAt(num));
        }
        return buf.toString();
    }
}
