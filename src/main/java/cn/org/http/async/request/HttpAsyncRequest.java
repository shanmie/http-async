package cn.org.http.async.request;

import cn.org.http.async.RegistryClient;
import cn.org.http.async.config.HttpConfig;
import cn.org.http.async.enums.HttpMethods;
import cn.org.http.async.util.AcceptArgs;
import cn.org.http.async.wrap.EntityWrap;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author deacon
 * @since 2019/8/27
 */
@Slf4j
public class HttpAsyncRequest {

    private static void setHttpClient(HttpConfig config) {
        if (config.client() == null) {//如果为空，设为默认client对象
            if (config.url().toLowerCase().startsWith("https://")) {
                config.client(RegistryClient.registry().ssl().build());
            } else {
                config.client(RegistryClient.registry().build());
            }
        }
    }

    /**
     * 请求资源或服务
     *
     * @param config 请求参数配置
     * @return 返回HttpResponse对象
     */
    public static HttpResponse execute(HttpConfig config) {
        try {
            setHttpClient(config);//获取链接
            HttpRequestBase request = getRequest(config.url(), config.method());//创建请求对象
            request.setConfig(config.requestConfig());//设置超时
            request.setHeaders(config.headers());//设置header信息
            List<NameValuePair> nValPairs = new ArrayList<>();
            if (request.getClass() == HttpGet.class) {
                int idx = config.url().indexOf("?");
                System.out.println("请求地址：" + config.url().substring(0, (idx > 0 ? idx : config.url().length())));
                if (idx > 0) {
                    //检测url中是否存在参数
                    //注：截取get请求url参数
                    AcceptArgs.checkUrlHasParams(config.url(), nValPairs, "UTF-8");
                    log.info("URL 请求参数 : {}", config.url().substring(idx + 1));
                } else {
                    //装填get参数
                    AcceptArgs.buildGetParams(nValPairs, config.map(), "UTF-8");
                    if (nValPairs.size() > 0) {
                        log.info("GET 请求参数 : {}", nValPairs.toString());
                    }
                    if (config.json() != null) {
                        log.info("GET JSON 请求参数 : {}", config.json());
                    }
                }
            }

            //判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
            if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())) {
                //装填参数
                HttpEntity entity = EntityWrap.mapConvertHttpEntity(nValPairs, config.map(), "UTF-8");

                //设置参数到请求对象中
                ((HttpEntityEnclosingRequestBase) request).setEntity(entity);

                if (nValPairs.size() > 0) {
                    log.info("POST 请求参数 : {}", nValPairs.toString());
                }
                if (config.json() != null) {
                    log.info("POST JSON 请求参数 : {}", config.json());

                }
            }

            //执行请求操作，并拿到结果（同步阻塞）
            HttpResponse resp = (config.context() == null) ? config.client().execute(request)
                    : config.client().execute(request, config.context());


            if (config.isReturnRespHeaders()) {
                //获取所有response的header信息
                config.headers(resp.getAllHeaders());
            }

            //获取结果实体
            return resp;

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据请求方法名，获取request对象
     *
     * @param url    资源地址
     * @param method 请求方式
     * @return 返回Http处理request基类
     */
    private static HttpRequestBase getRequest(String url, HttpMethods method) {
        HttpRequestBase request;
        switch (method.getCode()) {
            case 0:// HttpGet
                request = new HttpGet(url);
                break;
            case 1:// HttpPost
                request = new HttpPost(url);
                break;
            case 2:// HttpHead
                request = new HttpHead(url);
                break;
            case 3:// HttpPut
                request = new HttpPut(url);
                break;
            case 4:// HttpDelete
                request = new HttpDelete(url);
                break;
            case 5:// HttpTrace
                request = new HttpTrace(url);
                break;
            case 6:// HttpPatch
                request = new HttpPatch(url);
                break;
            case 7:// HttpOptions
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpPost(url);
                break;
        }
        return request;
    }
}
