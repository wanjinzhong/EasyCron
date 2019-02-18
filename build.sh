#!/bin/bash
cd /var/webapps/docker_easy-cron/
echo "Try to find easy-cron container."
container_name=easy-cron
container_id=`docker ps -af name="${container_name}" -q`
if [ -n "$container_id" ]
then
	echo "The easy-cron container found: $container_id."
	echo "Stoping easy-cron container($container_id)"
	image_id=`sudo docker inspect --format='{{.Image}}' ${container_id}`
	docker stop $container_id
	echo "Try to delete the easy-cron container($container_id)"
	docker rm $container_id
	echo "Try to delete the easy-cron image(${image_id})"
	docker rmi $image_id
else
	echo "The easy-cron container not found, skip."
fi
echo 'Build new image'
docker build -q -t easy-cron .
echo 'Run image'
docker run -d -p 8088:8080 --name easy-cron -v /var/webapps/logs/easy-cron/:/var/logs/easy-cron easy-cron:latest