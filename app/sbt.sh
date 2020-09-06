#!/bin/bash

_get_app_dir() {
  local real_path="$(readlink --canonicalize "$0")"
  (
    cd "$(dirname "$real_path")"
    pwd
  )
}

export MY_APP_DIR="$(_get_app_dir)"

~/app/sbt/1.3.13/bin/sbt "$@"
