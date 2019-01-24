#!/usr/bin/env bash

# -----------------------------------------------------------------------------
# Environment Variable Prerequisites
#
#   GENAUTH_HOME      Point to the sword-risk-server home directory.
#
#   GENAUTH_ENV       Should be DEV/PRD.
#
#   JAVA_HOME       Must point at your Java Development Kit installation.
#
#   JAVA_OPTS       (Optional) Java runtime options used when any command
#                   is executed.
# -----------------------------------------------------------------------------

# Main class of sword-risk-server
MAIN_CLASS="com.genauth.GenAuthApplication"

# Check environment variables
if [ -z "${GENAUTH_HOME}" ]; then
  GENAUTH_HOME="$(cd `dirname $0`;cd ..;pwd)"
fi
if [ -z "${GENAUTH_ENV}" ]; then
  echo "GENAUTH_ENV environment variable not defined, value should be DEV/PRD."
  exit 1
fi
if [ -z "${JAVA_HOME}" ]; then
  JAVA_PATH=`which java 2>/dev/null`
  if [ "x${JAVA_PATH}" != "x" ]; then
    JAVA_HOME=`dirname $(dirname ${JAVA_PATH}) 2>/dev/null`
  fi
  if [ -z "${JAVA_HOME}" ]; then
    echo "JAVA_HOME environment variable not defined."
    exit 1
  fi
fi

# Check config file
GENAUTH_CONFIG_FILE=$1
if [ -z "${GENAUTH_CONFIG_FILE}" ]; then
  GENAUTH_CONFIG_ENV=$(echo ${GENAUTH_ENV} | tr '[A-Z]' '[a-z]')
  GENAUTH_CONFIG_FILE="application-${GENAUTH_CONFIG_ENV}.properties"
fi
GENAUTH_CONFIG="${GENAUTH_HOME}/config/${GENAUTH_CONFIG_FILE}"
if [ ! -f "${GENAUTH_CONFIG}" ]; then
  echo "Config file ${GENAUTH_CONFIG} not found."
  exit 1
fi

# Check server port, default is 9877
PORT=`grep "server.port" "${GENAUTH_CONFIG}" | cut -d "=" -f 2 | tr -d "\r"`
if [ -z "${PORT}" ]; then
  PORT=9877
fi

# Set PID file
PID_FILE="${GENAUTH_HOME}/${PORT}.pid"

# Check if server is running
if [ -f "${PID_FILE}" ]; then
  EXIST_PID=`cat "${PID_FILE}"`
  num=`ps -p "${EXIST_PID}" | grep "${EXIST_PID}" | wc -l`
  if [ ${num} -ge 1 ]; then
    echo "Can't start sword risk server, an existing server[${EXIST_PID}] is running."
    exit 1
  fi
fi

# Backup previous logs
LOG_DIR="${GENAUTH_HOME}/logs/${PORT}"
BACK_DIR="${GENAUTH_HOME}/backup/${PORT}"
if [ ! -d "${BACK_DIR}" ]; then
  mkdir -p "${BACK_DIR}"
fi
TS=`date +%Y%m%d%H%M%S`
if [ -d "${LOG_DIR}" ]; then
  mv "${LOG_DIR}" "${BACK_DIR}/${TS}"
fi

# Log files
`mkdir -p "${LOG_DIR}"`
OUT_FILE="${LOG_DIR}/server.out"
ERR_FILE="${LOG_DIR}/server.err"
GC_FILE="${LOG_DIR}/server-gc.log"

# Set options for server starting
MEM_SIZE_MB="1024"
MEM_OPTS="-Xms${MEM_SIZE_MB}m -Xmx${MEM_SIZE_MB}m"
GC_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=50 -verbose:gc -Xloggc:${GC_FILE} -XX:+PrintGCDateStamps -XX:+PrintGCDetails"
DEBUG_OPTS="-Xverify:none -XX:+HeapDumpOnOutOfMemoryError -XX:+UnlockCommercialFeatures -XX:+FlightRecorder"
DEBUG_OPTS="-XX:AutoBoxCacheMax=10000 -XX:+PrintCommandLineFlags ${DEBUG_OPTS}"
JAVA_OPTS="${JAVA_OPTS} ${MEM_OPTS} ${DEBUG_OPTS} ${GC_OPTS}"

CLASSPATH="."
LIB_DIR="${GENAUTH_HOME}/lib"
if [ -d "$LIB_DIR" ]; then
  for f in `ls ${LIB_DIR}/*.jar | grep -v sword-risk-rules`
  do
    CLASSPATH="${CLASSPATH}:${f}"
  done
fi

GENAUTH_OPTS="-Dboot.env=${GENAUTH_ENV} -Dgenauth.home=${GENAUTH_HOME} -Dgenauth.log.dir=${LOG_DIR}"
SPRING_OPTS="--spring.config.location=file:${GENAUTH_CONFIG}"

echo "--------------------------------------------------"
echo "Starting Genauth"
echo "--------------------------------------------------"
echo "GENAUTH_HOME   : ${GENAUTH_HOME}"
echo "GENAUTH_ENV    : ${GENAUTH_ENV}"
echo "GENAUTH_MAIN   : ${GENAUTH_MAIN}"
echo "GENAUTH_CONFIG : ${GENAUTH_CONFIG}"
echo "JAVA_HOME    : ${JAVA_HOME}"
echo "JAVA_OPTS    : ${JAVA_OPTS}"
echo "--------------------------------------------------"

# Start server
RUN_CMD="${JAVA_HOME}/bin/java ${JAVA_OPTS} -cp ${CLASSPATH} ${GENAUTH_OPTS} ${GENAUTH_MAIN} ${SPRING_OPTS}"
echo "Ready to run Sword Risk Server with command: " >${OUT_FILE}
echo "${RUN_CMD}" >>${OUT_FILE}
nohup ${RUN_CMD} >>${OUT_FILE} 2>${ERR_FILE} &

# Save PID file
PID=$!
echo ${PID} >"${PID_FILE}"

# Waiting for server starting
echo -n "Waiting for server[${PID}] at port[${PORT}] to start."
start_sec=0
max_sec=30
while [ ${start_sec} -lt ${max_sec} ] ; do
  num=`netstat -an | grep -w "${PORT}" | grep -w "LISTEN" | wc -l`
  if [ ${num} -ge 1 ]; then
    echo
    exit 0
  fi
  echo -n "."
  min=`expr ${start_sec} + 1`
  sleep 1
done
echo "Server did not started in ${max_sec} seconds, please check log files."
exit 1
