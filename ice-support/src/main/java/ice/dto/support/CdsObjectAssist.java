package ice.dto.support;

import ice.dto.CdsConceptWrapper;
import ice.enumeration.CodeSystemOid;
import ice.exception.IceException;
import ice.util.Constants;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.opencds.CD;
import org.opencds.OpenCdsConceptMappingSpecificationFile;
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
    private static final List<CdsConceptWrapper> cdsConcepts = new ArrayList<CdsConceptWrapper>();
    private static final Map<String, Map<String, String>> codeSets = new HashMap<String, Map<String, String>>();

    static {
        try {
            jaxbContext = JAXBContext.newInstance(Constants.CDS_NAMESPACE);
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

    public static <S> byte[] cdsObjectToByteArray(S cdsObject, Class<S> cdsObjectClass)
            throws IceException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            getMarshaller().marshal(cdsObject, stream);
        } catch (JAXBException e) {
            logger.error(e);
            throw new IceException(e.getMessage());
        }
        return stream.toByteArray();
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

    public static <S> S cdsObjectFromFile(String fileName, Class<S> cdsObjectClass)
            throws IceException {
        return cdsObjectFromFile(fileName, null, cdsObjectClass);
    }

    public static <S> S cdsObjectFromFile(String fileName, String transformFileName, Class<S> cdsObjectClass)
            throws IceException {
        S cdsObject = null;
        try {
            cdsObject = cdsObjectFromStream(new FileInputStream(fileName), transformFileName, cdsObjectClass);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new IceException(e.getMessage());
        }
        return cdsObject;
    }

    public static <S> S cdsObjectFromByteArray(byte[] bytes, Class<S> cdsObjectClass)
            throws IceException {
        return cdsObjectFromByteArray(bytes, null, cdsObjectClass);
    }

    public static <S> S cdsObjectFromByteArray(byte[] bytes, String transformFileName, Class<S> cdsObjectClass)
            throws IceException {
        return cdsObjectFromStream(new ByteArrayInputStream(bytes), transformFileName, cdsObjectClass);
    }

    public static <S> S cdsObjectFromStream(InputStream inputStream, String transformFileName, Class<S> cdsObjectClass)
            throws IceException {
        S cdsObject = null;
        InputStream resource = null;
        try {
            if (transformFileName != null) {
                JAXBResult result = new JAXBResult(jaxbContext);
                TransformerFactory tf = TransformerFactory.newInstance();
                resource = CdsObjectAssist.class.getClassLoader().getResourceAsStream(transformFileName);
                StreamSource xslt = new StreamSource(resource);
                Transformer t = tf.newTransformer(xslt);
                StreamSource source = new StreamSource(inputStream);
                t.transform(source, result);
                cdsObject = (S) result.getResult();
            } else {
                cdsObject = (S) getUnmarshaller().unmarshal(inputStream);
            }
        } catch (TransformerException e) {
            logger.error(e);
            throw new IceException(e.getMessage());
        } catch (JAXBException e) {
            throw new IceException(e.getMessage());
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
            throws IceException {
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

    public static List<CdsConceptWrapper> getCdsConcepts()
            throws IceException {
        if (cdsConcepts.isEmpty()) {
            BufferedReader in = null;
            InputStreamReader inputStreamReader = null;
            InputStream resourceAsStream = null;
            InputStream cdsConceptStream = null;
            try {
                resourceAsStream = CdsObjectAssist.class.getClassLoader().getResourceAsStream(Constants.CDS_CONCEPT_RESOURCE_LIST);
                inputStreamReader = new InputStreamReader(resourceAsStream);
                in = new BufferedReader(inputStreamReader);
                String currentLine = "begin";
                while (currentLine != null) {
                    currentLine = in.readLine();
                    if (currentLine != null) {
                        try {
                            cdsConceptStream = CdsObjectAssist.class.getClassLoader().getResourceAsStream(currentLine);
                            OpenCdsConceptMappingSpecificationFile cdsObject = cdsObjectFromStream(
                                    cdsConceptStream,
                                    Constants.CDS_CONCEPT_XSLT,
                                    OpenCdsConceptMappingSpecificationFile.class);
                            cdsConcepts.add(new CdsConceptWrapper(cdsObject));
                        } finally {
                            cdsConceptStream.close();
                        }
                    }
                }
            } catch (IOException e) {
                logger.error(e);
                throw new IceException(e.getMessage());
            } finally {
                try {
                    in.close();
                    inputStreamReader.close();
                    resourceAsStream.close();
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }
        return cdsConcepts;
    }

    public static Map<String, Map<String, String>> getCodeSets()
            throws IceException {
        if (codeSets.isEmpty()) {
            List<CdsConceptWrapper> concepts = getCdsConcepts();
            for (CdsConceptWrapper item : concepts) {
                Map<String, String> codeSetMembers = codeSets.get(item.getCdsConceptMap().getMembersForCodeSystem().getCodeSystem());
                if (codeSetMembers == null) {
                    codeSetMembers = new HashMap<String, String>();
                    codeSets.put(item.getCdsConceptMap().getMembersForCodeSystem().getCodeSystem(), codeSetMembers);
                }
                for (CD cd : item.getCdsConceptMap().getMembersForCodeSystem().getCDS()) {
                    codeSetMembers.put(cd.getCode(), cd.getDisplayName());
                }
            }
        }
        return codeSets;
    }

    public static Map<String, String> getCodeSystemMap(CodeSystemOid codeSystemOid)
            throws IceException {
        return getCodeSets().get(codeSystemOid.getOid());
    }
//
//    // 2.16.840.1.113883.5.1
//    public static Map<String, String> getGenderCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.GENDER_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.12.292
//    public static Map<String, String> getSubstanceCodeSet()
//            throws IceException {
//        return getCodeSystemMap(Constants.ADMINISTERED_SUBSTANCE_ROOT_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.1
//    public static Map<String, String> getValidityFocusCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.VALIDITY_FOCUS_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.2
//    public static Map<String, String> getValidationCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.VALIDATION_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.3
//    public static Map<String, String> getEvaluatedReasonCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.EVALUATED_REASON_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.4
//    public static Map<String, String> getRecommendedFocusCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.RECOMMENDED_FOCUS_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.5
//    public static Map<String, String> getRecommendationCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.RECOMMENDATION_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.6
//    public static Map<String, String> getRecommendedActionCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.RECOMMENDED_ACTION_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.7
//    public static Map<String, String> getDiseaseFocusCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.DISEASE_FOCUS_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.8
//    public static Map<String, String> getImmunityValueCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.IMMUNITY_VALUE_CODE_SYSTEM_OID);
//    }
//
//    // 2.16.840.1.113883.3.795.12.100.9
//    public static Map<String, String> getImmunityIntCodeSet()
//            throws IceException {
//        return getCodeSet(Constants.IMMUNITY_INT_CODE_SYSTEM_OID);
//    }
}
