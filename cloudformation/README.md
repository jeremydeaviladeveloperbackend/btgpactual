# Despliegue Backend - AWS CloudFormation

Despliegue del API de fondos BTG Pactual en **Elastic Beanstalk** (1 instancia EC2) usando CloudFormation.

---

## Requisitos

- **AWS CLI** configurado (`aws configure`)
- Cuenta AWS con permisos para crear: Elastic Beanstalk, EC2, IAM, VPC
- **MongoDB Atlas**: connection string del cluster
- **VPC y al menos 1 subnet** con salida a Internet (ruta `0.0.0.0/0` → Internet Gateway)
- **JAR** construido con Gradle (puerto 5000 para EB)

---

## Parámetros del stack

| Parámetro        | Descripción                                      |
|------------------|--------------------------------------------------|
| JarS3Bucket      | Bucket S3 donde está el JAR                      |
| JarS3Key         | Ruta del JAR en S3 (ej. `btgpactual/app.jar`)    |
| MongoDbUri       | Connection string de MongoDB Atlas                |
| SolutionStack    | Solution stack de EB (Corretto 17 en tu región)  |
| VpcId            | ID de la VPC donde desplegar                      |
| SubnetIds        | ID(s) de subnets (una o varias, separadas por coma) |

Ejemplo de `params.json` (sustituir valores reales):

```json
[
  { "ParameterKey": "JarS3Bucket", "ParameterValue": "mi-bucket" },
  { "ParameterKey": "JarS3Key", "ParameterValue": "btgpactual/app.jar" },
  { "ParameterKey": "MongoDbUri", "ParameterValue": "mongodb+srv://user:pass@cluster.xxx.mongodb.net/btgpactual" },
  { "ParameterKey": "SolutionStack", "ParameterValue": "64bit Amazon Linux 2 v3.12.0 running Corretto 17" },
  { "ParameterKey": "VpcId", "ParameterValue": "vpc-xxxxxxxx" },
  { "ParameterKey": "SubnetIds", "ParameterValue": "subnet-xxxxxxxx" }
]
```

Para listar solution stacks disponibles en tu región:

```powershell
aws elasticbeanstalk list-available-solution-stacks --query "SolutionStacks[?contains(@, 'Corretto 17')]" --output text
```

---

## Subir el stack (crear todo)

### 1. Compilar y subir el JAR

La aplicación debe escuchar en el puerto **5000** en Elastic Beanstalk (configurado en el template). El JAR se genera con `server.port=${PORT:5000}`.

```powershell
cd <raíz-del-proyecto>

.\gradlew.bat bootJar

aws s3 cp build/libs/btgpactual-0.0.1-SNAPSHOT.jar s3://<BUCKET>/btgpactual/app.jar
```

Comprobar que el objeto existe:

```powershell
aws s3 ls s3://<BUCKET>/btgpactual/app.jar
```

### 2. Crear el stack

Desde la raíz del proyecto (ajustar ruta al template si es necesario):

```powershell
aws cloudformation create-stack `
  --stack-name btgpactual-backend `
  --template-body file://cloudformation/backend-stack.yaml `
  --capabilities CAPABILITY_NAMED_IAM `
  --parameters file://cloudformation/params.json
```

El despliegue tarda varios minutos. Ver estado:

```powershell
aws cloudformation describe-stacks --stack-name btgpactual-backend --query "Stacks[0].StackStatus"
```

### 3. URL de la API

En la consola: **Elastic Beanstalk → Applications → btgpactual-backend → environment (prod)**. La URL del environment es la base del API (ej. `http://prod.eba-xxx.us-east-1.elasticbeanstalk.com`).

Probar: `GET <URL>/api/v1/funds`

---

## Bajar el stack (eliminar todo)

Equivalente a “destroy”: se eliminan la aplicación EB, el environment, roles IAM y recursos creados por el stack.

```powershell
aws cloudformation delete-stack --stack-name btgpactual-backend
aws cloudformation wait stack-delete-complete --stack-name btgpactual-backend
```

---

## Volver a subir después de bajar

1. Asegurar que el JAR actual está en S3 (mismo bucket y key que en `params.json`). Si hubo cambios en el código:
   ```powershell
   .\gradlew.bat bootJar
   aws s3 cp build/libs/btgpactual-0.0.1-SNAPSHOT.jar s3://<BUCKET>/btgpactual/app.jar
   ```
2. Crear el stack de nuevo con el mismo comando de la sección **Subir el stack**.

Si el JAR en S3 es el correcto (puerto 5000) y los parámetros son los mismos, el environment volverá a funcionar correctamente.

---

## Actualizar solo la aplicación (sin tocar CloudFormation)

1. Compilar y subir el JAR a S3 (mismo key).
2. En Elastic Beanstalk: **Upload and deploy** en el environment, subiendo el JAR, o crear una nueva aplicación version desde S3 y desplegarla en el environment.

O por CLI:

```powershell
aws elasticbeanstalk create-application-version `
  --application-name btgpactual-backend `
  --version-label v2 `
  --source-bundle S3Bucket=<BUCKET>,S3Key=btgpactual/app.jar

aws elasticbeanstalk update-environment `
  --application-name btgpactual-backend `
  --environment-name prod `
  --version-label v2
```

---

## Archivos en esta carpeta

| Archivo              | Descripción                          |
|----------------------|--------------------------------------|
| `backend-stack.yaml` | Template CloudFormation              |
| `params.json`        | Parámetros del stack (no subir a repo con datos sensibles) |
| `README.md`          | Esta documentación                   |
