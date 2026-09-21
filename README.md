# 📚 Biblioteca de Libros

Proyecto universitario para la gestión y consulta de información sobre
libros. La aplicación permite a los usuarios explorar un catálogo,
buscar libros y compartir sus opiniones mediante calificaciones y reseñas.

## Participantes 
- DIANE ORTEGA SOTO 
- SANTIAGO OSPINA SANCHEZ
- JHOYNER SANTIAGO PAEZ YATE 
- JUAN DIEGO PEREZ UPEGUI
- JUAN ESTEBAN PIÑEROS MALDONADO
- SANTIAGO TORRES RODRIGUEZ

## Funcionalidades

El proyecto cuenta con algunas funcionalidades basicas requeridas:

### Búsqueda
Permite buscar libros dentro del sistema utilizando diferentes criterios,
como el título, autor o género.

### Catálogo
Permite visualizar los libros disponibles y consultar su información
principal.

### Rating
Permite a los usuarios calificar los libros mediante una puntuación.

### Review
Permite a los usuarios escribir y consultar reseñas sobre los libros.

## Tecnologías aplicadas

- Java
- Spring Boot
- Neon postgresSQL 
- Git
- GitHub

## Estructura del proyecto 

```text
src/
└── main/
    └── java/
        └── com/
            └── libreria/
                │
                ├── config/
                │   └── DataInitializer.java
                │
                ├── controller/
                │   └── BookController.java
                │
                ├── domain/
                │   ├── Book.java
                │   ├── Product.java
                │   ├── ProductType.java
                │   ├── CatalogPolicy.java
                │   │
                │   ├── rating/
                │   │   ├── Rating.java
                │   │   └── RatingEmbeddable.java
                │   │
                │   └── review/
                │       ├── Review.java
                │       └── ReviewStatus.java
                │
                ├── dto/
                │   ├── RatingDto.java
                │   └── ReviewDto.java
                │
                ├── repository/
                │   └── BookRepository.java
                │
                ├── search/
                │   ├── SearchCriteria.java
                │   ├── SearchStrategy.java
                │   ├── SimpleSearchStrategy.java
                │   └── AdvancedSearchStrategy.java
                │
                ├── service/
                │   └── BookService.java
                │ 
                │
                └── LibreriaApplication.java


