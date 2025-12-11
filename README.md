# usuario

Este repositorio contiene varios microservicios de ejemplo en Java + Spring Boot.

Cómo ejecutar servicios en desarrollo:

1) Ejecutar un módulo específico (por ejemplo `auth-service`):

```bat
cd auth-service
mvn -DskipTests spring-boot:run
```

2) Ejecutar todos los servicios (Windows) — abre cada servicio en una ventana nueva:

```bat
start-all.bat
```

3) Ejecutar un servicio desde la raíz sin abrir ventanas extra (usar -f):

```bat
mvn -f auth-service/pom.xml -DskipTests spring-boot:run
```

4) Alternativamente, puedes compilar todo y arrancar el servicio de tu elección:

```bat
mvn -DskipTests -pl auth-service,productos-service, ... -am package
mvn -f auth-service/pom.xml -DskipTests spring-boot:run
```

Notas:
- Si prefieres usar `spring-boot:start`, ejecuta el comando dentro del módulo (por ejemplo `cd auth-service && mvn spring-boot:start`).
- Si ejecutas `mvn spring-boot:start` desde la raíz y no existe un plugin declarado en el POM raíz, Maven puede fallar con `No plugin found for prefix 'spring-boot'`.

Si deseas, puedo cambiar el POM raíz para declarar y usar un `spring-boot-maven-plugin` global que permita `mvn spring-boot:run` desde la raíz (esto puede enmascarar dependencias particulares del módulo). 
5) Ejecutar un módulo desde la raíz con un script (se creó `run-module.bat`):

```bat
run-module.bat auth-service
```

6) Ejecución rápida desde la raíz con `mvn -pl`:

```bat
mvn -DskipTests -pl auth-service -am spring-boot:run
```

