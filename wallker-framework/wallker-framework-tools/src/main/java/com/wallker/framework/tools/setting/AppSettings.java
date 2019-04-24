package com.wallker.framework.tools.setting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class AppSettings {

    private static Logger      logger     = LoggerFactory.getLogger(AppSettings.class);

    private static AppSettings instance   = null;

    private Properties         properties = new Properties();
    private String             staticVersion;
    private String             currentAppPath;
    private String             profiles;


    public void init() {
        try {
            //获取当前环境变量
            properties.load(new InputStreamReader(
                AppSettings.class.getResourceAsStream("/application.properties"), "UTF-8"));
            this.profiles = SpringUtil.getActiveProfile();
//            this.profiles = get("spring.profiles.active");
            logger.info("**********************************************");
            logger.info("**********************************************");
            logger.info("**********************************************");
            logger.info("**************当前环境："+profiles+"*****************");
            logger.info("**********************************************");
            logger.info("**********************************************");
            logger.info("**********************************************");

            StringBuffer sb = new StringBuffer();
            sb.append("/config/application-").append(profiles).append(".properties");

            logger.info(sb.toString());
            properties.load(new InputStreamReader(
                    AppSettings.class.getResourceAsStream(sb.toString()), "UTF-8"));
            this.staticVersion = get("paat.static.version");
            this.currentAppPath = get("paat.current.app.url");

        } catch (FileNotFoundException e) {
            logger.error("Error reading properties [{}] in AppSettings",
                "paat.application.settings.properties\n [{}]", e.toString());
        } catch (IOException e) {
            logger.error(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
        }
    }

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
            instance.init();
        }
        return instance;
    }

    public String getCurrentAppPath() {
        return currentAppPath;
    }

    public void setCurrentAppPath(String currentAppPath) {
        this.currentAppPath = currentAppPath;
    }

    public String getStaticVersion() {
        return staticVersion;
    }

    public void setStaticVersion(String staticVersion) {
        this.staticVersion = staticVersion;
    }

    public String get(String name) {
        return properties.getProperty(name);
    }

    public Integer getInt(String name) {
        return NumberUtils.toInt(this.get(name), -1);
    }

    public Long getLong(String name) {
        return NumberUtils.toLong(this.get(name), -1);
    }

    public Boolean getBoolean(String name) {
        return BooleanUtils.toBoolean(this.get(name));
    }

    public Double getDouble(String name) {
        return NumberUtils.toDouble(this.get(name), 0d);
    }

    public String[] getByPrefix(String prefix) {
        Set<String> values = Sets.newHashSet();
        String value = this.properties.getProperty(prefix);
        if (StringUtils.isNotBlank(value)) {
            values.add(value);
        }

        Set<Object> keys = properties.keySet();
        for (Object key : keys) {
            if (key.toString().startsWith(prefix)) {
                value = this.properties.getProperty(key.toString());
                if (StringUtils.isNotBlank(value)) {
                    values.add(value);
                }
            }
        }
        return values.toArray(new String[0]);
    }

    public Map<String, String> getAll() {
        Map<String, String> settings = Maps.newHashMap();
        for (Object key : this.properties.keySet()) {
            settings.put(key.toString(), properties.getProperty((String) key));
        }
        return settings;
    }

    public String getSSOLoginPath() {
        return properties.getProperty("sso.login.url");
    }

    public String getSSOLogoutPath() {
        String login = getSSOLoginPath();
        if (StringUtils.isEmpty(login)) {
             return "";
        }else {
            String logout = login.replace("/login", "/logout");
            return logout;
        }
    }

    public Properties getAppSetting() {
        return this.properties;
    }
}
