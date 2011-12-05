package ice.dto.support;

import ice.exception.IceException;
import ice.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.log4j.Logger;
import org.opencds.CD;
import org.opencds.CDSContext;
import org.opencds.CdsInput;
import org.opencds.CdsOutput;
import org.opencds.EvaluatedPerson;
import org.opencds.EvaluatedPerson.ClinicalStatements;
import org.opencds.EvaluatedPerson.Demographics;
import org.opencds.II;
import org.opencds.Testcase;
import org.opencds.Vmr;
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

    private static void initializeCdsInput(CdsInput cdsInput) {
        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795.11.1.1");
        cdsInput.getTemplateIds().add(ii);
        cdsInput.setCdsContext(getCDSContext());
        cdsInput.setVmrInput(getVmr());
    }

    private static void initializeCdsOutput(CdsOutput cdsOutput) {
        cdsOutput.setVmrOutput(getVmr());
    }

    private static void initializeTestcase(Testcase testcase) {
    }

    public static CdsOutput getCdsOutput() {
        CdsOutput cdsOutput = new CdsOutput();
        initializeCdsOutput(cdsOutput);
        return cdsOutput;
    }

    public static CdsInput getCdsInput() {
        CdsInput cdsInput = new CdsInput();
        initializeCdsInput(cdsInput);
        return cdsInput;
    }

    public static Testcase getTestcase() {
        Testcase testcase = new Testcase();
        initializeTestcase(testcase);
        return testcase;
    }

    private static Vmr getVmr() {
        final String METHODNAME = "getVmr ";
        Vmr vmr = new Vmr();
        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795.11.1.1");
        vmr.getTemplateIds().add(ii);
        vmr.setPatient(getEvaluatedPerson());
        return vmr;
    }

    private static CDSContext getCDSContext() {
        final String METHODNAME = "getCDSContext ";
        CD cd = new CD();
        cd.setCode("en");
        cd.setCodeSystem("1.2.3");
        cd.setDisplayName("English");
        CDSContext cdsContext = new CDSContext();
        cdsContext.setCdsSystemUserPreferredLanguage(cd);
        return cdsContext;
    }

    private static EvaluatedPerson getEvaluatedPerson() {
        final String METHODNAME = "getEvaluatedPerson ";
        EvaluatedPerson evaluatedPerson = new EvaluatedPerson();
        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795.11.2.1");
        evaluatedPerson.getTemplateIds().add(ii);
        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        evaluatedPerson.setId(ii);
        evaluatedPerson.setDemographics(getDemographics());
        evaluatedPerson.setClinicalStatements(getClinicalStatements());
        return evaluatedPerson;
    }

    private static ClinicalStatements getClinicalStatements() {
        final String METHODNAME = "getClinicalStatements ";
        ClinicalStatements clinicalStatements = new ClinicalStatements();
        return clinicalStatements;
    }

    private static Demographics getDemographics() {
        final String METHODNAME = "getDemographics ";
        Demographics demographics = new Demographics();
        return demographics;
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
        String encodedFilename = (path == null || path.isEmpty() ? "" : path + "/") + StringUtils.getShaHashFromString(filename) + ".xml";

        File file = new File(encodedFilename);
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
        return encodedFilename;
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
