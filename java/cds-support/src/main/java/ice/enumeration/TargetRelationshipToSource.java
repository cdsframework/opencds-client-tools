package ice.enumeration;

/**
 *
 * @author HLN Consulting, LLC
 */
public enum TargetRelationshipToSource {

    PERT("has pertinent information"),
    RSON("has reason");
    private final String label;

    TargetRelationshipToSource(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
