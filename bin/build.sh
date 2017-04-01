PROJECT_PATH=/root/project/my-code/
APP_PATH=/root/

cd $PROJECT_PATH
git pull

mvn clean install -DskipTests -Ptar

tar -xvf $PROJECT_PATH/target/*-publish.tar.gz -C  $APP_PATH