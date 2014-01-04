/* $Revision: 9167 $ $Author: rajarshi $ $Date: 2007-10-22 01:26:11 +0200 (Mon, 22 Oct 2007) $
 *
 * Copyright (C) 2008  Rajarshi Guha <rajarshi@users.sf.net>
 *               2011  Jonathan Alvarsson <jonalv@users.sf.net>
 *
 * Contact: cdk-devel@lists.sf.net
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package net.sf.cdk.tools.bodr;

import org.openscience.cdk.CDKConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Generates enum entries from BODR chemical element data. The enum values
 * are used in a CDK data structure.
 */
public class BODRElementsEnumGenerator {

    // To run this class within an IDE ensure 'cdk-core' ensure not a 'provided' dependency
    public static void main(String[] args) {
        initialize();
        List<PeriodicTableElement> es = new ArrayList<PeriodicTableElement>(elements.values());
        Collections.sort(es, new Comparator<PeriodicTableElement>() {
            @Override public int compare(PeriodicTableElement a, PeriodicTableElement b) {
                return a.getAtomicNumber() - b.getAtomicNumber();
            }
        });
        
        Map<String,Set<String>> series = new HashMap<String, Set<String>>(); 
        Map<String,Set<String>> phases = new HashMap<String, Set<String>>(); 

        for (PeriodicTableElement e : es) {
            System.out.println(toString(e));
            
            String serie = e.getChemicalSerie();
            if (serie != null && !serie.isEmpty()) {
                if (!series.containsKey(serie))
                    series.put(serie, new TreeSet<String>());
                series.get(serie).add(e.getName());
            }

            String phase = e.getPhase();
            if (phase != null && !phase.isEmpty()) {
                if (!phases.containsKey(phase))
                    phases.put(phase, new TreeSet<String>());
                phases.get(phase).add(e.getName());
            }
            
        }
        System.out.println();
        System.out.println();
        System.out.println();
        for (Map.Entry<String,Set<String>> e : series.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String value : e.getValue()) {
                if (sb.length() > 0)
                    sb.append(", ");
                sb.append(value);
            }
            System.out.println(e.getKey() + "(" + sb.toString() + "),");
        }
        System.out.println();
        System.out.println();
        for (Map.Entry<String,Set<String>> e : phases.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (String value : e.getValue()) {
                if (sb.length() > 0)
                    sb.append(", ");
                sb.append(value);
            }
            System.out.println(e.getKey() + "(" + sb.toString() + "),");
        }
        
    }
    
    private static volatile boolean isInitialized = false;
    private static volatile Map<String, PeriodicTableElement>  elements;
    private static volatile Map<Integer, PeriodicTableElement> elementsByNumber;

    private synchronized static void initialize() {
        if (isInitialized) return;

        ElementPTFactory factory;
        try {
            factory = ElementPTFactory.getInstance();
        } catch (IOException e) {
            elements = null;
            return;
        }

        elements = new HashMap<String, PeriodicTableElement>();
        elementsByNumber = new HashMap<Integer, PeriodicTableElement>();
        List<PeriodicTableElement> tmp = factory.getElements();
        for (PeriodicTableElement element : tmp) {
            elements.put(element.getSymbol(), element);
            elementsByNumber.put(element.getAtomicNumber(), element);
        }

        try {
            readVDW();
            readCovalent();
            readPEneg();
        } catch (IOException e) {
            return;
        }

        isInitialized = true;
    }

    private static void readVDW() throws IOException {
        // now read in the VdW radii
        InputStream ins = BODRElementsEnumGenerator.class.getResourceAsStream("radii-vdw.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

        for (int i = 0; i < 6; i++) reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] toks = line.split("\\s");
            int atnum = Integer.parseInt(toks[0]);
            double vdw = Double.parseDouble(toks[1]);
            PeriodicTableElement e = elementsByNumber.get(atnum);
            if (e != null) {
                String symbol = e.getSymbol();
                if (vdw == 2)
                    elements.get(symbol).setVdwRadius((Double) CDKConstants.UNSET);
                else elements.get(symbol).setVdwRadius(vdw);
            }
        }
    }

    private static void readCovalent() throws IOException {
        // now read in the covalent radi
        InputStream ins = BODRElementsEnumGenerator.class.getResourceAsStream("radii-covalent.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

        for (int i = 0; i < 5; i++) reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] toks = line.split("\\s");
            int atnum = Integer.parseInt(toks[0]);
            double vdw = Double.parseDouble(toks[1]);
            PeriodicTableElement e = elementsByNumber.get(atnum);
            if (e != null) {
                String symbol = e.getSymbol();
                elements.get(symbol).setCovalentRadius(vdw);
            }
        }
    }

    private static void readPEneg() throws IOException {
        // now read in the VdW radii
        InputStream ins = BODRElementsEnumGenerator.class.getResourceAsStream("electroneg-pauling.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(ins));

        for (int i = 0; i < 6; i++) reader.readLine();

        String line;
        while ((line = reader.readLine()) != null) {
            String[] toks = line.split("\\s");
            int atnum = Integer.parseInt(toks[0]);
            double eneg = Double.parseDouble(toks[1]);
            PeriodicTableElement e = elementsByNumber.get(atnum);
            if (e != null) {
                String symbol = e.getSymbol();
                elements.get(symbol).setPaulingEneg(eneg);
            }
        }
    }

    static String toString(PeriodicTableElement e) {
        return String.format("%20s(%3s, %-6s %s, %2s, %4s, %4s, %4s),",
                             e.getName(),
                             e.getAtomicNumber(),
                             "\"" + e.getSymbol() + "\",",
                             toString(e.getPeriod()),
                             toString(e.getGroup()),
                             toString(e.getCovalentRadius()),
                             toString(e.getVdwRadius()),
                             toString(e.getPaulingEneg()));
    }
    
    static String toString(Integer x) {
        return x == null ? "0" : x.toString();
    }

    static String toString(Double x) {
        return x == null ? "null" : String.format("%.2f", x);
    }
}
