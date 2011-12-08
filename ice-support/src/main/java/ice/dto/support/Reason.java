package ice.dto.support;

/**
 *
 * @author HLN Consulting, LLC
 */
public class Reason {

    private String focus;
    private String value;
    private String interpretation;

    public Reason(String focus, String value, String interpretation) {
        this.focus = focus;
        this.value = value;
        this.interpretation = interpretation;
    }

    public String getFocus() {
        return focus;
    }

    public String getInterpretation() {
        return interpretation;
    }

    public String getValue() {
        return value;
    }
}
