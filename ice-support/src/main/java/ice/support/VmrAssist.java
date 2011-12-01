package ice.support;

import ice.exception.IceException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import org.opencds.AdministrableSubstance;
import org.opencds.CD;
import org.opencds.CDSContext;
import org.opencds.CDSInput;
import org.opencds.CDSOutput;
import org.opencds.EvaluatedPerson;
import org.opencds.EvaluatedPerson.ClinicalStatements;
import org.opencds.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationEvents;
import org.opencds.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationProposals;
import org.opencds.EvaluatedPerson.Demographics;
import org.opencds.II;
import org.opencds.IVLTS;
import org.opencds.ObservationResult;
import org.opencds.ObservationResult.ObservationValue;
import org.opencds.RelatedClinicalStatement;
import org.opencds.SubstanceAdministrationEvent;
import org.opencds.SubstanceAdministrationProposal;
import org.opencds.TS;
import org.opencds.VMR;

/**
 *
 * @author HLN Consulting, LLC
 */
public class VmrAssist {

    private final static Logger logger = Logger.getLogger(VmrAssist.class);
    private final JAXBContext jaxbContext;
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public VmrAssist() {
        try {
            jaxbContext = JAXBContext.newInstance("org.opencds");
            marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            unmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            throw new Error(e.getMessage());
        }
    }

    public static ObservationResult getObservationResult() {
        ObservationResult observationResult = new ObservationResult();

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.10.20.1.31");
        observationResult.getTemplateId().add(ii);

        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        observationResult.setId(ii);

        return observationResult;
    }

    public static RelatedClinicalStatement getRelatedClinicalStatement() {
        RelatedClinicalStatement relatedClinicalStatement = new RelatedClinicalStatement();

        CD cd = new CD();
        cd.setCodeSystem("2.16.840.1.113883.5.1002");
        cd.setCode("COMP");
        relatedClinicalStatement.setTargetRelationshipToSource(cd);

        relatedClinicalStatement.setObservationResult(getObservationResult());
        return relatedClinicalStatement;
    }

    public static AdministrableSubstance getAdministrableSubstance() {

        II ii = new II();
        ii.setRoot(UUID.randomUUID().toString());

        CD cd = new CD();
        cd.setCodeSystem("2.16.840.1.113883.12.292");

        AdministrableSubstance administerableSubstance = new AdministrableSubstance();
        administerableSubstance.setId(ii);
        administerableSubstance.setSubstanceCode(cd);

        return administerableSubstance;
    }

    public static SubstanceAdministrationEvent getSubstanceAdministrationEvent() {
        SubstanceAdministrationEvent substanceAdministrationEvent = new SubstanceAdministrationEvent();

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795");
        substanceAdministrationEvent.getTemplateId().add(ii);

        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        substanceAdministrationEvent.setId(ii);

        CD cd = new CD();
        cd.setCode("384810002");
        cd.setCodeSystem("2.16.840.1.113883.6.5");
        cd.setCodeSystemName("SNOMED CT");
        cd.setDisplayName("Immunization/vaccination management (procedure)");
        substanceAdministrationEvent.setSubstanceAdministrationGeneralPurpose(cd);

        substanceAdministrationEvent.setSubstance(getAdministrableSubstance());

        return substanceAdministrationEvent;
    }

    public static SubstanceAdministrationEvents getSubstanceAdministrationEvents() {
        SubstanceAdministrationEvents substanceAdministrationEvents = new SubstanceAdministrationEvents();
        return substanceAdministrationEvents;
    }

    public static SubstanceAdministrationProposal getSubstanceAdministrationProposal() {
        SubstanceAdministrationProposal substanceAdministrationProposal = new SubstanceAdministrationProposal();

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795");
        substanceAdministrationProposal.getTemplateId().add(ii);

        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        substanceAdministrationProposal.setId(ii);

        CD cd = new CD();
        cd.setCode("384810002");
        cd.setCodeSystem("2.16.840.1.113883.6.5");
        cd.setCodeSystemName("SNOMED CT");
        cd.setDisplayName("Immunization/vaccination management (procedure)");
        substanceAdministrationProposal.setSubstanceAdministrationGeneralPurpose(cd);

        substanceAdministrationProposal.setSubstance(getAdministrableSubstance());

        return substanceAdministrationProposal;
    }

    public static SubstanceAdministrationProposals getSubstanceAdministrationProposals() {
        SubstanceAdministrationProposals substanceAdministrationProposals = new SubstanceAdministrationProposals();
        return substanceAdministrationProposals;
    }

    public static ClinicalStatements getClinicalStatements() {
        ClinicalStatements clinicalStatements = new ClinicalStatements();
        clinicalStatements.setSubstanceAdministrationEvents(getSubstanceAdministrationEvents());
        clinicalStatements.setSubstanceAdministrationProposals(getSubstanceAdministrationProposals());
        return clinicalStatements;
    }

    public static Demographics getDemographics() {
        Demographics demographics = new Demographics();
        return demographics;
    }

    public static EvaluatedPerson getEvaluatedPerson() {
        EvaluatedPerson evaluatedPerson = new EvaluatedPerson();

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795.11.2.1");
        evaluatedPerson.getTemplateId().add(ii);

        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        evaluatedPerson.setId(ii);
//
//        CD cd = new CD();
//        cd.setCode("PRSN");
//        cd.setCodeSystem("2.16.840.1.113883.5");
//        cd.setDisplayName("person");
//        evaluatedPerson.setEntityType(cd);

        evaluatedPerson.setDemographics(getDemographics());

        evaluatedPerson.setClinicalStatements(getClinicalStatements());

        return evaluatedPerson;
    }

    public static VMR getVMR() {
        VMR vmr = new VMR();

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795.11.1.1");
        vmr.getTemplateId().add(ii);

        vmr.setPatient(getEvaluatedPerson());

        return vmr;
    }

    public static CDSContext getCDSContext() {
        CD cd = new CD();
        cd.setCode("en");
        cd.setCodeSystem("1.2.3");
        cd.setDisplayName("English");

        CDSContext cdsContext = new CDSContext();
        cdsContext.setCdsSystemUserPreferredLanguage(cd);
        return cdsContext;
    }

    public static VMR getVMR(Object cdsObject) throws IceException {
        if (cdsObject == null) {
            throw new IceException("cdsObject was null!");
        }
        VMR vmr;
        if (cdsObject instanceof CDSInput) {
            vmr = ((CDSInput) cdsObject).getVmrInput();
        } else if (cdsObject instanceof CDSOutput) {
            vmr = ((CDSOutput) cdsObject).getVmrOutput();
        } else {
            throw new IceException("Unexpected class type: " + cdsObject.getClass().getSimpleName());
        }
        if (vmr == null) {
            vmr = getVMR();
            if (cdsObject instanceof CDSInput) {
                ((CDSInput) cdsObject).setVmrInput(vmr);
            } else {
                ((CDSOutput) cdsObject).setVmrOutput(vmr);
            }
        }
        return vmr;
    }

    public static EvaluatedPerson getEvaluatedPerson(Object cdsObject) throws IceException {
        VMR vmr = getVMR(cdsObject);
        EvaluatedPerson patient = vmr.getPatient();
        if (patient == null) {
            patient =  getEvaluatedPerson();
            vmr.setPatient(patient);
        }
        return patient;
    }

    public static Demographics getDemographics(Object cdsObject) throws IceException {
        EvaluatedPerson evaluatedPerson = getEvaluatedPerson(cdsObject);
        Demographics demographics = evaluatedPerson.getDemographics();
        if (demographics == null) {
            demographics =  getDemographics();
            evaluatedPerson.setDemographics(demographics);
        }
        return demographics;
    }

    public static ClinicalStatements getClinicalStatements(Object cdsObject) throws IceException {
        EvaluatedPerson evaluatedPerson = getEvaluatedPerson(cdsObject);
        ClinicalStatements clinicalStatements = evaluatedPerson.getClinicalStatements();
        if (clinicalStatements == null) {
            clinicalStatements =  getClinicalStatements();
            evaluatedPerson.setClinicalStatements(clinicalStatements);
        }
        return clinicalStatements;
    }

    public static SubstanceAdministrationEvents getSubstanceAdministrationEvents(Object cdsObject) throws IceException {
        ClinicalStatements clinicalStatements = getClinicalStatements(cdsObject);
        SubstanceAdministrationEvents substanceAdministrationEvents = clinicalStatements.getSubstanceAdministrationEvents();
        if (substanceAdministrationEvents == null) {
            substanceAdministrationEvents =  getSubstanceAdministrationEvents();
            clinicalStatements.setSubstanceAdministrationEvents(substanceAdministrationEvents);
        }
        return substanceAdministrationEvents;
    }

    public static SubstanceAdministrationProposals getSubstanceAdministrationProposals(Object cdsObject) throws IceException {
        ClinicalStatements clinicalStatements = getClinicalStatements(cdsObject);
        SubstanceAdministrationProposals substanceAdministrationProposals = clinicalStatements.getSubstanceAdministrationProposals();
        if (substanceAdministrationProposals == null) {
            substanceAdministrationProposals =  getSubstanceAdministrationProposals();
            clinicalStatements.setSubstanceAdministrationProposals(substanceAdministrationProposals);
        }
        return substanceAdministrationProposals;
    }

    public static void addOrUpdatePatientBirthTime(Object cdsObject, String birthTime) throws IceException {
        final String METHODNAME = "addOrUpdateBirthTime ";
        TS ts = new TS();
        ts.setValue(birthTime);

        Demographics demographics = getDemographics(cdsObject);
        demographics.setBirthTime(ts);
    }

    public static void addOrUpdatePatientGender(Object cdsObject, String gender) throws IceException {
        final String METHODNAME = "addOrUpdateGender ";
        String genderCode;
        String displayName;
        CD cd = new CD();
        if ("F".equalsIgnoreCase(gender) || "FEMALE".equalsIgnoreCase(gender)) {
            genderCode = "F";
            displayName = "Female";
        } else if ("M".equalsIgnoreCase(gender) || "MALE".equalsIgnoreCase(gender)) {
            genderCode = "M";
            displayName = "Male";
        } else {
            genderCode = "UN";
            displayName = "Undifferentiated";
        }
        cd.setCode(genderCode);
        cd.setCodeSystem("2.16.840.1.113883.1.11.1");
        cd.setDisplayName(displayName);

        Demographics demographics = getDemographics(cdsObject);
        demographics.setGender(cd);
    }

    public static SubstanceAdministrationEvent addSubstanceAdministrationEvent(Object cdsObject, String substanceCode, String administrationTimeInterval)
            throws IceException {
        SubstanceAdministrationEvent substanceAdministrationEvent = getSubstanceAdministrationEvent();

        AdministrableSubstance substance = substanceAdministrationEvent.getSubstance();
        substance.getSubstanceCode().setCode(substanceCode);
        substance.getSubstanceCode().setDisplayName("TBD");

        IVLTS ivlts = new IVLTS();
        ivlts.setHigh(administrationTimeInterval);
        ivlts.setLow(administrationTimeInterval);

        substanceAdministrationEvent.setAdministrationTimeInterval(ivlts);

        SubstanceAdministrationEvents substanceAdministrationEvents = getSubstanceAdministrationEvents(cdsObject);
        substanceAdministrationEvents.getSubstanceAdministrationEvent().add(substanceAdministrationEvent);
        return substanceAdministrationEvent;
    }

    public static SubstanceAdministrationProposal addSubstanceAdministrationProposal(Object cdsObject, String substanceCode, String administrationTimeInterval)
            throws IceException {
        SubstanceAdministrationProposal substanceAdministrationProposal = getSubstanceAdministrationProposal();

        AdministrableSubstance substance = substanceAdministrationProposal.getSubstance();
        substance.getSubstanceCode().setCode(substanceCode);
        substance.getSubstanceCode().setDisplayName("TBD");

        IVLTS ivlts = new IVLTS();
        ivlts.setHigh(administrationTimeInterval);
        ivlts.setLow(administrationTimeInterval);

        substanceAdministrationProposal.setProposedAdministrationTimeInterval(ivlts);

        SubstanceAdministrationProposals substanceAdministrationProposals = getSubstanceAdministrationProposals(cdsObject);
        substanceAdministrationProposals.getSubstanceAdministrationProposal().add(substanceAdministrationProposal);
        return substanceAdministrationProposal;
    }

    public static RelatedClinicalStatement addRelatedClinicalStatement(Object substanceAdministrationObject) throws IceException {
        List<RelatedClinicalStatement> relatedClinicalStatements;
        RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement();
        if (substanceAdministrationObject instanceof SubstanceAdministrationProposal) {
            SubstanceAdministrationProposal substanceAdministrationProposal = (SubstanceAdministrationProposal) substanceAdministrationObject;
            relatedClinicalStatements = substanceAdministrationProposal.getRelatedClinicalStatement();
        } else if (substanceAdministrationObject instanceof SubstanceAdministrationEvent) {
            SubstanceAdministrationEvent substanceAdministrationEvent = (SubstanceAdministrationEvent) substanceAdministrationObject;
            relatedClinicalStatements = substanceAdministrationEvent.getRelatedClinicalStatement();
        } else {
            throw new IceException("Unexpected class type: " + substanceAdministrationObject.getClass().getSimpleName());
        }
        relatedClinicalStatements.add(relatedClinicalStatement);

        return relatedClinicalStatement;
    }

    public static ObservationResult addOrUpdateObservationResult(RelatedClinicalStatement relatedClinicalStatement, String focus, String value, String interpretation)
            throws IceException {
        ObservationResult observationResult = relatedClinicalStatement.getObservationResult();

        CD cd = new CD();
        cd.setCode(interpretation);
        cd.setDisplayName("TBD");
        cd.setCodeSystem("2.16.840.1.113883.5.83");
        observationResult.getInterpretation().add(cd);

        ObservationValue observationValue = new ObservationValue();
        cd = new CD();
        cd.setCode(value);
        cd.setCodeSystem("TBD");
        cd.setDisplayName("TBD");
        observationValue.setConcept(cd);
        observationResult.setObservationValue(observationValue);

        cd = new CD();
        cd.setCode(focus);
        observationResult.setObservationFocus(cd);

        return observationResult;
    }

    public static CDSInput getCDSInput() throws IceException {
        CDSInput cdsInput = new CDSInput();

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795.11.1.1");
        cdsInput.getTemplateId().add(ii);

        cdsInput.setCdsContext(getCDSContext());
        cdsInput.setVmrInput(getVMR());

        return cdsInput;
    }

    public static CDSOutput getCDSOutput() throws IceException {
        CDSOutput cdsOutput = new CDSOutput();
        VMR vmrOutput = getVMR();
        cdsOutput.setVmrOutput(vmrOutput);

        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795.11.1.1");
        vmrOutput.getTemplateId().add(ii);

        EvaluatedPerson patient = vmrOutput.getPatient();
        ClinicalStatements clinicalStatements = patient.getClinicalStatements();

        return cdsOutput;
    }

    public <S> byte[] cdsObjectToByteArray(S cdsObject) throws IceException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Class<? extends Object> returnClass = cdsObject.getClass();
        String className;
        if (cdsObject instanceof CDSInput) {
            className = "cdsInput";
        } else if (cdsObject instanceof CDSOutput) {
            className = "cdsOutput";
        } else {
            throw new UnsupportedOperationException("Unexpected object type: " + returnClass.getSimpleName());
        }
        try {
            QName qName = new QName("org.opencds.vmr.v1_0.schema." + className.toLowerCase(), className);
            JAXBElement jAXBElement = new JAXBElement(qName, returnClass, cdsObject);
            marshaller.marshal(jAXBElement, stream);
        } catch (JAXBException e) {
            throw new IceException(e.getMessage());
        }
        return stream.toByteArray();
    }

    public <S> String cdsObjectToString(S cdsInput) throws IceException {
        return new String(cdsObjectToByteArray(cdsInput));
    }

    public CDSInput cdsInputFromFile(String filename) throws IceException {
        File file = new File(filename);
        CDSInput cdsInput = new CDSInput();
        try {
            JAXBElement<CDSInput> jAXBElement = (JAXBElement<CDSInput>) unmarshaller.unmarshal(file);
            cdsInput = jAXBElement.getValue();
        } catch (JAXBException e) {
            throw new IceException(e.getMessage());
        }
        return cdsInput;
    }

    public CDSOutput cdsOutputFromByteArray(byte[] bytes) throws IceException {
        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        CDSOutput cdsOutput = new CDSOutput();
        try {
            JAXBElement<CDSOutput> jAXBElement = (JAXBElement<CDSOutput>) unmarshaller.unmarshal(input);
            cdsOutput = jAXBElement.getValue();
        } catch (JAXBException e) {
            throw new IceException(e.getMessage());
        }
        return cdsOutput;
    }
}
