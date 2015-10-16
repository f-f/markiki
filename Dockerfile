FROM java:latest
MAINTAINER Fabrizio Ferrai <fabrizio.ferrai@gmail.com>

RUN apt-get update && apt-get install lighttpd wget -y
RUN wget https://github.com/ff-/markiki/releases/download/0.3.0/markiki
RUN chmod +x markiki && mv markiki /usr/bin/
COPY lighttpd.conf.docker ./
EXPOSE 80
CMD lighttpd -f lighttpd.conf.docker && markiki --watch /home/markiki
