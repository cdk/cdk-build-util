package org.openscience.cdk.layout;

import org.openscience.cdk.geometry.GeometryUtil;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.iterator.IteratingSDFReader;
import org.openscience.cdk.isomorphism.matchers.IQueryAtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;

import javax.vecmath.Point2d;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Given an SDfile create an optimised SMILES + coordinates file. The input file should be the
 * output from {@link org.openscience.cdk.layout.ExtractTemplates}.
 *
 * @author John May
 * @see <a href="https://github.com/cdk/cdk/wiki/Making-a-template-library">Making a template library</a>
 */
public class SDfileToTemplateLibrary {

    static double SDG_BOND_LENGTH = 1.5;

    public static void main(String[] args) throws IOException {

        if (args.length < 2)
            throw new IllegalArgumentException("need SDfile path and output as argument.");

        if (args[0].startsWith("~"))
            args[0] = System.getProperty("user.home") + args[0].substring(1);
        if (args[1].startsWith("~"))
            args[1] = System.getProperty("user.home") + args[1].substring(1);
        
        String sdfPath = args[0];
        String libPath = args[1];

        System.out.println("Creating identity template library at '" + libPath + "'");
        System.out.println(" - input SDfile: '" + sdfPath + "'");

        IChemObjectBuilder bldr = SilentChemObjectBuilder.getInstance();

        IteratingSDFReader sdfReader = new IteratingSDFReader(new FileReader(args[0]),
                                                              bldr);

        IdentityTemplateLibrary lib = IdentityTemplateLibrary.empty();

        int cnt = 0;

        SDfile:
        while (sdfReader.hasNext()) {
            cnt++;

            IAtomContainer container = sdfReader.next();

            if (container instanceof IQueryAtomContainer)
                continue;

            for (IAtom atom : container.atoms()) {
                if (atom.getPoint2d() == null)
                    continue SDfile;
            }

            rescale(container);
            center(container);

            for (IAtom atom : container.atoms())
                atom.setImplicitHydrogenCount(0);

            lib.add(container);
        }

        sdfReader.close();

        lib.store(new FileOutputStream(libPath));
    }

    static void center(IAtomContainer container) {
        Point2d center = GeometryUtil.get2DCenter(container);
        GeometryUtil.translate2D(container,
                                 -center.x,
                                 -center.y);
    }

    static void rescale(IAtomContainer container) {
        double bondLength = GeometryUtil.getBondLengthMedian(container);

        double scale = SDG_BOND_LENGTH / bondLength;
        GeometryUtil.scaleMolecule(container, scale);
    }

}
