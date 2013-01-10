package org.cdsframework.dto;

import org.cdsframework.util.Utilities;
import org.cdsframework.util.support.cds.CdsObjectAssist;
import org.cdsframework.util.support.cds.CdsOutputWrapper;
import java.util.Date;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencds.support.CdsOutput;
import org.opencds.support.SubstanceAdministrationEvent;

import static org.junit.Assert.*;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OutputTest {

    private final static Logger logger = Logger.getLogger(OutputTest.class);

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
    public void testCdsOutput() throws Exception {
        long start;
        logger.info("Starting testCdsOutput...");

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        CdsOutputWrapper.getCdsOutputWrapper();

        start = System.nanoTime();
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        Utilities.logDuration("getCdsOutputWrapper", start);

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

        output.addSubstanceAdministrationEvent("45", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent1, hepBComponent2});

        output.addSubstanceAdministrationEvent("110", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent3});

        output.addSubstanceAdministrationProposal("100", "45", "20111201", "100", "RECOMMENDED", "DUE_NOW");

        Utilities.logDuration("output init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(output.getCdsObject(), CdsOutput.class));
        Utilities.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        CdsObjectAssist.validateCdsObject(output.getCdsObject(), "src/main/resources/jaxb/testcase/cdsOutput.xsd");
        Utilities.logDuration("validateCdsObject", start);

        logger.info("Finished testCdsOutput...");
        assertTrue(true);
    }
}
