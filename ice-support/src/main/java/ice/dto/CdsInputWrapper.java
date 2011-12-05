package ice.dto;

import ice.base.BaseCdsObject;
import ice.exception.IceException;
import org.opencds.AdministrableSubstance;
import org.opencds.CdsInput;
import org.opencds.EvaluatedPerson.ClinicalStatements;
import org.opencds.EvaluatedPerson.ClinicalStatements.SubstanceAdministrationEvents;
import org.opencds.IVLTS;
import org.opencds.SubstanceAdministrationEvent;
import org.opencds.SubstanceAdministrationProposal;

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

    @Override
    public SubstanceAdministrationEvent addSubstanceAdministrationEvent(String vaccineGroup, String substanceCode, String administrationTimeInterval) throws IceException {

        SubstanceAdministrationEvent substanceAdministrationEvent = getSubstanceAdministrationEvent();
        SubstanceAdministrationEvents substanceAdministrationEvents = getSubstanceAdministrationEvents(this.getCdsObject());
        substanceAdministrationEvents.getSubstanceAdministrationEvents().add(substanceAdministrationEvent);

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
        throw new UnsupportedOperationException("Operation not supported.");
    }

    private static SubstanceAdministrationEvents getSubstanceAdministrationEvents() {
        SubstanceAdministrationEvents substanceAdministrationEvents = new SubstanceAdministrationEvents();
        return substanceAdministrationEvents;
    }

    private SubstanceAdministrationEvents getSubstanceAdministrationEvents(CdsInput cdsObject) throws IceException {
        ClinicalStatements clinicalStatements = cdsObject.getVmrInput().getPatient().getClinicalStatements();
        SubstanceAdministrationEvents substanceAdministrationEvents = clinicalStatements.getSubstanceAdministrationEvents();
        if (substanceAdministrationEvents == null) {
            substanceAdministrationEvents = getSubstanceAdministrationEvents();
            clinicalStatements.setSubstanceAdministrationEvents(substanceAdministrationEvents);
        }
        return substanceAdministrationEvents;
    }
}
