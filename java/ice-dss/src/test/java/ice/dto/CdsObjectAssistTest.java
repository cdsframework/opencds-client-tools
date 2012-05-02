package ice.dto;

import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import org.opencds.CdsInput;
import org.opencds.CdsOutput;
import ice.dto.support.CdsObjectAssist;
import ice.enumeration.EvaluationValidityType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.opencds.CD;
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
        SubstanceAdministrationEvent hepBComponent = CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("08", "20080223", EvaluationValidityType.VALID, "100", "");
        output.addSubstanceAdministrationEvent("08", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal("100", "", "", "100", "NOT_RECOMMENDED", "COMPLETE");

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
        SubstanceAdministrationEvent hepBComponent = CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("08", "20080223", EvaluationValidityType.VALID, "100", "");
        output.addSubstanceAdministrationEvent("08", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal("100", "", "", "100", "NOT_RECOMMENDED", "COMPLETE");

        tmp = CdsObjectAssist.cdsObjectToByteArray(output.getCdsObject(), CdsOutput.class);
        logger.info("Finished testCdsObjectToByteArray...");
        assertTrue(true);
    }

    @Test
    public void testCdsObjectFromByteArray() throws Exception {
        logger.info("Starting testCdsObjectFromByteArray...");
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        output.setPatientGender("F");
        output.setPatientBirthTime("19830630");
        SubstanceAdministrationEvent hepBComponent = CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("08", "20080223", EvaluationValidityType.VALID, "100", "");
        output.addSubstanceAdministrationEvent("08", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal("100", "", "", "100", "NOT_RECOMMENDED", "COMPLETE");

        tmp = CdsObjectAssist.cdsObjectToByteArray(output.getCdsObject(), CdsOutput.class);

        CdsOutput cdsObjectFromByteArray = CdsObjectAssist.cdsObjectFromByteArray(tmp, CdsOutput.class);

        logger.info("Gender: " + cdsObjectFromByteArray.getVmrOutput().getPatient().getDemographics().getGender().getCode());

        logger.info("Finished testCdsObjectFromByteArray...");
        assertTrue(true);
    }

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

    @Test
    public void testCdsObjectToFile() throws Exception {
        logger.info("Starting testCdsObjectToFile...");
        CdsOutputWrapper output = CdsOutputWrapper.getCdsOutputWrapper();
        output.setPatientGender("F");
        output.setPatientBirthTime("19830630");
        SubstanceAdministrationEvent hepBComponent = CdsOutputWrapper.getEvaluationSubstanceAdministrationEvent("08", "20080223", EvaluationValidityType.VALID, "100", "");
        output.addSubstanceAdministrationEvent("08", "20080223", null, new SubstanceAdministrationEvent[]{hepBComponent});
        output.addSubstanceAdministrationProposal("100", "", "", "100", "NOT_RECOMMENDED", "COMPLETE");

        String filename = CdsObjectAssist.cdsObjectToFile(output.getCdsObject(), null, "sampleCdsOutput.xml");
        File file = new File(filename);
        file.delete();

        logger.info(filename);

        logger.info("Finished testCdsObjectToFile...");
        assertTrue(true);
    }

    @Test
    public void testGetCdsConcepts() throws Exception {
        logger.info("Starting testGetCdsConcepts...");
        Map<String, List<String>> codeSets = new HashMap<String, List<String>>();
        List<CdsConceptWrapper> cdsConcepts = CdsObjectAssist.getCdsConcepts();
        for (CdsConceptWrapper item : cdsConcepts) {
            List<String> codeSetMembers = codeSets.get(item.getCdsConceptMap().getMembersForCodeSystem().getCodeSystem());
            if (codeSetMembers == null) {
                codeSetMembers = new ArrayList<String>();
            }
            for (CD cd : item.getCdsConceptMap().getMembersForCodeSystem().getCDS()) {
                codeSetMembers.add(cd.getCode());
                logger.info(item.getCdsConceptMap().getMembersForCodeSystem().getCodeSystem() + " - " + cd.getCode());
            }
            assertNotNull(item.getCdsConceptMap().getSpecificationNotes());
        }
        logger.info("Finished testGetCdsConcepts...");
    }

    @Test
    public void testCodeSets() throws Exception {
        logger.info("Starting testCodeSets...");
        Map<String, Map<String, String>> codeSets = CdsObjectAssist.getCodeSets();
        for (Entry<String, Map<String, String>> item : codeSets.entrySet()) {
            logger.info("item.getKey(): " + item.getKey());
        }
        logger.info("Finished testCodeSets...");
    }
}
