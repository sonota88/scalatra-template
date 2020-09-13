#!/bin/bash

_print_project_dir() {
  local real_path="$(readlink --canonicalize "$0")"
  (
    cd "$(dirname "$real_path")"
    pwd
  )
}

# --------------------------------

RES_DIR=src/main/resources
SBT_CMD=~/app/sbt/1.3.13/bin/sbt

cmd_package(){
  cp ${RES_DIR}/logback_prod.xml \
     ${RES_DIR}/logback.xml

  ./sbt.sh assembly

  cp ${RES_DIR}/logback_devel.xml \
     ${RES_DIR}/logback.xml
}

cmd_up_devel(){
  cp ${RES_DIR}/logback_devel.xml \
     ${RES_DIR}/logback.xml

  export PUBLIC_DIR="$PWD"
  ./sbt.sh '~;jetty:stop;jetty:start'
}

cmd_up_prod(){
  java -jar target/scala-2.12/static-server-scalatra-assembly-0.1.0-SNAPSHOT.jar
}

# --------------------------------

prod_port=8108

cd "$(_print_project_dir)"

case "$1" in
  test)
    $SBT_CMD test
    ;;
  package)
    cmd_package
    ;;
  up)
    # devel
    cmd_up_devel
    ;;
  up-prod)
    export PUBLIC_DIR="$PWD"
    export PORT=$prod_port
    cmd_up_prod
    ;;
  restart-prod)
    export PUBLIC_DIR="$PWD"
    export PORT=$prod_port
    curl http://localhost:${PORT}/shutdown
    cmd_up_prod
    ;;
  *)
    echo "invalid command" >&2
    ;;
esac
