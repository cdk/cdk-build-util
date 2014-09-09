package org.openscience.cdk.layout;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IStereoElement;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.ringsearch.RingSearch;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import javax.vecmath.Point2d;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author John May
 */
public class RingTemplateExtractor {

    private Multimap<String, IAtomContainer> anonymous = HashMultimap.create();
    private Multimap<String, IAtomContainer> skeltons  = HashMultimap.create();
    private Multimap<String, IAtomContainer> library   = HashMultimap.create();

    SmilesGenerator smigen = SmilesGenerator.unique();

    public RingTemplateExtractor() {
    }

    void add(IAtomContainer container) {

        // remove explicit hydrogens and all stereochemistry
        AtomContainerManipulator.suppressHydrogens(container);
        container.setStereoElements(new ArrayList<IStereoElement>());

        RingSearch ringSearch = new RingSearch(container);

        List<IAtomContainer> fused = ringSearch.fusedRingFragments();
        List<IAtomContainer> isolated = ringSearch.isolatedRingFragments();

        // only use compounds with a single ring system
        if (fused.size() == 1 && isolated.isEmpty()) {
            add(fused.get(0), container);
        }
        else if (isolated.size() == 1 && fused.isEmpty()) {
            add(isolated.get(0), container);
        }
    }

    void writeSDfile(File file) throws CDKException, IOException {
        final SDFWriter sdfw = new SDFWriter(new FileWriter(file));

        List<Map.Entry<String, Collection<IAtomContainer>>> list = 
                new ArrayList<Map.Entry<String, Collection<IAtomContainer>>>(library.asMap().entrySet());
        
        // sort by frequency
        list.sort(new Comparator<Map.Entry<String, Collection<IAtomContainer>>>() {
            @Override public int compare(Map.Entry<String, Collection<IAtomContainer>> o1, Map.Entry<String, Collection<IAtomContainer>> o2) {
                return o2.getValue().size() - o1.getValue().size();
            }
        });         
        
        for (Map.Entry<String, Collection<IAtomContainer>> entry : list) {
            
            // only one occurance of this system don't write to the output
            if (entry.getValue().size() < 2)
                continue;
            
            String key = entry.getKey();
            if (anonymous.containsKey(key)) {
                sdfw.write(anonymous.get(key).iterator().next());
            } else if (skeltons.containsKey(key)) {
                sdfw.write(skeltons.get(key).iterator().next());
            } else {
                sdfw.write(library.get(key).iterator().next());
            }
        }
        
        sdfw.close();
    }

    void add(IAtomContainer ringSystem, IAtomContainer container) {

        ringSystem.setProperty(CDKConstants.TITLE, container.getProperty(CDKConstants.TITLE));

        for (IBond bond : ringSystem.bonds())
            if (bond.getOrder().numeric() > 2 || bond.getOrder().numeric() < 1)
                return;

        // if whole compound is cyclic add these to the skeleton and/or anonymous maps
        // already. these will then not be pushed out once we reduce system later
        if (ringSystem.getAtomCount() == container.getAtomCount()) {
            if (allCarbon(ringSystem))
                addAnonymous(ringSystem);
            else
                addSkeleton(ringSystem);
        }

        Set<IAtom> ringAtoms = new HashSet<IAtom>();
        for (IAtom atom : ringSystem.atoms())
            ringAtoms.add(atom);

        IAtomContainer ringWithStubs = new AtomContainer();
        ringWithStubs.setProperty(CDKConstants.TITLE, container.getProperty(CDKConstants.TITLE));
        ringWithStubs.add(ringSystem);

        for (IBond bond : container.bonds()) {
            IAtom atom1 = bond.getAtom(0);
            IAtom atom2 = bond.getAtom(1);
            // only add when one atom is in the ring
            if (ringAtoms.contains(atom1) ^ ringAtoms.contains(atom2)) {
                ringWithStubs.addBond(bond);
                ringWithStubs.addAtom(atom1);
                ringWithStubs.addAtom(atom2);
            }
        }

        addSkeletonToMainLib(ringWithStubs);
        addSkeletonToMainLib(ringSystem);
        addAnonymousToMainLib(ringSystem);
    }

    void addSkeletonToMainLib(IAtomContainer container) {
        IAtomContainer skeleton = clearHydrogens(AtomContainerManipulator.skeleton(container));
        for (int i = 0; i < container.getAtomCount(); i++)
            skeleton.getAtom(i).setPoint2d(new Point2d(container.getAtom(i).getPoint2d()));
        skeleton.setProperty(CDKConstants.TITLE, container.getProperty(CDKConstants.TITLE));
        String skeletonKey = toCanSmi(skeleton);
        library.put(skeletonKey, skeleton);
    }
    
    void addAnonymousToMainLib(IAtomContainer container) {
        IAtomContainer skeleton = clearHydrogens(AtomContainerManipulator.skeleton(container));
        for (int i = 0; i < container.getAtomCount(); i++)
            skeleton.getAtom(i).setPoint2d(new Point2d(container.getAtom(i).getPoint2d()));
        skeleton.setProperty(CDKConstants.TITLE, container.getProperty(CDKConstants.TITLE));
        String skeletonKey = toCanSmi(skeleton);
        library.put(skeletonKey, skeleton);
    }

    void addSkeleton(IAtomContainer container) {
        IAtomContainer skeleton = clearHydrogens(AtomContainerManipulator.skeleton(container));
        for (int i = 0; i < container.getAtomCount(); i++)
            skeleton.getAtom(i).setPoint2d(new Point2d(container.getAtom(i).getPoint2d()));
        skeleton.setProperty(CDKConstants.TITLE, container.getProperty(CDKConstants.TITLE));
        String skeletonKey = toCanSmi(skeleton);
        if (!skeltons.containsKey(skeletonKey))
            skeltons.put(skeletonKey, skeleton);
    }

    void addAnonymous(IAtomContainer container) {
        IAtomContainer anonymous = clearHydrogens(AtomContainerManipulator.anonymise(container));
        for (int i = 0; i < container.getAtomCount(); i++)
            anonymous.getAtom(i).setPoint2d(new Point2d(container.getAtom(i).getPoint2d()));
        anonymous.setProperty(CDKConstants.TITLE, container.getProperty(CDKConstants.TITLE));
        String anonymousKey = toCanSmi(anonymous);
        if (!this.anonymous.containsKey(anonymousKey))
            this.anonymous.put(anonymousKey, anonymous);
    }

    String toCanSmi(IAtomContainer container) {
        try {
            return smigen.create(container);
        } catch (CDKException e) {
            return null;
        }
    }

    static IAtomContainer clearHydrogens(IAtomContainer container) {
        for (IAtom atom : container.atoms())
            atom.setImplicitHydrogenCount(0);
        return container;
    }

    static boolean allCarbon(IAtomContainer container) {
        for (IAtom atom : container.atoms())
            if (!"C".equals(atom.getSymbol()))
                return false;
        return true;
    }
}
