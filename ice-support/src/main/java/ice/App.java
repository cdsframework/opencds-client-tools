package ice;

import ice.exception.IceException;
import ice.support.VmrAssist;
import org.apache.log4j.Logger;
import org.opencds.CDSInput;
import org.opencds.RelatedClinicalStatement;
import org.opencds.SubstanceAdministrationEvent;
import org.opencds.SubstanceAdministrationProposal;

public class App {
    private final static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) throws IceException {
        logger.info("Starting...");
//        File file = new File("src/main/resources/sample.xml");
//        InputStream in = new FileInputStream(file);
//        long length = file.length();
//        byte[] bytes = new byte[(int) length];
//        int offset = 0;
//        int numRead = 0;
//        while (offset < bytes.length
//                && (numRead = in.read(bytes, offset, bytes.length - offset)) >= 0) {
//            offset += numRead;
//        }
//        in.close();

        VmrAssist vmrAssist = new VmrAssist();

        CDSInput cdsInput = VmrAssist.getCDSInput();

        VmrAssist.addOrUpdatePatientGender(cdsInput, "F");

        VmrAssist.addOrUpdatePatientBirthTime(cdsInput, "19830630");

        SubstanceAdministrationEvent substanceAdministrationEvent = VmrAssist.addSubstanceAdministrationEvent(cdsInput, "45", "20110803");

        RelatedClinicalStatement relatedClinicalStatement = VmrAssist.addRelatedClinicalStatement(substanceAdministrationEvent);

        VmrAssist.addOrUpdateObservationResult(relatedClinicalStatement, "VALIDITY", "VALID", "");

        SubstanceAdministrationEvent substanceAdministrationEvent1 = VmrAssist.addSubstanceAdministrationEvent(cdsInput, "45", "20090223");

        relatedClinicalStatement = VmrAssist.addRelatedClinicalStatement(substanceAdministrationEvent1);

        VmrAssist.addOrUpdateObservationResult(relatedClinicalStatement, "VALIDITY", "INVALID", "TOO_BLUE");

        SubstanceAdministrationProposal substanceAdministrationProposal = VmrAssist.addSubstanceAdministrationProposal(cdsInput, "45", "20111130");

        relatedClinicalStatement = VmrAssist.addRelatedClinicalStatement(substanceAdministrationProposal);

        VmrAssist.addOrUpdateObservationResult(relatedClinicalStatement, "PROPOSAL", "RECOMMENDED", "DUE_NOW");

        logger.info(vmrAssist.cdsObjectToString(cdsInput));

//        CDSOutput cdsOutput = OpenCdsAssist.evaluate(cdsInput, "bounce", new Date());
//
//        System.out.println(vmrAssist.cdsObjectToString(cdsOutput));

        logger.info("Finished...");

    }
}