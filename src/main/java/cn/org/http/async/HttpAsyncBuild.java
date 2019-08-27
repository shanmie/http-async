package cn.org.http.async;

import cn.org.http.async.config.HttpConfig;
import cn.org.http.async.enums.HttpMethods;
import cn.org.http.async.request.HttpAsyncRequest;
import cn.org.http.async.response.HttpAsyncResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

/**
 * @author deacon
 * @since 2019/8/27
 */
public class HttpAsyncBuild {

    private ThreadLocal<HttpConfig> threadLocal = new ThreadLocal<>();

    public HttpAsyncBuild(HttpClient client){

    }

    static HttpAsyncResponse get(HttpConfig config)   {
        return curl(config.method(HttpMethods.GET));
    }

    public static HttpAsyncResponse post(HttpConfig config)   {
        return curl(config.method(HttpMethods.POST));
    }

    public static HttpAsyncResponse put(HttpConfig config)   {
        return curl(config.method(HttpMethods.PUT));
    }

    public static HttpAsyncResponse patch(HttpConfig config)   {
        return curl(config.method(HttpMethods.PATCH));
    }

    public static HttpAsyncResponse delete(HttpConfig config)   {
        return curl(config.method(HttpMethods.DELETE));
    }





    private static HttpAsyncResponse curl(HttpConfig config) {
        HttpResponse resp = HttpAsyncRequest.execute(config);
        if (resp != null) {
            HttpAsyncResponse har = new HttpAsyncResponse(resp);
            har.setReqHeaders(config.headers());
            return har;
        }
        return new HttpAsyncResponse();
    }

}
