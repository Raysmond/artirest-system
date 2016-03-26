token="$(docker run swarm create)"

echo $token

docker-machine create -d virtualbox --swarm --swarm-master --swarm-discovery token://$token swarm-master
docker-machine create -d virtualbox --swarm --swarm-discovery token://$token swarm-agent-00
docker-machine create -d virtualbox --swarm --swarm-discovery token://$token swarm-agent-01


