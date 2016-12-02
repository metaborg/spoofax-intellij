#!/usr/bin/env bash
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

# This is just a little script that can be curled from the internet to
# install IntelliJ IDEA with Spoofax. It does some platform detection,
# downloads the binaries for IntelliJ IDEA and the Spoofax plugin,
# and installs then in the current directory or a specified directory.
# Optionally it can install Oracle JDK8 if no JDK was detected.

# This script is inspired by:
# https://github.com/rust-lang-nursery/rustup.rs/blob/master/rustup-init.sh

# EXECUTION
# curl https://raw.githubusercontent.com/metaborg/spoofax-intellij/master/repository/install.sh -sSLf | bash

set -o errexit
set -o pipefail

set -o nounset

# Main entry point.
main() {
	require_command git
	require_command make

	ensure git clone https://github.com/metaborg/spoofax-intellij.git
	ensure cd spoofax-intellij
	ensure ./gradlew install

	say "Ensure you have a JDK installed."
	say "To start IntelliJ IDEA:"
	say "  cd spoofax-intellij"
	say "  ./gradlew run"

	return 0
}

# Ensures the specified command completes successfully.
ensure() {
    "$@"
    need_ok "command failed: $*"
}

# Ensures the input is 0 (success).
need_ok() {
    if [ $? != 0 ]; then err "$1"; fi
}

# Ignores whether the specified command completes successfully.
ignore() {
    run "$@"
}

# Runs a command and prints it to the standard error output if it fails.
run() {
    "$@"
    local _retval=$?
    if [ $_retval != 0 ]; then
        say_err "command failed: $*"
    fi
    return $_retval
}

# Ensures the given command is available.
require_command() {
    if ! command -v "$1" > /dev/null 2>&1
    then err "require '$1' (command not found)"
    fi
}

# Prints an error message and exits.
err() {
    say_err "$1"
    exit 1
}

# Prints an error message.
say_err() {
    say "$1" >&2
}

# Prints a message.
say() {
    echo "$1"
}

main "$@" || exit 1
