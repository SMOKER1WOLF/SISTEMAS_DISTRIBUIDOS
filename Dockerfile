
FROM tomcat:10-jdk17

# Limpiar las apps por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR
COPY dist/SISTEMAS_DISTRIBUIDOS.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
