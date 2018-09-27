#!/usr/bin/env bash

host="localhost"
port=9070
count=${1:-10}
concurrency=${2:-2}

perform_ab() {
	url=$1
	echo "`date` | Performing AB to ${url}"
	ab -c ${concurrency} -n ${count} ${url} &
}

echo "`date` | Starting requests generations with ${count} requests per endpoint by ${concurrency} concurrently"

# Basic example
perform_ab "http://${host}:${port}/api/front/search?key-word=robot"

# Compose 2 services
perform_ab "http://${host}:${port}/api/front/details/item/15/user/5"

# The same but in parallel
perform_ab "http://${host}:${port}/api/front/parallel/details/item/15/user/5"

# Async span
perform_ab "http://${host}:${port}/api/front/parallel/details/item/15/user/55"

# Item not found
perform_ab "http://${host}:${port}/api/front/parallel/details/item/1500/user/55"

wait

echo "`date` | Finished the execution of requests generations"
