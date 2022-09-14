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
import jdk.javadoc.doclet.Taglet;

import javax.lang.model.element.Element;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Taglet used to indicate that the class is annotated to be <b>not</b> safe for
 * use in threaded environments. It does not have any parameters:
 * <pre>
 *   @cdk.threadnonsafe
 * </pre>
 */
public class CDKThreadNonSafeTaglet implements Taglet {

    private static final String NAME = "cdk.threadnonsafe";

    public String getName() {
        return NAME;
    }

    @Override
    public Set<Location> getAllowedLocations() {
        return EnumSet.of(Location.TYPE, Location.METHOD);
    }

    @Override
    public boolean isInlineTag() {
        return false;
    }

    @Override
    public String toString(List<? extends DocTree> tags, Element element) {
        return toString(tags.toArray(new DocTree[0]));
    }

    public String toString(DocTree tag) {
        return tag != null
            ? "<B>Thread Safe:</B> No\n"
            : "";
    }
    
    public String toString(DocTree[] tags) {
        return tags.length != 0
            ? "<B>Thread Safe:</B> No\n" :
            "";
    }

}
