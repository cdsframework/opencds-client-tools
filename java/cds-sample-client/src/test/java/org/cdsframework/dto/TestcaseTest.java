package org.cdsframework.dto;

import org.cdsframework.enumeration.TestCasePropertyType;
import org.cdsframework.util.Utilities;
import org.cdsframework.util.support.cds.CdsObjectAssist;
import org.cdsframework.util.support.cds.TestCaseWrapper;
import java.util.Date;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencds.support.SubstanceAdministrationEvent;
import org.opencds.support.TestCase;

import static org.junit.Assert.*;

/**
 *
 * @author HLN Consulting, LLC
 */
public class TestCaseTest {

    private final static Logger logger = Logger.getLogger(TestCaseTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTestcase() throws Exception {
        long start;
        logger.info("Starting testTestcase...");

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        TestCaseWrapper.getTestCaseWrapper();

        start = System.nanoTime();
        TestCaseWrapper testcase = TestCaseWrapper.getTestCaseWrapper();
        Utilities.logDuration("getTestCaseWrapper", start);

        start = System.nanoTime();
        testcase.setExecutiondate(new Date());
        testcase.setName("blah blah - hey hey");
        testcase.setNotes("blah blah blh blah blah ghlsdf sdfh aof asodiv das vasdvpoi asdv asdvsadvp asdfg");
        testcase.setPatientBirthTime("19700130");
        testcase.setPatientGender("M");
        testcase.setRuletotest("asjdfhla asdf alksd pdf ahf apahwfe qwefh3984 qenka sd87134tu3hpjahfiq asdf");
        testcase.addProperty("vaccineGroup", "HepB", TestCasePropertyType.STRING);
        testcase.addImmunityObservationResult(new Date(), "070.30", "PROOF_OF_IMMUNITY", "IS_IMMUNE");
        SubstanceAdministrationEvent hepBComponent =
                testcase.getEvaluationSubstanceAdministrationEvent("45", "20080223", "VALID", "100", "");

        testcase.addSubstanceAdministrationEvent("45", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent});


        testcase.addSubstanceAdministrationProposal("100", "", "20090223", "100", "RECOMMENDED", "DUE_NOW");
        testcase.addSubstanceAdministrationProposal("810", "", "", "810", "NOT_RECOMMENDED", "COMPLETE");
        Utilities.logDuration("testcase init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(testcase.getTestCase(), TestCase.class));
        Utilities.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        CdsObjectAssist.validateCdsObject(testcase.getTestCase(), "src/main/resources/jaxb/testcase/testcase.xsd");
        Utilities.logDuration("validateCdsObject", start);

        logger.info("Finished testTestcase...");
        assertTrue(true);
    }
}
