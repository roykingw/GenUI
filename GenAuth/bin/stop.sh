#!/usr/bin/env bash

# -----------------------------------------------------------------------------
# Environment Variable Prerequisites
#
#   GENAUTH_HOME      Point to the GenAuth home directory.
#
#   PID_FILE        Server pid file name when running.
# -----------------------------------------------------------------------------

# Check environment variables
if [ -z "${GENAUTH_HOME}" ]; then
  GENAUTH_HOME="$(cd `dirname $0`;cd ..;pwd)"
fi
if [ -z "${GENAUTH_ENV}" ]; then
  echo "GENAUTH_ENV environment variable not defined, value should be DEV/PRD."
  exit 1
fi

# Check config file
GENAUTH_CONFIG_FILE=$1
if [ -z "${GENAUTH_CONFIG_FILE}" ]; then
  GENAUTH_CONFIG_ENV=$(echo ${GENAUTH_ENV} | tr '[A-Z]' '[a-z]')
  GENAuTH_CONFIG_FILE="application-${GENAUTH_CONFIG_ENV}.properties"
fi
GENAUTH_CONFIG="${GENAUTH_HOME}/config/${GENAUTH_CONFIG_FILE}"
if [ ! -f "${GENAUTH_CONFIG}" ]; then
  echo "Config file ${GENAUTH_CONFIG} not found."
  exit 1
fi

# Check server port, default is 9877
PORT=`grep "server.port" "${GENAUTH_CONFIG}" | cut -d "=" -f 2 | tr -d "\r"`
if [ -z "${PORT}" ]; then
  PORT=1111
fi

# Set PID file
PID_FILE="${GENAUTH_HOME}/${PORT}.pid"

# Check if server is running
if [ ! -f "${PID_FILE}" ]; then
  echo "PID file ${PID_FILE} not found. May the server is not running."
  exit 1
fi

PID=`cat "${PID_FILE}"`

echo "--------------------------------------------------"
echo "Stoping Sword Risk Server"
echo "--------------------------------------------------"
echo "GENAUTH_HOME   : ${GENAUTH_HOME}"
echo "GENAUTH_CONFIG : ${GENAUTH_CONFIG}"
echo "PID          : ${PID}"
echo "--------------------------------------------------"

# Stop server
`kill "${PID}"`
if [ $? -eq 1 ]; then
  exit 1
fi

echo -n "Waiting for server to stop."
while [ 1 ] ; do
  num=`ps -p "${PID}" | grep "${PID}" | wc -l`
  if [ ${num} -eq 0 ]; then
    break
  fi
  echo -n "."
  sleep 1
done
echo

`rm "${PID_FILE}"`
