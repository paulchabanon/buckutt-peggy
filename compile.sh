# On supprime les fichier de la précédente compil
rm -rf bin/
mkdir bin/

# On regénère les .class
#export LANG=fr_FR.UTF-8
javac -cp lib/axis.jar:lib/commons-discovery-0.2.jar:lib/javax.wsdl_1.5.1.v200806030408.jar:lib/jaxrpc.jar:lib/org.apache.commons.logging_1.0.4.v200904062259.jar:lib/PertelianLib2.jar:lib/saaj.jar src/axis/*.java -encoding utf8 src/gui/*.java -d bin/

# On extrait les lib
cd bin/
jar xf ../lib/axis.jar
jar xf ../lib/commons-discovery-0.2.jar
jar xf ../lib/javax.wsdl_1.5.1.v200806030408.jar
jar xf ../lib/jaxrpc.jar
jar xf ../lib/org.apache.commons.logging_1.0.4.v200904062259.jar
jar xf ../lib/PertelianLib2.jar
jar xf ../lib/saaj.jar

# On copie les images pour les inclures
cp -r ../src/images ./

# Idem pour le manifest
cp ../manifest.mft ./

# On supprime les meta données des lib
rm -rf META-INF/

# On package tout le dossier bin (donc le dossier courant)
jar cvfm ../$1 manifest.mft ./

