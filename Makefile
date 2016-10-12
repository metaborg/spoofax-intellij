# Spoofax plugin for IntelliJ IDEA
# ================================
#
# Copyright © 2015-2016
#
# This file is part of Spoofax for IntelliJ.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# Usage
# -----
# - `make all`   Builds the plugin and its dependencies.
# - `make run`   Builds the plugin and starts an instance
#                of IntelliJ IDEA with the plugin loaded.
# - `make clean` Cleans all projects.

GRADLE_OPTS ?= --quiet

.PHONY: all run clean jps-deps jps intellij run-intellij clean-jps clean-jps-deps clean-intellij
.SILENT:

all: intellij jps
run: run-intellij
clean: clean-intellij clean-jps clean-jps-deps

jps-deps:
	echo "Building: org.metaborg.jps-deps"
	cd org.metaborg.jps-deps/ ; \
	./gradlew publishToMavenLocal ${GRADLE_OPTS}

jps: jps-deps
	echo "Building: org.metaborg.jps"
	cd org.metaborg.jps/ ; \
	./gradlew install ${GRADLE_OPTS}

intellij: jps
	echo "Building: org.metaborg.intellij"
	cd org.metaborg.intellij/ ; \
	./gradlew build ${GRADLE_OPTS}

run-intellij: intellij
	echo "Running: org.metaborg.intellij"
	cd org.metaborg.intellij/ ; \
	./gradlew runIdea ${GRADLE_OPTS}

clean-jps-deps:
	echo "Cleaning: org.metaborg.jps-deps"
	cd org.metaborg.jps-deps/ ; \
	./gradlew clean ${GRADLE_OPTS}

clean-jps:
	echo "Cleaning: org.metaborg.jps"
	cd org.metaborg.jps/ ; \
	./gradlew clean ${GRADLE_OPTS}

clean-intellij:
	echo "Cleaning: org.metaborg.intellij"
	cd org.metaborg.intellij/ ; \
	./gradlew clean ${GRADLE_OPTS}
