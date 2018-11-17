package cn.itcast.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DoPOST {

    public static void main(String[] args) throws Exception {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 创建http POST请求
        HttpPost httpPost = new HttpPost("https://www.oschina.net/");

        //设置浏览器头部信息
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.err.println(content);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient.close();
        }

    }

}
