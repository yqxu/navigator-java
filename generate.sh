curl -o generate.jar http://maven.pingpongx.org/nexus/content/repositories/snapshots/com/pingpongx/business/dal-generator/2.0.0-SNAPSHOT/dal-generator-2.0.0-20191216.112515-6.jar
java -jar generate.jar dal-generate.yml 
rm -f generate.jar