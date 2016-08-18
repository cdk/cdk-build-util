# run in the root CDK folder

# first, create the dependencies with Maven's dependency:tree
mvn -DoutputFile=dependencies.dot -DoutputType=dot -Dincludes=org.openscience.cdk dependency:tree

# merge all dependencies.dot files created by Maven and remove
# a few high or app level modules
echo "digraph {" > cdkDeps.dot
find . -name "dependencies.dot" | xargs cat | grep -v digraph | \
  grep -v "}" | sed s/org.openscience.cdk://g | sed s/:jar:1.5.13-SNAPSHOT//g | \
  grep -v "test" | sed s/:compile//g | grep -v "cdk-bundle" | \
  grep -v "cdk-builder3dtools" | grep -v "cdk-legacy" | grep -v "cdk-depict" | \
  grep -v "cdk-smsd" | \
  sort | uniq >> cdkDeps.dot
echo "}" >> cdkDeps.dot

# simplify the graph by removing transient dependencies
tred cdkDeps.dot > cdkDeps-2.dot

# create an PNG image
dot -Tpng cdkDeps-2.dot -o cdkDeps.png
