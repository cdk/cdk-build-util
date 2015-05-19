/* Copyright (C) 2007,2011  Egon Willighagen <egonw@users.sf.net>
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package net.sf.cdk.tools.bibtex;

import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.XPathContext;

/**
 * This is a tool that creates HTML for a subset of the entry types
 * defined in BibTeXML. It expects the document to be valid according to
 * the BibTeXML schema, or will fail horribly.
 * 
 * @author egonw
 */
public class BibTeXMLEntry {

	private Node entry;
	private XPathContext context;
	
	public BibTeXMLEntry(Node entry) {
		this.entry = entry;
		context = new XPathContext("bibtex", BibTeXMLFile.BIBTEXML_NAMESPACE);
		context.addNamespace("b", BibTeXMLFile.BIBTEXML_NAMESPACE);		
	}
	
	/**
	 * The style is undefined and just made to look nice.
	 */
	public String toHTML() {
		// b:article
		Nodes results = entry.query("./b:article", context);
		for (int i=0; i<results.size(); i++) {
			Element article = (Element)results.get(i);
			// the obligatory fields
			return formatArticle(
				getString(article, "author", "?Authors?"),
				getString(article, "title", "?Title?"),
				getString(article, "journal", "?Journal?"),
				getString(article, "year", "19??"),
				getString(article, "volume", "?"),
				getString(article, "pages", "?-?"),
				getString(article, "doi", null)
			);
		}
		// b:inbook
		results = entry.query("./b:inbook", context);
		for (int i=0; i<results.size(); i++) {
			Element inbook = (Element)results.get(i);
			// the obligatory fields
			return formatInBook(
				getString(inbook, "author", "?Authors?"),
				getString(inbook, "title", "?Title?"),
				getString(inbook, "chapter", "?chapter?"),
				getString(inbook, "year", "19??"),
				getString(inbook, "volume", "?"),
				getString(inbook, "series", "?In Series?"),
				getString(inbook, "editor", "?Editors?"),
				getString(inbook, "pages", "?-?"),
				getString(inbook, "doi", null)
			);
		}
		// b:thesis
		results = entry.query("./b:phdthesis", context);
		for (int i=0; i<results.size(); i++) {
			Element thesis = (Element)results.get(i);
			// the obligatory fields
			return formatThesis(
				getString(thesis, "author", "?Authors?"),
				getString(thesis, "title", "?Title?"),
                getString(thesis, "year", "19??"),
                getString(thesis, "adress", "?Institute?")
			);
		}
        // b:phdthesis
        results = entry.query("./b:book", context);
        for (int i=0; i<results.size(); i++) {
            Element book = (Element)results.get(i);
            // the obligatory fields
            return formatBook(
                getString(book, "author", "?Authors?"),
                getString(book, "title", "?Title?"),
                getString(book, "year", "19??"),
                getString(book, "publisher", "?Publisher?")
            );
        }
        // b:misc
        results = entry.query("./b:misc", context);
        for (int i=0; i<results.size(); i++) {
            Element misc = (Element)results.get(i);
            // the obligatory fields
            return formatMisc(
                getString(misc, "author", "?Authors?"),
                getString(misc, "title", "?Title?")
            );
        }
        // b:techreport
        results = entry.query("./b:techreport", context);
        for (int i=0; i<results.size(); i++) {
            Element techreport = (Element)results.get(i);
            // the obligatory fields
            return formatTechReport(
                getString(techreport, "author", "?Authors?"),
                getString(techreport, "title", "?Title?"),
                getString(techreport, "year", "19??"),
                getString(techreport, "institution", "?Institute?"),
                getString(techreport, "url", null)
            );
        }
		return "Unknown BibTeXML type: " + ((Element)entry).getAttributeValue("id");
	}

	private String formatInBook(String authors, String title, String chapter,
			String year, String volume, String series, String editor,
			String pages, String doi) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(authors).append(", <i>").append(title).append("</i>, ");
		buffer.append(series).append(", Ch. ").append(chapter);
		buffer.append(", <b>").append(year).append("</b>, ");
		buffer.append(volume).append(":").append(pages);
		buffer.append(", Eds. ").append(editor);
		optionallyAppendDOI(doi, buffer);
		return buffer.toString();
	}

	private void optionallyAppendDOI(String doi, StringBuffer buffer) {
		if (doi == null) return;
		buffer.append(", doi:<a href=\"http://dx.doi.org/")
		    .append(doi).append("\">").append(doi).append("</a>");
	}

	protected String formatArticle(String authors, String title, String journal, String year,
			String volume, String pages, String doi) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(authors).append(", <i>").append(title).append("</i>, ");
		buffer.append(journal).append(", <b>").append(year).append("</b>, ");
		buffer.append(volume).append(":").append(pages);
		optionallyAppendDOI(doi, buffer);
		return buffer.toString();
	}
	
	protected String formatMisc(String authors, String title) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(authors).append(", <i>").append(title).append("</i>");
		return buffer.toString();
	}
	
    protected String formatThesis(String authors, String title, String year, String institute) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(authors).append(", <i>").append(title).append("</i>, ");
        buffer.append("<b>").append(year).append("</b>, ");
        buffer.append(institute);
        return buffer.toString();
    }
    
    protected String formatBook(String authors, String title, String year, String publisher) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(authors).append(", <i>").append(title).append("</i>, ");
        buffer.append("<b>").append(year).append("</b>, ");
        buffer.append(publisher);
        return buffer.toString();
    }

    protected String formatTechReport(String authors, String title, String year, String institution, String url) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(authors).append(", <i>").append(title).append("</i>, ");
        buffer.append("<b>").append(year).append("</b>, ");
        buffer.append(institution);
        if (url != null) {
            buffer.append(", ").append("<a href=\"").append(url);
            buffer.append("\">").append(url).append("</a>");
        }
        return buffer.toString();
    }
    
	/**
	 * @param node         Parent for the child.
	 * @param childElement Localname of the child element.
	 * @param def          String to default to if no child element is found.
	 * @return             String value for the child node.
	 */
	private String getString(Node node, String childElement, String def) {
		Nodes result = node.query("./b:" + childElement, context);
		return result.size() > 0 ? ((Element)result.get(0)).getValue() : def;
	}
	
}
