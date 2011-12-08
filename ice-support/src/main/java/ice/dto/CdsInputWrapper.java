package ice.dto;

import ice.base.BaseCdsObject;
import ice.exception.IceException;
import ice.util.DateUtils;
import java.util.Date;
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

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(String substanceCode, String administrationTimeInterval)
            throws IceException {
        return addSubstanceAdministrationEvent(this.getCdsObject().getVmrInput(), substanceCode, administrationTimeInterval);
    }

    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(
            String substanceCode,
            Date administrationTimeIntervalDate)
            throws IceException {
        return addSubstanceAdministrationEvent(
                substanceCode,
                DateUtils.getISODateFormat(administrationTimeIntervalDate));
    }

    public ObservationResult addImmunityObservationResult(boolean immune, int vaccineGroup)
            throws IceException {
        return addImmunityObservationResult(this.getCdsObject().getVmrInput(), immune, vaccineGroup);
    }

    public void addObservationResult(ObservationResult observationResult)
            throws IceException {
        addObservationResult(this.getCdsObject().getVmrInput(), observationResult);
    }
}
