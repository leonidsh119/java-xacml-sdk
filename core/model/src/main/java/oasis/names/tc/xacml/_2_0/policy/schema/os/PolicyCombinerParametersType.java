//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.07.30 at 07:46:41 PM MESZ 
//


package oasis.names.tc.xacml._2_0.policy.schema.os;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PolicyCombinerParametersType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PolicyCombinerParametersType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:xacml:2.0:policy:schema:os}CombinerParametersType">
 *       &lt;attribute name="PolicyIdRef" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyCombinerParametersType")
public class PolicyCombinerParametersType
    extends CombinerParametersType
{

    @XmlAttribute(name = "PolicyIdRef", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String policyIdRef;

    /**
     * Gets the value of the policyIdRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyIdRef() {
        return policyIdRef;
    }

    /**
     * Sets the value of the policyIdRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyIdRef(String value) {
        this.policyIdRef = value;
    }

}
