@echo off
rem Uso: run-module.bat auth-service
if "%1"=="" (
  echo Debes pasar el nombre del modulo, por ejemplo: run-module.bat auth-service
  goto :eof
)
set MAVEN_HOME=C:\tools\apache-maven-3.9.11
set PATH=%MAVEN_HOME%\bin;%PATH%
set MODULE=%1
mvn -DskipTests -pl %MODULE% -am spring-boot:run
