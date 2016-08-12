package lmo.utils.jaxb;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @munkhochir<lmo0731@gmail.com>
 */
final public class XmlUtil {

    private static <T> JAXBContext getContext(Class<? extends T>... c) throws JAXBException {
        JAXBContext context = null;
        if (c.length == 1) {
            if (instances.containsKey(c[0])) {
                context = instances.get(c[0]);
            } else {
                context = JAXBContext.newInstance(c[0]);
                instances.put(c[0], context);
            }
        } else {
            context = JAXBContext.newInstance(c);
        }
        return context;
    }
    private static HashMap<Class, JAXBContext> instances = new HashMap<Class, JAXBContext>();

    public static String marshal(Object object) throws JAXBException {
        String response = null;
        if (object == null) {
            return "";
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JAXBContext context = getContext(object.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(object, baos);
            XmlValidator.validateRequired(object, object.getClass());
            response = new String(baos.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new JAXBException(ex);
        }
        return response;
    }

    public static <T> T unmarshal(String xmlString, Class<? extends T>... c) throws JAXBException {
        T object = null;
        try {
            byte[] bytes = xmlString.getBytes("UTF-8");
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            JAXBContext context = getContext(c[0]);
            Unmarshaller um = context.createUnmarshaller();
            object = (T) um.unmarshal(bais);
            XmlValidator.validateRequired(object, object.getClass());
        } catch (UnsupportedEncodingException ex) {
            throw new JAXBException(ex);
        }
        return object;
    }
}
