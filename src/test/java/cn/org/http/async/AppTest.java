package cn.org.http.async;

import static org.junit.Assert.assertTrue;

import cn.org.http.async.config.HttpConfig;
import cn.org.http.async.response.HttpAsyncResponse;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void test()  {
        //用法1
        HttpAsync h = new HttpAsync("http://www.baidu.com?name=123&a=456");
        /*h.addParams("name",123);
        h.addParams("a",456);
        h.addParams("bb",789);*/
        h.addEntityParams("{\n" +
                "    \"type\":\"image\",\n" +
                "    \"offset\":0,\n" +
                "    \"count\":50\n" +
                "}");
        HttpAsyncResponse r = h.post();
        System.out.println(r.getBody());

    }

    @Test
    public void test2(){
        //用法1
        String tok = "22_cirHzWpGWiGsxe8CbVh85kDrdtU2BZCQ5qOAj0sDf9fow5anfIjZd0QImEpEktXjR7CL-mSY-1OVFHZ6DE87r8qDLb1sKf8m0TQztVXqVmzpynC1O8BZblYoQNsM4tU4C88aZtrq-5tC1fFxGVYcCIAMBE";
        HttpAsync hh = new HttpAsync("https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token="+tok);
        hh.addEntityParams("{\n" +
                "    \"type\":\"image\",\n" +
                "    \"offset\":0,\n" +
                "    \"count\":50\n" +
                "}");

        HttpAsyncResponse rr = hh.post();
        System.out.println(rr.getBody());
    }

    @Test
    public void test3(){
        //用法2 自定义配置
        HttpConfig config = HttpConfig.create().url("http://www.baidu.com")
                .client(RegistryClient.registry().pool(100, 10).build());
        HttpAsyncResponse response = HttpAsyncBuild.get(config);
        System.out.println(response.getBody());
    }
}
