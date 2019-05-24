echo "running the project"

cd ..
echo "resolving dependencies"
mvn install
echo "dependencies installed successfully!"
echo "packaging as jar"
mvn package
echo "packaging completed!"
echo "running jar"
java -cp target/servicewall-1.0-B.jar servicewall.App
