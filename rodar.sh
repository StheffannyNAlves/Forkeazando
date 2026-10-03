#!/bin/bash

mvn compile dependency:build-classpath \
    -Dmdep.outputFile=cp.txt \
    -Dmdep.scope=runtime && \
java -cp "target/classes:$(cat cp.txt)" br.uefs.forkeazando.Main