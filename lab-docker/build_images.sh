#!/bin/bash

unset DOCKER_TLS_VERIFY

# gradle -Pprod clean buildDocker

for (( i = 0; i < 10; i++ )); do
    IP="10.141.201.20$i"
    docker -H tcp://$IP:2375 build -t raysmond/artirest:0.1.2 build/docker
done
