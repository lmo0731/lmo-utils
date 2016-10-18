/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.bson.factory;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 *
 * @munkhochir<lmo0731@gmail.com>
 */
public class BSONDateFactory extends AbstractTransformer implements ObjectFactory {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Override
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        System.out.println("value: " + value);
        System.out.println("valueType: " + value.getClass());
        System.out.println("targetType: " + targetType);
        System.out.println("targetClass: " + targetClass);
        if (targetType == null) {
            return getDate(value);
        } else {
            try {
                Date.class.asSubclass(targetClass);
                return getDate(value);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    Object getDate(Object value) {

        if (value instanceof Map) {
            Map map = (Map) value;
            if (map.containsKey("$date") && map.size() == 1) {
                Object ts = map.get("$date");
                if (ts instanceof Number) {
                    return new Date(((Number) ts).longValue());
                } else if (ts instanceof String) {
                    try {
                        return sdf2.parse(ts.toString());
                    } catch (Exception ex) {
                        try {
                            return sdf1.parse(ts.toString());
                        } catch (Exception ex1) {
                            try {
                                return sdf.parse(ts.toString());
                            } catch (Exception ex2) {
                                throw new JSONException("invalid date format", ex);
                            }
                        }
                    }
                } else {
                    throw new JSONException("invalid type");
                }
            }
        } else if (value instanceof String) {
            try {
                return sdf2.parse(value.toString());
            } catch (Exception ex) {
                try {
                    return sdf1.parse(value.toString());
                } catch (Exception ex1) {
                    try {
                        return sdf.parse(value.toString());
                    } catch (Exception ex2) {
                        throw new JSONException("invalid date format", ex);
                    }
                }
            }
        } else if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        return null;
    }

    @Override
    public void transform(Object object) {
//        this.getContext().writeOpenObject();
//        this.getContext().writeName("$date");
        this.getContext().write(sdf2.format((Date) object));
//        this.getContext().writeCloseObject();
    }
}
