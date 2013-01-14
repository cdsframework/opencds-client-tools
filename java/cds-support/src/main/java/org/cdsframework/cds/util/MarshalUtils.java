package org.cdsframework.cds.util;

import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import org.apache.log4j.Logger;
import org.cdsframework.cds.util.Configuration;
import org.cdsframework.cds.exceptions.CdsException;
import org.xml.sax.ContentHandler;

/**
 *
 * @author HLN Consulting, LLC
 */
public class MarshalUtils {

    protected final static Logger logger = Logger.getLogger(MarshalUtils.class);
    private static JAXBContext jaxbContext;
    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    private static JAXBContext getJAXBContext() throws CdsException {
        try {
            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(Configuration.getCdsNamespace());
            }
        } catch (JAXBException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        }
        return jaxbContext;
    }

    public synchronized static void marshal(Object jaxbElement, Object dst, Schema schema) throws CdsException {
        try {
            if (marshaller == null) {
                marshaller = getJAXBContext().createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            }
            marshaller.setSchema(schema);
            if (dst instanceof ContentHandler) {
                marshaller.marshal(jaxbElement, (ContentHandler) dst);
            } else if (dst instanceof OutputStream) {
                marshaller.marshal(jaxbElement, (OutputStream) dst);
            } else {
                throw new CdsException("Unsupported dst type: " + dst);
            }
        } catch (JAXBException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        }
    }

    public static void marshal(Object jaxbElement, OutputStream os) throws CdsException {
        marshal(jaxbElement, os, null);
    }

    public synchronized static <S> S unmarshal(InputStream inputStream, Class<S> returnType) throws CdsException {
        S result = null;
        try {
            if (unmarshaller == null) {
                unmarshaller = getJAXBContext().createUnmarshaller();
            }
            result = (S) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        } catch (ClassCastException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        }
        return result;
    }

}
