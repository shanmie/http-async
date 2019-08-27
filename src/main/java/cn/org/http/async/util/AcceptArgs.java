package cn.org.http.async.util;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.org.http.async.wrap.EntityWrap.mapConvertHttpEntity;

/**
 * @author deacon
 * @since 2019/8/27
 */
public class AcceptArgs {
    /**
     * 检测url是否含有参数，如果有，则把参数加到参数列表中
     *
     * @param url	资源地址
     * @param nvps	参数列表
     * @param encoding	编码
     * @return	返回去掉参数的url
     */
    public static String checkUrlHasParams(String url, List<NameValuePair> nvps, String encoding) throws UnsupportedEncodingException {
        // 检测url中是否存在参数
        if (url.contains("?") && url.indexOf("?") < url.indexOf("=")) {
            Map<String, Object> map = buildParams(url.substring(url.indexOf("?") + 1));
            mapConvertHttpEntity(nvps, map, encoding);
            url = url.substring(0, url.indexOf("?"));
        }
        return url;
    }

    public static void buildGetParams(List<NameValuePair> nvps,Map<String,Object> map,String encoding) throws UnsupportedEncodingException {
        mapConvertHttpEntity(nvps, map, encoding);
    }



    /**
     * 生成参数
     * 参数格式：k1=v1&amp;k2=v2
     *
     * @param paras				参数列表
     * @return					返回参数列表（map）
     */
    public static Map<String,Object> buildParams (String paras){
        String[] p = paras.split("&");
        String[][] ps = new String[p.length][2];
        int pos ;
        for (int i = 0; i < p.length; i++) {
            pos = p[i].indexOf("=");
            ps[i][0]=p[i].substring(0,pos);
            ps[i][1]=p[i].substring(pos+1);
        }
        return buildParams(ps);
    }

    /**
     * 生成参数
     * 参数类型：{{"k1","v1"},{"k2","v2"}}
     *
     * @param paras 				参数列表
     * @return						返回参数列表（map）
     */
    public static Map<String,Object> buildParams(String[][] paras){
        // 创建参数队列
        Map<String,Object> map = new HashMap<>();
        for (String[] para: paras) {
            map.put(para[0], para[1]);
        }
        return map;
    }
}
