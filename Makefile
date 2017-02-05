

cp=out:jars/junit-4.12.jar:jars/hamcrest-core-1.3.jar


run:
	javac -classpath $(cp) -d out src/*.java
	java -classpath $(cp) EREdit


junit:
	javac -classpath $(cp) -d out src/*.java test/*.java
	java -classpath $(cp) org.junit.runner.JUnitCore ERModelTest