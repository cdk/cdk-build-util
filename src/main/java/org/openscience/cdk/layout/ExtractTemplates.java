package org.openscience.cdk.layout;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Given an SDfile - extract the layout of core ring templates to a separate SDfile.
 * 
 * @author John May
 * @see <a href="https://github.com/cdk/cdk/wiki/Making-a-template-library">Making a template library</a>
 */
public class ExtractTemplates {
    
    public static void main(String[] args) throws IOException, CDKException {
    
        if (args.length < 1 || !args[0].endsWith(".sdf")) {
            System.err.println("Expected input SDF as argument.");
            return;
        }
        
        if (args[0].startsWith("~"))
            args[0] = System.getProperty("user.home") + args[0].substring(1);
        
        String sdfInPath  = args[0];
        String sdfOutPath = sdfInPath.substring(0, sdfInPath.length() - 4) + "-templates.sdf";
        
        System.out.println("Extracting ring templates to '" + sdfOutPath + "'");
        System.out.println(" - input SDfile: '" + sdfInPath + "'");

        RingTemplateExtractor extractor = new RingTemplateExtractor();

        IteratingSDFReader sdfReader = new IteratingSDFReader(new FileReader(sdfInPath),
                                                              SilentChemObjectBuilder.getInstance());
        SDfile:
        while (sdfReader.hasNext()) {

            IAtomContainer container = sdfReader.next();            
            
            if (container instanceof IQueryAtomContainer)
                continue;

            for (IAtom atom : container.atoms()) {
                if (atom.getImplicitHydrogenCount() == null)
                    continue SDfile;
                if (atom.getPoint2d() == null)
                    continue SDfile;
            }

            extractor.add(container);
        }
        
        extractor.writeSDfile(new File(sdfOutPath));
    }
    
}
