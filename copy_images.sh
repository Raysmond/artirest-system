ips="10.131.245.176 10.131.245.196 10.131.245.181 10.131.245.166"

for ip in $ips
do
  echo "$ip"
  scp artirest.tar root@$ip:/root/raysmond/
done


