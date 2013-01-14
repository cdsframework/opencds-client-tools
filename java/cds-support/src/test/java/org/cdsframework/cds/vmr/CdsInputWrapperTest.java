package org.cdsframework.cds.vmr;

import java.io.File;
import java.util.Date;
import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.cdsframework.cds.util.LogUtils;
import org.cdsframework.cds.util.MarshalUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencds.support.CdsInput;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsInputWrapperTest {

    private static LogUtils logger = LogUtils.getLogger(CdsInputWrapperTest.class);

    public CdsInputWrapperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getCdsInputWrapper method, of class CdsInputWrapper.
     * @throws Exception
     */
    @Test
    public void testGetCdsInputWrapper() throws Exception {
        long start;
        logger.info("Starting testGetCdsInputWrapper...");

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        CdsInputWrapper.getCdsInputWrapper();

        start = System.nanoTime();
        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        logger.logDuration("getInput", start);

        start = System.nanoTime();
        input.setPatientGender("F");
        input.setPatientBirthTime("19830630");
        input.addSubstanceAdministrationEvent("45", "20080223", "12345");
        input.addSubstanceAdministrationEvent("08", "20080223", "12346");
        input.addImmunityObservationResult(new Date(), "070.30", "DISEASE_DOCUMENTED", "IS_IMMUNE");
        logger.logDuration("input init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(input.getCdsObject(), CdsInput.class));
        logger.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/schema/cdsInput.xsd"));
        MarshalUtils.marshal(input.getCdsObject(), new DefaultHandler(), schema);
        logger.logDuration("validateCdsObject", start);

        logger.info("Finished testGetCdsInputWrapper...");
        assertTrue(true);
    }

    /**
     * Test of addSubstanceAdministrationEvent method, of class CdsInputWrapper.
     * @throws Exception
     */
    @Test
    public void testAddSubstanceAdministrationEvent_StringDate() throws Exception {
        long start;
        logger.info("Starting testAddSubstanceAdministrationEvent_StringDate...");

        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        start = System.nanoTime();
        input.addSubstanceAdministrationEvent("45", "20080223", "12345");
        logger.logDuration("input init", start);

        logger.info("Finished testAddSubstanceAdministrationEvent_StringDate...");
        assertTrue(true);
    }

    /**
     * Test of addSubstanceAdministrationEvent method, of class CdsInputWrapper.
     * @throws Exception
     */
    @Test
    public void testAddSubstanceAdministrationEvent_Date() throws Exception {
        long start;
        logger.info("Starting testAddSubstanceAdministrationEvent_Date...");

        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        start = System.nanoTime();
        input.addSubstanceAdministrationEvent("45", new Date(), "12345");
        logger.logDuration("input init", start);

        logger.info("Finished testAddSubstanceAdministrationEvent_Date...");
        assertTrue(true);
    }

    /**
     * Test of addImmunityObservationResult method, of class CdsInputWrapper.
     * @throws Exception
     */
    @Test
    public void testAddImmunityObservationResult() throws Exception {
        long start;
        logger.info("Starting testAddImmunityObservationResult...");

        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        start = System.nanoTime();
        input.addImmunityObservationResult(new Date(), "070.30", "DISEASE_DOCUMENTED", "IS_IMMUNE");
        logger.logDuration("input init", start);

        logger.info("Finished testAddImmunityObservationResult...");
        assertTrue(true);
    }
}
