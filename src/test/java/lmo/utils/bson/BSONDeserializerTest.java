/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lmo.utils.bson;

import flexjson.JSON;
import flexjson.JSONException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author munkhochir
 */
public class BSONDeserializerTest extends TestCase {

    public BSONDeserializerTest(String testName) {
        super(testName);
    }

    public void testA() {
        String s = "{\"a\": {\"$date\": 123}, \"b\":123, \"c\": {\"d\": \"345\"},"
                + "files:{$1binary: \"c2FuIGJhaW51%&&*\"}, bobj:{zaza:2}}";
        //s = "{\"$date\": 1230}";
        //s = "\"xoxo\"";
        System.out.println(new BSONDeserializer().deserialize(s));
        System.out.println("---------------------------------------------");
        A a = new A();
        a = new BSONDeserializer<A>().deserialize(s, A.class);
        System.out.println(a);
        System.out.println("---------------------------------------------");
        System.out.println(new BSONDeserializer<A>().deserialize(s, A.class));
        System.out.println("---------------------------------------------");
        System.out.println((new BSONDeserializer().deserialize(s)));
        assert (true);
    }

    public void testB() {
        String json = "{zaza: 'a@a', b:{$date: 123}, c:{}, }";
        BSONDeserializer<B> deser = new BSONDeserializer<B>();
        B b = new B();
        deser.deserializeInto(json, b);
        System.out.println(b.a);
        System.out.println(b.b);
        System.out.println(b.c);
        System.out.println(b.d);
        assert (true);
    }

    public void testC() {
        String json = "{res:[{\"_id\":{\"$oid\":\"5343db3b6406fc0811fdb26a\"},\"trans_type_name\":\"Credit card\",\"trans_type_id\":1.0,\"trans_type_desc\":\"+\"},{\"_id\":{\"$oid\":\"5343db3b6406fc0811fdb26b\"},\"trans_type_name\":\"Cash\",\"trans_type_id\":2.0,\"trans_type_desc\":\"+\"},{\"_id\":{\"$oid\":\"5343db3b6406fc0811fdb26c\"},\"trans_type_name\":\"Invoice\",\"trans_type_id\":3.0,\"trans_type_desc\":\"+\"},{\"_id\":{\"$oid\":\"5343db3b6406fc0811fdb26d\"},\"trans_type_name\":\"Bonus\",\"trans_type_id\":4.0,\"trans_type_desc\":\"+\"},{\"_id\":{\"$oid\":\"5343db3b6406fc0811fdb26e\"},\"trans_type_name\":\"Click\",\"trans_type_id\":5.0,\"trans_type_desc\":\"-\"},{\"_id\":{\"$oid\":\"5343db3d6406fc0811fdb26f\"},\"trans_type_name\":\"View\",\"trans_type_id\":6.0,\"trans_type_desc\":\"-\"}]}";
        CL<C> c = new CL<C>();
        R r = new R();
        r = new BSONDeserializer<R>().deserializeInto(json, r);
        System.out.println(r.res);
        System.out.println(r.res.get(0).trans_type_id);
    }

    public static class CL<E> extends ArrayList<E> {
    }

    public static class R {

        public List<C> res;
    }

    public static class C {

        public String trans_type_name;
        public Object trans_type_id;
        public String trans_type_desc;
    }

    public static class A {

        public Integer b;
        public Map<String, Object> a;
        public String d;
        public ByteArrayInputStream files;
        @BSONNotNull
        public B bobj;

        @Override
        public String toString() {
            return String.format("{b=%s, a=%s, d=%s, files=%s, bobj=%s}", b, a, d, files, bobj);
        }
    }

    public static class B {

        @JSON(name = "zaza", include = true)
        @BSON(validate = BSONEmail.class)
        public Object a;
        public Object b;
        public Object c;
        public Object d;
    }

    public static class BSONEmail implements BSONElementValidator<String> {

        public void validate(String t) throws JSONException {
            if (!t.contains("@")) {
                throw new JSONException("not email");
            }
        }

    }
}
