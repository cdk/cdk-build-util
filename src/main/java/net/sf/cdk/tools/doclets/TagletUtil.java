package net.sf.cdk.tools.doclets;

import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.TextTree;
import com.sun.source.doctree.UnknownBlockTagTree;
import com.sun.source.doctree.UnknownInlineTagTree;
import com.sun.source.util.SimpleDocTreeVisitor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

final class TagletUtil {
    private TagletUtil() {}

    static String getText(DocTree dt) {
        return new SimpleDocTreeVisitor<String, Void>() {
            @Override
            public String visitUnknownBlockTag(UnknownBlockTagTree node, Void p) {
                for (DocTree dt : node.getContent()) {
                    return dt.accept(this, null);
                }
                return "";
            }

            @Override
            public String visitUnknownInlineTag(UnknownInlineTagTree node, Void p) {
                for (DocTree dt : node.getContent()) {
                    return dt.accept(this, null);
                }
                return "";
            }

            @Override
            public String visitText(TextTree node, Void p) {
                return node.getBody();
            }

            @Override
            protected String defaultAction(DocTree node, Void p) {
                return "";
            }

        }.visit(dt, null);
    }

    static String visit(Element e) {
        return new ElementVisitor<String, Void>() {

            @Override
            public String visit(Element e, Void unused) {
                System.err.println("Element: " + e);
                return null;
            }

            @Override
            public String visitPackage(PackageElement e, Void unused) {
                System.err.println("Package: " + e);
                return null;
            }

            @Override
            public String visitType(TypeElement e, Void unused) {
                System.err.println("Type: " + e);
                return null;
            }

            @Override
            public String visitVariable(VariableElement e, Void unused) {
                System.err.println("Var: " + e);
                return null;
            }

            @Override
            public String visitExecutable(ExecutableElement e, Void unused) {
                System.err.println("Exec: " + e);
                return null;
            }

            @Override
            public String visitTypeParameter(TypeParameterElement e, Void unused) {
                System.err.println("Parameter: " + e);
                return null;
            }

            @Override
            public String visitUnknown(Element e, Void unused) {
                System.err.println("Unknown: " + e);
                return null;
            }
        }.visit(e);
    }
}
