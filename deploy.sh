
REPOSITORY=/home/ec2-user/app/MyBoard_v1
PROJECT_NAME=MyBoard_v1

echo "> Git pull"
git pull

echo "> Maven Build(package no test)"
cd REPOSITORY/PROJECT_NAME
mvn package -Dmaven.test.skip=true

echo "> application run"
java -jar target/portfolio-0.0.1-SNAPSHOT.war
