echo "this script would run properly only when under sudo"

echo "java installation is a pre-requisite"

java -version

echo "installing maven"

echo "updating registry keys"

apt update

apt install maven

echo "installation complete!"

mvn -version
