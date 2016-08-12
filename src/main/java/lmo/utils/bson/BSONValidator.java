/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.bson;

import flexjson.JSON;
import flexjson.JSONException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lmo.utils.StringUtils;

/**
 *
 * @munkhochir<lmo0731@gmail.com>
 */
public class BSONValidator {

    public static Object validate(Object o) throws JSONException {
        return validate(null, o);
    }

    public static Object validate(String path, Object o) throws JSONException {
        if (o == null) {
            return null;
        }
        path = ((path == null || path.isEmpty()) ? "" : (path + "."));
        for (Field f : o.getClass().getFields()) {
            try {
                if (f.isAnnotationPresent(BSONNotNull.class)
                        || (f.isAnnotationPresent(BSON.class) && f.getAnnotation(BSON.class).required())) {
                    String name = f.getName();
                    if (f.isAnnotationPresent(JSON.class)) {
                        JSON json = f.getAnnotation(JSON.class);
                        if (!json.include()) {
                            continue;
                        }
                        name = json.name();
                    }
                    Object e = f.get(o);
                    if (e == null) {
                        throw new JSONException("'" + path + name + "' element is missing or invalid");
                    }
                }
                if (f.isAnnotationPresent(BSON.class)) {
                    BSON bson = f.getAnnotation(BSON.class);
                    if (bson.validate() != BSONElementValidator.class) {
                        String name = f.getName();
                        if (f.isAnnotationPresent(JSON.class)) {
                            JSON json = f.getAnnotation(JSON.class);
                            if (!json.include()) {
                                continue;
                            }
                            name = json.name();
                        }
                        Object e = f.get(o);
                        if (e != null) {
                            BSONElementValidator type = bson.validate().newInstance();
                            try {
                                type.validate(e);
                            } catch (JSONException ex) {
                                throw new JSONException("'" + path + name + "' element validation error: " + ex.getMessage());
                            }
                        }
                    }
                }
            } catch (JSONException ex) {
                throw ex;
            } catch (Exception ex) {
            }
        }
        for (Method m : o.getClass().getMethods()) {
            try {
                if ((m.isAnnotationPresent(BSONNotNull.class)
                        || (m.isAnnotationPresent(BSON.class) && m.getAnnotation(BSON.class).required()))
                        && m.getParameterTypes().length == 0
                        && m.getName().startsWith("get")) {
                    String name = StringUtils.decapitalize(m.getName());
                    if (m.isAnnotationPresent(JSON.class)) {
                        JSON json = m.getAnnotation(JSON.class);
                        if (!json.include()) {
                            continue;
                        }
                        name = json.name();
                    }
                    Object e = m.invoke(o);
                    if (e == null) {
                        throw new JSONException("'" + path + name + "' element is missing or invalid");
                    }
                }
                if (m.isAnnotationPresent(BSON.class)) {
                    BSON bson = m.getAnnotation(BSON.class);
                    if (bson.validate() != BSONElementValidator.class) {
                        String name = m.getName();
                        if (m.isAnnotationPresent(JSON.class)) {
                            JSON json = m.getAnnotation(JSON.class);
                            if (!json.include()) {
                                continue;
                            }
                            name = json.name();
                        }
                        Object e = m.invoke(o);
                        if (e != null) {
                            BSONElementValidator type = bson.validate().newInstance();
                            try {
                                type.validate(e);
                            } catch (JSONException ex) {
                                throw new JSONException("'" + path + name + "' element validation error: " + ex.getMessage());
                            }
                        }
                    }
                }
            } catch (JSONException ex) {
                throw ex;
            } catch (Exception ex) {
            }
        }
        return o;
    }
}
