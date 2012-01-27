package ice.enumeration;

/**
 *
 * @author HLN Consulting, LLC
 */
public enum CodeSystemOid {

    ADMINISTERED_SUBSTANCE("2.16.840.1.113883.12.292"),
    EVALUATED_REASON("2.16.840.1.113883.3.795.12.100.3"),
    GENDER("2.16.840.1.113883.5.1"),
    GENERAL_PURPOSE("2.16.840.1.113883.6.5"),
    IMMUNITY_FOCUS("2.16.840.1.113883.3.795.12.100.7"),
    IMMUNITY_INTERPRETATION("2.16.840.1.113883.3.795.12.100.9"),
    IMMUNITY_VALUE("2.16.840.1.113883.3.795.12.100.8"),
    LANG("1.2.3"),
    RECOMMENDATION("2.16.840.1.113883.3.795.12.100.5"),
    RECOMMENDED_REASON("2.16.840.1.113883.3.795.12.100.6"),
    RECOMMENDED_GROUP_FOCUS("2.16.840.1.113883.3.795.12.100.4"),
    TARGET_RELATIONSHIP_TO_SOURCE("2.16.840.1.113883.5.1002"),
    VALIDATION("2.16.840.1.113883.3.795.12.100.2"),
    VALIDITY_FOCUS("2.16.840.1.113883.3.795.12.100.1"),
    CIR_IMMUNIZATION_ID("2.16.840.1.113883.3.795.12.100.10"),
    CIR_PATIENT_ID("2.16.840.1.113883.3.795.12.100.11");
    private final String oid;

    CodeSystemOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

    public static CodeSystemOid valueOfOid(String oid) {
        CodeSystemOid result = null;
        for (CodeSystemOid item : CodeSystemOid.values()) {
            if (item.getOid() != null && item.getOid().equals(oid)) {
                result = item;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("No enum const class " + EvaluationValidityType.class.getCanonicalName() + " for " + oid);
        }
        return result;
    }
}
