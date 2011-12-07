package ice.dto;

import ice.base.BaseCdsObject;
import ice.exception.IceException;
import org.opencds.CdsInput;
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
        return this.addSubstanceAdministrationEvent(this.getCdsObject().getVmrInput(), substanceCode, administrationTimeInterval);
    }
}
