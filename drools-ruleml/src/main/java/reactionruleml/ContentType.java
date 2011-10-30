//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.02 at 03:31:08 PM MEZ 
//


package reactionruleml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for content.type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="content.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.ruleml.org/1.0/xsd}content.content"/>
 *       &lt;attGroup ref="{http://www.ruleml.org/1.0/xsd}content.attlist"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "content.type", propOrder = {
    "rulebase",
    "atom",
    "and",
    "or",
    "entails",
    "exists",
    "equal",
    "naf",
    "neg",
    "ind",
    "var",
    "expr",
    "plex"
})
public class ContentType {

    @XmlElement(name = "Rulebase")
    protected RulebaseType rulebase;
    @XmlElement(name = "Atom")
    protected AtomType atom;
    @XmlElement(name = "And")
    protected AndQueryType and;
    @XmlElement(name = "Or")
    protected OrQueryType or;
    @XmlElement(name = "Entails")
    protected EntailsType entails;
    @XmlElement(name = "Exists")
    protected ExistsType exists;
    @XmlElement(name = "Equal")
    protected EqualType equal;
    @XmlElement(name = "Naf")
    protected NafType naf;
    @XmlElement(name = "Neg")
    protected NegType neg;
    @XmlElement(name = "Ind")
    protected IndType ind;
    @XmlElement(name = "Var")
    protected VarType var;
    @XmlElement(name = "Expr")
    protected ExprType expr;
    @XmlElement(name = "Plex")
    protected PlexType plex;

    /**
     * Gets the value of the rulebase property.
     * 
     * @return
     *     possible object is
     *     {@link RulebaseType }
     *     
     */
    public RulebaseType getRulebase() {
        return rulebase;
    }

    /**
     * Sets the value of the rulebase property.
     * 
     * @param value
     *     allowed object is
     *     {@link RulebaseType }
     *     
     */
    public void setRulebase(RulebaseType value) {
        this.rulebase = value;
    }

    /**
     * Gets the value of the atom property.
     * 
     * @return
     *     possible object is
     *     {@link AtomType }
     *     
     */
    public AtomType getAtom() {
        return atom;
    }

    /**
     * Sets the value of the atom property.
     * 
     * @param value
     *     allowed object is
     *     {@link AtomType }
     *     
     */
    public void setAtom(AtomType value) {
        this.atom = value;
    }

    /**
     * Gets the value of the and property.
     * 
     * @return
     *     possible object is
     *     {@link AndQueryType }
     *     
     */
    public AndQueryType getAnd() {
        return and;
    }

    /**
     * Sets the value of the and property.
     * 
     * @param value
     *     allowed object is
     *     {@link AndQueryType }
     *     
     */
    public void setAnd(AndQueryType value) {
        this.and = value;
    }

    /**
     * Gets the value of the or property.
     * 
     * @return
     *     possible object is
     *     {@link OrQueryType }
     *     
     */
    public OrQueryType getOr() {
        return or;
    }

    /**
     * Sets the value of the or property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrQueryType }
     *     
     */
    public void setOr(OrQueryType value) {
        this.or = value;
    }

    /**
     * Gets the value of the entails property.
     * 
     * @return
     *     possible object is
     *     {@link EntailsType }
     *     
     */
    public EntailsType getEntails() {
        return entails;
    }

    /**
     * Sets the value of the entails property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntailsType }
     *     
     */
    public void setEntails(EntailsType value) {
        this.entails = value;
    }

    /**
     * Gets the value of the exists property.
     * 
     * @return
     *     possible object is
     *     {@link ExistsType }
     *     
     */
    public ExistsType getExists() {
        return exists;
    }

    /**
     * Sets the value of the exists property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExistsType }
     *     
     */
    public void setExists(ExistsType value) {
        this.exists = value;
    }

    /**
     * Gets the value of the equal property.
     * 
     * @return
     *     possible object is
     *     {@link EqualType }
     *     
     */
    public EqualType getEqual() {
        return equal;
    }

    /**
     * Sets the value of the equal property.
     * 
     * @param value
     *     allowed object is
     *     {@link EqualType }
     *     
     */
    public void setEqual(EqualType value) {
        this.equal = value;
    }

    /**
     * Gets the value of the naf property.
     * 
     * @return
     *     possible object is
     *     {@link NafType }
     *     
     */
    public NafType getNaf() {
        return naf;
    }

    /**
     * Sets the value of the naf property.
     * 
     * @param value
     *     allowed object is
     *     {@link NafType }
     *     
     */
    public void setNaf(NafType value) {
        this.naf = value;
    }

    /**
     * Gets the value of the neg property.
     * 
     * @return
     *     possible object is
     *     {@link NegType }
     *     
     */
    public NegType getNeg() {
        return neg;
    }

    /**
     * Sets the value of the neg property.
     * 
     * @param value
     *     allowed object is
     *     {@link NegType }
     *     
     */
    public void setNeg(NegType value) {
        this.neg = value;
    }

    /**
     * Gets the value of the ind property.
     * 
     * @return
     *     possible object is
     *     {@link IndType }
     *     
     */
    public IndType getInd() {
        return ind;
    }

    /**
     * Sets the value of the ind property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndType }
     *     
     */
    public void setInd(IndType value) {
        this.ind = value;
    }

    /**
     * Gets the value of the var property.
     * 
     * @return
     *     possible object is
     *     {@link VarType }
     *     
     */
    public VarType getVar() {
        return var;
    }

    /**
     * Sets the value of the var property.
     * 
     * @param value
     *     allowed object is
     *     {@link VarType }
     *     
     */
    public void setVar(VarType value) {
        this.var = value;
    }

    /**
     * Gets the value of the expr property.
     * 
     * @return
     *     possible object is
     *     {@link ExprType }
     *     
     */
    public ExprType getExpr() {
        return expr;
    }

    /**
     * Sets the value of the expr property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExprType }
     *     
     */
    public void setExpr(ExprType value) {
        this.expr = value;
    }

    /**
     * Gets the value of the plex property.
     * 
     * @return
     *     possible object is
     *     {@link PlexType }
     *     
     */
    public PlexType getPlex() {
        return plex;
    }

    /**
     * Sets the value of the plex property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlexType }
     *     
     */
    public void setPlex(PlexType value) {
        this.plex = value;
    }

}
