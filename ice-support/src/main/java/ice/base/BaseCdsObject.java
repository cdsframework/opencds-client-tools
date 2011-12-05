package ice.base;

import ice.dto.support.CdsObjectAssist;
import ice.exception.IceException;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.opencds.AdministrableSubstance;
import org.opencds.CD;
import org.opencds.CdsInput;
import org.opencds.CdsOutput;
import org.opencds.EvaluatedPerson.Demographics;
import org.opencds.II;
import org.opencds.ObservationResult;
import org.opencds.ObservationResult.ObservationValue;
import org.opencds.RelatedClinicalStatement;
import org.opencds.SubstanceAdministrationEvent;
import org.opencds.SubstanceAdministrationProposal;
import org.opencds.TS;
import org.opencds.Vmr;

/**
 *
 * @param <T>
 * @author HLN Consulting, LLC
 */
public abstract class BaseCdsObject<T> {

    protected final Logger logger;
    private Class<T> cdsObjectClass;
    private T cdsObject;

    protected BaseCdsObject(Class loggerClass, Class<T> cdsObjectClass) {
        logger = Logger.getLogger(loggerClass);
        this.cdsObjectClass = cdsObjectClass;
        initializeCdsObject();
    }

    private void initializeCdsObject() {
        final String METHODNAME = "initializeCdsObject ";
        if (cdsObjectClass == CdsInput.class) {
            cdsObject = (T) CdsObjectAssist.getCdsInput();
        } else {
            cdsObject = (T) CdsObjectAssist.getCdsOutput();
        }
    }

    public T getCdsObject() {
        return cdsObject;
    }

    public Class<T> getCdsObjectClass() {
        return cdsObjectClass;
    }

    protected static ObservationResult getObservationResult() {
        final String METHODNAME = "getObservationResult ";
        ObservationResult observationResult = new ObservationResult();
        II ii = new II();
        ii.setRoot("2.16.840.1.113883.10.20.1.31");
        observationResult.getTemplateIds().add(ii);
        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        observationResult.setId(ii);
        return observationResult;
    }

    protected static AdministrableSubstance getAdministrableSubstance() {
        final String METHODNAME = "getAdministrableSubstance ";
        II ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        CD cd = new CD();
        cd.setCodeSystem("2.16.840.1.113883.12.292");
        AdministrableSubstance administerableSubstance = new AdministrableSubstance();
        administerableSubstance.setId(ii);
        administerableSubstance.setSubstanceCode(cd);
        return administerableSubstance;
    }

    protected static SubstanceAdministrationEvent getSubstanceAdministrationEvent() {
        final String METHODNAME = "getSubstanceAdministrationProposal ";
        SubstanceAdministrationEvent substanceAdministrationEvent = new SubstanceAdministrationEvent();
        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795");
        substanceAdministrationEvent.getTemplateIds().add(ii);
        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        substanceAdministrationEvent.setId(ii);
        CD cd = new CD();
        cd.setCode("384810002");
        cd.setCodeSystem("2.16.840.1.113883.6.5");
        cd.setCodeSystemName("SNOMED CT");
        cd.setDisplayName("Immunization/vaccination management (procedure)");
        substanceAdministrationEvent.setSubstanceAdministrationGeneralPurpose(cd);
        return substanceAdministrationEvent;
    }

    protected static SubstanceAdministrationProposal getSubstanceAdministrationProposal() {
        final String METHODNAME = "getSubstanceAdministrationProposal ";
        SubstanceAdministrationProposal substanceAdministrationProposal = new SubstanceAdministrationProposal();
        II ii = new II();
        ii.setRoot("2.16.840.1.113883.3.795");
        substanceAdministrationProposal.getTemplateIds().add(ii);
        ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        substanceAdministrationProposal.setId(ii);
        CD cd = new CD();
        cd.setCode("384810002");
        cd.setCodeSystem("2.16.840.1.113883.6.5");
        cd.setCodeSystemName("SNOMED CT");
        cd.setDisplayName("Immunization/vaccination management (procedure)");
        substanceAdministrationProposal.setSubstanceAdministrationGeneralPurpose(cd);
        return substanceAdministrationProposal;
    }

    protected static RelatedClinicalStatement getRelatedClinicalStatement(String code) {
        final String METHODNAME = "getRelatedClinicalStatement ";
        RelatedClinicalStatement relatedClinicalStatement = new RelatedClinicalStatement();
        CD cd = new CD();
        cd.setCodeSystem("2.16.840.1.113883.5.111");
        cd.setCode(code);
        relatedClinicalStatement.setTargetRelationshipToSource(cd);
        return relatedClinicalStatement;
    }

    protected static <S> S addObservationResult(S substanceAdministrationObject, String focus, String value, String interpretation)
            throws IceException {
        RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement("IND");
        if (substanceAdministrationObject instanceof SubstanceAdministrationEvent) {
            ((SubstanceAdministrationEvent)substanceAdministrationObject).getRelatedClinicalStatements().add(relatedClinicalStatement);
        } else if (substanceAdministrationObject instanceof SubstanceAdministrationProposal) {
            ((SubstanceAdministrationProposal)substanceAdministrationObject).getRelatedClinicalStatements().add(relatedClinicalStatement);
        } else {
            throw new IceException("Unexpected substance administration class: " + substanceAdministrationObject.getClass().getSimpleName());
        }
        addObservationResult(relatedClinicalStatement, focus, value, interpretation);
        return substanceAdministrationObject;
    }

    private Demographics getDemographics() throws IceException {
        Vmr vmr;
        if (cdsObject instanceof CdsInput) {
            vmr = ((CdsInput) cdsObject).getVmrInput();
        } else if (cdsObject instanceof CdsOutput) {
            vmr = ((CdsOutput) cdsObject).getVmrOutput();
        } else {
            throw new IceException("Unexpected class type: " + cdsObject.getClass().getSimpleName());
        }
        return vmr.getPatient().getDemographics();
    }

    private static void addObservationResult(RelatedClinicalStatement relatedClinicalStatement, String focus, String value, String interpretation)
            throws IceException {
        ObservationResult observationResult = getObservationResult();
        relatedClinicalStatement.setObservationResult(observationResult);

        CD cd = new CD();
        cd.setCode(interpretation);
        cd.setDisplayName("TBD");
        cd.setCodeSystem("2.16.840.1.113883.5.83");
        observationResult.getInterpretations().add(cd);

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

    }

    public void setPatientBirthTime(String birthTime) throws IceException {
        final String METHODNAME = "addOrUpdateBirthTime ";
        TS ts = new TS();
        ts.setValue(birthTime);
        Demographics demographics = getDemographics();
        demographics.setBirthTime(ts);
    }

    public void setPatientGender(String gender) throws IceException {
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
        cd.setOriginalText(gender);

        Demographics demographics = getDemographics();
        demographics.setGender(cd);
    }

    protected abstract SubstanceAdministrationEvent addSubstanceAdministrationEvent(String vaccineGroup, String substanceCode, String administrationTimeInterval) throws IceException;
    protected abstract SubstanceAdministrationProposal addSubstanceAdministrationProposal(String vaccineGroup, String substanceCode, String administrationTimeInterval) throws IceException;

}
