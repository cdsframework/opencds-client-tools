package org.cdsframework.util.support.cds;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.Constants;
import org.opencds.support.TestCase;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsObjectAssist {

    protected final static Logger logger = Logger.getLogger(CdsObjectAssist.class);
    private static JAXBContext jaxbContext;
    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    private static JAXBContext getJAXBContext() throws CdsException {
        try {
            if (jaxbContext == null) {
                jaxbContext = JAXBContext.newInstance(Constants.getCdsNamespace());
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

    private static void initializeTestCase(TestCase testCase) {
    }

    public static TestCase getTestCase() {
        TestCase testCase = new TestCase();
        initializeTestCase(testCase);
        return testCase;
    }

    public static <S> byte[] cdsObjectToByteArray(S cdsObject, Class<S> cdsObjectClass)
            throws CdsException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        marshal(cdsObject, stream);
        return stream.toByteArray();
    }

    public static <S> String cdsObjectToFile(S cdsObject, String path, String filename)
            throws CdsException, FileNotFoundException, IOException {
        String fullPath = (path == null || path.isEmpty() ? "" : path + "/") + filename + ".xml";

        File file = new File(fullPath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            marshal(cdsObject, fileOutputStream);
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return fullPath;
    }

    public static <S> S cdsObjectFromFile(String fileName, Class<S> cdsObjectClass)
            throws CdsException {
        return cdsObjectFromFile(fileName, null, cdsObjectClass);
    }

    public static <S> S cdsObjectFromFile(String fileName, String transformFileName, Class<S> cdsObjectClass)
            throws CdsException {
        S cdsObject = null;
        try {
            cdsObject = cdsObjectFromStream(new FileInputStream(fileName), transformFileName, cdsObjectClass);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        }
        return cdsObject;
    }

    public static <S> S cdsObjectFromByteArray(byte[] bytes, Class<S> cdsObjectClass)
            throws CdsException {
        return cdsObjectFromByteArray(bytes, null, cdsObjectClass);
    }

    public static <S> S cdsObjectFromByteArray(byte[] bytes, String transformFileName, Class<S> cdsObjectClass)
            throws CdsException {
        return cdsObjectFromStream(new ByteArrayInputStream(bytes), transformFileName, cdsObjectClass);
    }

    public static <S> S cdsObjectFromStream(InputStream inputStream, String transformFileName, Class<S> cdsObjectClass)
            throws CdsException {
        S cdsObject = null;
        InputStream resource = null;
        try {
            if (transformFileName != null) {
                JAXBResult result = new JAXBResult(getJAXBContext());
                TransformerFactory tf = TransformerFactory.newInstance();
                resource = CdsObjectAssist.class.getClassLoader().getResourceAsStream(transformFileName);
                StreamSource xslt = new StreamSource(resource);
                Transformer t = tf.newTransformer(xslt);
                StreamSource source = new StreamSource(inputStream);
                t.transform(source, result);
                cdsObject = (S) result.getResult();
            } else {
                cdsObject = unmarshal(inputStream, cdsObjectClass);
            }
        } catch (TransformerException e) {
            logger.error(e);
            throw new CdsException(e.getMessage());
        } catch (JAXBException e) {
            throw new CdsException(e.getMessage());
        } finally {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        return cdsObject;
    }

    public static <S> String cdsObjectToString(S cdsObject, Class<S> cdsObjectClass)
            throws CdsException {
        return new String(cdsObjectToByteArray(cdsObject, cdsObjectClass));
    }

    public static void validateCdsObject(Object cdsObject, String schemaUrl)
            throws CdsException, SAXException, JAXBException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(schemaUrl));
        marshal(cdsObject, new DefaultHandler(), schema);
    }
}
