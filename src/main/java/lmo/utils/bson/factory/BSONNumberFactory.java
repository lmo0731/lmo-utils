/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.bson.factory;

import flexjson.JSONException;
import flexjson.JsonNumber;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import java.lang.reflect.Type;

/**
 *
 * @munkhochir<lmo0731@gmail.com>
 */
public class BSONNumberFactory implements ObjectFactory {

    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value == null) {
            return null;
        }
        if (value instanceof JsonNumber) {
            Number number = (Number) value;
            if (number instanceof JsonNumber) {
                if (((JsonNumber) number).isDecimal()) {
                    number = number.doubleValue();
                } else {
                    number = number.longValue();
                }
            }
            if (targetType == Integer.class) {
                return number.intValue();
            }
            if (targetType == Long.class) {
                return number.longValue();
            }
            if (targetType == Double.class) {
                return number.doubleValue();
            }
            if (targetType == Float.class) {
                return number.floatValue();
            }
            if (targetType == Short.class) {
                return number.shortValue();
            }
            if (targetType == Byte.class) {
                return number.byteValue();
            }
            if (targetType == String.class) {
                return "" + number.doubleValue();
            }
            return number;
        }
        throw new JSONException(context.getCurrentPath().toString() + " is not number object");
    }
}
