/* Copyright (C) 2012  Egon Willighagen <egonw@users.sf.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package net.sf.cdk.tools.doclets;

import com.sun.source.doctree.DocTree;
import jdk.javadoc.doclet.Taglet;
import org.openscience.cdk.io.IChemObjectIO;
import org.openscience.cdk.io.setting.IOSetting;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Source for the cdk.iooptions JavaDoc tag. When a class is tagged with this
 * tag, the JavaDoc will an overview of the IO options.
 *
 * <p>The syntax must be as follows:
 * <pre>
 *   @cdk.iooptions
 * </pre>
 */
public class CDKIOOptionsTaglet implements Taglet {

  private static final String NAME = "cdk.iooptions";


  /**
   * @inheritDoc
   */
  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public Set<Location> getAllowedLocations() {
    return EnumSet.of(Location.TYPE);
  }

  @Override
  public boolean isInlineTag() {
    return false;
  }

  /**
   * Constructs a HTML table row (tr) for the given IO Setting. The row
   * consist of a cell for the setting name and a cell with the question
   * associated with that setting.
   *
   * @param setting instance of an IO Setting (should not be null)
   * @return the table row
   */
  private static String getRow(IOSetting setting) {
    StringBuilder row = new StringBuilder();
    row.append("<tr>");
    row.append("<td><b>").append(setting.getName()).append("</b></td>");
    row.append("<td>").append(setting.getQuestion()).append("</td>");
    row.append("<td>").append(setting.getDefaultSetting()).append("</td>");
    row.append("</tr>");
    return row.toString();
  }

  /**
   * @inheritDoc
   */
  public String toString(String name) {
    StringBuilder tableContent = new StringBuilder();
    try {

      // get the class name and try invoking the default constructor
      Class c = Class.forName(name);
      Object instance = c.newInstance();

      // ensures the tag is on an IChemObjectIO
      if (instance instanceof IChemObjectIO) {
        tableContent.append("<dt><b>IO options:</b><dd>");
        IChemObjectIO objectIO = (IChemObjectIO) instance;
        if (objectIO.getIOSettings().length == 0) return "";
        tableContent.append("<table>");
        tableContent.append("<tr><th>Name</th><th>Question</th><th>Default</th></tr>");
        for (IOSetting setting : objectIO.getIOSettings()) {
          if (setting != null) {
            tableContent.append(getRow(setting));
          } else {
            System.err.println("[IOOptionsTaglet] Null IOSetting in class: " + name);
          }
        }
        tableContent.append("</table>");
        tableContent.append("</dd></dt>");
      } else {
        System.err.println(name + " is not an instance of IChemObjectIO");
      }

    } catch (ClassNotFoundException ex) {
      System.err.println("[IOOptionsTaglet] Unable to find: " + name);
    } catch (InstantiationException ex) {
      System.err.println("[IOOptionsTaglet] Unable to create an instance of: "
                         + name + ". Does this class have a default constructor?");
    } catch (IllegalAccessException ex) {
      System.err.println("[IOOptionsTaglet] Default constructor for " + name
                         + " was not accessible from this package (i.e. private/protected)");
    }

    return tableContent.toString();

  }

  @Override
  public String toString(List<? extends DocTree> tags, Element element) {
    if (element.getKind() != ElementKind.CLASS)
      return "";
    return toString(element.toString());
  }
}
