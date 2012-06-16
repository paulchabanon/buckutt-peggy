cp PBUY.wsdl temp.wsdl
chemin2=${1//\//\.}
chemin=${1//\//\\\/}
sed -i "s/localhost\//$chemin/g" temp.wsdl
rm -rf src/axis/
rm -rf src/temp/
mkdir src/axis
mkdir src/temp
cd src/temp/
mkdir test
java -cp ../../lib/axis.jar:../../lib/commons-discovery-0.2.jar:../../lib/javax.wsdl_1.5.1.v200806030408.jar:../../lib/jaxrpc.jar:../../lib/org.apache.commons.logging_1.0.4.v200904062259.jar:../../lib/PertelianLib2.jar:../../lib/saaj.jar org.apache.axis.wsdl.WSDL2Java -S test ../../temp.wsdl 

find -name "*.java" -exec mv {} ../axis/ \;
sed -i "s/${chemin2}PBUY_class_php/axis/g" ../axis/*.java
cd ..
rm -rf temp/
rm -f ../temp.wsdl
