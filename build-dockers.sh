#!/usr/bin/env sh

cd "$(dirname "$0")"

wget -q 'https://dl.bintray.com/kamon-io/releases/io/kamon/kanela-agent-bundle/0.0.14/kanela-agent-bundle-0.0.14.jar'

cd front-api
sbt clean
sbt stage
cp ../kanela-agent-bundle-0.0.14.jar ./target/universal/stage/

cd ..

cd items-api
gradle clean
gradle build
cd build/distributions/
tar xvzf items-api-0.0.1-SNAPSHOT.tar
cd -
cp ../kanela-agent-bundle-0.0.14.jar ./build/distributions/items-api-0.0.1-SNAPSHOT/

cd ..

cd sellers-api
gradle clean
gradle build
cd build/distributions/
tar xvzf sellers-api-0.0.1-SNAPSHOT.tar
cd -
cp ../kanela-agent-bundle-0.0.14.jar ./build/distributions/sellers-api-0.0.1-SNAPSHOT/

cd ..

cd users-api
sbt clean
sbt one-jar

cd ..

docker-compose build

rm kanela-agent-bundle-0.0.14.jar
