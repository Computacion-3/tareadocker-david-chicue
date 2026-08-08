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

Para ejecutar:

En la terminal en la ruta base ejecutar `docker compose up` esperar a que carge e ingresar en http://localhost:80/ para acceder a la aplicación y empezar a usarla.