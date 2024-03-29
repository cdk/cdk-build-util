/*
 *  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 2005-2007  Miguel Rojasch <miguelrojasch@users.sf.net>
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package net.sf.cdk.tools.bodr;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;

/**
 * <p>An PeriodicTableElement class is instantiated with at least the atom symbol:
 * <pre>
 *   PeriodicTableElement pte = new PeriodicTableElement("C");
 * </pre>
 *
 * <p>This class is not the same than IElement class. Once instantiated all field not filled by passing parameters
 * to the constructor are null. PeriodicTableElement can be configured by using
 * the PeriodicTableElement.configure() method:
 * <pre>
 *   ElementPTFactory eptf = ElementPTFactory.getInstance(a.getNewBuilder());
 *   ElementPTFactory.configure(pte);
 * </pre>
 *
 * <p>More examples about using this class can be found in the
 * Junit test for this class.
 *
 * @author        Miguel Rojas
 * @cdk.created   May 8, 2005
 * @cdk.keyword   element
 * @cdk.module    core
 * @cdk.githash
 */
class PeriodicTableElement implements Cloneable
{

    /**
     * Determines if a de-serialized object is compatible with this class.
     *
     * This value must only be changed if and only if the new version
     * of this class is incompatible with the old version. See Sun docs
     * for <a href=http://java.sun.com/products/jdk/1.1/docs/guide
     * /serialization/spec/version.doc.html>details</a>.
     */
    private static final long serialVersionUID = -2508810950266128526L;

    /**
     * The name for this element.
     */
    protected String name;

    /** The chemical series for this element. 
     *  A chemical series is a group of chemical elements whose physical and 
     *    chemical characteristics vary progressively from one end of the series 
     *    to another.
     *  Chemical series were discovered before the creation of the periodic 
     *    table of the chemical elements, which was created to try to organize 
     *    the elements according to their chemical properties. 
     */
    protected String chemicalSerie;

    /** The period which this element belong. 
     *  In the periodic table of the elements, a period is a row of the table.
     */
    protected Integer period = (Integer) CDKConstants.UNSET;

    /** The group which this element belong.
     *  In the periodic table of the elements, a period is a row of the table.
     *  the elements in a same group have similar configurations of the outermost 
     *    electron shells of their atoms
     */
    protected Integer group = (Integer) CDKConstants.UNSET;

    /** The phase which this element find.
     *   In the physical sciences, a phase is a set of states of a macroscopic 
     *    physical system that have relatively uniform chemical composition 
     *    and physical properties.
     *   Most familiar examples of phases are solids, liquids, and gases
     */
    protected String phase;

    /**
     * The CAS (Chemical Abstracts Service) number which this element has.
     */
    protected String casId;

    /**
     * The Van der Waals radius of the element.
     */
    protected Double vdwRadius = (Double) CDKConstants.UNSET;

    /**
     * The covalent radius of the element.
     */
    protected Double covalentRadius = (Double) CDKConstants.UNSET;

    /**
     * The Pauling electronegativity of the element.
     */
    protected Double paulingEneg = (Double) CDKConstants.UNSET;

    /**
     * The atomic number of the element.
     */
    private Integer atomicNumber = (Integer) CDKConstants.UNSET;

    /**
     * Symbol for the element.
     */
    private String symbol;

    /**
     *  Constructor for the PeriodicTableElement object.
     *
     * @param symbol The symbol of the element
     */
    public PeriodicTableElement(String symbol) {
        this.symbol = symbol;
    }

    /**
     *  Constructor for the PeriodicTableElement object.
     *
     * @param symbol The symbol of the element
     */
    public PeriodicTableElement(String symbol, Integer atomicNumber) {
        this.symbol = symbol;
        this.atomicNumber = atomicNumber;
    }

    /**
     * Get this elements symbol.
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Set the symbol of the element.
     *
     * @param symbol the symbol
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the name of this element.
     *
     * @return The name of this element. Null if unset.
     *
     * @see    #setName
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this element.
     *
     * @param name The name to be assigned to this element
     *
     * @see    #getName
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the chemical series of this element.
     *
     * @return The chemical series of this element. Null if unset.
     *
     * @see    #setChemicalSerie
     */
    public String getChemicalSerie() {
        return this.chemicalSerie;
    }

    /**
     * Sets the chemical series of this element.
     *
     * @param chemicalSerie The chemical series to be assigned to this element
     *
     * @see    #getChemicalSerie
     */
    public void setChemicalSerie(String chemicalSerie) {
        this.chemicalSerie = chemicalSerie;
    }

    /**
     * Returns the period which this element belongs.
     *
     * @return The period of this element. Null if unset.
     *
     * @see    #setPeriod
     */
    public Integer getPeriod() {
        return this.period;
    }

    /**
     * Sets the chemical series of this element.
	*
	* @param period The period to be assigned to this element
	*
	* @see    #getPeriod
	*/
    public void setPeriod(Integer period)
	{
		this.period = period;
	}
	
	/**
	* Returns the Group which this element belongs.
	* 
	* @return The group of this element. Null if unset.
	*
	* @see    #setGroup
	*/
    public Integer getGroup()
	{
		return this.group;
	}

    /**
     * Sets the group, which this element belongs.
     *
     * @param group The group to be assigned to this atom
     * @throws org.openscience.cdk.exception.CDKException
     *          if an invalid IUPAC group number is specified.
     * @see #getGroup
     */
    public void setGroup(Integer group) throws CDKException {
        if (group < 1 || group > 18) throw new CDKException("Invalid group number specified. Must be between 1 and 18");
        this.group = group;
    }
	
	/**
	* Returns the phase which this element find.
	* 
	* @return The phase of this element. Null if unset.
	*
	* @see    #setPhase
	*/
    public String getPhase()
	{
		return this.phase;
	}

	/**
	* Sets the phase, which this element finds.
	*
	* @param phase The phase to be assigned to this element
	*
	* @see    #getGroup
	* @see #getPhase
	*/
    public void setPhase(String  phase)
	{
		this.phase = phase;
	}
	
	/**
	* Returns the CAS (Chemical Abstracts Service), which this element has.
	* 
	* @return The CAS of this element. Null if unset.
	*
	* @see    #setCASid
	*/
    public String getCASid()
	{
		return this.casId;
	}
	
	/**
	* Sets the CAS (Chemical Abstracts Service), which this element has.
	*
	* @param casId The CAS number to be assigned to this element
	*
	* @see    #getCASid
	*/
    public void setCASid(String  casId)
	{
		this.casId = casId;
	}

    /**
     * Get the VdW radius for this element.
     *
     * @return The VdW radius, or null if it is unavailable
     */
    public Double getVdwRadius() {
        return vdwRadius;
    }

    /**
     * Set the VdW radius of this element.
     *
     * @param vdwRadius  The VdW radius
     */
    public void setVdwRadius(Double vdwRadius) {
        this.vdwRadius = vdwRadius;
    }

    /**
     * Get the covalent radius.
     *
     * @return the covalent radius, or null if it is unavailable
     */
    public Double getCovalentRadius() {
        return covalentRadius;
    }

    /**
     * Set the covalent radius.
     *
     * @param covalentRadius the covalent radius
     */
    public void setCovalentRadius(Double covalentRadius) {
        this.covalentRadius = covalentRadius;
    }

    /**
     * Get the Pauling electronegativity of this element.
     *
     * @return   The electronegativity, null if not available for the element
     */
    public Double getPaulingEneg() {
        return paulingEneg;
    }

    /**
     * Set the Pauling electronegativity for this element.
     *
     * @param paulingEneg The electronegativity
     */
    public void setPaulingEneg(Double paulingEneg) {
        this.paulingEneg = paulingEneg;
    }

    /**
     * Clones this element object.
     *
     * @return The cloned object
     */
    public Object clone() throws CloneNotSupportedException {
        PeriodicTableElement clone = null;
        try {
            clone = (PeriodicTableElement) super.clone();
            clone.setSymbol(symbol);
            clone.setAtomicNumber(atomicNumber);
            clone.setChemicalSerie(chemicalSerie);
            clone.setCASid(casId);
            clone.setCovalentRadius(covalentRadius);
            clone.setGroup(group);
            clone.setName(name);
            clone.setPaulingEneg(paulingEneg);
            clone.setPeriod(period);
            clone.setPhase(phase);
            clone.setVdwRadius(vdwRadius);            
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return clone;
	 }

    /**
     * Get the atomic number for this element.
     *
     * @return the atomic number
     */
    public Integer getAtomicNumber() {
        return atomicNumber;
    }

    /**
     * Set the atomic number for this element.
     *
     * @param atomicNumber the atomic number
     */
    public void setAtomicNumber(Integer atomicNumber) {
        this.atomicNumber = atomicNumber;
    }

    /**
     *
     *@return resultString  String
     */
    public String toString() {
		StringBuffer resultString = new StringBuffer(128);
		resultString.append("PeriodicTableElement(");

		resultString.append(getSymbol());
		resultString.append(", AN:"); resultString.append(getAtomicNumber());
		resultString.append(", N:"); resultString.append(getName());
		resultString.append(", CS:"); resultString.append(getChemicalSerie());
		resultString.append(", P:"); resultString.append(getPeriod());
		resultString.append(", G:"); resultString.append(getGroup());
		resultString.append(", Ph:"); resultString.append(getPhase());
		resultString.append(", CAS:"); resultString.append(getCASid());
        resultString.append(", VdW:"); resultString.append(getVdwRadius());
        resultString.append(", Cov:"); resultString.append(getCovalentRadius());
        resultString.append(", Eneg:"); resultString.append(getPaulingEneg());

        resultString.append(')');
		return resultString.toString();
	}
}

