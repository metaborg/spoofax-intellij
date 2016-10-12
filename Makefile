# Spoofax plugin for IntelliJ IDEA
# ================================
#
# `make all` - Builds the plugin and its dependencies.
# `make run` - Builds the plugin and starts an instance
#              of IntelliJ with the plugin.

.PHONY: clean
.SILENT:

all: intellij jps

jps-deps:
	echo "Building org.metaborg.jps-deps:"
	cd org.metaborg.jps-deps/ ; \
	./gradlew publishToMavenLocal

jps: jps-deps
	echo "Building org.metaborg.jps:"
	cd org.metaborg.jps/ ; \
	./gradlew install

intellij: jps
	echo "Building org.metaborg.intellij:"
	cd org.metaborg.intellij/ ; \
	./gradlew build

run: jps
	echo "Building and running org.metaborg.intellij:"
	cd org.metaborg.intellij/ ; \
	./gradlew runIdea
	
