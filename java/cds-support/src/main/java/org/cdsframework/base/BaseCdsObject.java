package org.cdsframework.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.Constants;
import org.cdsframework.util.DateUtils;
import org.cdsframework.util.LogUtils;
import org.opencds.support.AdministrableSubstance;
import org.opencds.support.BL;
import org.opencds.support.CD;
import org.opencds.support.CDSContext;
import org.opencds.support.CdsInput;
import org.opencds.support.CdsOutput;
import org.opencds.support.EvaluatedPerson;
import org.opencds.support.EvaluatedPerson.ClinicalStatements;
import org.opencds.support.EvaluatedPerson.ClinicalStatements.ObservationResults;
import org.opencds.support.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationEvents;
import org.opencds.support.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationProposals;
import org.opencds.support.EvaluatedPerson.Demographics;
import org.opencds.support.II;
import org.opencds.support.IVLTS;
import org.opencds.support.ObservationResult;
import org.opencds.support.ObservationResult.ObservationValue;
import org.opencds.support.RelatedClinicalStatement;
import org.opencds.support.SubstanceAdministrationEvent;
import org.opencds.support.SubstanceAdministrationProposal;
import org.opencds.support.TS;
import org.opencds.support.Vmr;

/**
 *
 * @param <T>
 * @author HLN Consulting, LLC
 */
public abstract class BaseCdsObject<T> {

    protected static LogUtils logger = LogUtils.getLogger(BaseCdsObject.class);
    private Class<T> cdsObjectClass;
    private T cdsObject;

    protected BaseCdsObject(Class loggerClass, Class<T> cdsObjectClass) {
        logger = LogUtils.getLogger(loggerClass);
        this.cdsObjectClass = cdsObjectClass;
        if (cdsObjectClass == CdsInput.class) {
            cdsObject = (T) getCdsInput();
            logger.debug("Set cdsObject instance via getCdsInput: " + cdsObject);
        } else {
            cdsObject = (T) getCdsOutput();
            logger.debug("Set cdsObject instance via getCdsOutput: " + cdsObject);
        }
    }

    protected BaseCdsObject(Class loggerClass, Class<T> cdsObjectClass, T newInstance) {
        logger = LogUtils.getLogger(loggerClass);
        this.cdsObjectClass = cdsObjectClass;
        cdsObject = newInstance;
        logger.debug("Set cdsObject instance via newInstance: " + newInstance);
    }

    public T getCdsObject() {
        return cdsObject;
    }

    public Class<T> getCdsObjectClass() {
        return cdsObjectClass;
    }

    private static CD getCD(String code, String codeSystem, String displayName, String originalText) {
        CD cd = new CD();
        if (code != null) {
            cd.setCode(code);
        }
        if (codeSystem != null) {
            cd.setCodeSystem(codeSystem);
        }
        if (displayName != null) {
            cd.setDisplayName(displayName);
        }
        if (originalText != null) {
            cd.setOriginalText(originalText);
        }
        return cd;
    }

    private static CD getCD(String code, String codeSystem, String displayName) {
        return getCD(code, codeSystem, displayName, null);
    }

    private static CD getCD(String code, String codeSystem) {
        return getCD(code, codeSystem, null, null);
    }

    private static CD getCD(String codeSystem) {
        return getCD(null, codeSystem, null, null);
    }

    private static II getII(String rootOid) {
        II ii = new II();
        ii.setRoot(rootOid);
        return ii;
    }

    private static II getII() {
        II ii = new II();
        ii.setRoot(UUID.randomUUID().toString());
        return ii;
    }

    protected static BL getBL(boolean value) {
        BL bl = new BL();
        bl.setValue(value);
        return bl;
    }

    private static CdsInput getCdsInput() {
        CdsInput cdsInput = new CdsInput();
        cdsInput.getTemplateIds().add(getII(Constants.getCodeSystemOid("CDS_INPUT_ROOT")));
        cdsInput.setCdsContext(getCDSContext());
        cdsInput.setVmrInput(getVmr());
        return cdsInput;
    }

    private static CdsOutput getCdsOutput() {
        CdsOutput cdsOutput = new CdsOutput();
        cdsOutput.setVmrOutput(getVmr());
        return cdsOutput;
    }

    private static CDSContext getCDSContext() {
        CDSContext cdsContext = new CDSContext();
        cdsContext.setCdsSystemUserPreferredLanguage(
                getCD(Constants.getDefaultLanguageCode(),
                Constants.getDefaultLanguageOid(),
                Constants.getDefaultLanguageDisplayName()));
        return cdsContext;
    }

    private static Vmr getVmr() {
        Vmr vmr = new Vmr();
        vmr.getTemplateIds().add(getII(Constants.getCodeSystemOid("VMR_ROOT")));
        vmr.setPatient(getEvaluatedPerson());
        return vmr;
    }

    private static EvaluatedPerson getEvaluatedPerson() {
        EvaluatedPerson evaluatedPerson = new EvaluatedPerson();
        evaluatedPerson.getTemplateIds().add(getII(Constants.getCodeSystemOid("EVALUATED_PERSON_ROOT")));
        evaluatedPerson.setId(getII());
        evaluatedPerson.setDemographics(getDemographics());
        ClinicalStatements clinicalStatements = getClinicalStatements();
        evaluatedPerson.setClinicalStatements(clinicalStatements);
        ObservationResults observationResults = clinicalStatements.getObservationResults();
        if (observationResults == null) {
            clinicalStatements.setObservationResults(getObservationResults());
        }
        return evaluatedPerson;
    }

    private static ClinicalStatements getClinicalStatements() {
        ClinicalStatements clinicalStatements = new ClinicalStatements();
        return clinicalStatements;
    }

    private static Demographics getDemographics() {
        Demographics demographics = new Demographics();
        return demographics;
    }

    private static ObservationResult getObservationResult() {
        ObservationResult observationResult = new ObservationResult();
        observationResult.getTemplateIds().add(getII(Constants.getCodeSystemOid("OBSERVATION_RESULT_ROOT")));
        observationResult.setId(getII());
        return observationResult;
    }

    private static AdministrableSubstance getAdministrableSubstance() {
        AdministrableSubstance administerableSubstance = new AdministrableSubstance();
        administerableSubstance.setId(getII());
        return administerableSubstance;
    }

    private static CD getGeneralPurposeCode() {
        return getCD(Constants.getGeneralPurposeCode(),
                Constants.getCodeSystemOid("GENERAL_PURPOSE"));
    }

    public static SubstanceAdministrationEvent getSubstanceAdministrationEvent() {
        SubstanceAdministrationEvent substanceAdministrationEvent = new SubstanceAdministrationEvent();
        substanceAdministrationEvent.getTemplateIds().add(getII(Constants.getCodeSystemOid("SUBSTANCE_ADMINISTRATION_EVENT_ROOT")));
        substanceAdministrationEvent.setId(getII());
        substanceAdministrationEvent.setSubstanceAdministrationGeneralPurpose(getGeneralPurposeCode());
        return substanceAdministrationEvent;
    }

    private static SubstanceAdministrationProposal getSubstanceAdministrationProposal() {
        SubstanceAdministrationProposal substanceAdministrationProposal = new SubstanceAdministrationProposal();
        substanceAdministrationProposal.getTemplateIds().add(getII(Constants.getCodeSystemOid("SUBSTANCE_ADMINISTRATION_PROPOSAL_ROOT")));
        substanceAdministrationProposal.setId(getII());
        substanceAdministrationProposal.setSubstanceAdministrationGeneralPurpose(getGeneralPurposeCode());
        return substanceAdministrationProposal;
    }

    public static SubstanceAdministrationProposals getSubstanceAdministrationProposals(Vmr vmr) throws CdsException {
        ClinicalStatements clinicalStatements = vmr.getPatient().getClinicalStatements();
        SubstanceAdministrationProposals substanceAdministrationProposals = clinicalStatements.getSubstanceAdministrationProposals();
        if (substanceAdministrationProposals == null) {
            substanceAdministrationProposals = getSubstanceAdministrationProposals();
            clinicalStatements.setSubstanceAdministrationProposals(substanceAdministrationProposals);
        }
        return substanceAdministrationProposals;
    }

    private static SubstanceAdministrationProposals getSubstanceAdministrationProposals() {
        SubstanceAdministrationProposals substanceAdministrationProposals = new SubstanceAdministrationProposals();
        return substanceAdministrationProposals;
    }

    private static SubstanceAdministrationEvents getSubstanceAdministrationEvents(Vmr vmr) throws CdsException {
        ClinicalStatements clinicalStatements = vmr.getPatient().getClinicalStatements();
        SubstanceAdministrationEvents substanceAdministrationEvents = clinicalStatements.getSubstanceAdministrationEvents();
        if (substanceAdministrationEvents == null) {
            substanceAdministrationEvents = getSubstanceAdministrationEvents();
            clinicalStatements.setSubstanceAdministrationEvents(substanceAdministrationEvents);
        }
        return substanceAdministrationEvents;
    }

    private static ObservationResults getObservationResults() {
        ObservationResults observationResults = new ObservationResults();
        return observationResults;
    }

    private static ObservationResults getObservationResults(Vmr vmr) {
        ClinicalStatements clinicalStatements = vmr.getPatient().getClinicalStatements();
        ObservationResults observationResults = clinicalStatements.getObservationResults();
        if (observationResults == null) {
            observationResults = getObservationResults();
            clinicalStatements.setObservationResults(observationResults);
        }
        return observationResults;
    }

    private static SubstanceAdministrationEvents getSubstanceAdministrationEvents() {
        SubstanceAdministrationEvents substanceAdministrationEvents = new SubstanceAdministrationEvents();
        return substanceAdministrationEvents;
    }

    public static RelatedClinicalStatement getRelatedClinicalStatement(String targetRelationshipToSourceCode) {
        RelatedClinicalStatement relatedClinicalStatement = new RelatedClinicalStatement();
        relatedClinicalStatement.setTargetRelationshipToSource(getCD(targetRelationshipToSourceCode, Constants.getCodeSystemOid("TARGET_RELATIONSHIP_TO_SOURCE")));
        return relatedClinicalStatement;
    }

    protected static <S> S addObservationResult(
            S substanceAdministrationObject,
            String focus,
            String value,
            String[] interpretations)
            throws CdsException {

        RelatedClinicalStatement relatedClinicalStatement;
        CD focusValue;
        ObservationValue observationValue;
        List<CD> interpretationValues = new ArrayList<CD>();
        if (substanceAdministrationObject instanceof SubstanceAdministrationEvent) {
            SubstanceAdministrationEvent substanceAdministrationEvent = (SubstanceAdministrationEvent) substanceAdministrationObject;

            if (interpretations == null || interpretations.length == 0) {
                if (!substanceAdministrationEvent.getIsValid().isValue()) {
                    throw new CdsException("Substance administration event is marked as invalid but no reason was given: "
                            + substanceAdministrationEvent.getSubstance().getSubstanceCode().getCode() + "; "
                            + substanceAdministrationEvent.getAdministrationTimeInterval().getHigh());
                } else {
                    logger.debug("Event is valid and reason is null or empty");
                    //   interpretations = new String[]{"VALID_DOSE"};
                }
            }
			AdministrableSubstance substance = substanceAdministrationEvent.getSubstance();

            relatedClinicalStatement = getRelatedClinicalStatement("PERT");
            substanceAdministrationEvent.getRelatedClinicalStatements().add(relatedClinicalStatement);

            // TODO: maybe replace this with: focusValue = substance.getSubstanceCode()
            focusValue = getConceptCode(Constants.getCodeSystemOid("VACCINE_GROUP"), focus);

            observationValue = getObservationValue(Constants.getCodeSystemOid("EVALUATION_VALUE"), value);

            for (String interpretation : interpretations) {
                if (interpretation != null && !interpretation.trim().isEmpty()) {
                    interpretationValues.add(getConceptCode(Constants.getCodeSystemOid("EVALUATION_INTERPRETATION"), interpretation));
                }
            }

        } else if (substanceAdministrationObject instanceof SubstanceAdministrationProposal) {
            relatedClinicalStatement = getRelatedClinicalStatement("RSON");
            ((SubstanceAdministrationProposal) substanceAdministrationObject).getRelatedClinicalStatements().add(relatedClinicalStatement);
            AdministrableSubstance substance = ((SubstanceAdministrationProposal) substanceAdministrationObject).getSubstance();

            focusValue = substance.getSubstanceCode();

            observationValue = getObservationValue(Constants.getCodeSystemOid("RECOMMENDATION_VALUE"), value);

            for (String interpretation : interpretations) {
                interpretationValues.add(getConceptCode(Constants.getCodeSystemOid("RECOMMENDATION_INTERPRETATION"), interpretation));
            }

        } else {
            throw new CdsException("Unexpected substance administration class: "
                    + substanceAdministrationObject.getClass().getSimpleName());
        }
        addObservationResult(relatedClinicalStatement, focusValue, observationValue, interpretationValues);
        return substanceAdministrationObject;
    }

    private static CD getConceptCode(String codeSystem, String originalText) throws CdsException {
        CD cd = getCD(codeSystem);
        cd.setCode(originalText);
        return cd;
    }

    private static ObservationValue getObservationValue(String codeSystem, String value) throws CdsException {
        CD cd = getCD(codeSystem);
        cd.setCode(value);
        ObservationValue observationValue = new ObservationValue();
        observationValue.setConcept(cd);
        return observationValue;
    }

    protected static ObservationResult getObservationResult(
            CD focusValue,
            ObservationValue observationValue,
            List<CD> interpretationValues) {
        ObservationResult observationResult = getObservationResult();
        observationResult.getInterpretations().addAll(interpretationValues);
        observationResult.setObservationValue(observationValue);
        observationResult.setObservationFocus(focusValue);

        return observationResult;
    }

    private static void addObservationResult(
            RelatedClinicalStatement relatedClinicalStatement,
            CD focusValue,
            ObservationValue observationValue,
            List<CD> interpretationValues)
            throws CdsException {
        ObservationResult observationResult = getObservationResult(focusValue, observationValue, interpretationValues);
        relatedClinicalStatement.setObservationResult(observationResult);
    }

    protected static void setPatientBirthTime(Vmr vmr, String birthTime) throws CdsException {
        TS ts = new TS();
        ts.setValue(birthTime);
        vmr.getPatient().getDemographics().setBirthTime(ts);
    }

    protected static void setPatientGender(Vmr vmr, String gender) throws CdsException {
        CD cd = getConceptCode(Constants.getCodeSystemOid("GENDER"), gender);
        vmr.getPatient().getDemographics().setGender(cd);
    }

    protected static SubstanceAdministrationEvent getSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval) throws CdsException {
        SubstanceAdministrationEvent substanceAdministrationEvent = getSubstanceAdministrationEvent();

        AdministrableSubstance substance = getAdministrableSubstance();
        substance.setSubstanceCode(getConceptCode(Constants.getCodeSystemOid("VACCINE"), substanceCode));

        substanceAdministrationEvent.setSubstance(substance);

        IVLTS ivlts = new IVLTS();
        ivlts.setHigh(administrationTimeInterval);
        ivlts.setLow(administrationTimeInterval);

        substanceAdministrationEvent.setAdministrationTimeInterval(ivlts);

        return substanceAdministrationEvent;
    }

    // add substance administration event components with evaluation(s)
    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            String validity,
            String focus,
            String interpretation) throws CdsException {

        return getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                administrationTimeInterval,
                validity,
                focus,
                new String[]{interpretation});
    }

    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            String validity,
            String focus,
            String interpretation) throws CdsException {
        return getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                validity,
                focus,
                new String[]{interpretation});
    }

    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            String validity,
            String focus,
            String[] interpretations) throws CdsException {
        return getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                validity,
                focus,
                interpretations);
    }

    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            String validity,
            String focus,
            String[] interpretations) throws CdsException {
        boolean valid = !"INVALID".equalsIgnoreCase(validity);
        SubstanceAdministrationEvent substanceAdministrationEvent =
                getSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        substanceAdministrationEvent.setIsValid(getBL(valid));
        substanceAdministrationEvent = addObservationResult(
                substanceAdministrationEvent,
                focus,
                validity.toString(),
                interpretations);
        return substanceAdministrationEvent;
    }

    protected static SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            Vmr vmr,
            String substanceCode,
            String administrationTimeInterval,
            String immId)
            throws CdsException {
        SubstanceAdministrationEvent substanceAdministrationEvent =
                getSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        if (immId != null && !immId.trim().isEmpty()) {
            substanceAdministrationEvent.getId().setRoot(Constants.getCodeSystemOid("CIR_IMMUNIZATION_ID"));
            substanceAdministrationEvent.getId().setExtension(immId);
        }
        SubstanceAdministrationEvents substanceAdministrationEvents = getSubstanceAdministrationEvents(vmr);
        substanceAdministrationEvents.getSubstanceAdministrationEvents().add(substanceAdministrationEvent);
        return substanceAdministrationEvent;
    }

    protected static void addObservationResult(Vmr vmr, ObservationResult observationResult)
            throws CdsException {
        ObservationResults observationResults = getObservationResults(vmr);
        observationResults.getObservationResults().add(observationResult);
    }

    protected static ObservationResult addImmunityObservationResult(Vmr vmr, Date observationEventTime, String focus, String value, String interpretation)
            throws CdsException {
        CD focusValue = getConceptCode(Constants.getCodeSystemOid("DISEASE"), focus);
        ObservationValue observationValue = getObservationValue(Constants.getCodeSystemOid("IMMUNITY_VALUE"), value);
        List<CD> interpretationValues = new ArrayList<CD>();
        CD interpretationValue = getConceptCode(Constants.getCodeSystemOid("IMMUNITY_INTERPRETATION"), interpretation);
        interpretationValues.add(interpretationValue);
        ObservationResult observationResult = getObservationResult(focusValue, observationValue, interpretationValues);
        String observationEventTimeString = DateUtils.getISODateFormat(observationEventTime);
        IVLTS ivlts = new IVLTS();
        ivlts.setHigh(observationEventTimeString);
        ivlts.setLow(observationEventTimeString);
        observationResult.setObservationEventTime(ivlts);
        addObservationResult(vmr, observationResult);
        return observationResult;
    }

    protected SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            Vmr vmr,
            String vaccineGroup,
            String substanceCode,
            String administrationTimeInterval)
            throws CdsException {

        SubstanceAdministrationProposal substanceAdministrationProposal = getSubstanceAdministrationProposal();
        SubstanceAdministrationProposals substanceAdministrationProposals = getSubstanceAdministrationProposals(vmr);
        substanceAdministrationProposals.getSubstanceAdministrationProposals().add(substanceAdministrationProposal);

        AdministrableSubstance substance = getAdministrableSubstance();

        if (substanceCode != null && !substanceCode.trim().isEmpty()) {
            substance.setSubstanceCode(getConceptCode(Constants.getCodeSystemOid("VACCINE"), substanceCode));
        } else {
            substance.setSubstanceCode(getConceptCode(Constants.getCodeSystemOid("VACCINE_GROUP"), vaccineGroup));
        }

        substanceAdministrationProposal.setSubstance(substance);

        if (administrationTimeInterval != null && !administrationTimeInterval.trim().isEmpty()) {
            IVLTS ivlts = new IVLTS();
            ivlts.setHigh(administrationTimeInterval);
            ivlts.setLow(administrationTimeInterval);

            substanceAdministrationProposal.setProposedAdministrationTimeInterval(ivlts);
        }

        return substanceAdministrationProposal;
    }

    protected Vmr getCdsObjectVmr() throws CdsException {
        Vmr vmr;
        if (cdsObject == null) {
            throw new CdsException("cdsObject was null!");
        }
        if (cdsObject instanceof CdsInput) {
            vmr = ((CdsInput) cdsObject).getVmrInput();
        } else if (cdsObject instanceof CdsOutput) {
            vmr = ((CdsOutput) cdsObject).getVmrOutput();
        } else {
            throw new CdsException("Unexpected cdsObject class: " + cdsObject.getClass().getSimpleName());
        }
        return vmr;
    }

    public void setPatientBirthTime(Date birthDate) throws CdsException {
        setPatientBirthTime(DateUtils.getISODateFormat(birthDate));
    }

    public void setPatientBirthTime(String birthTime) throws CdsException {
        setPatientBirthTime(getCdsObjectVmr(), birthTime);
    }

    public void setPatientGender(String gender) throws CdsException {
        setPatientGender(getCdsObjectVmr(), gender);
    }

    public void setPatientId(String patientId) throws CdsException {
        Vmr cdsObjectVmr = getCdsObjectVmr();
        if (cdsObjectVmr != null) {
            EvaluatedPerson patient = cdsObjectVmr.getPatient();
            if (patient != null) {
                if (patientId != null && !patientId.trim().isEmpty()) {
                    patient.getId().setRoot(Constants.getCodeSystemOid("CIR_PATIENT_ID"));
                    patient.getId().setExtension(patientId);
                }
            } else {
                throw new CdsException("Retrieved patient object was null.");
            }
        } else {
            throw new CdsException("Retrieved VMR object was null.");
        }
    }
}
