package org.cdsframework.cds.testcase;

/**
 *
 * @author HLN Consulting, LLC
 */
public enum TestCasePropertyType {

    STRING("string"),
    BOOLEAN("boolean"),
    INTEGER("integer"),
    DECIMAL("decimal"),
    DATETIME("dateTime"),
    DATE("date"),
    TIME("time"),
    DURATION("duration");

    private String typeString;

    private TestCasePropertyType (String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }

}
