@echo off
rem Uso: start-single.bat auth-service
if "%1"=="" (
  echo Debes pasar el nombre del modulo, por ejemplo: start-single.bat auth-service
  goto :eof
)
set MAVEN_HOME=C:\tools\apache-maven-3.9.11
set PATH=%MAVEN_HOME%\bin;%PATH%
set MODULE=%1
start "%MODULE%" cmd /k "cd %MODULE% && mvn -DskipTests spring-boot:run"
