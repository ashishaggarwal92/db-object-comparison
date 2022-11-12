#!/bin/bash
# **********************************
# This is unix test script
# JOB_PARAMS - OrderDate in YYMMDD format
# **********************************


# this allows to run the script from any location
BASEDIR=$(dirname $0)
cd $BASEDIR

oDate="$1"
nextODate=$(date -d "${oDate} +1 day" + '%y%m%d')

echo "oDate = ${odate} : nextODate = ${nextODate}"

export CLASSPATH=$CLASSPATH:.:../lib/*:../config/
export CLASSPATH=$CLASSPATH

MEM_ARGS='-ms1024m -mx16384m'
JOB_NAME='-Dspring.batch.job.names=file.generation.parse.universe.job'
JOB_PARAMS="orderDate=${oDate} nextODate=${nextODate} runCount=$RANDOM"

${JAVA_HOME_11}/bin/java -classpath $CLASSPATH ${MEM_ARGS} ${JOB_NAME} com.csg.Main ${JOB_PARAMS}

exitcode=$?
if [ "$exitcode" -eq "0" ]; then
	echo "No Error"
else
	echo "Encountered Error"
fi
exit $exitcode