#!/usr/bin/env bash
while ! ping -c 1 -W 1 github.com; do
  echo "Waiting for github.com - network interface might be down..."
  sleep 1
done

set -e
VERSION="$1"
[[ -z "${VERSION}" ]] && {
  echo "Missing version argument"
  exit 1
}

cd "$(dirname "${0}")" || exit 2
mkdir -p build
LOG="pull_and_build.log"
rm "${LOG}"
export JAVA_HOME="$HOME/java/jdk-14"
{
  git fetch --all
  git reset --hard origin/master
  echo "Version=${VERSION}"
  ./gradlew clean fullPackage "-DVERSION=${VERSION}" "-DCLIENT_SECRET_PATH=$HOME/java/clientSecret.json"
} >>"${LOG}" 2>&1
