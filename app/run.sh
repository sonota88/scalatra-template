#!/bin/bash

cmd="$1"; shift
case $cmd in
  package)
    ./sbt.sh assembly
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
