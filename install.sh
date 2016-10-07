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

# This script is inspired by https://github.com/rust-lang-nursery/rustup.rs/blob/master/rustup-init.sh

set -o errexit
set -o pipefail
set -o nounset
#set -o xtrace

__dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
__file="${__dir}/$(basename "${BASH_SOURCE[0]}")"
__base="$(basename ${__file} .sh)"
#__user=$(who | head -n1 | awk '{print $1}')
#arg1="${1:-}"

INTELLIJ_IDEA_LINUX="https://download.jetbrains.com/idea/ideaIC-2016.2.4.tar.gz"
INTELLIJ_IDEA_MACOS="https://download.jetbrains.com/idea/ideaIC-2016.2.4.dmg"
INTELLIJ_IDEA_WNDWS="https://download.jetbrains.com/idea/ideaIC-2016.2.4.exe"
SPOOFAX_INTELLIJ_PLUGIN="http://download.spoofax.org/update/nightly/spoofax-intellij.zip"

# Main entry point.
main() {
	require_command curl
	require_command tar
	require_command unzip
	
	detect_platform || return 1
	local _os="$RETVAL"
	
	# Get and create the installation directory
	local _installdir="$__dir/ideaIC"
	ensure mkdir -p "$_installdir"
	
	# Get and create a temporary directory
    local _tmpdir="$(mktemp -d 2>/dev/null || ensure mktemp -d -t spoofax-intellij)"
    ensure mkdir -p "$_tmpdir"
	
	if [ "$_os" == "Linux" ]; then
		# Linux
		local _ideaurl="$INTELLIJ_IDEA_LINUX"
		local _ideafile="$_tmpdir/ideaIC.tar.gz"
		local _spoofaxurl="$SPOOFAX_INTELLIJ_PLUGIN"
		local _spoofaxfile="$_tmpdir/spoofax-intellij.zip"
		
		# Download IntelliJ IDEA
		say "Downloading IntelliJ IDEA..."
		ensure curl -SfL "$_ideaurl" -o "$_ideafile"
		# Extract it into the installation directory
		ensure cd "$_installdir"
		ensure tar --strip 1 -xzf "$_ideafile"
		
		# Download Spoofax for IntelliJ IDEA
		say "Downloading Spoofac for IntelliJ..."
		ensure curl -SfL "$_spoofaxurl" -o "$_spoofaxfile"
		# Extract it into the plugins directory
		ensure unzip -q "$_spoofaxfile" -d "$_installdir/plugins/"
		
		# Done.
		say "Done!"
		
		# Install a JDK
		if ! detect_jdk; then
		    say "No JDK detected..."
			if ask "Do you want to install Oracle JDK 8 now?" N; then
				say "Installing JDK..."
				ensure sudo add-apt-repository -y ppa:webupd8team/java > /dev/null 2>&1
				ensure sudo apt-get -qq update
				ensure echo oracle-java8-installer shared/accepted-oracle-license-v1-1 \
				select true | sudo /usr/bin/debconf-set-selections
				ensure sudo apt-get -qq -y install oracle-java8-installer
				ensure sudo update-java-alternatives -s java-8-oracle
				ensure sudo apt-get -qq -y install oracle-java8-set-default
				say "Oracle JDK 8 installed"
			else
				say "Install a JDK."
			fi
		fi

		# Run IntelliJ IDEA
		if ask "Do you want to run IntelliJ IDEA now?" N; then
		    say "Starting IntelliJ IDEA..."
			bin/idea.sh &>/dev/null &disown
		else
			say "Run ideaIC/bin/idea.sh to start IntelliJ IDEA."
		fi
	elif [ "$_os" == "MacOS" ]; then
		# MacOS
		err "not implemented for OS type: $_os"
	elif [ "$_os" == "Windows" ]; then
		# Windows
		err "not implemented for OS type: $_os"
	fi
	
	return 0
}

# Detects the current platform.
detect_platform() {

	local _os="$(uname -s)"
	
	case "$_os" in
        Linux | FreeBSD | DragonFly)
            local _os="Linux"
            ;;
        Darwin)
            local _os="MacOS"
            ;;
        MINGW* | MSYS* | CYGWIN*)
            local _os="Windows"
            ;;
        *)
            err "unrecognized OS type: $_os"
            ;;
    esac
	
    RETVAL="$_os"
}

# Detects whether a JDK is found.
detect_jdk() {
	which javac > /dev/null 2>&1
	return $?
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

# Prints a message.
say() {
    echo "$1"
}

# Prints an error message.
say_err() {
    say "$1" >&2
}

# Prints an error message and exits.
err() {
    say_err "$1"
    exit 1
}

# Asks the user to confirm.
ask() {
    # Adapted from: http://djm.me/ask
    local prompt default REPLY

    while true; do

        if [ "${2:-}" = "Y" ]; then
            prompt="Y/n"
            default=Y
        elif [ "${2:-}" = "N" ]; then
            prompt="y/N"
            default=N
        else
            prompt="y/n"
            default=
        fi

        echo -n "${1:-Are you sure?} [$prompt] "
        read REPLY </dev/tty

        if [ -z "$REPLY" ]; then
            REPLY=$default
        fi

        case "$REPLY" in
            Y*|y*) return 0 ;;
            N*|n*) return 1 ;;
        esac

    done
}

main "$@" || exit 1
