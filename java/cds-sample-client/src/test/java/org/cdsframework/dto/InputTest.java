package org.cdsframework.dto;

import java.util.Date;
import org.cdsframework.util.Utilities;
import org.cdsframework.util.support.cds.CdsInputWrapper;
import org.cdsframework.util.support.cds.CdsObjectAssist;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencds.support.CdsInput;

import static org.junit.Assert.*;

/**
 *
 * @author HLN Consulting, LLC
 */
public class InputTest {

    private final static Logger logger = Logger.getLogger(InputTest.class);

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
    public void testCdsInput() throws Exception {
        long start;
        logger.info("Starting testCdsInput...");


        // preform first to time get the static initialization of CdsObjectAssist out of the way
        CdsInputWrapper.getCdsInputWrapper();

        start = System.nanoTime();
        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        Utilities.logDuration("getInput", start);

        start = System.nanoTime();
        input.setPatientGender("F");
        input.setPatientBirthTime("19830630");
        input.addSubstanceAdministrationEvent("45", "20080223", null);
        input.addSubstanceAdministrationEvent("08", "20080223", null);
        input.addImmunityObservationResult(new Date(), "070.30", "DISEASE_DOCUMENTED", "IS_IMMUNE");
        Utilities.logDuration("input init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(input.getCdsObject(), CdsInput.class));
        Utilities.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        CdsObjectAssist.validateCdsObject(input.getCdsObject(), "src/main/resources/jaxb/testcase/cdsInput.xsd");
        Utilities.logDuration("validateCdsObject", start);

        logger.info("Finished testCdsInput...");
        assertTrue(true);
    }
}
