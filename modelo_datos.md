# Modelo de Datos NoSQL - Plataforma de Fondos BTG Pactual

## Resumen

Modelo documental en MongoDB para soportar las operaciones de suscripción, cancelación y historial de transacciones de fondos de inversión.

---

## Colecciones

### 1. `clients`

Representa a los clientes con su saldo y preferencia de notificación.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `_id` | ObjectId | ID interno MongoDB |
| `clientId` | String | Identificador de negocio (índice único) - vincula con Cliente del modelo SQL |
| `nombre` | String | Nombre del cliente |
| `apellidos` | String | Apellidos del cliente |
| `ciudad` | String | Ciudad de residencia |
| `saldo` | Decimal | Saldo disponible en COP (inicial: 500.000) |
| `preferenciaNotificacion` | Enum | EMAIL \| SMS |

**Índices:**
- `clientId` (único)

---

### 2. `funds`

Catálogo de fondos disponibles. Se cargan al iniciar la aplicación.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `_id` | ObjectId | ID interno MongoDB |
| `fundId` | String | Identificador de negocio ("1", "2", etc.) |
| `nombre` | String | Nombre del fondo |
| `montoMinimo` | Decimal | Monto mínimo de vinculación en COP |
| `categoria` | String | FPV \| FIC |

**Fondos precargados:**
| fundId | nombre | montoMinimo | categoria |
|--------|--------|-------------|-----------|
| 1 | FPV_BTG_PACTUAL_RECAUDADORA | 75.000 | FPV |
| 2 | FPV_BTG_PACTUAL_ECOPETROL | 125.000 | FPV |
| 3 | DEUDAPRIVADA | 50.000 | FIC |
| 4 | FDO-ACCIONES | 250.000 | FIC |
| 5 | FPV_BTG_PACTUAL_DINAMICA | 100.000 | FPV |

---

### 3. `subscriptions`

Suscripciones activas de clientes a fondos.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `_id` | ObjectId | ID de la suscripción (usado para cancelar) |
| `clientId` | String | Referencia al cliente |
| `fundId` | String | Referencia al fondo (ID de negocio) |
| `montoVinculado` | Decimal | Monto invertido en el fondo |
| `fechaApertura` | Instant | Fecha de apertura |

**Índices:**
- Compuesto único: `(clientId, fundId)` — un cliente solo puede tener una suscripción por fondo

---

### 4. `transactions`

Historial de todas las transacciones (aperturas y cancelaciones).

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `_id` | ObjectId | ID interno MongoDB |
| `transactionId` | String | Identificador único de negocio (UUID) — cumple requisito |
| `clientId` | String | Cliente de la transacción |
| `fundId` | String | Fondo de la transacción |
| `subscriptionId` | String | Referencia a la suscripción (si aplica) |
| `tipo` | Enum | APERTURA \| CANCELACION |
| `monto` | Decimal | Monto de la transacción |
| `fecha` | Instant | Fecha y hora |

**Índices:**
- `transactionId` (único)
- `clientId` + `fecha` (para consultas de historial)

---

## Diagrama de relaciones

```
Client (clientId) ----< Subscription >---- Fund (fundId)
      |                      |                    |
      |                      |                    |
      +----< Transaction >---+--------------------+
             (transactionId)
```

- Un cliente tiene muchas suscripciones (una por fondo)
- Un cliente tiene muchas transacciones
- Cada transacción está vinculada a un fondo y opcionalmente a una suscripción

---

## Configuración MongoDB Atlas

Definir la variable de entorno `MONGODB_URI` con el connection string de tu cluster:

```
MONGODB_URI=mongodb+srv://usuario:password@cluster.xxxxx.mongodb.net/btgpactual
```

O configurar en `application.properties`:

```properties
spring.data.mongodb.uri=mongodb+srv://...
```
