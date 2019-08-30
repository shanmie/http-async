package cn.org.http.async.wrap;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static cn.org.http.async.cons.EntityCons.*;

/**
 * @author deacon
 * @since 2019/8/27
 */
@Slf4j
public class EntityWrap {


    /**
     * 参数转换，将map中的参数，转到参数列表中
     *
     * @param nValPairs     参数列表
     * @param map      参数列表（map）
     * @param encoding 编码
     * @return 返回HttpEntity
     */
    public static HttpEntity mapConvertHttpEntity(List<NameValuePair> nValPairs, Map<String, Object> map, String encoding) throws UnsupportedEncodingException {
        HttpEntity entity = null;
        if (map != null && map.size() > 0) {
            boolean isSpecial = false;
            // 拼接参数
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (SPECIAL_ENTITY.contains(entry.getKey())) {//判断是否在之中
                    isSpecial = true;
                    if (ENTITY_STRING.equals(entry.getKey())) {     //string
                        entity = new StringEntity(String.valueOf(entry.getValue()), encoding);
                        break;
                    } else if (ENTITY_JSON.equals(entry.getKey())) {//json
                        entity = new StringEntity(String.valueOf(entry.getValue()), encoding);
                        String contentType = "application/json";
                        if (encoding != null) {
                            contentType += ";charset=" + encoding;
                        }
                        ((StringEntity) entity).setContentType(contentType);
                        break;
                    } else if (ENTITY_BYTES.equals(entry.getKey())) {//file
                        entity = new ByteArrayEntity((byte[]) entry.getValue());
                        break;
                    } else if (ENTITY_FILE.equals(entry.getKey())) {//file
                        if (File.class.isAssignableFrom(entry.getValue().getClass())) {
                            entity = new FileEntity((File) entry.getValue(), ContentType.APPLICATION_OCTET_STREAM);
                        } else if (entry.getValue().getClass() == String.class) {
                            entity = new FileEntity(new File((String) entry.getValue()), ContentType.create("text/plain", "UTF-8"));
                        }
                        break;
                    } else if (ENTITY_INPUT_STREAM.equals(entry.getKey())) {//inputstream
//						entity = new InputStreamEntity();
                        break;
                    } else if (ENTITY_SERIALIZABLE.equals(entry.getKey())) {//serializeable
//						entity = new SerializableEntity()
                        break;
                    } else if (ENTITY_MULTIPART.equals(entry.getKey())) {//MultipartEntityBuilder
                        File[] files = null;
                        if (File.class.isAssignableFrom(entry.getValue().getClass().getComponentType())) {
                            files = (File[]) entry.getValue();
                        } else if (entry.getValue().getClass().getComponentType() == String.class) {
                            String[] names = (String[]) entry.getValue();
                            files = new File[names.length];
                            for (int i = 0; i < names.length; i++) {
                                files[i] = new File(names[i]);
                            }
                        }
                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        builder.setCharset(Charset.forName(encoding));// 设置请求的编码格式
                        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);// 设置浏览器兼容模式
                        int count = 0;
                        for (File file : files) {
//							//把文件转换成流对象FileBody
//							FileBody fileBody = new FileBody(file);
//							builder.addPart(String.valueOf(map.get(ENTITY_MULTIPART+".name")) + count++, fileBody);
                            builder.addBinaryBody(String.valueOf(map.get(ENTITY_MULTIPART + ".name")) + count++, file);
                        }
                        boolean forceRemoveContentTypeCharset = (Boolean) map.get(ENTITY_MULTIPART + ".rmCharset");
                        Map<String, Object> m = new HashMap<>();
                        m.putAll(map);
                        m.remove(ENTITY_MULTIPART);
                        m.remove(ENTITY_MULTIPART + ".name");
                        m.remove(ENTITY_MULTIPART + ".rmCharset");
                        Iterator<Map.Entry<String, Object>> iterator = m.entrySet().iterator();
                        // 发送的数据
                        while (iterator.hasNext()) {
                            Map.Entry<String, Object> e = iterator.next();
                            builder.addTextBody(e.getKey(), String.valueOf(e.getValue()), ContentType.create("text/plain", encoding));
                        }
                        entity = builder.build();// 生成 HTTP POST 实体

                        //强制去除contentType中的编码设置，否则，在某些情况下会导致上传失败
                        if (forceRemoveContentTypeCharset) {
                            removeContentTypeCharset(encoding, entity);
                        }
                        break;
                    } else {
                        nValPairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                    }
                } else {
                    nValPairs.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
                }
            }
            if (!isSpecial) {
                entity = new UrlEncodedFormEntity(nValPairs, encoding);
            }
        }
        return entity;
    }

    /**
     * 移除content-type中的charset
     *
     * @param encoding 编码
     * @param entity   请求参数及数据信息
     */
    private static void removeContentTypeCharset(String encoding, HttpEntity entity) {
        try {
            Class<?> clazz = entity.getClass();
            Field field = clazz.getDeclaredField("contentType");
            field.setAccessible(true); //将字段的访问权限设为true：即去除private修饰符的影响
            if (Modifier.isFinal(field.getModifiers())) {
                Field modifiersField = Field.class.getDeclaredField("modifiers"); //去除final修饰符的影响，将字段设为可修改的
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }
            BasicHeader o = (BasicHeader) field.get(entity);
            field.set(entity, new BasicHeader(HTTP.CONTENT_TYPE, o.getValue().replace("; charset=" + encoding, "")));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error(e.getMessage(), e);
        }
    }


}
