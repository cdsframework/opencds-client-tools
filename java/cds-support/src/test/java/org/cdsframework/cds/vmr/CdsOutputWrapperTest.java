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
import org.opencds.support.CdsOutput;
import org.opencds.support.SubstanceAdministrationEvent;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsOutputWrapperTest {

    private static LogUtils logger = LogUtils.getLogger(CdsOutputWrapperTest.class);

    public CdsOutputWrapperTest() {
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
     * Test of getCdsOutputWrapper method, of class CdsOutputWrapper.
     * @throws Exception
     */
    @Test
    public void testGetCdsOutputWrapper() throws Exception {
        long start;
        logger.info("Starting testGetCdsOutputWrapper...");

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        CdsOutputWrapper.getCdsOutputWrapper();

        start = System.nanoTime();
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        logger.logDuration("getCdsOutputWrapper", start);

        start = System.nanoTime();
        output.setPatientGender("F");
        output.setPatientBirthTime("19830630");
        output.addImmunityObservationResult(new Date(), "070.30", "DISEASE_DOCUMENTED", "IS_IMMUNE");

        SubstanceAdministrationEvent hepBComponent1 =
                CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("45", "20080223", "VALID", "100", "");
        SubstanceAdministrationEvent hepBComponent2 =
                CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("08", "20080223", "INVALID", "100", "BELOW_MINIMUM_INTERVAL");
        SubstanceAdministrationEvent hepBComponent3 =
                CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("110", "20080223", "INVALID", "100", "EXTRA_DOSE");

        output.addSubstanceAdministrationEvent("45", "20080223", "12345", new SubstanceAdministrationEvent[]{hepBComponent1, hepBComponent2});

        output.addSubstanceAdministrationEvent("110", "20080223", "12346", new SubstanceAdministrationEvent[]{hepBComponent3});

        output.addSubstanceAdministrationProposal("100", "45", "20111201", "100", "RECOMMENDED", "DUE_NOW");

        logger.logDuration("output init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(output.getCdsObject(), CdsOutput.class));
        logger.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/schema/cdsOutput.xsd"));
        MarshalUtils.marshal(output.getCdsObject(), new DefaultHandler(), schema);
        logger.logDuration("validateCdsObject", start);

        logger.info("Finished testGetCdsOutputWrapper...");
        assertTrue(true);
    }

    /**
     * Test of addImmunityObservationResult method, of class CdsOutputWrapper.
     * @throws Exception
     */
    @Test
    public void testAddImmunityObservationResult() throws Exception {
        long start;
        logger.info("Starting testAddImmunityObservationResult...");

        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        start = System.nanoTime();
        output.addImmunityObservationResult(new Date(), "070.30", "DISEASE_DOCUMENTED", "IS_IMMUNE");
        logger.logDuration("output init", start);

        logger.info("Finished testAddImmunityObservationResult...");
        assertTrue(true);
    }

    /**
     * Test of addSubstanceAdministrationEvent method, of class CdsOutputWrapper.
     * @throws Exception
     */
    @Test
    public void testAddSubstanceAdministrationEvent() throws Exception {
        long start;
        logger.info("Starting testAddSubstanceAdministrationEvent...");

        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        start = System.nanoTime();

        SubstanceAdministrationEvent hepBComponent1 =
                CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("45", "20080223", "VALID", "100", "");
        SubstanceAdministrationEvent hepBComponent2 =
                CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("08", "20080223", "INVALID", "100", "BELOW_MINIMUM_INTERVAL");
        SubstanceAdministrationEvent hepBComponent3 =
                CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("110", "20080223", "INVALID", "100", "EXTRA_DOSE");

        output.addSubstanceAdministrationEvent("45", "20080223", "12345", new SubstanceAdministrationEvent[]{hepBComponent1, hepBComponent2});

        output.addSubstanceAdministrationEvent("110", "20080223", "12346", new SubstanceAdministrationEvent[]{hepBComponent3});

        logger.logDuration("output init", start);

        logger.info("Finished testAddSubstanceAdministrationEvent...");
        assertTrue(true);
    }

    /**
     * Test of addSubstanceAdministrationProposal method, of class CdsOutputWrapper.
     * @throws Exception
     */
    @Test
    public void testAddSubstanceAdministrationProposal() throws Exception {
        long start;
        logger.info("Starting testAddSubstanceAdministrationProposal...");

        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        start = System.nanoTime();
        output.addSubstanceAdministrationProposal("100", "45", "20111201", "100", "RECOMMENDED", "DUE_NOW");
        logger.logDuration("output init", start);

        logger.info("Finished testAddSubstanceAdministrationProposal...");
        assertTrue(true);
    }
}
