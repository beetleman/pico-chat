FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/pico-chat.jar /pico-chat/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/pico-chat/app.jar"]
