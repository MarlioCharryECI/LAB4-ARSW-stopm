# 🎨 BluePrints RT - Sistema de Dibujo Colaborativo en Tiempo Real

## 📋 Descripción

**BluePrints RT** es un backend basado en Spring Boot 3 que proporciona funcionalidades de dibujo colaborativo en tiempo real utilizando WebSocket con protocolo STOMP. Permite que múltiples usuarios dibujen simultáneamente en lienzos compartidos, con sincronización instantánea de todos los cambios.

## ✨ Características Principales

- 🔄 **Sincronización en Tiempo Real**: Actualizaciones instantáneas via WebSocket
- 🎯 **Dibujo Colaborativo**: Múltiples usuarios pueden dibujar en el mismo plano
- 📡 **Protocolo STOMP**: Comunicación eficiente y confiable
- 🌐 **API REST**: Endpoints para obtener y gestionar planos
- 💾 **Almacenamiento en Memoria**: Gestión temporal de puntos de dibujo
- 🔧 **Configuración Flexible**: CORS y puertos configurables
- 📊 **Logging Nivel DEBUG**: Traza completa para desarrollo

## 🏗️ Arquitectura

```
┌─────────────────┐    WebSocket/STOMP    ┌─────────────────┐
│   Cliente Web   │ ◄─────────────────► │   Backend       │
│   (Frontend)    │                      │   (Spring Boot) │
└─────────────────┘                      └─────────────────┘
                                                │
                                                ▼
                                        ┌─────────────────┐
                                        │   API CRUD      │
                                        │   (Externa)     │
                                        └─────────────────┘
```

## 🚀 Tecnologías Utilizadas

- **Java 21** - Última versión LTS
- **Spring Boot 3.3.4** - Framework principal
- **Spring WebSocket** - Comunicación en tiempo real
- **STOMP Protocol** - Protocolo de mensajería
- **Maven** - Gestión de dependencias
- **Jackson** - Serialización JSON
- **Tomcat Embedded** - Servidor web

## 📁 Estructura del Proyecto

```
src/main/java/com/eci/blueprints/rt/
├── Application.java              # Clase principal Spring Boot
├── BlueprintController.java      # Controlador REST + WebSocket
├── WebSocketConfig.java          # Configuración STOMP
├── dto/                          # Data Transfer Objects
│   ├── Point.java               # Coordenadas (x, y)
│   ├── DrawEvent.java           # Evento de dibujo
│   ├── BlueprintUpdate.java     # Actualización de plano
│   └── BlueprintRequest.java    # Solicitud de plano
└── service/
    └── BlueprintAPIService.java # Cliente API CRUD externa
```

## 🛠️ Configuración y Ejecución

### Prerrequisitos
- Java 21 o superior
- Maven 3.6+
- IDE (IntelliJ, Eclipse, VS Code)

### Pasos para Ejecutar

1**Compilar el proyecto**
   ```bash
   mvn clean compile
   ```

2**Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

3**Acceder a la aplicación**
   - **API REST**: `http://localhost:8081`
   - **WebSocket Endpoint**: `ws://localhost:8081/ws-blueprints`
   - **Actuator**: `http://localhost:8081/actuator`

## 📡 Endpoints

### WebSocket
- **Conexión**: `ws://localhost:8081/ws-blueprints`
- **Publicar**: `/app/draw` (enviar eventos de dibujo)
- **Suscribir**: `/topic/blueprints.{author}.{name}` (recibir actualizaciones)

### API REST
- **GET** `/api/blueprints/{author}/{name}` - Obtener plano específico

## 🎮 Flujo de Uso

### Dibujo Colaborativo

1. **Conexión**: Cliente se conecta al WebSocket endpoint
2. **Suscripción**: Cliente se suscribe a `/topic/blueprints.{author}.{name}`
3. **Dibujo**: Cliente envía `DrawEvent` a `/app/draw`
4. **Broadcast**: Servidor procesa y publica `BlueprintUpdate` a todos los suscritos
5. **Sincronización**: Todos los clientes reciben la actualización instantáneamente

### Formato de Mensajes

**DrawEvent** (Cliente → Servidor):
```json
{
  "author": "juan",
  "name": "plano1",
  "point": {
    "x": 100,
    "y": 200
  }
}
```

**BlueprintUpdate** (Servidor → Clientes):
```json
{
  "author": "juan",
  "name": "plano1",
  "points": [
    {"x": 100, "y": 200},
    {"x": 150, "y": 250}
  ]
}
```

## ⚙️ Configuración

### application.properties
```properties
# API CRUD externa
blueprints.crud.api.url=http://localhost:8080/api/v1/blueprints

# Servidor
server.port=8081

# Logging
logging.level.com.eci.blueprints.rt=DEBUG
```

### WebSocket Configuration
- **Endpoint**: `/ws-blueprints`
- **CORS**: Permite `http://localhost:5173`
- **SockJS**: Habilitado para fallback
- **Broker**: Simple broker para `/topic`



## 📝 Notas Importantes

- **Almacenamiento Temporal**: Los puntos se guardan en memoria (`ConcurrentHashMap`)
- **Manejo de Errores**: La aplicación continúa funcionando si la API CRUD externa no está disponible
- **Concurrencia**: Uso de estructuras thread-safe para manejo concurrente
- **Validación**: Se validan los mensajes recibidos antes de procesarlos


## 👥 Autor

**Marlio Jose Charry Espitia**  


