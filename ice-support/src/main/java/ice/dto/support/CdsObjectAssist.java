package ice.dto.support;

import ice.exception.IceException;
import ice.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.opencds.Testcase;
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

    static {
        try {
            jaxbContext = JAXBContext.newInstance("org.opencds");
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error(e);
        }
    }

    public static Marshaller getMarshaller() {
        marshaller.setSchema(null);
        return marshaller;
    }

    public static Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    private static void initializeTestcase(Testcase testcase) {
    }

    public static Testcase getTestcase() {
        Testcase testcase = new Testcase();
        initializeTestcase(testcase);
        return testcase;
    }

    public static <S> byte[] cdsObjectToByteArray(S cdsObject, Class<S> cdsObjectClass) throws IceException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            getMarshaller().marshal(cdsObject, stream);
        } catch (JAXBException e) {
            logger.error(e);
            throw new IceException(e.getMessage());
        }
        return stream.toByteArray();
    }

    public static <S> S cdsObjectFromFile(String filename, Class<S> cdsObjectClass) throws IceException {
        S cdsObject = null;
        File file = new File(filename);
        try {
            cdsObject = (S) getUnmarshaller().unmarshal(file);
        } catch (JAXBException e) {
            logger.error(e);
            throw new IceException(e.getMessage());
        }
        return cdsObject;
    }

    public static <S> String cdsObjectToFile(S cdsObject, String path, String filename)
            throws IceException, FileNotFoundException, IOException {
        String fullPath = (path == null || path.isEmpty() ? "" : path + "/") + filename + ".xml";

        File file = new File(fullPath);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        try {
            getMarshaller().marshal(cdsObject, fileOutputStream);
        } catch (JAXBException e) {
            throw new IceException(e.getMessage());
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return fullPath;
    }

    public static <S> S cdsObjectFromByteArray(byte[] bytes, Class<S> cdsObjectClass) throws IceException {
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        S cdsObject = null;
        try {
            cdsObject = (S) getUnmarshaller().unmarshal(input);
        } catch (JAXBException e) {
            throw new IceException(e.getMessage());
        }
        return cdsObject;
    }

    public static <S> String cdsObjectToString(S cdsObject, Class<S> cdsObjectClass) throws IceException {
        return new String(cdsObjectToByteArray(cdsObject, cdsObjectClass));
    }

    public static void validateCdsObject(Object cdsObject, String schemaUrl)
            throws IceException, SAXException, JAXBException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(schemaUrl));

        Marshaller m = getMarshaller();
        m.setSchema(schema);
        m.marshal(cdsObject, new DefaultHandler());
    }
}
