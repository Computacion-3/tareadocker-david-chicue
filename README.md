# ICESI Gym - Docker Compose

Aplicación web desarrollada con Spring Boot, React y PostgreSQL,
contenedorizada mediante Docker y orquestada con Docker Compose.

## Arquitectura

La aplicación está compuesta por tres servicios:

- PostgreSQL: base de datos.
- Spring Boot: backend REST.
- React + Nginx: frontend.

## Requisitos

- Docker
- Docker Compose

## Configuración

Crear un archivo `.env` o usa el que viene en el repositorio, todo funciona en local(No interactua con la app real puesto que se encuentra desplega en un BaaS inaccesible así que con confianza) en la raíz del proyecto:

```env
POSTGRES_DB=postgres
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

BACKEND_PORT=8085
FRONTEND_PORT=80

APP_SECURITY_SECRETKEY=change_me
```

## Construcción de las imágenes

Desde la raíz del proyecto ejecutar:

`docker compose build`

También es posible construir y levantar los servicios directamente:

`docker compose up --build`

Ejecución

`docker compose up`

Para ejecutar los contenedores en segundo plano:

`docker compose up -d`

Servicios

**Frontend**: Punto de entrada de la aplicación como tal

http://localhost:80/

**Backend**: Servicio con conexión directa a la BD Pgsql y con utilidad de SpringMVC para manejo administrativo.

http://localhost:8085/

**PostgreSQL**

Sirviendo de forma externa http://localhost:5440

Dentro de la red Docker, el backend accede a PostgreSQL mediante: `postgres:5432`

## Detener la aplicación

`docker compose down`

Para eliminar también el volumen de PostgreSQL:

`docker compose down -v`

Advertencia: eliminar el volumen borra los datos almacenados de PostgreSQL.