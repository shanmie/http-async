package cn.org.http.async;

import cn.org.http.async.config.HttpConfig;
import cn.org.http.async.cons.EntityCons;
import cn.org.http.async.enums.HttpMethods;
import cn.org.http.async.request.HttpAsyncRequest;
import cn.org.http.async.response.HttpAsyncResponse;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * @author deacon
 * @since 2019/8/26
 */
public class HttpAsync {

    private ThreadLocal<HttpConfig> threadLocal = new ThreadLocal<>();

    HttpAsync(String url) {
        HttpConfig httpConfig = HttpConfig.create();
        httpConfig.url(url);
        if (threadLocal.get() == null) {
            threadLocal.set(httpConfig);
        }
    }

    HttpAsync addParams(String name, Object val) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, String.valueOf(val));
        threadLocal.get().map(map);
        return this;
    }

    HttpAsync addParams(Map<String, Object> map) {
        threadLocal.get().map(map);
        return this;
    }

    /** post请求 设置消息体参数 注:(和 addParams 函数有冲突 使用时 二选一 ) */
    HttpAsync addEntityParams(String jsonString){
        threadLocal.get().json(jsonString);
        return this;
    }

    HttpAsyncResponse get() {
        return curl(threadLocal.get().method(HttpMethods.GET));
    }

    HttpAsyncResponse post() {
        return curl(threadLocal.get().method(HttpMethods.POST));
    }

    HttpAsyncResponse put() {
        return curl(threadLocal.get().method(HttpMethods.PUT));
    }

    HttpAsyncResponse patch() {
        return curl(threadLocal.get().method(HttpMethods.PATCH));
    }

    HttpAsyncResponse delete() {
        return curl(threadLocal.get().method(HttpMethods.DELETE));
    }

    private HttpAsyncResponse curl(HttpConfig config) {
        HttpResponse resp = HttpAsyncRequest.execute(config);
        if (resp == null) {
            return new HttpAsyncResponse();
        }
        HttpAsyncResponse har = new HttpAsyncResponse(resp);
        har.setReqHeaders(config.headers());
        return har;
    }




}
