@echo off
rem Uso: start-single.bat auth-service
if "%1"=="" (
  echo Debes pasar el nombre del modulo, por ejemplo: start-single.bat auth-service
  goto :eof
)
set MODULE=%1
start "%MODULE%" cmd /k "cd %MODULE% && mvn -DskipTests spring-boot:run"
