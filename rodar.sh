#!/bin/bash

mvn -q compile dependency:build-classpath \
    -Dmdep.outputFile=cp.txt \
    -Dmdep.scope=runtime && \
clear && \
java -cp "target/classes:$(cat cp.txt)" br.uefs.forkeazando.Main