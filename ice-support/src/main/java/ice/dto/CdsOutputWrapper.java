package ice.dto;

import ice.base.BaseCdsObject;
import ice.dto.support.Reason;
import ice.exception.IceException;
import ice.util.Constants;
import ice.util.DateUtils;
import java.util.Date;
import java.util.List;
import org.opencds.CdsOutput;
import org.opencds.ObservationResult;
import org.opencds.RelatedClinicalStatement;
import org.opencds.SubstanceAdministrationEvent;
import org.opencds.SubstanceAdministrationProposal;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsOutputWrapper extends BaseCdsObject<CdsOutput> {

    public CdsOutputWrapper() {
        super(CdsOutputWrapper.class, CdsOutput.class);
    }

    public static CdsOutputWrapper getCdsOutputWrapper() {
        return new CdsOutputWrapper();
    }

    public ObservationResult addImmunityObservationResult(String focus, String value, String interpretation)
            throws IceException {
        return addImmunityObservationResult(this.getCdsObject().getVmrOutput(), focus, value, interpretation);
    }

    public void addObservationResult(ObservationResult observationResult)
            throws IceException {
        addObservationResult(this.getCdsObject().getVmrOutput(), observationResult);
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            boolean valid,
            String focus,
            String value,
            String interpretation) throws IceException {
        SubstanceAdministrationEvent substanceAdministrationEvent =
                getSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        substanceAdministrationEvent.setIsValid(getBL(valid));
        substanceAdministrationEvent = addObservationResult(substanceAdministrationEvent, focus, value, interpretation);
        return substanceAdministrationEvent;
    }

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
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

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
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

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
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

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            SubstanceAdministrationEvent[] components)
            throws IceException {

        SubstanceAdministrationEvent substanceAdministrationEvent =
                addSubstanceAdministrationEvent(this.getCdsObject().getVmrOutput(), substanceCode, administrationTimeInterval);
        List<RelatedClinicalStatement> relatedClinicalStatements = substanceAdministrationEvent.getRelatedClinicalStatements();

        for (SubstanceAdministrationEvent sae : components) {
            RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement(Constants.TARGET_RELATIONSHIP_TO_SOURCE_PERTINENT_INFO_CODE);
            relatedClinicalStatement.setSubstanceAdministrationEvent(sae);
            relatedClinicalStatements.add(relatedClinicalStatement);
        }

        return substanceAdministrationEvent;
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            SubstanceAdministrationEvent[] components)
            throws IceException {
        return addSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                components);
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            int vaccineGroup,
            String substanceCode,
            String administrationTimeInterval,
            String focus,
            String value,
            String interpretation)
            throws IceException {

        SubstanceAdministrationProposal substanceAdministrationProposal = addSubstanceAdministrationProposal(
                this.getCdsObject().getVmrOutput(),
                vaccineGroup,
                substanceCode,
                administrationTimeInterval);

        substanceAdministrationProposal =
                addObservationResult(substanceAdministrationProposal, focus, value, interpretation);
        return substanceAdministrationProposal;
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            int vaccineGroup,
            String substanceCode,
            Date administrationTimeIntervalDate,
            String focus,
            String value,
            String interpretation)
            throws IceException {
        return addSubstanceAdministrationProposal(
                vaccineGroup,
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                focus,
                value,
                interpretation);
    }
}
