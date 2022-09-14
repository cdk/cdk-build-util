/*  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 2005-2007  Miguel Rojas <miguelrojas@users.sf.net>
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.cdk.tools.bodr;

import org.openscience.cdk.exception.CDKException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Used to store and return data of a particular chemicalElement. As this class is a
 * singleton class, one gets an instance with: 
 * <pre>
 *   ElementPTFactory efac = ElementPTFactory.getInstance();
 * </pre>
 *
 * @author     	   Miguel Rojas
 * @cdk.created    May 8, 2005
 * @cdk.module     core
 * @cdk.githash
 */
class ElementPTFactory
{

    private static ElementPTFactory efac     = null;
    private        List<PeriodicTableElement>                               elements = null;
    private        boolean                                                  debug    = false;

    /**
     * Private constructor for the ElementPTFactory object.
     *
     *@exception java.io.IOException  A problem with reading the chemicalElements.xml file
     */
    private ElementPTFactory() throws IOException {

        InputStream ins = null;
        String errorMessage = "There was a problem getting org.openscience.cdk." +
                "config.chemicalElements.xml as a stream";
        try {
            ins = this.getClass().getResourceAsStream("chemicalElements.xml");
        } catch (Exception exception) {
            throw new IOException(errorMessage);
        }
        if (ins == null) {
            throw new IOException(errorMessage);
        }
        ElementPTReader reader = new ElementPTReader(new InputStreamReader(ins));
        elements = reader.readElements();

    }

    /**
     *  Returns an ElementPTFactory instance.
     *
     *@return The instance value
     *@exception java.io.IOException             Description of the Exception
     */
    public static ElementPTFactory getInstance() throws IOException {
        if (efac == null) {
            efac = new ElementPTFactory();
        }
        return efac;
    }


    /**
     *  Returns the number of elements defined by this class.
     *
     *@return The size value
     */
    public int getSize() {
        return elements.size();
    }

    /**
     * Get all the elements loaded by the factory.
     *
     * @return  A Vector of PeriodicTableElement objects
     * @see PeriodicTableElement
     */
    public List<PeriodicTableElement> getElements() {
        return elements;
    }

    /**
	 * Returns an Element with a given element symbol.
	 *
	 *@param  symbol  An element symbol to search for
	 *@return         An array of element that matches the given element symbol
	 */
	public PeriodicTableElement getElement(String symbol) {
        for (PeriodicTableElement element : elements) {
            if (element.getSymbol().equals(symbol)) {
                try {
                    return (PeriodicTableElement) element.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
	}
    
	/**
	 *  Configures a PeriodicTableElement. Finds the correct element type
	 *  by looking at the element symbol.
	 *
	 *@param  element     The PeriodicTableElement to be configure
	 *@return             The configured PeriodicTableElement
     * @throws org.openscience.cdk.exception.CDKException if there is an error during configuration
     * (such as invalid IUPAC group number)
	 */
	public PeriodicTableElement configure(PeriodicTableElement element) throws CDKException {
		PeriodicTableElement elementInt = getElement(element.getSymbol());
		
		element.setSymbol(elementInt.getSymbol());
		element.setAtomicNumber(elementInt.getAtomicNumber());
		element.setName(elementInt.getName());
		element.setChemicalSerie(elementInt.getChemicalSerie());
		element.setPeriod(elementInt.getPeriod());
		if(elementInt.getGroup()!=null)
			element.setGroup(elementInt.getGroup());
		element.setPhase(elementInt.getPhase());
		element.setCASid(elementInt.getCASid());
		element.setPaulingEneg(elementInt.getPaulingEneg());
		return element;
	}	
	
	/**
	 *  Gets the atomic number of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The atomic number value
	 */
	public double getAtomicNumber(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getAtomicNumber();
	}

    /**
	 *  Gets the name of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The name value of this element
	 */
	public String getName(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getName();
	}

    /**
	 *  Gets the chemical serie of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The chemical serie value of this element
	 */
	public String getChemicalSerie(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getChemicalSerie();
	}
    
    /**
	 *  Gets the period of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The period value of this element
	 */
	public int getPeriod(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getPeriod();
	}
    
    /**
	 *  Gets the group of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The group value of this element
	 */
	public int getGroup(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getGroup();
	}

    /**
	 *  Gets the phase of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The phase value of this element
	 */
	public String getPhase(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getPhase();
	}

    /**
	 *  Gets the CAS id of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The CASE id value of this element
	 */
	public String getCASid(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getCASid();
	}

    /**
	 *  Gets the Vdw radios of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The Vdw radio value of this element
	 */
	public double getVdwRadius(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getVdwRadius();
	}

    /**
	 *  Gets the covalent radios of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The covalent radio value of this element
	 */
	public double getCovalentRadius(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getCovalentRadius();
	}
    /**
	 *  Gets the Pauling Electronegativity radios of this element in the periodic table.
	 *
	 * @param  element                     The PeriodicTableElement object
	 * @return                             The Pauling Electronegativity value of this element
	 */
	public double getPaulingEneg(PeriodicTableElement element){
    	PeriodicTableElement elementInt = getElement(element.getSymbol());
		return elementInt.getPaulingEneg();
	}
}

