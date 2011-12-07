package ice.dto;

import ice.base.BaseCdsObject;
import ice.exception.IceException;
import java.util.List;
import org.opencds.CdsOutput;
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

    public SubstanceAdministrationEvent getEvaluationSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            boolean valid,
            String focus,
            String value,
            String interpretation) throws IceException {
        SubstanceAdministrationEvent substanceAdministrationEvent = getSubstanceAdministrationEvent(substanceCode, administrationTimeInterval);
        substanceAdministrationEvent = addObservationResult(substanceAdministrationEvent, valid, focus, value, interpretation);
        return substanceAdministrationEvent;
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            List<SubstanceAdministrationEvent> componentEvents)
            throws IceException {

        SubstanceAdministrationEvent substanceAdministrationEvent =
                addSubstanceAdministrationEvent(this.getCdsObject().getVmrOutput(), substanceCode, administrationTimeInterval);
        List<RelatedClinicalStatement> relatedClinicalStatements = substanceAdministrationEvent.getRelatedClinicalStatements();
        for (SubstanceAdministrationEvent sae : componentEvents) {
            RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement("COMP");
            relatedClinicalStatement.setSubstanceAdministrationEvent(sae);
            relatedClinicalStatements.add(relatedClinicalStatement);
        }

        return substanceAdministrationEvent;
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            String vaccineGroup,
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

        substanceAdministrationProposal = addObservationResult(substanceAdministrationProposal, true, focus, value, interpretation);
        return substanceAdministrationProposal;
    }

}
