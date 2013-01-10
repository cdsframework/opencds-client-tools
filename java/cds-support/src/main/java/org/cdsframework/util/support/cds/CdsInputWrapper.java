package org.cdsframework.util.support.cds;

import java.util.Date;
import org.cdsframework.base.BaseCdsObject;
import org.cdsframework.exceptions.CdsException;
import org.cdsframework.util.DateUtils;
import org.opencds.support.CdsInput;
import org.opencds.support.ObservationResult;
import org.opencds.support.SubstanceAdministrationEvent;

/**
 * 
 * @author HLN Consulting, LLC
 */
public class CdsInputWrapper extends BaseCdsObject<CdsInput> {

	public CdsInputWrapper() {
		super(CdsInputWrapper.class, CdsInput.class);
		logger.debug("no arg constructor called");
	}

	public CdsInputWrapper(CdsInput cdsInput) {
		super(CdsInputWrapper.class, CdsInput.class, cdsInput);
		logger.debug("CdsInput arg constructor called: " + cdsInput);
	}

	public static CdsInputWrapper getCdsInputWrapper() {
		return new CdsInputWrapper();
	}

	public static CdsInputWrapper getCdsInputWrapper(CdsInput cdsInput) {
		if (cdsInput == null) {
			return new CdsInputWrapper();
		} else {
			return new CdsInputWrapper(cdsInput);
		}
	}

	public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
			String substanceCode, String administrationTimeInterval,
			String immId) throws CdsException {
		return addSubstanceAdministrationEvent(this.getCdsObject()
				.getVmrInput(), substanceCode, administrationTimeInterval,
				immId);
	}

	public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
			String substanceCode, Date administrationTimeIntervalDate,
			String immId) throws CdsException {
		return addSubstanceAdministrationEvent(substanceCode,
				DateUtils.getISODateFormat(administrationTimeIntervalDate),
				immId);
	}

	public ObservationResult addImmunityObservationResult(
			Date observationEventTime, String focus, String value,
			String interpretation) throws CdsException {
		return addImmunityObservationResult(this.getCdsObject().getVmrInput(),
				observationEventTime, focus, value, interpretation);
	}

	public void addObservationResult(ObservationResult observationResult)
			throws CdsException {
		addObservationResult(this.getCdsObject().getVmrInput(),
				observationResult);
	}
}
