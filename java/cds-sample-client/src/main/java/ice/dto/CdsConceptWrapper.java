package ice.dto;

import ice.dto.support.CdsObjectAssist;
import ice.exception.IceException;
import ice.util.Constants;
import java.util.List;
import org.opencds.CD;
import org.opencds.OpenCdsConceptMappingSpecificationFile;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CdsConceptWrapper {

    private OpenCdsConceptMappingSpecificationFile cdsConceptMap;

    public CdsConceptWrapper(String fileName) throws IceException {
        this.cdsConceptMap = CdsObjectAssist.cdsObjectFromFile(fileName, Constants.CDS_CONCEPT_XSLT, OpenCdsConceptMappingSpecificationFile.class);
    }

    public CdsConceptWrapper(OpenCdsConceptMappingSpecificationFile cdsConceptMap) {
        this.cdsConceptMap = cdsConceptMap;
    }

    public OpenCdsConceptMappingSpecificationFile getCdsConceptMap() {
        return cdsConceptMap;
    }

    public String getCodeSystem() {
        if (cdsConceptMap == null) {
            return null;
        }
        if (cdsConceptMap.getMembersForCodeSystem() == null) {
            return null;
        }
        return cdsConceptMap.getMembersForCodeSystem().getCodeSystem();
    }

    public String getCodeSystemName() {
        if (cdsConceptMap == null) {
            return null;
        }
        if (cdsConceptMap.getMembersForCodeSystem() == null) {
            return null;
        }
        return cdsConceptMap.getMembersForCodeSystem().getCodeSystemName();
    }


    public List<CD> getCodeSystemMemberCodes() {
        if (cdsConceptMap == null) {
            return null;
        }
        if (cdsConceptMap.getMembersForCodeSystem() == null) {
            return null;
        }
        if (cdsConceptMap.getMembersForCodeSystem().getCDS() == null) {
            return null;
        }
        return cdsConceptMap.getMembersForCodeSystem().getCDS();
    }

}
