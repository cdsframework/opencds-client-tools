package org.cdsframework.dto;

import java.util.Date;
import java.util.List;

import org.cdsframework.base.BaseCdsObject;
import org.cdsframework.enumeration.TargetRelationshipToSource;
import org.cdsframework.exception.IceException;
import org.cdsframework.util.DateUtils;
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

    public ObservationResult addImmunityObservationResult(Date observationEventTime, String focus, String value, String interpretation)
            throws IceException {
        return addImmunityObservationResult(this.getCdsObject().getVmrOutput(), observationEventTime, focus, value, interpretation);
    }

    public void addObservationResult(ObservationResult observationResult)
            throws IceException {
        addObservationResult(this.getCdsObject().getVmrOutput(), observationResult);
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            String immId,
            SubstanceAdministrationEvent[] components)
            throws IceException {

        SubstanceAdministrationEvent substanceAdministrationEvent =
                addSubstanceAdministrationEvent(this.getCdsObject().getVmrOutput(), substanceCode, administrationTimeInterval, immId);
        List<RelatedClinicalStatement> relatedClinicalStatements = substanceAdministrationEvent.getRelatedClinicalStatements();

        for (SubstanceAdministrationEvent sae : components) {
            RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement(TargetRelationshipToSource.PERT);
            relatedClinicalStatement.setSubstanceAdministrationEvent(sae);
            relatedClinicalStatements.add(relatedClinicalStatement);
        }

        return substanceAdministrationEvent;
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            String immId,
            SubstanceAdministrationEvent[] components)
            throws IceException {
        return addSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                immId,
                components);
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

        substanceAdministrationProposal =
                addObservationResult(substanceAdministrationProposal, focus, value, new String[]{interpretation});
        return substanceAdministrationProposal;
    }

    public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
            String vaccineGroup,
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
