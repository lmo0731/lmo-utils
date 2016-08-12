/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.map;

import lmo.utils.map.MapUtil;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import junit.framework.TestCase;
import lmo.utils.bson.BSONSerializer;

/**
 *
 * @author developer
 */
public class MapUtilTest extends TestCase {

    public MapUtilTest(String testName) {
        super(testName);
    }

    public void test1() throws JAXBException {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        map.put("a", Arrays.asList("1"));
        map.put("b", Arrays.asList("2"));
        map.put("c", Arrays.asList("3"));
        map.put("d", Arrays.asList("a", "b", "c"));
        map.put("e", Arrays.asList("1", "2", "3"));
        TestClass t = new TestClass();
        MapUtil.deserializeInto(map, t);
        System.out.println(new BSONSerializer().serialize(t));
    }

    public void testParse() {
        System.out.println("parse");
        List<String> list = Arrays.asList("2012-03-05", "1", "b", "c");
        Type[] genericTypes = new Type[]{String.class};
        Object result = MapUtil.parse(Date.class, list, genericTypes);
        System.out.println(result);
    }

    public static class TestClass {

        private String a;
        private Double b;
        private Integer c;
        private List<String> d;
        private List<Double> e;

        public String getA() {
            return a;
        }

        @XmlElement(name = "e")
        public Double getB() {
            return b;
        }

        public Integer getC() {
            return c;
        }

        public List<String> getD() {
            return d;
        }

        public List<Double> getE() {
            return e;
        }

        public void setA(String a) {
            this.a = a;
        }

        public void setB(Double b) {
            this.b = b;
        }

        public void setC(Integer c) {
            this.c = c;
        }

        public void setD(List<String> d) {
            this.d = d;
        }

        public void setE(List<Double> e) {
            this.e = e;
        }

    }
}
