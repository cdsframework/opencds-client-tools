package ice.dto;

import org.opencds.CdsOutput;
import ice.dto.support.CdsObjectAssist;
import ice.util.Utilities;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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
        output.addSubstanceAdministrationEvent("100", "08", "20080223", "VALIDITY", "VALID", "");
        output.addSubstanceAdministrationEvent("100", "08", "20090223", "VALIDITY", "INVALID", "TOO_RED");
        output.addSubstanceAdministrationEvent("200", "43", "20080223", "VALIDITY", "INVALID", "TOO_RED");
        output.addSubstanceAdministrationEvent("200", "43", "20090223", "VALIDITY", "VALID", "");
        output.addSubstanceAdministrationProposal("200", "43", "20090223", "PROPOSAL", "RECOMMENDED", "DUE_NOW");
        output.addSubstanceAdministrationProposal("100", "", "", "PROPOSAL", "NOT_RECOMMENDED", "COMPLETED");
        Utilities.logDuration("output init", start);

        start = System.nanoTime();
        logger.info(CdsObjectAssist.cdsObjectToString(output.getCdsObject(), CdsOutput.class));
        Utilities.logDuration("cdsObjectToString", start);

        start = System.nanoTime();
        CdsObjectAssist.validateCdsObject(output.getCdsObject(), "src/main/resources/jaxb/opencds/cdsOutput.xsd");
        Utilities.logDuration("validateCdsObject", start);

        logger.info("Finished testCdsOutput...");
        assertTrue(true);
    }
}
