// Script ejecutable para inicializar el modelo de datos en MongoDB
// Uso: mongosh "mongodb+srv://user:pass@cluster.xxx.mongodb.net/btgpactual" --file scripts/init-mongo.js

const dbName = "btgpactual";
const db = db.getSiblingDB(dbName);

// Crear índices (ignorar si ya existen)
function createIndex(collection, keys, options = {}) {
  try {
    db.getCollection(collection).createIndex(keys, options);
    print("  Índice creado: " + collection + " -> " + JSON.stringify(keys));
  } catch (e) {
    if (e.code === 85 || e.codeName === "IndexOptionsConflict" || e.codeName === "IndexKeySpecsConflict") {
      print("  Índice ya existe: " + collection);
    } else {
      throw e;
    }
  }
}

print("Creando índices...");

createIndex("clients", { clientId: 1 }, { unique: true });
createIndex("funds", { fundId: 1 }, { unique: true });
createIndex("subscriptions", { clientId: 1, fundId: 1 }, { unique: true });
createIndex("transactions", { transactionId: 1 }, { unique: true });
createIndex("transactions", { clientId: 1, fecha: -1 });

print("Insertando fondos (si no existen)...");

const funds = [
  { fundId: "1", nombre: "FPV_BTG_PACTUAL_RECAUDADORA", montoMinimo: 75000, categoria: "FPV" },
  { fundId: "2", nombre: "FPV_BTG_PACTUAL_ECOPETROL", montoMinimo: 125000, categoria: "FPV" },
  { fundId: "3", nombre: "DEUDAPRIVADA", montoMinimo: 50000, categoria: "FIC" },
  { fundId: "4", nombre: "FDO-ACCIONES", montoMinimo: 250000, categoria: "FIC" },
  { fundId: "5", nombre: "FPV_BTG_PACTUAL_DINAMICA", montoMinimo: 100000, categoria: "FPV" }
];

const count = db.funds.countDocuments();
if (count === 0) {
  db.funds.insertMany(funds);
  print("  " + funds.length + " fondos insertados.");
} else {
  print("  La colección funds ya tiene " + count + " documentos. No se insertan.");
}

print("Listo. Modelo inicializado correctamente.");
