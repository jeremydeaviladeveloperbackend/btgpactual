# BTG Pactual - API Fondos

API REST reactiva de fondos de inversión (Spring WebFlux + MongoDB).

## Cómo funciona

- Los clientes se crean con saldo inicial y pueden suscribirse a fondos (cada fondo tiene un monto mínimo).
- Al suscribirse se descuenta el monto del saldo y se registra una transacción. Las notificaciones se simulan por EMAIL o SMS según la preferencia del cliente.
- Los clientes pueden cancelar suscripciones (se devuelve el monto al saldo) y consultar historial de transacciones.

## Ejecución

**Requisitos:** Java 17, MongoDB (local o Atlas).

**Comandos:**

```bash
# Linux / macOS
./gradlew bootRun

# Windows
.\gradlew.bat bootRun
```

**Variables de entorno:**

- `MONGODB_URI`: connection string de MongoDB (obligatorio en producción).

**Puerto:** 5000 por defecto. Para usar otro: `-Dserver.port=8080`

**JAR:** `./gradlew bootJar` genera el JAR en `build/libs/`.

## Endpoints


| Método | Ruta                                                        | Descripción                                                                        |
| ------ | ----------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| GET    | `/api/v1/funds`                                             | Lista fondos disponibles                                                           |
| POST   | `/api/v1/clients`                                           | Crea cliente (body: nombre, apellidos, ciudad, preferenciaNotificacion: EMAIL|SMS) |
| GET    | `/api/v1/clients/{clientId}`                                | Obtiene cliente                                                                    |
| POST   | `/api/v1/clients/{clientId}/subscriptions`                  | Suscripción a fondo (body: fundId)                                                 |
| GET    | `/api/v1/clients/{clientId}/subscriptions`                  | Suscripciones activas                                                              |
| DELETE | `/api/v1/clients/{clientId}/subscriptions/{subscriptionId}` | Cancela suscripción                                                                |
| GET    | `/api/v1/clients/{clientId}/transactions`                   | Historial de transacciones                                                         |


## SQL

La respuesta a la pregunta SQL está en `scripts/script.sql`.

## Seguridad y mantenibilidad

Por limitación de tiempo no se implementó autenticación (JWT, OAuth) ni autorización. La API es abierta; en un escenario real habría que añadir:

- Autenticación (p. ej. JWT) y que cada cliente solo acceda a sus propios datos.
- Credenciales en variables de entorno o gestor de secretos, no en `params.json` ni en código.
- Limitar las peticiones y CORS explícito.

Se aplica validación de entrada en DTOs (`@Valid`, `@NotBlank`) y manejo centralizado de excepciones.