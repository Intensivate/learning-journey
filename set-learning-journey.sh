#!/bin/bash

# Chisel Learning Journey
# Set-up script
# December 31st, 2017
#
# January 3rd, 2018	- adjusting verilator installation
#			- adjusting permissions automatically
#
# January 20th, 2018	- adapting the script to the learning-journey repo
#
# January 21st, 2018	- correcting the chown issue
#
# February 2nd, 2018	- added generator-bootcamp
#
# Aleksandar Pajkanovic
# aleksandar [dot] pajkanovic [at] gmail [dot] com

# Let the user know what is going on and ask for permission:
echo "	Welcome to the Chisel Learning Journey!

	This script will set your environment in the right manner for you to
	start making first steps on the Chisel Learning	Journey.

	The script has been prepared for and tested on Mint 18, which is the
	recommended OS for the Learning Journey. For details on how to prepare
	the virtual machine, please visit:

	https://github.com/Intensivate/learning-journey/wiki/Chisel-Environment 

	In case you are not running Mint nor any other Ubuntu-like OS, we
	advise you to abort running this script.

	First of all, the script will run tests to check whether
	dependencies (git, make, autoconf, g++, flex and bison) are
	installed. Each one that is missing will be installed.

	Furthermore, it will install Java, SBT and Verilator on your system,
	setting all of them correctly.

	The script must be run with super user privileges, i.e. sudo
	must come before \"./set-learning-journey\"

	Please report issues to the e-mail given within the script header.

"

while true;
do
	read -p "	Do you agree?
					" response
	case $response in
		[Yy]* ) break;;
		[Nn]* ) exit;;
		* ) echo "Please answer yes or no.";;
	esac
done


export current_user=`who | awk '{print $1}'`

export LJHOME=$PWD

echo "export LJHOME=$PWD" >> /home/$current_user/.bashrc

echo "Home of Learning Journey set in \$LJHOME"

# Check for dependencies and install those that are missing
if [ `dpkg-query -l | grep make | wc -l` -eq 0 ]
then
	echo "make not present, installing..."
	apt-get install make
	echo "make installed"
else
	echo "make already present"
fi

if [ `dpkg-query -l | grep autoconf | wc -l` -eq 0 ]
then
	echo "autoconf not present, installing..."
	apt-get install autoconf
	echo "autoconf installed"
else
	echo "autoconf already present"
fi

if [ `dpkg-query -l | grep g++ | wc -l` -eq 0 ]
then
	echo "g++ not present, installing..."
	apt-get install g++
	echo "g++ installed"
else
	echo "g++ already present"
fi

if [ `dpkg-query -l | grep flex | wc -l` -eq 0 ]
then
	echo "flex not present, installing..."
	apt-get install flex
	echo "flex installed"
else
	echo "flex already present"
fi

if [ `dpkg-query -l | grep bison | wc -l` -eq 0 ]
then
	echo "bison not present, installing..."
	apt-get install bison
	echo "bison installed"
else
	echo "bison already present"
fi
	

# install Java:

apt-get install default-jdk

# install SBT:

echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
apt-get update
apt-get install sbt

# install verilator:

git clone http://git.veripool.org/git/verilator
cd verilator

unset VERILATOR_ROOT
git pull
git checkout verilator_3_904 
# Version 3.904 is dated May 30th, 2017, however Chisel3 has issues with
# more up-to-date versions. This is where the bug was reported and may be tracked:
#	https://github.com/ucb-bar/chisel-tutorial/issues/111
autoconf # Create ./configure script
./configure
make
make install

cd $LJHOME

# install prerequisites for generator-bootcamp
if [ `dpkg-query -l | grep python | wc -l` -eq 0 ]
then
	echo "python not present, installing..."
	apt-get install python3 python3-pip python3-setuptools
	echo "python installed"
else
	echo "python already present"
fi

# installing jupyter
pip3 install --upgrade pip
pip3 install jupyter --ignore-installed
pip3 install jupyterlab

# installing jupyter-scala
rm -rf ~/.local/share/jupyter/kernels/scala/
curl -L -o coursier https://git.io/coursier-cli && chmod +x coursier
SCALA_VERSION=2.12.10 ALMOND_VERSION=0.9.1
./coursier bootstrap -r jitpack \
    -i user -I user:sh.almond:scala-kernel-api_$SCALA_VERSION:$ALMOND_VERSION \
    sh.almond:scala-kernel_$SCALA_VERSION:$ALMOND_VERSION \
    --sources --default=true \
    -o almond
./almond --install
cd $LJHOME

# get chisel-bootcamp:
git clone https://github.com/freechipsproject/chisel-bootcamp.git
cd chisel-bootcamp
mkdir -p ~/.jupyter/custom
cp source/custom.js ~/.jupyter/custom/custom.js
cd $LJHOME

chown -R $current_user:$current_user $LJHOME

# Installations finished, just give some instructions
echo "	
	You are ready now to start walking the Chisel Learning Journey!

	Please visit:

	https://github.com/Intensivate/learning-journey/wiki/Setting-up-Chisel#testing-your-system

	for instructions on how to test your system and to find out what is your first step.

	Once you've passed through those Wiki pages, please run:

		cd \$LJHOME/generator-bootcamp && jupyter notebook
				"

