package ice.dto;

import java.io.File;
import org.opencds.CdsInput;
import org.opencds.CdsOutput;
import ice.dto.support.CdsObjectAssist;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.opencds.SubstanceAdministrationEvent;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsObjectAssistTest {

    private final static Logger logger = Logger.getLogger(CdsObjectAssistTest.class);
    private byte[] tmp;

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

    @Ignore
    @Test
    public void testCdsObjectToString() throws Exception {
        logger.info("Starting testCdsObjectToString...");
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        output.setPatientGender("F");
        output.setPatientBirthTime("19830630");
        SubstanceAdministrationEvent hepBComponent = output.getEvaluationSubstanceAdministrationEvent("08", "20080223", true, "VALIDITY", "VALID", "");
        output.addSubstanceAdministrationEvent("08", "20080223", new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal(100, "", "", "PROPOSAL", "NOT_RECOMMENDED", "COMPLETED");

        logger.info(CdsObjectAssist.cdsObjectToString(output.getCdsObject(), CdsOutput.class));
        logger.info("Finished testCdsObjectToString...");
        assertTrue(true);
    }

    @Ignore
    @Test
    public void testCdsObjectToByteArray() throws Exception {
        logger.info("Starting testCdsObjectToByteArray...");
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        output.setPatientGender("F");
        output.setPatientBirthTime("19830630");
        SubstanceAdministrationEvent hepBComponent = output.getEvaluationSubstanceAdministrationEvent("08", "20080223", true, "VALIDITY", "VALID", "");
        output.addSubstanceAdministrationEvent("08", "20080223", new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal(100, "", "", "PROPOSAL", "NOT_RECOMMENDED", "COMPLETED");

        tmp = CdsObjectAssist.cdsObjectToByteArray(output.getCdsObject(), CdsOutput.class);
        logger.info("Finished testCdsObjectToByteArray...");
        assertTrue(true);
    }

    @Ignore
    @Test
    public void testCdsObjectFromByteArray() throws Exception {
        logger.info("Starting testCdsObjectFromByteArray...");
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        output.setPatientGender("F");
        output.setPatientBirthTime("19830630");
        SubstanceAdministrationEvent hepBComponent = output.getEvaluationSubstanceAdministrationEvent("08", "20080223", true, "VALIDITY", "VALID", "");
        output.addSubstanceAdministrationEvent("08", "20080223", new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal(100, "", "", "PROPOSAL", "NOT_RECOMMENDED", "COMPLETED");

        tmp = CdsObjectAssist.cdsObjectToByteArray(output.getCdsObject(), CdsOutput.class);

        CdsOutput cdsObjectFromByteArray = CdsObjectAssist.cdsObjectFromByteArray(tmp, CdsOutput.class);

        logger.info("Gender: " + cdsObjectFromByteArray.getVmrOutput().getPatient().getDemographics().getGender().getCode());

        logger.info("Finished testCdsObjectFromByteArray...");
        assertTrue(true);
    }

    @Ignore
    @Test
    public void testCdsObjectFromFile() throws Exception {
        logger.info("Starting testCdsObjectFromFile...");

        CdsOutput cdsOutputFromFile = CdsObjectAssist.cdsObjectFromFile("src/main/resources/sampleCdsOutput.xml", CdsOutput.class);

        logger.info("Gender: " + cdsOutputFromFile.getVmrOutput().getPatient().getDemographics().getGender().getCode());

        CdsInput cdsInputFromFile = CdsObjectAssist.cdsObjectFromFile("src/main/resources/sampleCdsInput.xml", CdsInput.class);

        logger.info("Gender: " + cdsInputFromFile.getVmrInput().getPatient().getDemographics().getGender().getCode());

        logger.info("Finished testCdsObjectFromFile...");
        assertTrue(true);
    }

    @Ignore
    @Test
    public void testCdsObjectToFile() throws Exception {
        logger.info("Starting testCdsObjectToFile...");
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        output.setPatientGender("F");
        output.setPatientBirthTime("19830630");
        SubstanceAdministrationEvent hepBComponent = output.getEvaluationSubstanceAdministrationEvent("08", "20080223", true, "VALIDITY", "VALID", "");
        output.addSubstanceAdministrationEvent("08", "20080223", new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal(100, "", "", "PROPOSAL", "NOT_RECOMMENDED", "COMPLETED");

        String filename = CdsObjectAssist.cdsObjectToFile(output.getCdsObject(), null, "sampleCdsOutput.xml");
        File file = new File(filename);
        file.delete();

        logger.info(filename);

        logger.info("Finished testCdsObjectToFile...");
        assertTrue(true);
    }
}
