package com.wallker.framework.tools.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * http工具类
 *                       
 * @Description: 
 * @Copyright:
 * @History:<br>
 *<li>Author: feng.gao</li>
 *<li>Date: 2016年9月22日</li>
 *
 */

public class HttpHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);

    public static String httpGetByUrl(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String returnStr = "";
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                returnStr = EntityUtils.toString(entity);
            }
        } finally {
            response.close();
        }
        return returnStr;
    }

    public static JSONObject httpGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000)
            .setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);
        try {
            response = httpClient.execute(httpGet, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                System.out.println("request url failed, http code="
                                   + response.getStatusLine().getStatusCode() + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                JSONObject result = JSON.parseObject(resultStr);
                Object errcode = result.get("errcode");
                Object state = result.get("state");
                if (null == errcode || (null != errcode && result.getInteger("errcode") == 0)
                    || (null != state && result.getInteger("state") == 0)) {
                    //                if (result.getInteger("errcode") == 0) {
                    //                  result.remove("errcode");
                    //                  result.remove("errmsg");
                    return result;
                } else {
                    System.out.println("request url=" + url + ",return value=");
                    System.out.println(resultStr);
                    int errCode = result.getInteger("errcode");
                    String errMsg = result.getString("errmsg");
                    throw new Exception(errMsg);
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static JSONObject httpPost(String url, Object data) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000)
            .setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/json");

        try {
            StringEntity requestEntity = new StringEntity(JSON.toJSONString(data), "utf-8");
            httpPost.setEntity(requestEntity);

            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                System.out.println("request url failed, http code="
                                   + response.getStatusLine().getStatusCode() + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");

                JSONObject result = JSON.parseObject(resultStr);
                if (0 == result.getInteger("errcode")) {
                    result.remove("errcode");
                    result.remove("errmsg");
                    return result;
                } else {
                    System.out.println("request url=" + url + ",return value=");
                    System.out.println(resultStr);
                    int errCode = result.getInteger("errcode");
                    String errMsg = result.getString("errmsg");
                    throw new Exception(errMsg);
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static JSONObject httpPostCode0(String url, Object data) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000)
                .setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/json");

        try {
            StringEntity requestEntity = new StringEntity(JSON.toJSONString(data), "utf-8");
            httpPost.setEntity(requestEntity);

            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                System.out.println("request url failed, http code="
                        + response.getStatusLine().getStatusCode() + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");

                JSONObject result = JSON.parseObject(resultStr);
                if (0 == result.getInteger("errcode")) {
//                    result.remove("errcode");
//                    result.remove("errmsg");
                    return result;
                } else {
                    System.out.println("request url=" + url + ",return value=");
                    System.out.println(resultStr);
                    int errCode = result.getInteger("errcode");
                    String errMsg = result.getString("errmsg");
                    throw new Exception(errMsg);
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static JSONObject httpPostCode(String url, Object data) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000)
            .setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);
        httpPost.addHeader("Content-Type", "application/json");
        try {
            StringEntity requestEntity = new StringEntity(JSON.toJSONString(data), "utf-8");
            httpPost.setEntity(requestEntity);

            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                LOGGER.info("request url failed, http code="
                            + response.getStatusLine().getStatusCode() + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");
                JSONObject result = JSON.parseObject(resultStr);
                return result;
            }
        } catch (IOException e) {
            LOGGER.info("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static JSONObject httpPostForm(String url, Map<String, String> map) {
        //创建默认的httpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建httppost
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        if (null == map || map.isEmpty()) {
            return null;
        }
        //创建参数队列
        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        HttpEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(formParams, "UTF-8");
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);

            HttpEntity resultEntity = response.getEntity();
            if (resultEntity != null) {
                String resultStr = EntityUtils.toString(resultEntity, "utf-8");
                JSONObject result = JSON.parseObject(resultStr);
                return result;
            }
        } catch (Exception e) {
            LOGGER.info("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public static JSONObject uploadMedia(String url, File file, String fileName) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000)
            .setConnectTimeout(2000).build();
        httpPost.setConfig(requestConfig);

        HttpEntity requestEntity = MultipartEntityBuilder
            .create()
            .addPart(fileName,
                new FileBody(file, ContentType.APPLICATION_OCTET_STREAM, file.getName())).build();
        httpPost.setEntity(requestEntity);

        try {
            response = httpClient.execute(httpPost, new BasicHttpContext());

            if (response.getStatusLine().getStatusCode() != 200) {

                System.out.println("request url failed, http code="
                                   + response.getStatusLine().getStatusCode() + ", url=" + url);
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String resultStr = EntityUtils.toString(entity, "utf-8");

                JSONObject result = JSON.parseObject(resultStr);
                if (result.getInteger("errcode") == 0) {
                    // 成功
                    result.remove("errcode");
                    result.remove("errmsg");
                    return result;
                } else {
                    System.out.println("request url=" + url + ",return value=");
                    System.out.println(resultStr);
                    int errCode = result.getInteger("errcode");
                    String errMsg = result.getString("errmsg");
                    throw new Exception(errMsg);
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static JSONObject postImage(String url, File file, String fileName,
                                       Map<String, String> map) {
        CloseableHttpResponse response = null;
        JSONObject jsonObj = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        FileBody fileBody = new FileBody(file);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart(fileName, fileBody);
        // 发送的数据
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            builder.addTextBody(entry.getKey(), entry.getValue(),
                ContentType.create("text/plain", Charset.forName("UTF-8")));
        }
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            response = httpClient.execute(post);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() != 200) {

            System.out.println("request url failed, http code="
                               + response.getStatusLine().getStatusCode() + ", url=" + url);
            return null;
        }
        HttpEntity result = response.getEntity();
        if (result != null) {
            try {
                String resultStr = EntityUtils.toString(result, "utf-8");
                jsonObj = JSON.parseObject(resultStr);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return jsonObj;
    }

    public static JSONObject postFile(String url, File file, String fileName) {
        CloseableHttpResponse response = null;
        JSONObject jsonObj = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        FileBody fileBody = new FileBody(file);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart(fileName, fileBody);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            response = httpClient.execute(post);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() != 200) {

            System.out.println("request url failed, http code="
                               + response.getStatusLine().getStatusCode() + ", url=" + url);
            return null;
        }
        HttpEntity result = response.getEntity();
        if (result != null) {
            try {
                String resultStr = EntityUtils.toString(result, "utf-8");
                jsonObj = JSON.parseObject(resultStr);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return jsonObj;
    }

    public static JSONObject downloadMedia(String url, String fileDir) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000)
            .setConnectTimeout(2000).build();
        httpGet.setConfig(requestConfig);

        try {
            HttpContext localContext = new BasicHttpContext();

            response = httpClient.execute(httpGet, localContext);

            RedirectLocations locations = (RedirectLocations) localContext
                .getAttribute(HttpClientContext.REDIRECT_LOCATIONS);
            if (locations != null) {
                URI downloadUrl = locations.getAll().get(0);
                String filename = downloadUrl.toURL().getFile();
                System.out.println("downloadUrl=" + downloadUrl);
                File downloadFile = new File(fileDir + File.separator + filename);
                FileUtils.writeByteArrayToFile(downloadFile,
                    EntityUtils.toByteArray(response.getEntity()));
                JSONObject obj = new JSONObject();
                obj.put("downloadFilePath", downloadFile.getAbsolutePath());
                obj.put("httpcode", response.getStatusLine().getStatusCode());

                return obj;
            } else {
                if (response.getStatusLine().getStatusCode() != 200) {

                    System.out.println("request url failed, http code="
                                       + response.getStatusLine().getStatusCode() + ", url=" + url);
                    return null;
                }
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String resultStr = EntityUtils.toString(entity, "utf-8");

                    JSONObject result = JSON.parseObject(resultStr);
                    if (result.getInteger("errcode") == 0) {
                        // 成功
                        result.remove("errcode");
                        result.remove("errmsg");
                        return result;
                    } else {
                        System.out.println("request url=" + url + ",return value=");
                        System.out.println(resultStr);
                        int errCode = result.getInteger("errcode");
                        String errMsg = result.getString("errmsg");
                        throw new Exception(errMsg);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("request url=" + url + ", exception, msg=" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static String redirect302Url(String reqUrl) {
        String location = "";
        try {
            URL serverUrl = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
            conn.setRequestMethod("GET");
            // 必须设置false，否则会自动redirect到Location的地址  
            conn.setInstanceFollowRedirects(false);
            conn.addRequestProperty("Accept-Charset", "UTF-8;");
            conn.addRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
            conn.connect();
            location = conn.getHeaderField("Location");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * GET提交，成功后返回的结果拼成字符串
     * @param urlStr
     * @param paramMap
     * @return
     */
    public static String doGet(String urlStr, Map<String, String> paramMap) {
        try {
            String paramStr = prepareParam(paramMap);
            if (paramStr == null || paramStr.trim().length() < 1) {

            } else {
                urlStr += "?" + paramStr;
            }
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(TIMEOUIT);
            conn.setRequestMethod(SERVLET_GET);
            conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");

            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                "UTF-8"));
            String line;
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            br.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String prepareParam(Map<String, String> paramMap) {
        StringBuffer sb = new StringBuffer();
        if (paramMap == null || paramMap.isEmpty()) {
            return "";
        } else {
            for (String key : paramMap.keySet()) {
                String value = paramMap.get(key);
                if (sb.length() < 1) {
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append("&").append(key).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }

    private static final int    TIMEOUIT    = 5000;
    private static final String SERVLET_GET = "GET";

    public static void main(String[] srg) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("sizes", "100,100;200,300;500,500");
        System.out.print(HttpHelper.postImage("http://fileserver.qqsscs.net//file/uploadimg",
            new File("C:\\Users\\feng.gao\\Desktop\\pp\\31e652fe2d.jpg"), "file", map));
    }

}
