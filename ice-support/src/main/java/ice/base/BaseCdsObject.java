package ice.base;

import ice.dto.support.CdsObjectAssist;
import ice.dto.support.Reason;
import ice.enumeration.CodeSystemOid;
import ice.enumeration.RootOid;
import ice.enumeration.TargetRelationshipToSource;
import ice.exception.IceException;
import ice.util.Constants;
import ice.util.DateUtils;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
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

    protected static Logger logger = Logger.getLogger(BaseCdsObject.class);
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

    private static CD getCD(String code, CodeSystemOid codeSystem, String displayName, String originalText) {
        CD cd = new CD();
        if (code != null) {
            cd.setCode(code);
        }
        if (codeSystem != null) {
            cd.setCodeSystem(codeSystem.getOid());
        }
        if (displayName != null) {
            cd.setDisplayName(displayName);
        }
        if (originalText != null) {
            cd.setOriginalText(originalText);
        }
        return cd;
    }

    private static CD getCD(String code, CodeSystemOid codeSystem, String displayName) {
        return getCD(code, codeSystem, displayName, null);
    }

    private static CD getCD(String code, CodeSystemOid codeSystem) {
        return getCD(code, codeSystem, null, null);
    }

    private static CD getCD(CodeSystemOid codeSystem) {
        return getCD(null, codeSystem, null, null);
    }

    private static II getII(RootOid rootId) {
        II ii = new II();
        ii.setRoot(rootId.getOid());
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
        cdsInput.getTemplateIds().add(getII(RootOid.CDS_INPUT));
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
                CodeSystemOid.LANG,
                Constants.DEFAULT_LANG_DISPLAY_NAME));
        return cdsContext;
    }

    private static Vmr getVmr() {
        Vmr vmr = new Vmr();
        vmr.getTemplateIds().add(getII(RootOid.VMR));
        vmr.setPatient(getEvaluatedPerson());
        return vmr;
    }

    private static EvaluatedPerson getEvaluatedPerson() {
        EvaluatedPerson evaluatedPerson = new EvaluatedPerson();
        evaluatedPerson.getTemplateIds().add(getII(RootOid.EVALUATED_PERSON));
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
        observationResult.getTemplateIds().add(getII(RootOid.OBSERVATION_RESULT));
        observationResult.setId(getII());
        return observationResult;
    }

    private static AdministrableSubstance getAdministrableSubstance() {
        AdministrableSubstance administerableSubstance = new AdministrableSubstance();
        administerableSubstance.setId(getII());
        return administerableSubstance;
    }

    private static CD getGeneralPurposeCode() {
        return getCD(Constants.DEFAULT_GENERAL_PURPOSE_CODE,
                CodeSystemOid.GENERAL_PURPOSE);
    }

    private static SubstanceAdministrationEvent getSubstanceAdministrationEvent() {
        SubstanceAdministrationEvent substanceAdministrationEvent = new SubstanceAdministrationEvent();
        substanceAdministrationEvent.getTemplateIds().add(getII(RootOid.SUBSTANCE_ADMINISTRATION_EVENT));
        substanceAdministrationEvent.setId(getII());
        substanceAdministrationEvent.setSubstanceAdministrationGeneralPurpose(getGeneralPurposeCode());
        return substanceAdministrationEvent;
    }

    private static SubstanceAdministrationProposal getSubstanceAdministrationProposal() {
        SubstanceAdministrationProposal substanceAdministrationProposal = new SubstanceAdministrationProposal();
        substanceAdministrationProposal.getTemplateIds().add(getII(RootOid.SUBSTANCE_ADMINISTRATION_PROPOSAL));
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

    protected static RelatedClinicalStatement getRelatedClinicalStatement(TargetRelationshipToSource code) {
        RelatedClinicalStatement relatedClinicalStatement = new RelatedClinicalStatement();
        relatedClinicalStatement.setTargetRelationshipToSource(getCD(code.toString(), CodeSystemOid.TARGET_RELATIONSHIP_TO_SOURCE));
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
            SubstanceAdministrationEvent substanceAdministrationEvent = (SubstanceAdministrationEvent) substanceAdministrationObject;

            if (!substanceAdministrationEvent.getIsValid().isValue() && (interpretation == null || interpretation.trim().isEmpty())) {
                throw new IceException("Substance administration event is marked as invalid but no reason was given: "
                        + substanceAdministrationEvent.getSubstance().getSubstanceCode().getCode() + "; "
                        + substanceAdministrationEvent.getAdministrationTimeInterval().getHigh());
            }
            if (interpretation == null || interpretation.trim().isEmpty()) {
                logger.info("Event is valid and reason is null or empty - skipping addObservationResult");
                return substanceAdministrationObject;
            }

            relatedClinicalStatement = getRelatedClinicalStatement(TargetRelationshipToSource.PERT);
            substanceAdministrationEvent.getRelatedClinicalStatements().add(relatedClinicalStatement);

            focusValue = getConceptCode(CodeSystemOid.VALIDITY_FOCUS, focus);

            observationValue = getObservationValue(CodeSystemOid.VALIDATION, value);

            interpretationValue = getConceptCode(CodeSystemOid.EVALUATED_REASON, interpretation);

        } else if (substanceAdministrationObject instanceof SubstanceAdministrationProposal) {
            relatedClinicalStatement = getRelatedClinicalStatement(TargetRelationshipToSource.RSON);
            ((SubstanceAdministrationProposal) substanceAdministrationObject).getRelatedClinicalStatements().add(relatedClinicalStatement);

            focusValue = getConceptCode(CodeSystemOid.RECOMMENDED_FOCUS, focus);

            observationValue = getObservationValue(CodeSystemOid.RECOMMENDATION, value);

            interpretationValue = getConceptCode(CodeSystemOid.RECOMMENDED_ACTION, interpretation);

        } else {
            throw new IceException("Unexpected substance administration class: "
                    + substanceAdministrationObject.getClass().getSimpleName());
        }
        addObservationResult(relatedClinicalStatement, focusValue, observationValue, interpretationValue);
        return substanceAdministrationObject;
    }

    private static CD getConceptCode(CodeSystemOid codeSystemOid, String originalText) throws IceException {
        CD cd = getCD(codeSystemOid);
        setConceptCode(cd, codeSystemOid, originalText);
        return cd;
    }

    private static void setConceptCode(CD cd, CodeSystemOid codeSystemOid, String originalText) throws IceException {
        Map<String, String> codeset = CdsObjectAssist.getCodeSystemMap(codeSystemOid);
        String code = null;
        String displayName = null;
        cd.setOriginalText(originalText);
        for (Entry<String, String> entry : codeset.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(originalText)) {
                code = entry.getKey();
                displayName = entry.getValue();
                break;
            }
        }
        if (code != null) {
            cd.setCode(code);
            cd.setDisplayName(displayName);
        } else {
            throw new IceException("Code value '" + originalText + "' not found in codeset: " + codeSystemOid.toString());
        }
    }

    private static ObservationValue getObservationValue(CodeSystemOid codeSystemOid, String value) throws IceException {
        CD cd = getCD(codeSystemOid);
        setConceptCode(cd, codeSystemOid, value);
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
        CD cd = getConceptCode(CodeSystemOid.GENDER, gender);
        vmr.getPatient().getDemographics().setGender(cd);
    }

    protected static SubstanceAdministrationEvent getSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval) throws IceException {
        SubstanceAdministrationEvent substanceAdministrationEvent = getSubstanceAdministrationEvent();

        AdministrableSubstance substance = getAdministrableSubstance();
        substance.setSubstanceCode(getConceptCode(CodeSystemOid.ADMINISTERED_SUBSTANCE, substanceCode));

        substanceAdministrationEvent.setSubstance(substance);

        IVLTS ivlts = new IVLTS();
        ivlts.setHigh(administrationTimeInterval);
        ivlts.setLow(administrationTimeInterval);

        substanceAdministrationEvent.setAdministrationTimeInterval(ivlts);

        return substanceAdministrationEvent;
    }

    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            boolean valid,
            String focus,
            String value,
            String interpretation) throws IceException {
        SubstanceAdministrationEvent substanceAdministrationEvent =
                getSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        substanceAdministrationEvent.setIsValid(getBL(valid));
        if (!valid && (interpretation == null || interpretation.trim().isEmpty())) {
            throw new IceException("Substance administration event is marked as invalid but no reason was given: " + substanceCode + "; " + administrationTimeInterval);
        }
        if (interpretation != null && !interpretation.trim().isEmpty()) {
            substanceAdministrationEvent = addObservationResult(substanceAdministrationEvent, focus, value, interpretation);
        }
        return substanceAdministrationEvent;
    }

    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            boolean valid,
            Reason[] reasons) throws IceException {
        SubstanceAdministrationEvent substanceAdministrationEvent =
                getSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        substanceAdministrationEvent.setIsValid(getBL(valid));
        for (Reason reason : reasons) {
            if (reason.getInterpretation() != null && !reason.getInterpretation().isEmpty()) {
                substanceAdministrationEvent = addObservationResult(
                        substanceAdministrationEvent,
                        reason.getFocus(),
                        reason.getValue(),
                        reason.getInterpretation());
            }
        }
        return substanceAdministrationEvent;
    }

    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            boolean valid,
            Reason[] reasons) throws IceException {
        return getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                valid,
                reasons);
    }

    public static SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            boolean valid,
            String focus,
            String value,
            String interpretation) throws IceException {
        return getEvaluationSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                valid,
                focus,
                value,
                interpretation);
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

    protected static ObservationResult addImmunityObservationResult(Vmr vmr, String focus, String value, String interpretation)
            throws IceException {
        CD focusValue = getConceptCode(CodeSystemOid.IMMUNITY_FOCUS, focus);
        ObservationValue observationValue = getObservationValue(CodeSystemOid.IMMUNITY_VALUE, value);
        CD interpretationValue = getConceptCode(CodeSystemOid.IMMUNITY_INTERPRETATION, interpretation);
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

        // TODO: ifblock to recommend either a vaccode or a vaccine group...hardcoded to vaccine group now...
        String vaccineGroupString = String.valueOf(vaccineGroup);
        AdministrableSubstance substance = getAdministrableSubstance();
        substance.setSubstanceCode(getConceptCode(CodeSystemOid.RECOMMENDED_FOCUS, vaccineGroupString));

        substanceAdministrationProposal.setSubstance(substance);

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
