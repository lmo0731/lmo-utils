/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.conf;

import java.util.Properties;
import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author munkhochir
 */
public class ConfigLoaderTest extends TestCase {

    static {
        BasicConfigurator.configure();
    }

    public ConfigLoaderTest(String testName) {
        super(testName);
    }

    public void test1() {
        Properties p = new Properties();
        p.setProperty("OK.INT", "1");
        p.setProperty("OK.LONG", "2");
        p.setProperty("OK.STRING", "STRING");
        ObjectType o = new ObjectType();
        ConfigLoader.load(o, p, "OK");
        assertEquals(o.INT, new Integer(1));
        assertEquals(o.LONG, new Long(2));
        assertEquals(o.STRING, ("STRING"));
    }

    public static class ClassType {

        private static Integer INT;
        private static Long LONG;
        private static String STRING;

        public static Integer getINT() {
            return INT;
        }

        public static void setINT(Integer INT) {
            ClassType.INT = INT;
        }

        public static Long getLONG() {
            return LONG;
        }

        public static void setLONG(Long LONG) {
            ClassType.LONG = LONG;
        }

        public static String getSTRING() {
            return STRING;
        }

        public static void setSTRING(String STRING) {
            ClassType.STRING = STRING;
        }

    }

    public static class ObjectType {

        private Integer INT;
        private Long LONG;
        private String STRING;

        public Integer getINT() {
            return INT;
        }

        public void setINT(Integer INT) {
            this.INT = INT;
        }

        public Long getLONG() {
            return LONG;
        }

        public void setLONG(Long LONG) {
            this.LONG = LONG;
        }

        public String getSTRING() {
            return STRING;
        }

        public void setSTRING(String STRING) {
            this.STRING = STRING;
        }

    }
}
