#!/bin/sh
# compiles new jars - overwrites existing ones
javac -d out -sourcepath src src/**/**/*.java
jar cvf encoder.jar -m mf/encoder/META-INF/MANIFEST.MF out/
jar cvf decoder.jar -m mf/decoder/META-INF/MANIFEST.MF out/