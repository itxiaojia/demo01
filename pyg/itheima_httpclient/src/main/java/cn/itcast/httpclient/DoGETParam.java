package cn.itcast.httpclient;

import java.net.URI;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DoGETParam {

    public static void main(String[] args) throws Exception {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        // 定义请求的参数
        //http://www.pinyougou.com/content/findContentByCategoryId?categoryId=1
        //URI uri = new URIBuilder("http://www.baidu.com/s").setParameter("wd", "java").build();
        URI uri = new URIBuilder("http://login.pyg.com/content/findContentCategoryById").setParameter("categoryId", "1").build();

        System.err.println(uri);

        // 创建http GET请求
        HttpGet httpGet = new HttpGet(uri);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpGet);
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
