package com.jd.ecc.cloudbiz.open;

import com.alibaba.druid.util.StringUtils;
import com.jd.ecc.cloudbiz.common.constant.OpenApiConstants;
import com.jd.ecc.cloudbiz.common.utils.DateUtils;
import com.jd.ecc.cloudbiz.common.utils.HttpClientUtils;
import com.jd.ecc.cloudbiz.common.utils.JsonUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.security.MessageDigest;
import java.util.*;

public class OpenApiTest1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiTest1.class);

    public void queryItemCategory(){
        String apiUrl = "findItemCategory";
        Map<String, String> paramsVal = new HashMap<>();
        paramsVal.put("parentId", "0");
        paramsVal.put("level", "1");
        Map<String, String> paramsName = new HashMap<>();
        paramsName.put("param_json", JsonUtils.toJSONString(paramsVal));
        String result = queryDataFromOpenApi(paramsName, apiUrl);
        LOGGER.info("获取的返回结果:{}", result);
    }

    private String queryDataFromOpenApi(Map<String, String> paramMap, String apiUrl){
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> pairs = new ArrayList<>();
            String targetUrl = "http://yunshang-buyer-pre1-api.jcloudec.com/openApi/" + apiUrl;
            String appKey = "11111";
            String app_secret = "33333";
            String timestamp = DateUtils.getStringDate(new Date());
            //为了限制CPS调用量这里会针对每个租户进行CPS调用接口限制
            String param_json = "";
            if(!CollectionUtils.isEmpty(paramMap) && paramMap.get("param_json") != null){
                param_json = paramMap.get("param_json");
            }
            String sign = this.buildSign(timestamp, OpenApiConstants.API_V, OpenApiConstants.API_SIGN_METHOD, OpenApiConstants.API_FORMAT, apiUrl, param_json, OpenApiConstants.API_ACCESS_TOKEN, appKey, app_secret);
            pairs.add(new BasicNameValuePair("sign", sign));
            pairs.add(new BasicNameValuePair("sign_method", OpenApiConstants.API_SIGN_METHOD));
            pairs.add(new BasicNameValuePair("timestamp", timestamp));
            pairs.add(new BasicNameValuePair("v", OpenApiConstants.API_V));
            pairs.add(new BasicNameValuePair("app_id", appKey));
            pairs.add(new BasicNameValuePair("method", apiUrl));
            pairs.add(new BasicNameValuePair("format", OpenApiConstants.API_FORMAT));
            pairs.add(new BasicNameValuePair("access_token", OpenApiConstants.API_ACCESS_TOKEN));
            LOGGER.info("param_json:{}", param_json);
            if (!StringUtils.isEmpty(param_json)) {
                pairs.add(new BasicNameValuePair("param_json", param_json));
            }
            CloseableHttpClient httpclient = HttpClientUtils.getHttpClient();
            HttpPost httpPost = new HttpPost(targetUrl);
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
            response = httpclient.execute(httpPost);
            if (200 != response.getStatusLine().getStatusCode()) {
                Header[] headers = response.getAllHeaders();
                StringBuilder builder = new StringBuilder(128);
                for (Header header : headers) {
                    builder.append("###").append(header.toString());
                }
                LOGGER.error("queryDataFromVop response header" + builder.toString());
            }
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            LOGGER.error("queryDataFromVop paramMap: " + paramMap.toString(), e);
        } finally {
            HttpClientUtils.closeResponse(response);
        }
        return null;
    }

    private String buildSign(String timestamp, String version, String signMethod ,String format ,String method, String paramJson , String accessToken ,String appKey, String appSecret)
            throws Exception {
        //第一步，按照顺序填充参数
        LOGGER.info("带验证的参数 timestamp:{}, version:{}, signMethod:{}, format:{}, method:{}, paramJson:{}, accessToken:{}, appKey:{}, appSecret:{}", timestamp, version, signMethod, format, method, paramJson, accessToken, appKey, appSecret);
        Map<String, String> map = new TreeMap();
        map.put("access_token", accessToken);
        map.put("app_id", appKey);
        map.put("format", format);
        map.put("method", method);
        map.put("param_json", paramJson);
        map.put("sign_method", signMethod);
        map.put("timestamp", timestamp);
        map.put("v", version);
        //param_json为空的时候需要写成 "{}"
        StringBuilder sb = new StringBuilder(appSecret);
        //按照规则拼成字符串
        for (Map.Entry entry : map.entrySet()) {
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            //检测参数是否为空
            if (this.areNotEmpty(new String[]{name, value})) {
                sb.append(name).append(value);
            }
        }
        sb.append(appSecret);
        LOGGER.info("当前待加密的串  sb:{}", sb.toString());
        //MD5
        return this.md5(sb.toString());
    }

    private String md5(String source) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(source.getBytes("utf-8"));
        return byte2hex(bytes);
    }

    private static String byte2hex(byte[] bytes) {

        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    private boolean areNotEmpty(String[] values) {

        boolean result = true;
        if ((values == null) || (values.length == 0))
            result = false;
        else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    private boolean isEmpty(String value) {
        int strLen;
        if ((value == null) || ((strLen = value.length()) == 0)){
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
