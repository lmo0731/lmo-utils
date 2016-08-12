/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.bson;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @author LMO-PC
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD, TYPE})
public @interface BSON {

    boolean required() default false;

    Class<? extends BSONElementValidator> validate() default BSONElementValidator.class;

}
