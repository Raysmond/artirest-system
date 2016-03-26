
#gradle clean -Pprod build buildDocker

unset DOCKER_TLS_VERIFY

ips="10.131.245.176 10.131.245.196 10.131.245.181"

for ip in $ips
do
	echo "build image to server $ip"
	export DOCKER_HOST=tcp://$ip:2375
	
	docker build -t raysmond/artirest:0.0.1 build/docker
done
