/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2003-2007  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sf.net
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

import org.openscience.cdk.exception.CDKException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads an element list in CML2 format. An example definition is:
 * <pre>
 * <elementType id="Li">
 *     <label dictRef="cas:id">7439-93-2</label>
 *     <scalar dataType="xsd:Integer" dictRef="cdk:group">1</scalar>
 *     <scalar dataType="xsd:Integer" dictRef="cdk:period">2</scalar>
 *     <scalar dataType="xsd:String" dictRef="cdk:name">Lithium</scalar>
 *     <scalar dataType="xsd:Integer" dictRef="cdk:atomicNumber">3</scalar>
 *     <scalar dataType="xsd:String" dictRef="cdk:chemicalSerie">Alkali
 * Metals</scalar>
 *     <scalar dataType="xsd:String" dictRef="cdk:phase">Solid</scalar>
 * </elementType>
 * </pre>
 *
 * @author Miguel Rojas
 * @cdk.created May 8, 2005
 * @cdk.module core
 * @cdk.githash
 */
class ElementPTHandler extends DefaultHandler {
    private final int SCALAR_UNSET         = 0;
    private final int LABEL_CAS            = 1;
    private final int SCALAR_NAME          = 2;
    private final int SCALAR_ATOMICNUMBER  = 3;
    private final int SCALAR_CHEMICALSERIE = 4;
    private final int SCALAR_PERIOD        = 5;
    private final int SCALAR_GROUP         = 6;
    private final int SCALAR_PHASE         = 7;
    private final int SCALAR_RADCOV        = 8;
    private final int SCALAR_RADVDW        = 9;
    private final int SCALAR_PAULE         = 10;
    private int scalarType;
    private String                     currentChars;
    private List<PeriodicTableElement> elements;

    public PeriodicTableElement elementType;
    public String               currentElement;
    public String               dictRef;

    public ElementPTHandler() {
    }

    /**
     * Returns the element read from the XML file.
     *
     * @return A Vector object with all isotopes
     */
    public List<PeriodicTableElement> getElements() {
        return elements;
    }

    // SAX Parser methods

    public void startDocument() {
        elements = new ArrayList<PeriodicTableElement>();
        scalarType = SCALAR_UNSET;
        elementType = null;
    }

    public void endElement(String uri, String local, String raw) {
        if ("elementType".equals(local)) {
            elements.add(elementType);
        }
        else if ("label".equals(local)) {
            currentChars.trim();
            try {
                if (scalarType == LABEL_CAS)
                    elementType.setCASid(currentChars);
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }

        }
        else if ("scalar".equals(local)) {
            currentChars.trim();
            try {
                if (scalarType == SCALAR_NAME) {
                    elementType.setName(currentChars);
                }
                else if (scalarType == SCALAR_ATOMICNUMBER) {
                    elementType.setAtomicNumber(Integer.parseInt(currentChars));
                }
                else if (scalarType == SCALAR_CHEMICALSERIE) {
                    elementType.setChemicalSerie(currentChars);
                }
                else if (scalarType == SCALAR_PERIOD) {
                    if (!currentChars.isEmpty())
                        elementType.setPeriod(Integer.parseInt(currentChars));
                }
                else if (scalarType == SCALAR_GROUP) {
                    if (!currentChars.isEmpty())
                        elementType.setGroup(Integer.parseInt(currentChars));
                }
                else if (scalarType == SCALAR_PHASE) {
                    elementType.setPhase(currentChars);
                }
                else if (scalarType == SCALAR_RADCOV) {
                    if (!currentChars.isEmpty())
                        elementType.setCovalentRadius(Double.parseDouble(currentChars));
                }
                else if (scalarType == SCALAR_RADVDW) {
                    if (!currentChars.isEmpty())
                        elementType.setVdwRadius(Double.parseDouble(currentChars));
                }
                else if (scalarType == SCALAR_PAULE) {
                    if (!currentChars.isEmpty())
                        elementType.setPaulingEneg(Double.parseDouble(currentChars));
                }
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            } catch (CDKException e) {
                e.printStackTrace();
            }
            scalarType = SCALAR_UNSET;
        }
        currentChars = "";
    }

    public void startElement(String uri, String local,
                             String raw, Attributes atts) {
        currentChars = "";
        dictRef = "";
        if ("elementType".equals(local)) {
            for (int i = 0; i < atts.getLength(); i++) {
                if ("id".equals(atts.getQName(i))) {
                    elementType = new PeriodicTableElement(atts.getValue(i));
                }
            }
        }
        else if ("label".equals(local)) {
            for (int i = 0; i < atts.getLength(); i++) {
                if ("dictRef".equals(atts.getQName(i))) {
                    if ("cas:id".equals(atts.getValue(i))) {
                        scalarType = LABEL_CAS;
                    }
                }
            }
        }
        else if ("scalar".equals(local))
            for (int i = 0; i < atts.getLength(); i++) {
                if ("dictRef".equals(atts.getQName(i))) {
                    if ("cdk:name".equals(atts.getValue(i))) {
                        scalarType = SCALAR_NAME;
                    }
                    else if ("cdk:atomicNumber".equals(atts.getValue(i))) {
                        scalarType = SCALAR_ATOMICNUMBER;
                    }
                    else if ("cdk:name".equals(atts.getValue(i))) {
                        scalarType = SCALAR_NAME;
                    }
                    else if ("cdk:chemicalSerie".equals(atts.getValue(i))) {
                        scalarType = SCALAR_CHEMICALSERIE;
                    }
                    else if ("cdk:period".equals(atts.getValue(i))) {
                        scalarType = SCALAR_PERIOD;
                    }
                    else if ("cdk:group".equals(atts.getValue(i))) {
                        scalarType = SCALAR_GROUP;
                    }
                    else if ("cdk:phase".equals(atts.getValue(i))) {
                        scalarType = SCALAR_PHASE;
                    }
                    else if ("cdk:radiiCova".equals(atts.getValue(i))) {
                        scalarType = SCALAR_RADCOV;
                    }
                    else if ("cdk:radiiVdw".equals(atts.getValue(i))) {
                        scalarType = SCALAR_RADVDW;
                    }
                    else if ("cdk:paulingE".equals(atts.getValue(i))) {
                        scalarType = SCALAR_PAULE;
                    }

                }
            }

    }

    public void characters(char chars[], int start, int length) {
        currentChars += new String(chars, start, length);
    }

}
