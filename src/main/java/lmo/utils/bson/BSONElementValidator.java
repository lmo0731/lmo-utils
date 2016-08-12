/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.bson;

import flexjson.JSONException;

/**
 *
 * @author LMO-PC
 */
public interface BSONElementValidator<T> {

    void validate(T t) throws JSONException;
}
