FROM java:latest
MAINTAINER Fabrizio Ferrai <fabrizio.ferrai@gmail.com>

RUN apt-get install wget
RUN wget https://github.com/ff-/markiki/releases/download/0.1.0/markiki
RUN chmod +x markiki && mv markiki /usr/bin/
RUN apt-get update && apt-get install lighttpd -y
COPY lighttpd.conf.docker ./
EXPOSE 80
CMD markiki /srv/http/markiki && lighttpd -D -f lighttpd.conf.docker
