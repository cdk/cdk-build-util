/* $Revision: 7973 $ $Author: egonw $ $Date: 2007-02-19 13:16:03 +0100 (Mon, 19 Feb 2007) $
 *
 * Copyright (C) 2007  Egon Willighagen <egonw@users.sf.net>
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
import com.sun.source.util.DocTrees;
import com.sun.source.util.TreePath;
import jdk.javadoc.doclet.Doclet;
import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.Taglet;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Source for the cdk.githash JavaDoc tag. When a class is tagged with this
 * tag, the JavaDoc will contain a link to the source code in the Git repository.
 *
 * <p>The syntax must be as follows:
 * <pre>
 *   @cdk.githash
 * </pre>
 */
public class CDKGitTaglet implements Taglet {

  private static final String NAME = "cdk.githash";
  private final static Pattern pattern = Pattern.compile("([-_A-Za-z0-9]+/[-_A-Za-z0-9]+/src/main/.+.java)$");
  private final String BRANCH = "main";
  private DocTrees docTrees;

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

  @Override
  public String toString(List<? extends DocTree> tags, Element element) {
    return toString(tags.toArray(new DocTree[0]), element);
  }

  public String toString(DocTree tag, Element e) {
    return "<DT><B>Source code: </B><DD>"
           + expand(tag, e) + "</DD>\n";
  }

  public String toString(DocTree[] tags, Element e) {
    if (tags.length == 0) {
      return null;
    } else {
      return toString(tags[0], e);
    }
  }

  @Override
  public void init(DocletEnvironment env, Doclet doclet) {
    Taglet.super.init(env, doclet);
    docTrees = env.getDocTrees();
  }

  private String expand(DocTree tag, Element e) {


    // see https://openjdk.org/groups/compiler/using-new-doclet.html
    TreePath path = docTrees.getPath(e);
    if (path != null) {
      String pathAndFile = path.getCompilationUnit().getSourceFile().getName().replaceAll("\\\\", "/");
      Matcher matcher = pattern.matcher(pathAndFile);
      if (matcher.find()) {
        String url = "https://github.com/cdk/cdk/tree/" + BRANCH + "/" +
                     matcher.group(1);
        return "<a href=\"" + url + "\" target=\"_blank\">" + BRANCH + "</a>";
      } else {
        return "<b>Could not find path to source code: " + pathAndFile + "</b>";
      }
    }
    return "<b>Could not find path to source code</b>";
  }
}
