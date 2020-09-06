#!/bin/bash

_print_project_dir() {
  local real_path="$(readlink --canonicalize "$0")"
  (
    cd "$(dirname "$real_path")"
    pwd
  )
}

# --------------------------------

cmd="$1"; shift
case $cmd in
  package)
    ./sbt.sh assembly
    ;;
  test)
    ./sbt.sh test
    ;;
  run-jar)
    export MY_APP_DIR="$PWD"
    export PORT=9000
    java -jar target/scala-2.12/my-scalatra-web-app-assembly-0.1.0-SNAPSHOT.jar
    ;;
  *)
    echo "invalid command" >&2
    ;;
esac
