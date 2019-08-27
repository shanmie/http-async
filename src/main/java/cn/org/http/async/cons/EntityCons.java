package cn.org.http.async.cons;

import java.util.Arrays;
import java.util.List;

/**
 * @author deacon
 * @since 2019/8/27
 */
public class EntityCons {
    //传入参数特定类型
    public static final String ENTITY_STRING="$ENTITY_STRING$";
    public static final String ENTITY_JSON="$ENTITY_JSON$";
    public static final String ENTITY_FILE="$ENTITY_FILE$";
    public static final String ENTITY_BYTES="$ENTITY_BYTES$";
    public static final String ENTITY_INPUT_STREAM="$ENTITY_INPUT_STREAM$";
    public static final String ENTITY_SERIALIZABLE="$ENTITY_SERIALIZABLE$";
    public static final String ENTITY_MULTIPART="$ENTITY_MULTIPART$";
    public static final List<String> SPECIAL_ENTITY = Arrays.asList(ENTITY_STRING, ENTITY_JSON,
            ENTITY_BYTES, ENTITY_FILE, ENTITY_INPUT_STREAM, ENTITY_SERIALIZABLE, ENTITY_MULTIPART);

}
