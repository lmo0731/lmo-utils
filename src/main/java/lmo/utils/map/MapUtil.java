/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.map;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import lmo.utils.StringUtils;

/**
 *
 * @author munkhochir <munkhochir@mobicom.mn>
 */
public class MapUtil {

    public static <K, V> Map<K, V> build(Class<K> k, Class<V> v, Object... obj) {
        Map<K, V> map = new HashMap<K, V>();
        for (int i = 0; i < obj.length; i += 2) {
            map.put(k.cast(obj[i]), v.cast(obj[i + 1]));
        }
        return map;
    }

    public static <T> T get(Object o, String path, Class<T> c) {
        if (path == null) {
            return null;
        }
        if (o == null) {
            return null;
        }
        try {
            String[] keys = path.split("[.]", 2);
            if (o instanceof Map) {
                Object k = ((Map) o).get(keys[0]);
                if (keys.length == 2) {
                    return get(k, keys[1], c);
                } else {
                    return c.cast(k);
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public static <T> void deserializeInto(Map<String, List<String>> map, T o) {
        deserializeInto(map, o, false);
    }

    public static <T> void deserializeInto(Map<String, List<String>> map, T o, boolean ci) {
        if (ci) {
            Map<String, List<String>> cimap = new HashMap<String, List<String>>();
            for (String key : map.keySet()) {
                if (!cimap.containsKey(key.toLowerCase())) {
                    cimap.put(key.toLowerCase(), new ArrayList<String>());
                }
                cimap.get(key.toLowerCase()).addAll(map.get(key));
            }
            map = cimap;
        }
        //System.out.println(map);
        Class c;
        if (o.getClass() == Class.class) {
            c = (Class) o;
        } else {
            c = o.getClass();
        }
        for (Field f : c.getFields()) {
            //System.out.println(f.getName());
            String key = f.getName();
            boolean required = false;
            if (f.isAnnotationPresent(XmlTransient.class)) {
                continue;
            }
            if (f.isAnnotationPresent(XmlElement.class)) {
                XmlElement element = f.getAnnotation(XmlElement.class);
                key = element.name().equals("##default") ? key : element.name();
                required = element.required();
            }
            //System.out.println(key + " required: " + required);
            List<String> value = map.get(ci ? key.toLowerCase() : key);

            if (value != null) {
                try {
                    Type[] cs = new Type[]{};
                    try {
                        if (f.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType pType = (ParameterizedType) f.getGenericType();
                            cs = pType.getActualTypeArguments();
                            //logger.debug(o + "." + f.getName() + " generic type: " + Arrays.toString(cs));
                        }
                    } catch (Exception ex) {
                        //logger.error(key + "", ex);
                    }
                    Object v = parse(f.getType(), value, cs);
                    if (v != null) {
                        f.set(o, v);
                    }
                } catch (Exception ex) {
                }
            }
            if (required) {
                try {
                    if (f.get(o) == null) {
                        //logger.warn(key + " is required");
                        throw new IllegalArgumentException("'" + key + "' is required");
                    }
                } catch (IllegalAccessException ex) {
                }
            }
        }

        for (Method m : c.getMethods()) {
            //System.out.println(m.getName());
            String key = null;
            Method getter = null;
            Method setter = null;
            if (m.getName().length() > 3 && m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                getter = m;
                //System.out.println("getter: " + getter);
                String name = m.getName().substring(3);
                //System.out.println("name: " + name);
                key = StringUtils.decapitalize(name);

                try {
                    setter = c.getMethod("set" + name, getter.getReturnType());
                    //System.out.println("setter: " + setter);
                } catch (Exception ex) {
                    continue;
                }
            }
            if (key == null || getter == null || setter == null) {
                continue;
            }
            if (getter.isAnnotationPresent(XmlTransient.class)) {
                continue;
            }
            boolean required = false;
            if (getter.isAnnotationPresent(XmlElement.class)) {
                XmlElement element = getter.getAnnotation(XmlElement.class);
                key = element.name().equals("##default") ? key : element.name();
                required = element.required();
            }
            //System.out.println(key + " required: " + required);
            List<String> value = map.get(ci ? key.toLowerCase() : key);
            if (value != null) {
                try {
                    Type[] cs = new Type[]{};
                    try {
                        if (getter.getGenericReturnType() instanceof ParameterizedType) {
                            ParameterizedType pType = (ParameterizedType) getter.getGenericReturnType();
                            cs = pType.getActualTypeArguments();
                            //logger.debug(o + "." + f.getName() + " generic type: " + Arrays.toString(cs));
                        }
                    } catch (Exception ex) {
                        //logger.error(key + "", ex);
                    }
                    Object v = parse(getter.getReturnType(), value, cs);
                    if (v != null) {
                        setter.invoke(o, v);
                    }
                } catch (Exception ex) {
                }
            }
            if (required) {
                try {
                    if (getter.invoke(o) == null) {
                        //logger.warn(key + " is required");
                        throw new IllegalArgumentException("'" + key + "' is required");
                    }
                } catch (IllegalAccessException ex) {
                } catch (InvocationTargetException ex) {
                }
            }
        }
    }

    public static Object parse(Class type, List<String> list, Type... genericTypes) {
        Object v = null;
        String value = null;
        if (!list.isEmpty()) {
            value = list.get(0);
        }
        if (Integer.class.isAssignableFrom(type) || type == Integer.TYPE) {
            v = Integer.parseInt(value);
        } else if (Long.class.isAssignableFrom(type) || type == Long.TYPE) {
            v = Long.parseLong(value);
        } else if (Float.class.isAssignableFrom(type) || type == Float.TYPE) {
            v = Float.parseFloat(value);
        } else if (Double.class.isAssignableFrom(type) || type == Double.TYPE) {
            v = Double.parseDouble(value);
        } else if (Boolean.class.isAssignableFrom(type) || type == Boolean.TYPE) {
            v = Boolean.parseBoolean(value);
        } else if (Byte.class.isAssignableFrom(type) || type == Byte.TYPE) {
            v = Byte.parseByte(value);
        } else if (Short.class.isAssignableFrom(type) || type == Short.TYPE) {
            v = Short.parseShort(value);
        } else if (String.class.isAssignableFrom(type)) {
            v = value;
        } else if (type.isArray()) {
            if (!type.getComponentType().isPrimitive()) {
                String[] splitted = list.toArray(new String[]{});
                Object[] o = (Object[]) Array.newInstance(type.getComponentType(), splitted.length);
                for (int i = 0; i < splitted.length; i++) {
                    o[i] = (parse(type.getComponentType(), Arrays.asList(splitted[i])));
                }
                v = o;
            }
        } else if (List.class.isAssignableFrom(type)) {
            String[] splitted = list.toArray(new String[]{});
            List o;
            try {
                o = (List) type.newInstance();
            } catch (Exception ex) {
                o = new ArrayList<Object>();
            }
            for (int i = 0; i < splitted.length; i++) {
                Object e = (parse((Class) genericTypes[0], Arrays.asList(splitted[i])));
                o.add(e);
            }
            v = o;
        } else if (Date.class.isAssignableFrom(type)) {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            try {
                v = sdf1.parse(value);
            } catch (ParseException ex) {
                try {
                    v = sdf2.parse(value);
                } catch (ParseException ex1) {
                }
            }
        } else {
            //"Unsupported type";
        }
        return v;
    }

    public static Map<String, String> explode(String url) {
        HashMap<String, String> ret = new HashMap<String, String>();
        try {
            String params[] = url.split("[&]");
            for (String p : params) {
                String kv[] = p.split("[=]", 2);
                String key = URLDecoder.decode(kv[0], "UTF-8");
                if (!key.isEmpty()) {
                    if (kv.length == 2) {
                        String value = URLDecoder.decode(kv[1], "UTF-8");
                        ret.put(key, value);
                    } else {
                        ret.put(key, null);
                    }
                }
            }
        } catch (Exception ex) {
        }
        return ret;
    }

    public static Map<String, List<String>> explodeList(String url) {
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        try {
            String params[] = url.split("[&]");
            for (String p : params) {
                String kv[] = p.split("[=]", 2);
                String key = URLDecoder.decode(kv[0], "UTF-8");
                if (!key.isEmpty()) {
                    String value = null;
                    if (kv.length == 2) {
                        value = URLDecoder.decode(kv[1], "UTF-8");
                        if (ret.containsKey(key)) {
                            ret.get(key).add(value);
                        } else {
                            ret.put(key, new ArrayList<String>(Arrays.asList(value)));
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return ret;
    }

    public static <T extends Object> String implode(Map<String, T> map) {
        StringBuilder sb = new StringBuilder();
        try {
            for (String key : map.keySet()) {
                if (key.isEmpty()) {
                    continue;
                }
                Object value = map.get(key);
                if (value == null) {
                    sb.append("&").append(URLEncoder.encode(key, "UTF-8"));
                } else if (value instanceof Iterable) {
                    for (Object e : ((Iterable) value)) {
                        if (e != null) {
                            sb.append("&").append(URLEncoder.encode(key, "UTF-8"));
                            sb.append("=").append(URLEncoder.encode(String.valueOf(e), "UTF-8"));
                        } else {
                            sb.append("&").append(URLEncoder.encode(key, "UTF-8"));
                        }
                    }
                } else {
                    sb.append("&").append(URLEncoder.encode(key, "UTF-8"));
                    sb.append("=").append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
                }

            }
            return sb.substring(1);
        } catch (Exception ex) {
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        //System.out.println(MapUtil.build(Integer.class, Object.class, 1, "2"));
    }
}
