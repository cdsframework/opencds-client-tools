package ice.dto;

import ice.base.BaseCdsObject;
import ice.exception.IceException;
import java.util.List;
import org.opencds.AdministrableSubstance;
import org.opencds.CD;
import org.opencds.CdsOutput;
import org.opencds.EvaluatedPerson.ClinicalStatements;
import org.opencds.EvaluatedPerson.ClinicalStatements.ObservationResults;
import org.opencds.IVLTS;
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

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String vaccineGroup,
            String substanceCode,
            String administrationTimeInterval,
            String focus,
            String value,
            String interpretation)
            throws IceException {

        SubstanceAdministrationEvent substanceAdministrationEvent = addSubstanceAdministrationEvent(
                vaccineGroup,
                substanceCode,
                administrationTimeInterval);

        substanceAdministrationEvent = addObservationResult(substanceAdministrationEvent, focus, value, interpretation);
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
                vaccineGroup,
                substanceCode,
                administrationTimeInterval);

        substanceAdministrationProposal = addObservationResult(substanceAdministrationProposal, focus, value, interpretation);
        return substanceAdministrationProposal;
    }

    @Override
    protected SubstanceAdministrationEvent addSubstanceAdministrationEvent(String vaccineGroup, String substanceCode, String administrationTimeInterval) throws IceException {

        RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement("TBD");
        SubstanceAdministrationEvent substanceAdministrationEvent = getSubstanceAdministrationEvent();
        relatedClinicalStatement.setSubstanceAdministrationEvent(substanceAdministrationEvent);

        ObservationResult vaccineGroupObservationResult = getVaccineGroupObservationResult(vaccineGroup);

        vaccineGroupObservationResult.getRelatedClinicalStatements().add(relatedClinicalStatement);

        AdministrableSubstance substance = getAdministrableSubstance();
        substanceAdministrationEvent.setSubstance(substance);
        substance.getSubstanceCode().setCode(substanceCode);
        substance.getSubstanceCode().setDisplayName("TBD");

        IVLTS ivlts = new IVLTS();
        ivlts.setHigh(administrationTimeInterval);
        ivlts.setLow(administrationTimeInterval);

        substanceAdministrationEvent.setAdministrationTimeInterval(ivlts);

        return substanceAdministrationEvent;
    }

    @Override
    protected SubstanceAdministrationProposal addSubstanceAdministrationProposal(String vaccineGroup, String substanceCode, String administrationTimeInterval) throws IceException {

        RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement("TBD");
        SubstanceAdministrationProposal substanceAdministrationProposal = getSubstanceAdministrationProposal();
        relatedClinicalStatement.setSubstanceAdministrationProposal(substanceAdministrationProposal);

        ObservationResult vaccineGroupObservationResult = getVaccineGroupObservationResult(vaccineGroup);

        vaccineGroupObservationResult.getRelatedClinicalStatements().add(relatedClinicalStatement);

        AdministrableSubstance substance = getAdministrableSubstance();
        substanceAdministrationProposal.setSubstance(substance);
        if (substanceCode != null && !substanceCode.trim().isEmpty()) {
            substance.getSubstanceCode().setCode(substanceCode);
            substance.getSubstanceCode().setDisplayName("TBD");
        }

        if (administrationTimeInterval != null && !administrationTimeInterval.trim().isEmpty()) {
            IVLTS ivlts = new IVLTS();
            ivlts.setHigh(administrationTimeInterval);
            ivlts.setLow(administrationTimeInterval);

            substanceAdministrationProposal.setProposedAdministrationTimeInterval(ivlts);
        }

        return substanceAdministrationProposal;
    }

    private ObservationResult getVaccineGroupObservationResult(String vaccineGroup) throws IceException {
        ObservationResult result = null;
        if (vaccineGroup == null || vaccineGroup.isEmpty()) {
            throw new IceException("vaccineGroup is null.");
        }
        ClinicalStatements clinicalStatements = this.getCdsObject().getVmrOutput().getPatient().getClinicalStatements();

        ObservationResults observationResults = clinicalStatements.getObservationResults();
        if (observationResults == null) {
            observationResults = new ObservationResults();
            clinicalStatements.setObservationResults(observationResults);
        }

        List<ObservationResult> observationResultList = observationResults.getObservationResults();
        for (ObservationResult observationResult : observationResultList) {
            if (vaccineGroup.equals(observationResult.getObservationFocus().getCode())) {
                result = observationResult;
            }
        }

        if (result == null) {
            result = getObservationResult();
            CD cd = new CD();
            cd.setCode(vaccineGroup);
            cd.setDisplayName(vaccineGroup + " Vaccine Group");
            cd.setCodeSystem("TBD");
            result.setObservationFocus(cd);
            observationResults.getObservationResults().add(result);
        }
        return result;
    }
}
