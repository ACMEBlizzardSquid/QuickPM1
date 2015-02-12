#!/bin/bash

# $Id: javalimited.sh,v 1.3 2010/09/06 18:33:52 mokhov Exp $

JAVA_PROC_LIMIT=4
JAVA_PROC_LIMIT_FILE='java-proc.count'
SLEEP_LIMIT=5
REAL_JVM=java

if [ ! -e $JAVA_PROC_LIMIT_FILE ]; then
	echo -n 0 >| $JAVA_PROC_LIMIT_FILE
fi

# XXX: locking of JAVA_PROC_LIMIT_FILE; should parent be the main locker?

iCurrentJVMCount=`cat $JAVA_PROC_LIMIT_FILE`

echo "$0: Registered started JVMs: $iCurrentJVMCount"

while [ "$iCurrentJVMCount" -ge $JAVA_PROC_LIMIT ]; do
	echo "$0: Registered started JVMs: more or at the allowed limit: $JAVA_PROC_LIMIT. Sleeping for $SLEEP_LIMIT..."
	sleep $SLEEP_LIMIT

	# Should someone delete the file while we were sleeping
	if [ ! -e $JAVA_PROC_LIMIT_FILE ]; then
		echo -n 0 >| $JAVA_PROC_LIMIT_FILE
	fi
	
	iCurrentJVMCount=`cat $JAVA_PROC_LIMIT_FILE`
	echo "$0: Registered started JVMs: $iCurrentJVMCount"
done

echo "$0: Registering our JVM..."

iCurrentJVMCount=$(( $iCurrentJVMCount + 1 ))
echo -n "$iCurrentJVMCount" >| $JAVA_PROC_LIMIT_FILE

echo "$0: Running our real JVM [$REAL_JVM]: $iCurrentJVMCount..."

$REAL_JVM $@
iRetVal=$?

echo "$0: Done running our real JVM [$REAL_JVM]: $iRetVal"

iCurrentJVMCount=`cat $JAVA_PROC_LIMIT_FILE`
iCurrentJVMCount=$(( $iCurrentJVMCount - 1 ))

if [ "$iCurrentJVMCount" -le 0 ];
	echo -n 0 >| $JAVA_PROC_LIMIT_FILE
then
	echo -n "$iCurrentJVMCount" >| $JAVA_PROC_LIMIT_FILE
fi

echo -n "$0: Remaining JVMs: "
cat $JAVA_PROC_LIMIT_FILE
echo ""

exit $iRetVal

# EOF
