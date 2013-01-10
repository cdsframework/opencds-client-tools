package org.cdsframework.util.support.cds;

import java.util.Date;
import java.util.List;
import org.cdsframework.base.BaseCdsObject;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.DateUtils;
import org.opencds.support.CdsOutput;
import org.opencds.support.ObservationResult;
import org.opencds.support.RelatedClinicalStatement;
import org.opencds.support.SubstanceAdministrationEvent;
import org.opencds.support.SubstanceAdministrationProposal;

/**
 * 
 * @author HLN Consulting, LLC
 */
public class CdsOutputWrapper extends BaseCdsObject<CdsOutput> {

	public CdsOutputWrapper() {
		super(CdsOutputWrapper.class, CdsOutput.class);
		logger.debug("no arg constructor called");
	}

	public CdsOutputWrapper(CdsOutput cdsOutput) {
		super(CdsOutputWrapper.class, CdsOutput.class, cdsOutput);
		logger.debug("CdsOutput arg constructor called: " + cdsOutput);
	}

	public static CdsOutputWrapper getCdsOutputWrapper() {
		return new CdsOutputWrapper();
	}

	public static CdsOutputWrapper getCdsOutputWrapper(CdsOutput cdsOutput) {
		if (cdsOutput == null) {
			return new CdsOutputWrapper();
		} else {
			return new CdsOutputWrapper(cdsOutput);
		}
	}

	public ObservationResult addImmunityObservationResult(
			Date observationEventTime, String focus, String value,
			String interpretation) throws CdsException {
		return addImmunityObservationResult(this.getCdsObject().getVmrOutput(),
				observationEventTime, focus, value, interpretation);
	}

	public void addObservationResult(ObservationResult observationResult)
			throws CdsException {
		addObservationResult(this.getCdsObject().getVmrOutput(),
				observationResult);
	}

	public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
			String substanceCode, String administrationTimeInterval,
			String immId, SubstanceAdministrationEvent[] components)
			throws CdsException {

		SubstanceAdministrationEvent substanceAdministrationEvent = addSubstanceAdministrationEvent(
				this.getCdsObject().getVmrOutput(), substanceCode,
				administrationTimeInterval, immId);
		List<RelatedClinicalStatement> relatedClinicalStatements = substanceAdministrationEvent
				.getRelatedClinicalStatements();

		for (SubstanceAdministrationEvent sae : components) {
			RelatedClinicalStatement relatedClinicalStatement = getRelatedClinicalStatement("PERT");
			relatedClinicalStatement.setSubstanceAdministrationEvent(sae);
			relatedClinicalStatements.add(relatedClinicalStatement);
		}

		return substanceAdministrationEvent;
	}

	public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
			String substanceCode, Date administrationTimeIntervalDate,
			String immId, SubstanceAdministrationEvent[] components)
			throws CdsException {
		return addSubstanceAdministrationEvent(substanceCode,
				DateUtils.getISODateFormat(administrationTimeIntervalDate),
				immId, components);
	}

	public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
			String vaccineGroup, String substanceCode,
			String administrationTimeInterval, String focus, String value,
			String interpretation) throws CdsException {

		SubstanceAdministrationProposal substanceAdministrationProposal = addSubstanceAdministrationProposal(
				this.getCdsObject().getVmrOutput(), vaccineGroup,
				substanceCode, administrationTimeInterval);

		substanceAdministrationProposal = addObservationResult(
				substanceAdministrationProposal, focus, value,
				new String[] { interpretation });
		return substanceAdministrationProposal;
	}

	public SubstanceAdministrationProposal addSubstanceAdministrationProposal(
			String vaccineGroup, String substanceCode,
			Date administrationTimeIntervalDate, String focus, String value,
			String interpretation) throws CdsException {
		return addSubstanceAdministrationProposal(vaccineGroup, substanceCode,
				DateUtils.getISODateFormat(administrationTimeIntervalDate),
				focus, value, interpretation);
	}
}
