FROM tomcat:9-jdk17

# Limpiar las apps por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR de NetBeans
COPY dist/MiProyecto.war /usr/local/tomcat/webapps/ROOT.war

# Exponer el puerto habitual
EXPOSE 8080

CMD ["catalina.sh", "run"]
