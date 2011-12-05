package ice.service;

import ice.dto.CdsInputWrapper;
import ice.dto.support.CdsObjectAssist;
import ice.util.Utilities;
import java.util.Date;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencds.CdsInput;
import org.opencds.CdsOutput;

/**
 *
 * @author HLN Consulting, LLC
 */
public class OpenCdsAssistTest {

    private final static Logger logger = Logger.getLogger(OpenCdsAssistTest.class);

    public OpenCdsAssistTest() {
    }

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

    /**
     * Test of evaluate method, of class OpenCdsAssist.
     * @throws Exception
     */
    @Test
    public void testEvaluate() throws Exception {
        long start;
        logger.info("Starting testEvaluate...");

        // preform first to time get the static initialization of CdsObjectAssist out of the way
        CdsInputWrapper.getCdsInputWrapper();

        start = System.nanoTime();
        CdsInputWrapper input = CdsInputWrapper.getCdsInputWrapper();
        Utilities.logDuration("getCdsInputWrapper", start);

        start = System.nanoTime();
        input.setPatientGender("F");
        input.setPatientBirthTime("19830630");
        input.addSubstanceAdministrationEvent("100", "08", "20080223");
        input.addSubstanceAdministrationEvent("100", "08", "20090223");
        input.addSubstanceAdministrationEvent("200", "43", "20080223");
        input.addSubstanceAdministrationEvent("200", "43", "20090223");
        Utilities.logDuration("input init", start);

        String businessId = "bounce";
        Date executionDate = new Date();

        start = System.nanoTime();
        byte[] cdsObjectToByteArray = CdsObjectAssist.cdsObjectToByteArray(input.getCdsObject(), CdsInput.class);
        Utilities.logDuration("cdsObjectToByteArray", start);

        start = System.nanoTime();
        byte[] evaluation = OpenCdsAssist.evaluate(cdsObjectToByteArray, businessId, executionDate);
        Utilities.logDuration("evaluate", start);

        start = System.nanoTime();
        CdsOutput result = CdsObjectAssist.cdsObjectFromByteArray(evaluation, CdsOutput.class);
        Utilities.logDuration("cdsObjectFromByteArray", start);

        logger.info("Finished testEvaluate...");
        assertTrue(result instanceof CdsOutput);
    }
}
