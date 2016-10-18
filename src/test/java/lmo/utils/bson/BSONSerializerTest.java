/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lmo.utils.bson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import lmo.utils.bson.BSONSerializer;

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
        a.put("date", new Date());
        System.out.println(new BSONSerializer().serialize(a));
    }

    
    
}
