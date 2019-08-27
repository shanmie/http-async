package cn.org.http.async;

import static org.junit.Assert.assertTrue;

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

        /*HttpConfig c = HttpConfig.create();
        String s = HttpUtils.get(c.url("http://www.baidu.com"));
        System.out.println(s);*/

        //用法1
        HttpAsync h = new HttpAsync("http://www.baidu.com?name=123&a=456");
        /*h.addParams("name",123);
        h.addParams("a",456);
        h.addParams("bb",789);*/
        HttpAsyncResponse r = h.get();
        System.out.println(r.getBody());

        //用法1
        HttpAsync hh = new HttpAsync("http://www.baidu.com");
        hh.addParams("name",123);
        hh.addParams("a",456);
        hh.addParams("bb",789);
        HttpAsyncResponse rr = hh.get();
        System.out.println(rr.getBody());



        //用法2 自定义配置
       /* HttpConfig config = HttpConfig.create().url("http://www.baidu.com")
                .client(RegistryClient.registry().pool(100, 10).build());
        HttpAsyncResponse response = HttpAsyncBuild.get(config);
        System.out.println(response.getBody());*/


    }
}
