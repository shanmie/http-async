package cn.org.http.async.response;

import cn.org.http.async.config.HttpConfig;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.util.EntityUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author deacon
 * @since 2019/8/27
 */
@Slf4j
@Data
@NoArgsConstructor
public class HttpAsyncResponse {

    private static final ThreadLocal<OutputStream> outs = new ThreadLocal<>();

    private String body ;//执行结果
    private int statusCode;//状态码
    private StatusLine statusLine;//状态行
    private Header[] reqHeaders;//请求头
    private Header[] respHeaders;//响应头
    private ProtocolVersion protocolVersion;//协议版本
    private HttpResponse resp; //HttpResponse结果对象
    private HttpEntity httpEntity;
    private HttpConfig httpConfig;

    public HttpAsyncResponse(HttpResponse resp){
        this.statusCode = resp.getStatusLine().getStatusCode();
        this.statusLine = resp.getStatusLine();
        this.respHeaders = resp.getAllHeaders();
        this.protocolVersion = resp.getProtocolVersion();
        this.resp = resp;
        body(resp);
    }

    public void asOutputStream() {
        try {
            if (outs.get() == null) {
                outs.set(new FileOutputStream(""));
            }
            resp.getEntity().writeTo(outs.get());
            EntityUtils.consume(resp.getEntity());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        outs.get();
    }

    public void asFile(){}

    private void body(HttpResponse resp) {
        try {
            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                String strEntity = EntityUtils.toString(entity, "UTF-8");
                this.body = strEntity;
            } else {
                String strEntity = resp.getStatusLine().toString();
                this.body = strEntity;
            }
            EntityUtils.consume(entity);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

}
