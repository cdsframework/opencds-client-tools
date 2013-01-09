package org.cdsframework.enumeration;

/**
 *
 * @author HLN Consulting, LLC
 */
public enum RootOid {

    CDS_INPUT("2.16.840.1.113883.3.795.11.1.1"),
    EVALUATED_PERSON("2.16.840.1.113883.3.795.11.2.1.1"),
    OBSERVATION_RESULT("2.16.840.1.113883.3.795.11.6.3.1"),
    SUBSTANCE_ADMINISTRATION_EVENT("2.16.840.1.113883.3.795.11.9.1.1"),
    SUBSTANCE_ADMINISTRATION_PROPOSAL("2.16.840.1.113883.3.795.11.9.3.1"),
    VMR("2.16.840.1.113883.3.795.11.1.1");
    private final String oid;

    RootOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

}