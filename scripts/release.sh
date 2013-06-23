#!/bin/bash

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
working_dir="$( pwd )"
files=( "pom.xml" "README.md" )

source $script_dir/functions.sh

if ! ( contains_files ${files[@]} )
then
    working_dir=$working_dir/..
    cd $working_dir
fi

if ! ( contains_files ${files[@]} )
then
    echo "Some of the required files (${files[@]}) not found, aborting release"
    exit 1
fi

echo -n "Enter release version: "
read release_version

echo -n "Enter new development version: "
read develop_version

echo -n "Enter GPG passphrase: "
stty_orig=$(stty -g)
stty -echo 
read passphrase
stty $stty_orig

echo "$passphrase" | gpg --passphrase-fd 0 --armor --output pom.xml.asc --detach-sig pom.xml > /dev/null
gpg --verify pom.xml.asc > /dev/null
if [ $? -ne 0 ]; then
    echo "Seems that the GPG passphrase was invalid"
    exit $?
fi
rm pom.xml.asc

echo ""
echo "Starting release process for $release_version"
echo "Working in $( pwd )"
echo "Press return to continue or CTRL-C to abort"
read

echo "Cleaning project"
mvn clean > /dev/null

echo "Updating license information"
mvn license:update-project-license license:update-file-header > /dev/null
git add -A
git commit -m "Updated license information."

$script_dir/bump-version.sh $release_version

echo "Creating tag for v$release_version"
git tag -a v$release_version -m "jersey-mustache version $release_version"
git checkout v$release_version

echo ""
echo "If everything went fine artifacts can be deployed to staging repository"
echo "After artifacts are deployed, login to https://oss.sonatype.org/ and complete the release"
echo "Press return to continue deploying or CTRL-C to abort"
read

mvn -Pprepare-deploy,prepare-release -Dgpg.passphrase=$passphrase clean deploy

echo ""
echo "Preparing for next development version $develop_version"
git checkout master
$script_dir/bump-version.sh $develop_version

echo ""
echo "Pushing everything to origin"
git push origin
git push origin --tags

echo ""
echo "Release completed for $release_version, current development version is $develop_version!"
