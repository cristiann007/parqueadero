# 🚗 Sistema de Gestión de Parqueadero - Java JDBC

## 📝 Descripción
Aplicación de escritorio desarrollada en Java con Swing y JDBC para gestionar un parqueadero de vehículos. Permite administrar parqueaderos, espacios y carros, con asignación automática de espacios al ingresar y liberación al retirar vehículos. Incluye un dashboard con estadísticas en tiempo real y historial de salidas con cálculo automático de tarifas.

## 🛠️ Tecnologías Utilizadas
- **Lenguaje:** Java (JDK 8+)
- **Base de Datos:** MySQL
- **Conexión:** JDBC (Java Database Connectivity)
- **Interfaz Gráfica:** Java Swing
- **IDE:** Apache NetBeans

## 🚀 Cómo Ejecutar el Proyecto

### 1. Requisitos Previos
- Tener instalado Java JDK 8 o superior.
- Tener instalado MySQL Server.
- Agregar el driver JDBC (`mysql-connector-j.jar`) a las librerías del proyecto en NetBeans.

### 2. Configuración de la Base de Datos
1. Abre tu gestor de MySQL (Workbench, phpMyAdmin, etc.).
2. Crea una base de datos llamada `parqueadero_db`.
3. Ejecuta el script SQL para crear las tablas (`parqueaderos`, `espacios`, `carros`, `historial_salidas`). *Nota: Si tienes un archivo `estructura.sql`, ejecútalo aquí.*

### 3. Cargar Datos de Prueba (Opcional pero Recomendado)
Para ver la aplicación con datos pre-cargados:
1. Ejecuta el archivo `datos_prueba.sql` incluido en este repositorio en tu base de datos `parqueadero_db`.
2. Esto creará un parqueadero de ejemplo, 5 espacios y 2 carros estacionados.

### 4. Ejecutar la Aplicación
1. Abre el proyecto en NetBeans.
2. Asegúrate de que la conexión en `DatabaseConnection.java` tenga tu usuario y contraseña correctos de MySQL.
3. Ejecuta la clase `Main.java` ubicada en el paquete `vista`.
4. La interfaz gráfica se abrirá mostrando el Dashboard con las estadísticas.

## 📂 Estructura del Proyecto
- `src/conexion/`: Clase para gestionar la conexión a la BD.
- `src/dao/`: Objetos de Acceso a Datos (CRUDs).
- `src/modelo/`: Clases modelo (Entidades).
- `src/vista/`: Interfaz gráfica (Swing) y clase principal.
- `lib/`: Librerías externas (Driver JDBC).

## 👥 Autores
- [Tu Nombre]
- [Nombre Compañero 1]
- [Nombre Compañero 2]
- [Nombre Compañero 3]

## 📄 Licencia
Este proyecto es parte de una actividad académica.
