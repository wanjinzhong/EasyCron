FROM java:8-jre
COPY easy-cron.jar /var/webapps/easy-cron.jar
ENV DB_URL localhost:3306
ENV DB_NAME EASY_CRON
ENV DB_USERNAME ???
ENV DB_PWD ???
ENV LOG_HOME /var/logs/easy-cron
WORKDIR /var/webapps/
CMD java -jar easy-cron.jar