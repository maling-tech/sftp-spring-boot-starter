#!/usr/bin/env bash

if [[ "$OSTYPE" == "darwin"* ]]; then
  sed() {
    gsed "$@"
  }
  date() {
    gdate "$@"
  }
fi

new_version="${1}"

if [[ -z "${new_version}" ]]; then
  echo "Missing argument: version"
  exit 1
fi

current_version="$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)"
echo "Current version is: ${current_version}"
echo "New version will be: ${new_version}"
echo
read -p "Are you sure? " -r
if [[ $REPLY =~ ^[Yy]$ ]]; then
  mvn versions:set "-DnewVersion=${new_version}" -DoldVersion=* -DgroupId=* -DartifactId=* -q -DforceStdout
  mvn -f sftp-spring-boot-sample/pom.xml versions:set "-DnewVersion=${new_version}" -DoldVersion=* -DgroupId=* -DartifactId=* -q -DforceStdout
  mvn -f sftp-spring-boot-sample/pom.xml versions:update-parent "-DparentVersion=${new_version}" -q -DforceStdout
fi
