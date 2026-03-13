// Ejecutar con: mongosh "mongodb+srv://cluster.xxx.mongodb.net/btgpactual" --username user --file create-indexes.js

db = db.getSiblingDB("btgpactual");

// clients
db.clients.createIndex({ clientId: 1 }, { unique: true });

// funds
db.funds.createIndex({ fundId: 1 }, { unique: true });

// subscriptions
db.subscriptions.createIndex(
  { clientId: 1, fundId: 1 },
  { unique: true }
);

// transactions
db.transactions.createIndex({ transactionId: 1 }, { unique: true });
db.transactions.createIndex({ clientId: 1, fecha: -1 });

print("Índices creados correctamente.");
