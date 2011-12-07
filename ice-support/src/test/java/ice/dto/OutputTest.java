package ice.dto;

import org.opencds.CdsOutput;
import ice.dto.support.CdsObjectAssist;
import ice.util.Utilities;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.opencds.SubstanceAdministrationEvent;

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
        List<SubstanceAdministrationEvent> components = new ArrayList<SubstanceAdministrationEvent>();
        SubstanceAdministrationEvent hepBComponent =
                output.getEvaluationSubstanceAdministrationEvent("43", "20080223", true, "VALIDITY (HEP B COMPONENT)", "VALID", "");
        SubstanceAdministrationEvent hepAComponent =
                output.getEvaluationSubstanceAdministrationEvent("84", "20080223", false, "VALIDITY (HEP A COMPONENT)", "INVALID", "TOO_GHASTLY");
        SubstanceAdministrationEvent polioComponent =
                output.getEvaluationSubstanceAdministrationEvent("10", "20080223", false, "VALIDITY (POLIO)", "INVALID", "TOO_EGGY");

        components.add(hepBComponent);
        components.add(hepAComponent);
        output.addSubstanceAdministrationEvent("104", "20080223", components);

        components = new ArrayList<SubstanceAdministrationEvent>();
        components.add(polioComponent);
        output.addSubstanceAdministrationEvent("10", "20080223", components);

        output.addSubstanceAdministrationProposal("810", "104", "20111201", "PROPOSAL (HEP A)", "RECOMMENDED", "DUE_NOW");
        output.addSubstanceAdministrationProposal("100", "104", "20111201", "PROPOSAL (HEP B)", "RECOMMENDED", "DUE_NOW");
        output.addSubstanceAdministrationProposal("400", "10", "20111201", "PROPOSAL (POLIO)", "RECOMMENDED", "DUE_NOW");

        output.addSubstanceAdministrationProposal("500", "", "", "PROPOSAL (MMR)", "NOT_RECOMMENDED", "COMPLETED");

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
