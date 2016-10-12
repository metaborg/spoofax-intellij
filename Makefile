# Spoofax plugin for IntelliJ IDEA
# ================================
#
# `make all`   - Builds the plugin and its dependencies.
# `make run`   - Builds the plugin and starts an instance
#                of IntelliJ with the plugin.
# `make clean` - Cleans all projects.

GRADLEARGS ?= --quiet

.PHONY: all run clean jps-deps jps intellij run-intellij clean-jps clean-jps-deps clean-intellij
.SILENT:

all: intellij jps
run: run-intellij
clean: clean-intellij clean-jps clean-jps-deps

jps-deps:
	echo "Building: org.metaborg.jps-deps"
	cd org.metaborg.jps-deps/ ; \
	./gradlew publishToMavenLocal ${GRADLEARGS}

jps: jps-deps
	echo "Building: org.metaborg.jps"
	cd org.metaborg.jps/ ; \
	./gradlew install ${GRADLEARGS}

intellij: jps
	echo "Building: org.metaborg.intellij"
	cd org.metaborg.intellij/ ; \
	./gradlew build ${GRADLEARGS}

run-intellij: intellij
	echo "Running: org.metaborg.intellij"
	cd org.metaborg.intellij/ ; \
	./gradlew runIdea ${GRADLEARGS}

clean-jps-deps:
	echo "Cleaning: org.metaborg.jps-deps"
	cd org.metaborg.jps-deps/ ; \
	./gradlew clean ${GRADLEARGS}

clean-jps:
	echo "Cleaning: org.metaborg.jps"
	cd org.metaborg.jps/ ; \
	./gradlew clean ${GRADLEARGS}

clean-intellij:
	echo "Cleaning: org.metaborg.intellij"
	cd org.metaborg.intellij/ ; \
	./gradlew clean ${GRADLEARGS}
