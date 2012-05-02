<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<ns1:OpenCdsConceptMappingSpecificationFile xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:schemaLocation="org.opencds OpenCdsConceptMappingSpecificationFile.xsd"
 xmlns:ns1="org.opencds">
    <xsl:copy-of select="OpenCdsConceptMappingSpecificationFile/specificationNotes"/>
    <xsl:copy-of select="OpenCdsConceptMappingSpecificationFile/openCdsConcept"/>
    <xsl:copy-of select="OpenCdsConceptMappingSpecificationFile/conceptDeterminationMethod"/>
    <xsl:copy-of select="OpenCdsConceptMappingSpecificationFile/membersForCodeSystem"/>
</ns1:OpenCdsConceptMappingSpecificationFile>
    </xsl:template>
</xsl:stylesheet>