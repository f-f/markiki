FROM java:latest
MAINTAINER Fabrizio Ferrai <fabrizio.ferrai@gmail.com>

RUN apt-get install wget
RUN wget https://github.com/ff-/markiki/releases/download/0.2.1/markiki
RUN chmod +x markiki && mv markiki /usr/bin/
RUN apt-get update && apt-get install lighttpd -y
RUN mkdir -p /srv/http/markiki
COPY lighttpd.conf.docker ./
EXPOSE 80
CMD lighttpd -f lighttpd.conf.docker && markiki --watch /home/markiki /srv/http/markiki
