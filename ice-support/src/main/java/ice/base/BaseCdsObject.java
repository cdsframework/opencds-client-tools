package ice.base;

import ice.exception.IceException;
import ice.util.Constants;
import ice.util.DateUtils;
import java.util.Date;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.opencds.AdministrableSubstance;
import org.opencds.BL;
import org.opencds.CD;
import org.opencds.CDSContext;
import org.opencds.CdsInput;
import org.opencds.CdsOutput;
import org.opencds.EvaluatedPerson;
import org.opencds.EvaluatedPerson.ClinicalStatements;
import org.opencds.EvaluatedPerson.ClinicalStatements.ObservationResults;
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
        if (cdsObjectClass == CdsInput.class) {
            cdsObject = (T) getCdsInput();
        } else {
            cdsObject = (T) getCdsOutput();
        }
    }

    public T getCdsObject() {
        return cdsObject;
    }

    public Class<T> getCdsObjectClass() {
        return cdsObjectClass;
    }

    private static CD getCD(String code, String codeSystem, String codeSystemName, String displayName, String originalText) {
        CD cd = new CD();
        if (code != null) {
            cd.setCode(code);
        }
        if (codeSystem != null) {
            cd.setCodeSystem(codeSystem);
        }
        if (codeSystemName != null) {
            cd.setCodeSystemName(codeSystemName);
        }
        if (displayName != null) {
            cd.setDisplayName(displayName);
        }
        if (originalText != null) {
            cd.setOriginalText(originalText);
        }
        return cd;
    }

    private static CD getCD(String code, String codeSystem, String codeSystemName, String displayName) {
        return getCD(code, codeSystem, codeSystemName, displayName, null);
    }

    private static CD getCD(String code, String codeSystem, String displayName) {
        return getCD(code, codeSystem, null, displayName, null);
    }

    private static CD getCD(String code, String codeSystem) {
        return getCD(code, codeSystem, null, null, null);
    }

    private static II getII(String rootId) {
        II ii = new II();
        ii.setRoot(rootId);
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
        cdsInput.getTemplateIds().add(getII(Constants.CDS_INPUT_ROOT_OID));
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
                getCD(Constants.DEFAULT_LANG_CODE,
                Constants.LANG_CODE_SYSTEM_OID,
                Constants.DEFAULT_LANG_DISPLAY_NAME));
        return cdsContext;
    }

    private static Vmr getVmr() {
        Vmr vmr = new Vmr();
        vmr.getTemplateIds().add(getII(Constants.VMR_ROOT_OID));
        vmr.setPatient(getEvaluatedPerson());
        return vmr;
    }

    private static EvaluatedPerson getEvaluatedPerson() {
        EvaluatedPerson evaluatedPerson = new EvaluatedPerson();
        evaluatedPerson.getTemplateIds().add(getII(Constants.EVALUATED_PERSON_ROOT_OID));
        evaluatedPerson.setId(getII());
        evaluatedPerson.setDemographics(getDemographics());
        evaluatedPerson.setClinicalStatements(getClinicalStatements());
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
        observationResult.getTemplateIds().add(getII(Constants.OBSERVATION_RESULT_ROOT_OID));
        observationResult.setId(getII());
        return observationResult;
    }

    private static AdministrableSubstance getAdministrableSubstance() {
        AdministrableSubstance administerableSubstance = new AdministrableSubstance();
        administerableSubstance.setId(getII());
        administerableSubstance.setSubstanceCode(getCD(null, Constants.ADMINISTERED_SUBSTANCE_ROOT_OID, null));
        return administerableSubstance;
    }

    private static CD getGeneralPurposeCode() {
        return getCD(Constants.DEFAULT_GENERAL_PURPOSE_CODE,
                Constants.GENERAL_PURPOSE_CODE_SYSTEM_OID,
                Constants.DEFAULT_GENERAL_PURPOSE_CODE_SYSTEM_NAME,
                Constants.DEFAULT_GENERAL_PURPOSE_CODE_SYSTEM_DISPLAY_NAME);
    }

    private static SubstanceAdministrationEvent getSubstanceAdministrationEvent() {
        SubstanceAdministrationEvent substanceAdministrationEvent = new SubstanceAdministrationEvent();
        substanceAdministrationEvent.getTemplateIds().add(getII(Constants.SUBSTANCE_ADMINISTRATION_EVENT_ROOT_OID));
        substanceAdministrationEvent.setId(getII());
        substanceAdministrationEvent.setSubstanceAdministrationGeneralPurpose(getGeneralPurposeCode());
        return substanceAdministrationEvent;
    }

    private static SubstanceAdministrationProposal getSubstanceAdministrationProposal() {
        SubstanceAdministrationProposal substanceAdministrationProposal = new SubstanceAdministrationProposal();
        substanceAdministrationProposal.getTemplateIds().add(getII(Constants.SUBSTANCE_ADMINISTRATION_PROPOSAL_ROOT_OID));
        substanceAdministrationProposal.setId(getII());
        substanceAdministrationProposal.setSubstanceAdministrationGeneralPurpose(getGeneralPurposeCode());
        return substanceAdministrationProposal;
    }

    private static SubstanceAdministrationProposals getSubstanceAdministrationProposals(Vmr vmr) throws IceException {
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

    private static SubstanceAdministrationEvents getSubstanceAdministrationEvents(Vmr vmr) throws IceException {
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

    protected static RelatedClinicalStatement getRelatedClinicalStatement(String code) {
        RelatedClinicalStatement relatedClinicalStatement = new RelatedClinicalStatement();
        relatedClinicalStatement.setTargetRelationshipToSource(getCD(code, Constants.TARGET_RELATIONSHIP_TO_SOURCE_CODE_SYSTEM_OID));
        return relatedClinicalStatement;
    }

    protected static <S> S addObservationResult(
            S substanceAdministrationObject,
            String focus,
            String value,
            String interpretation)
            throws IceException {

        RelatedClinicalStatement relatedClinicalStatement;
        CD focusValue;
        ObservationValue observationValue;
        CD interpretationValue;
        if (substanceAdministrationObject instanceof SubstanceAdministrationEvent) {
            relatedClinicalStatement = getRelatedClinicalStatement("PERT");
            SubstanceAdministrationEvent substanceAdministrationEvent = (SubstanceAdministrationEvent) substanceAdministrationObject;
            substanceAdministrationEvent.getRelatedClinicalStatements().add(relatedClinicalStatement);
            focusValue = getObservationFocus(focus, "TBD_IMMUNIZATION_EVAL_FOCUS", "TBD");
            observationValue = getObservationValue(value, "TBD_IMM_VALIDITY", "TBD");
            interpretationValue = getInterpretation(interpretation, "TBD_EVAL_REASON", "TBD");
        } else if (substanceAdministrationObject instanceof SubstanceAdministrationProposal) {
            relatedClinicalStatement = getRelatedClinicalStatement("RSON");
            ((SubstanceAdministrationProposal) substanceAdministrationObject).getRelatedClinicalStatements().add(relatedClinicalStatement);
            focusValue = getObservationFocus(focus, "TBD_IMMUNIZATION_RECOMMENDATION_FOCUS", "TBD");
            observationValue = getObservationValue(value, "TBD_IMMUNIZATION_RECOMMENDATION", "TBD");
            interpretationValue = getInterpretation(interpretation, "TBD_RECOMMENDATION_REASON", "TBD");
        } else {
            throw new IceException("Unexpected substance administration class: "
                    + substanceAdministrationObject.getClass().getSimpleName());
        }
        addObservationResult(relatedClinicalStatement, focusValue, observationValue, interpretationValue);
        return substanceAdministrationObject;
    }

    private static CD getInterpretation(String code, String codeSystem, String displayName) {
        return getCD(code, codeSystem, displayName);
    }

    private static CD getObservationFocus(String code, String codeSystem, String displayName) {
        return getCD(code, codeSystem, displayName);
    }

    private static ObservationValue getObservationValue(String code, String codeSystem, String displayName) {
        CD cd = getCD(code, codeSystem, displayName);
        ObservationValue observationValue = new ObservationValue();
        observationValue.setConcept(cd);
        return observationValue;
    }

    protected static ObservationResult getObservationResult(
            CD focusValue,
            ObservationValue observationValue,
            CD interpretationValue) {
        ObservationResult observationResult = getObservationResult();

        observationResult.getInterpretations().add(interpretationValue);

        observationResult.setObservationValue(observationValue);

        observationResult.setObservationFocus(focusValue);

        return observationResult;
    }

    private static void addObservationResult(
            RelatedClinicalStatement relatedClinicalStatement,
            CD focusValue,
            ObservationValue observationValue,
            CD interpretationValue)
            throws IceException {
        ObservationResult observationResult = getObservationResult(focusValue, observationValue, interpretationValue);
        relatedClinicalStatement.setObservationResult(observationResult);
    }

    protected static void setPatientBirthTime(Vmr vmr, String birthTime) throws IceException {
        TS ts = new TS();
        ts.setValue(birthTime);
        vmr.getPatient().getDemographics().setBirthTime(ts);
    }

    protected static void setPatientGender(Vmr vmr, String gender) throws IceException {
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
        vmr.getPatient().getDemographics().setGender(getCD(genderCode, Constants.GENDER_CODE_SYSTEM_OID, null, displayName, gender));
    }

    protected static SubstanceAdministrationEvent getSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval) {
        SubstanceAdministrationEvent substanceAdministrationEvent = getSubstanceAdministrationEvent();

        AdministrableSubstance substance = getAdministrableSubstance();
        substanceAdministrationEvent.setSubstance(substance);
        substance.getSubstanceCode().setCode(substanceCode);
        substance.getSubstanceCode().setDisplayName("TBD - CVX VACCODE NAME");

        IVLTS ivlts = new IVLTS();
        ivlts.setHigh(administrationTimeInterval);
        ivlts.setLow(administrationTimeInterval);

        substanceAdministrationEvent.setAdministrationTimeInterval(ivlts);

        return substanceAdministrationEvent;
    }

    protected static SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            Vmr vmr,
            String substanceCode,
            String administrationTimeInterval)
            throws IceException {
        SubstanceAdministrationEvent substanceAdministrationEvent =
                getSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        SubstanceAdministrationEvents substanceAdministrationEvents = getSubstanceAdministrationEvents(vmr);
        substanceAdministrationEvents.getSubstanceAdministrationEvents().add(substanceAdministrationEvent);
        return substanceAdministrationEvent;
    }

    protected static void addObservationResult(Vmr vmr, ObservationResult observationResult)
            throws IceException {
        ObservationResults observationResults = getObservationResults(vmr);
        observationResults.getObservationResults().add(observationResult);
    }

    protected static ObservationResult addImmunityObservationResult(Vmr vmr, boolean immune, int vaccineGroup)
            throws IceException {
        CD focusValue = getObservationFocus(String.valueOf(vaccineGroup), "TBD_IMMUNIZATION_GROUP_FOCUS", "TBD");
        ObservationValue observationValue = getObservationValue(String.valueOf(immune), "TBD_IMMUNITY_VALUE", "TBD");
        CD interpretationValue = getInterpretation(immune ? "IMMUNE" : "NOT IMMUNE", "TBD_IMMUNITY_INTERPRETATION", "TBD");
        ObservationResult observationResult = getObservationResult(focusValue, observationValue, interpretationValue);
        addObservationResult(vmr, observationResult);
        return observationResult;
    }

    protected SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            Vmr vmr,
            int vaccineGroup,
            String substanceCode,
            String administrationTimeInterval)
            throws IceException {

        SubstanceAdministrationProposal substanceAdministrationProposal = getSubstanceAdministrationProposal();
        SubstanceAdministrationProposals substanceAdministrationProposals = getSubstanceAdministrationProposals(vmr);
        substanceAdministrationProposals.getSubstanceAdministrationProposals().add(substanceAdministrationProposal);



        AdministrableSubstance substance = getAdministrableSubstance();
        substanceAdministrationProposal.setSubstance(substance);
        substance.getSubstanceCode().setCode(String.valueOf(vaccineGroup));
        substance.getSubstanceCode().setDisplayName("TBD - vaccine group: " + vaccineGroup + " - cvx: " + substanceCode);

        if (administrationTimeInterval != null && !administrationTimeInterval.trim().isEmpty()) {
            IVLTS ivlts = new IVLTS();
            ivlts.setHigh(administrationTimeInterval);
            ivlts.setLow(administrationTimeInterval);

            substanceAdministrationProposal.setProposedAdministrationTimeInterval(ivlts);
        }

        return substanceAdministrationProposal;
    }

    protected Vmr getCdsObjectVmr() throws IceException {
        Vmr vmr;
        if (cdsObject instanceof CdsInput) {
            vmr = ((CdsInput) cdsObject).getVmrInput();
        } else if (cdsObject instanceof CdsOutput) {
            vmr = ((CdsOutput) cdsObject).getVmrOutput();
        } else {
            throw new IceException("Unexpected cdsObject class: " + cdsObject.getClass().getSimpleName());
        }
        return vmr;
    }

    public void setPatientBirthTime(Date birthDate) throws IceException {
        setPatientBirthTime(DateUtils.getISODateFormat(birthDate));
    }

    public void setPatientBirthTime(String birthTime) throws IceException {
        setPatientBirthTime(getCdsObjectVmr(), birthTime);
    }

    public void setPatientGender(String gender) throws IceException {
        setPatientGender(getCdsObjectVmr(), gender);
    }
}
