package org.cdsframework.cds.testcase;

import java.io.File;
import java.util.Date;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.cdsframework.cds.util.LogUtils;
import org.cdsframework.cds.util.MarshalUtils;
import org.cdsframework.cds.vmr.CdsInputWrapper;
import org.cdsframework.cds.vmr.CdsObjectAssist;
import org.cdsframework.cds.vmr.CdsOutputWrapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencds.support.CdsInput;
import org.opencds.support.CdsOutput;
import org.opencds.support.ObservationResult;
import org.opencds.support.SubstanceAdministrationEvent;
import org.opencds.support.SubstanceAdministrationProposal;
import org.opencds.support.TestCase;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author HLN Consulting, LLC
 */
public class TestCaseWrapperTest {

    private final static LogUtils logger = LogUtils.getLogger(TestCaseWrapperTest.class);

    public TestCaseWrapperTest() {
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
     * Test of getTestCaseWrapper method, of class TestCaseWrapper.
     * @throws Exception
     */
    @Test
    public void testGetTestCaseWrapper() throws Exception {
        long start;
        logger.info("Starting testGetTestCaseWrapper...");

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        TestCaseWrapper.getTestCaseWrapper();

        start = System.nanoTime();
        TestCaseWrapper testcase = TestCaseWrapper.getTestCaseWrapper();
        logger.logDuration("getTestCaseWrapper", start);

        start = System.nanoTime();
        testcase.setExecutiondate(new Date());
        testcase.setSuiteName("MyTestSuite");
        testcase.setGroupName("MyTestGroup");
        testcase.setIgnore(false);
        testcase.setName("blah blah - hey hey");
        testcase.setNotes("blah blah blh blah blah ghlsdf sdfh aof asodiv das vasdvpoi asdv asdvsadvp asdfg");
        testcase.setPatientBirthTime("19700130");
        testcase.setPatientGender("M");
        testcase.setPatientId("54321");
        testcase.setRuletotest("asjdfhla asdf alksd pdf ahf apahwfe qwefh3984 qenka sd87134tu3hpjahfiq asdf");
        testcase.addProperty("vaccineGroup", "HepB", TestCasePropertyType.STRING);
        testcase.addImmunityObservationResult(new Date(), "070.30", "PROOF_OF_IMMUNITY", "IS_IMMUNE");
        SubstanceAdministrationEvent hepBComponent =
                testcase.getEvaluationSubstanceAdministrationEvent("45", "20080223", "VALID", "100", "");

        testcase.addSubstanceAdministrationEvent("45", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent});


        testcase.addSubstanceAdministrationProposal("100", "", "20090223", "100", "RECOMMENDED", "DUE_NOW");
        testcase.addSubstanceAdministrationProposal("810", "", "", "810", "NOT_RECOMMENDED", "COMPLETE");
        logger.logDuration("testcase init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(testcase.getTestCase(), TestCase.class));
        logger.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File("src/main/resources/schema/testcase.xsd"));
        MarshalUtils.marshal(testcase.getTestCase(), new DefaultHandler(), schema);
        logger.logDuration("validateCdsObject", start);

        logger.info("Finished testGetTestCaseWrapper...");
        assertTrue(true);
    }
}
