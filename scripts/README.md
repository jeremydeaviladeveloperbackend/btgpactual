# Scripts MongoDB

## Ejecutar el script

Para inicializar índices y datos de fondos:

```bash
mongosh "mongodb+srv://USUARIO:PASSWORD@cluster.xxx.mongodb.net/btgpactual" --file scripts/init-mongo.js
```

En Windows (PowerShell), si la URL tiene caracteres especiales:

```powershell
mongosh "mongodb+srv://usuario:password@cluster.xxx.mongodb.net/btgpactual?retryWrites=true&w=majority" -f scripts/init-mongo.js
```

O conectarse primero y luego ejecutar:

```bash
mongosh "mongodb+srv://cluster.xxx.mongodb.net" -u usuario -p password
```

Luego en la consola:

```javascript
load("scripts/init-mongo.js")
```

## Archivos

| Archivo | Descripción |
|---------|-------------|
| `init-mongo.js` | Script ejecutable: crea índices e inserta fondos |
| `modelo-datos-mongo.json` | Documentación del modelo (no ejecutable) |
| `seed-funds.json` | Datos de fondos para mongoimport |
| `create-indexes.js` | Solo crea índices |
