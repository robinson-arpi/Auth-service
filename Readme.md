# Servicio de Autenticación

Este es un servicio de autenticación en Java utilizando Spring Boot, JWT, JPA, Lombok y PostgreSQL. El servicio proporciona autenticación segura de usuarios mediante cifrado de contraseñas y gestión de intentos fallidos de inicio de sesión.

## Funcionalidades

- **Cifrado de Contraseñas**: Las contraseñas se cifran antes de almacenarlas en la base de datos.
- **Autenticación JWT**: Se utilizan tokens JWT para manejar sesiones de usuario.
- **Gestión de Intentos de Inicio de Sesión**: Se bloquea temporalmente el intento de inicio de sesión después de más de 3 intentos fallidos.
- **Integración con PostgreSQL**: Los datos de usuario se almacenan en una base de datos PostgreSQL.
- **Uso de Lombok**: Lombok se utiliza para reducir el boilerplate de código en los modelos y controladores.

## Requisitos

- Java 21
- Maven 3.6
- PostgreSQL
- Spring Boot 3.x

## Instalación

1. **Clonar el Repositorio**

   ```bash
   git clone https://github.com/robinson-arpi/Auth-service.git


## Configuración de la base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true


## Instalación de dependencias
mvn clean install

## Contribuciones
Si deseas contribuir a este proyecto, por favor sigue estos pasos:

## Haz un fork del repositorio.
Crea una nueva rama para tu característica o corrección.
Realiza tus cambios y haz commits.
Envía un pull request con una descripción detallada de los cambios.
Licencia
Este proyecto está licenciado bajo la Licencia MIT - consulta el archivo LICENSE para más detalles.

## Contacto
Para cualquier pregunta o soporte, contacta a robinson.arpi@gmail.com.

