/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lmo.utils.bson;

import lmo.utils.bson.BSONSerializer;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author lmoo
 */
public class BSONSerializerTest extends TestCase {
    
    public BSONSerializerTest(String testName) {
        super(testName);
    }

    public void testSerialize_Object() {
        Map a = new HashMap();
        a.put("a", null);
        System.out.println(new BSONSerializer().serialize(null));
    }

    
    
}
