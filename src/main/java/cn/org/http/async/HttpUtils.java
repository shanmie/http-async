package cn.org.http.async;

import cn.org.http.async.config.HttpConfig;
import cn.org.http.async.enums.HttpMethods;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author deacon
 * @since 2019/8/26
 */
public class HttpUtils {
    //默认采用的http协议的HttpClient对象
    private static HttpClient client4HTTP;

    //默认采用的https协议的HttpClient对象
    private static HttpClient client4HTTPS;

    static{
            client4HTTP = RegistryClient.registry().build();
            client4HTTPS = RegistryClient.registry().ssl().build();
    }

    /**
     * 判定是否开启连接池、及url是http还是https <br>
     * 		如果已开启连接池，则自动调用build方法，从连接池中获取client对象<br>
     * 		否则，直接返回相应的默认client对象<br>
     *
     * @param config		请求参数配置
     */
    private static void create(HttpConfig config)   {
        if(config.client()==null){//如果为空，设为默认client对象
            if(config.url().toLowerCase().startsWith("https://")){
                config.client(client4HTTPS);
            }else{
                config.client(client4HTTP);
            }
        }
    }

    //-----------华----丽----分----割----线--------------
    //-----------华----丽----分----割----线--------------
    //-----------华----丽----分----割----线--------------

    /**
     * 以Get方式，请求资源或服务
     *
     * @param client				client对象
     * @param url					资源地址
     * @param headers			请求头信息
     * @param context			http上下文，用于cookie操作
     * @param encoding		编码
     * @return						返回处理结果
     */
    public static String get(HttpClient client, String url, Header[] headers, HttpContext context, String encoding)  {
        return get(HttpConfig.create().client(client).url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Get方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return	返回结果
     */
    public static String get(HttpConfig config)   {
        return send(config.method(HttpMethods.GET));
    }

    /**
     * 以Post方式，请求资源或服务
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param parasMap		请求参数
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public static String post(HttpClient client, String url, Header[] headers, Map<String,Object> parasMap, HttpContext context, String encoding)  {
        return post(HttpConfig.create().client(client).url(url).headers(headers).map(parasMap).context(context).encoding(encoding));
    }
    /**
     * 以Post方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String post(HttpConfig config)  {
        return send(config.method(HttpMethods.POST));
    }

    /**
     * 以Put方式，请求资源或服务
     *
     * @param client		client对象
     * @param url			资源地址
     * @param parasMap		请求参数
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public static String put(HttpClient client, String url, Map<String,Object>parasMap,Header[] headers, HttpContext context,String encoding)  {
        return put(HttpConfig.create().client(client).url(url).headers(headers).map(parasMap).context(context).encoding(encoding));
    }
    /**
     * 以Put方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String put(HttpConfig config)  {
        return send(config.method(HttpMethods.PUT));
    }

    /**
     * 以Delete方式，请求资源或服务
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public static String delete(HttpClient client, String url, Header[] headers, HttpContext context,String encoding) {
        return delete(HttpConfig.create().client(client).url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Delete方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String delete(HttpConfig config) {
        return send(config.method(HttpMethods.DELETE));
    }

    /**
     * 以Patch方式，请求资源或服务
     *
     * @param client		client对象
     * @param url			资源地址
     * @param parasMap		请求参数
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public static String patch(HttpClient client, String url, Map<String,Object>parasMap, Header[] headers, HttpContext context,String encoding) {
        return patch(HttpConfig.create().client(client).url(url).headers(headers).map(parasMap).context(context).encoding(encoding));
    }
    /**
     * 以Patch方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String patch(HttpConfig config) {
        return send(config.method(HttpMethods.PATCH));
    }

    /**
     * 以Head方式，请求资源或服务
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public static String head(HttpClient client, String url, Header[] headers, HttpContext context,String encoding) {
        return head(HttpConfig.create().client(client).url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Head方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String head(HttpConfig config) {
        return send(config.method(HttpMethods.HEAD));
    }

    /**
     * 以Options方式，请求资源或服务
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public static String options(HttpClient client, String url, Header[] headers, HttpContext context,String encoding) {
        return options(HttpConfig.create().client(client).url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Options方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String options(HttpConfig config) {
        return send(config.method(HttpMethods.OPTIONS));
    }

    /**
     * 以Trace方式，请求资源或服务
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param encoding		编码
     * @return				返回处理结果
     */
    public static String trace(HttpClient client, String url, Header[] headers, HttpContext context, String encoding)  {
        return trace(HttpConfig.create().client(client).url(url).headers(headers).context(context).encoding(encoding));
    }
    /**
     * 以Trace方式，请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String trace(HttpConfig config)   {
        return send(config.method(HttpMethods.TRACE));
    }

    /**
     * 下载文件
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @param out			输出流
     * @return				返回处理结果
     */
    public static OutputStream down(HttpClient client, String url, Header[] headers, HttpContext context, OutputStream out)   {
        return down(HttpConfig.create().client(client).url(url).headers(headers).context(context).out(out));
    }

    /**
     * 下载文件
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static OutputStream down(HttpConfig config)   {
        if(config.method() == null) {
            config.method(HttpMethods.GET);
        }
        return fmt2Stream(execute(config), config.out());
    }

    /**
     * 上传文件
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @return				返回处理结果
     */
    public static String upload(HttpClient client, String url, Header[] headers, HttpContext context)   {
        return upload(HttpConfig.create().client(client).url(url).headers(headers).context(context));
    }

    /**
     * 上传文件
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String upload(HttpConfig config)   {
        if(config.method() != HttpMethods.POST  && config.method() != HttpMethods.PUT){
            config.method(HttpMethods.POST);
        }
        return send(config);
    }

    /**
     * 查看资源链接情况，返回状态码
     *
     * @param client		client对象
     * @param url			资源地址
     * @param headers		请求头信息
     * @param context		http上下文，用于cookie操作
     * @return				返回处理结果
     */
    public static int status(HttpClient client, String url, Header[] headers, HttpContext context, HttpMethods method)   {
        return status(HttpConfig.create().client(client).url(url).headers(headers).context(context).method(method));
    }

    /**
     * 查看资源链接情况，返回状态码
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static int status(HttpConfig config)   {
        return fmt2Int(execute(config));
    }

    //-----------华----丽----分----割----线--------------
    //-----------华----丽----分----割----线--------------
    //-----------华----丽----分----割----线--------------

    /**
     * 请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回处理结果
     */
    public static String send(HttpConfig config)   {
        return fmt2String(execute(config), "utf-8");
    }

    /**
     * 请求资源或服务，返回HttpResult对象
     *
     * @param config		请求参数配置
     * @return				返回HttpResult处理结果
     */
   /* public static HttpResult sendAndGetResp(HttpConfig config)   {
        Header[] reqHeaders = config.headers();
        //执行结果
        HttpResponse resp =  execute(config);

        HttpResult result = new HttpResult(resp);
        //result.setResult(fmt2String(resp, config.outenc()));
        //result.setReqHeaders(reqHeaders);

        return result;
    }*/

    private static HttpResponse execute(HttpConfig config)   {return null;}

    /**
     * 请求资源或服务
     *
     * @param config		请求参数配置
     * @return				返回HttpResponse对象
     */
   /* private static HttpAsyncResponse execute(HttpConfig config)   {
        create(config);//获取链接
        HttpAsyncResponse resp = null;

        try {
            //创建请求对象
            HttpRequestBase request = getRequest(config.url(), config.method());

            //设置超时
            request.setConfig(config.requestConfig());

            //设置header信息
            request.setHeaders(config.headers());

            //判断是否支持设置entity(仅HttpPost、HttpPut、HttpPatch支持)
            if(HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())){
                List<NameValuePair> nvps = new ArrayList<>();

                if(request.getClass()==HttpGet.class) {
                    //检测url中是否存在参数
                    //注：只有get请求，才自动截取url中的参数，post等其他方式，不再截取
                    config.url(Utils.checkHasParas(config.url(), nvps, config.inenc()));
                }

                //装填参数
                HttpEntity entity = Utils.map2HttpEntity(nvps, config.map(), config.inenc());

                //设置参数到请求对象中
                ((HttpEntityEnclosingRequestBase)request).setEntity(entity);

                if(nvps.size()>0){
                    System.out.println("请求参数："+nvps.toString());
                }
                if(config.json()!=null){
                    System.out.println("请求参数："+config.json());

                }
            }else{
                int idx = config.url().indexOf("?");
                System.out.println("请求地址："+config.url().substring(0, (idx>0 ? idx : config.url().length())));
                if(idx>0){
                    System.out.println("请求参数："+config.url().substring(idx+1));
                }
            }
            //执行请求操作，并拿到结果（同步阻塞）
            resp = (config.context()==null)?config.client().execute(request) : config.client().execute(request, config.context()) ;

            if(config.isReturnRespHeaders()){
                //获取所有response的header信息
                config.headers(resp.getAllHeaders());
            }

            //获取结果实体
            return resp;

        } catch (IOException e) {
        }
        return resp;
    }*/

    //-----------华----丽----分----割----线--------------
    //-----------华----丽----分----割----线--------------
    //-----------华----丽----分----割----线--------------

    /**
     * 转化为字符串
     *
     * @param resp			响应对象
     * @param encoding		编码
     * @return				返回处理结果
     */
    private static String fmt2String(HttpResponse resp, String encoding)   {
        String body = "";
        try {
            if (resp.getEntity() != null) {
                // 按指定编码转换结果实体为String类型
                body = EntityUtils.toString(resp.getEntity(), encoding);
            }else{//有可能是head请求
                body =resp.getStatusLine().toString();
            }
            EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {

        }finally{
            close(resp);
        }
        return body;
    }

    /**
     * 转化为数字
     *
     * @param resp			响应对象
     * @return				返回处理结果
     */
    private static int fmt2Int(HttpResponse resp)   {
        int statusCode = 0;
        try {
            statusCode = resp.getStatusLine().getStatusCode();
            EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {
        }finally{
            close(resp);
        }
        return statusCode;
    }

    /**
     * 转化为流
     *
     * @param resp			响应对象
     * @param out			输出流
     * @return				返回输出流
     */
    public static OutputStream fmt2Stream(HttpResponse resp, OutputStream out)   {
        try {
            resp.getEntity().writeTo(out);
            EntityUtils.consume(resp.getEntity());
        } catch (IOException e) {

        }finally{
            close(resp);
        }
        return out;
    }

   /* *//**
     * 根据请求方法名，获取request对象
     *
     * @param url			资源地址
     * @param method		请求方式
     * @return				返回Http处理request基类
     *//*
    private static HttpRequestBase getRequest(String url, HttpMethods method) {
        HttpRequestBase request = null;
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
    }*/

    /**
     * 尝试关闭response
     *
     * @param resp				HttpResponse对象
     */
    private static void close(HttpResponse resp) {
        try {
            if(resp == null) return;
            //如果CloseableHttpResponse 是resp的父类，则支持关闭
            if(CloseableHttpResponse.class.isAssignableFrom(resp.getClass())){
                ((CloseableHttpResponse)resp).close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
