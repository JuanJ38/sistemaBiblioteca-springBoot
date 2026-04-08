# Sistema de Biblioteca — Spring Boot

Sistema de gestión de biblioteca desarrollado con Java 17 y Spring Boot 3, que incluye API REST, autenticación JWT, Spring Security, Spring Data JPA 

---

## Tecnologías utilizadas

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring MVC** — controladores REST y vistas HTML
- **Spring Data JPA** — persistencia con Hibernate
- **Spring Security** — autenticación y autorización con roles
- **JWT (JSON Web Token)** — seguridad para la API REST
- **Thymeleaf** — motor de plantillas HTML
- **MapStruct** — conversión entre entidades y DTOs
- **Lombok** — reducción de código repetitivo
- **Swagger / OpenAPI** — documentación de la API
- **Spring Boot Actuator** — monitoreo de la aplicación
- **MSQL** — base de datos
- **Maven** — gestión de dependencias
- **JUnit 5 + Mockito** — pruebas unitarias

---

## Arquitectura del proyecto

```
src/main/java/com/biblioteca/
├── api/               REST Controllers (@RestController)
│   ├── AuthRestController.java
│   ├── LibroRestController.java
│   ├── UsuarioRestController.java
│   └── PrestamoRestController.java
├── config/            Configuración
│   └── SecurityConfig.java
├── controller/        Controladores MVC (vistas HTML)
│   ├── LibroController.java
│   ├── UsuarioController.java
│   ├── PrestamoController.java
│   └── MainController.java
├── dto/               Data Transfer Objects
│   ├── LibroDTO.java
│   ├── UsuarioDTO.java
│   ├── PrestamoDTO.java
│   ├── AuthRequestDTO.java
│   └── AuthResponseDTO.java
├── exception/         Manejo global de errores
│   └── GlobalExceptionHandler.java
├── mapper/            MapStruct mappers
│   ├── LibroMapper.java
│   ├── UsuarioMapper.java
│   └── PrestamoMapper.java
├── model/             Entidades JPA
│   ├── Libro.java
│   ├── Usuario.java
│   └── Prestamo.java
├── repository/        Spring Data repositories
│   ├── LibroRepository.java
│   ├── UsuarioRepository.java
│   └── PrestamoRepository.java
├── security/          JWT
│   ├── JwtUtil.java
│   └── JwtFilter.java
└── service/           Lógica de negocio
    ├── LibroService.java
    ├── UsuarioService.java
    └── PrestamoService.java
```

---

## Requisitos previos

- Java 17 o superior
- Maven 3.6+
- MSQL
- IntelliJ IDEA o Eclipse

---

## Configuración de base de datos

La base de datos `BibliotecaDB` debe tener estas tablas:

```
CREATE TABLE libros (
    id INT IDENTITY PRIMARY KEY,
    titulo NVARCHAR(255) NOT NULL,
    autor NVARCHAR(255) NOT NULL,
    disponible BIT NOT NULL DEFAULT 1
);

CREATE TABLE usuarios (
    id INT IDENTITY PRIMARY KEY,
    nombre NVARCHAR(255) NOT NULL,
    correo NVARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE prestamos (
    id INT IDENTITY PRIMARY KEY,
    id_libro INT NOT NULL FOREIGN KEY REFERENCES libros(id),
    id_usuario INT NOT NULL FOREIGN KEY REFERENCES usuarios(id),
    fecha_prestamo DATE NOT NULL,
    fecha_devolucion DATE NULL
);
```

---

## Configuración en application.properties

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=BibliotecaDB;encrypt=false
spring.datasource.username=sa
spring.datasource.password=TU_PASSWORD
```

---

## Cómo ejecutar el proyecto

### En IntelliJ IDEA Community

1. `File → Open` → selecciona la carpeta `biblioteca-springboot`
2. Elige **Open as Maven Project**
3. Espera que Maven descargue las dependencias
4. Abre `BibliotecaApplication.java`
5. Click derecho → **Run 'BibliotecaApplication'**
6. Abre el navegador en `http://localhost:8080`

### Con Maven desde terminal

```bash
mvn spring-boot:run
```

---

## Credenciales de acceso

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| admin | 1234 | ADMIN |
| user | 1234 | USER |

---

## URLs disponibles

### Vistas Web (Thymeleaf)

| URL | Descripción |
|-----|-------------|
| `http://localhost:8080/login` | Pantalla de login |
| `http://localhost:8080/libros` | Lista de libros |
| `http://localhost:8080/usuarios` | Lista de usuarios (solo ADMIN) |
| `http://localhost:8080/prestamos` | Lista de préstamos |

### API REST

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/auth/login` | Obtener token JWT |
| GET | `/api/libros` | Listar todos los libros |
| GET | `/api/libros/{id}` | Buscar libro por ID |
| GET | `/api/libros/disponibles` | Libros disponibles |
| GET | `/api/libros/buscar?titulo=` | Buscar por título |
| GET | `/api/libros/paginado?page=0&size=10` | Libros paginados |
| POST | `/api/libros` | Crear libro |
| PUT | `/api/libros/{id}` | Actualizar libro |
| DELETE | `/api/libros/{id}` | Eliminar libro |
| GET | `/api/usuarios` | Listar usuarios |
| POST | `/api/usuarios` | Crear usuario |
| PUT | `/api/usuarios/{id}` | Actualizar usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |
| GET | `/api/prestamos` | Préstamos activos |
| POST | `/api/prestamos` | Registrar préstamo |
| PUT | `/api/prestamos/devolver/{id}` | Devolver libro |

### Documentación y monitoreo

| URL | Descripción |
|-----|-------------|
| `http://localhost:8080/swagger-ui.html` | Documentación Swagger |
| `http://localhost:8080/actuator/health` | Estado de la app |
| `http://localhost:8080/actuator/info` | Información de la app |

---

## Cómo usar la API REST con JWT

### 1. Obtener token

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "1234"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "rol": "ROLE_ADMIN"
}
```

### 2. Usar el token en las peticiones

```bash
GET /api/libros
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Pruebas unitarias

Ejecutar todas las pruebas:

```bash
mvn test
```

Las pruebas cubren:
- `LibroServiceTest` — 6 casos de prueba
- `PrestamoServiceTest` — 5 casos de prueba

---

## Autor
Juan Jose H.

Desarrollado como proyecto académico 
Stack: Java 17 + Spring Boot 3 + MSQL


<img width="2352" height="1244" alt="image" src="https://github.com/user-attachments/assets/4de8ac34-2884-40d8-bea5-1acf2b699a8b" />
