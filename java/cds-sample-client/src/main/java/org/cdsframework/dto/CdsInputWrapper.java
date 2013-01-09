package org.cdsframework.dto;

import java.util.Date;

import org.cdsframework.base.BaseCdsObject;
import org.cdsframework.exception.IceException;
import org.cdsframework.util.DateUtils;
import org.opencds.CdsInput;
import org.opencds.ObservationResult;
import org.opencds.SubstanceAdministrationEvent;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsInputWrapper extends BaseCdsObject<CdsInput> {

    public CdsInputWrapper() {
        super(CdsInputWrapper.class, CdsInput.class);
    }

    public static CdsInputWrapper getCdsInputWrapper() {
        return new CdsInputWrapper();
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            String administrationTimeInterval,
            String immId)
            throws IceException {
        return addSubstanceAdministrationEvent(this.getCdsObject().getVmrInput(), substanceCode, administrationTimeInterval, immId);
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate,
            String immId)
            throws IceException {
        return addSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate),
                immId);
    }

    public ObservationResult addImmunityObservationResult(Date observationEventTime, String focus, String value, String interpretation)
            throws IceException {
        return addImmunityObservationResult(this.getCdsObject().getVmrInput(), observationEventTime, focus, value, interpretation);
    }

    public void addObservationResult(ObservationResult observationResult)
            throws IceException {
        addObservationResult(this.getCdsObject().getVmrInput(), observationResult);
    }
}
