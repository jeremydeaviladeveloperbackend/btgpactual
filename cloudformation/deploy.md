# Despliegue con AWS CloudFormation

Despliegue simple del backend en **Elastic Beanstalk** (1 instancia EC2).

## Requisitos

- AWS CLI configurado (`aws configure`)
- Cuenta AWS con permisos para crear recursos
- MongoDB Atlas (connection string)
- JAR construido con Gradle

## Pasos

### 1. Crear bucket S3 para el JAR

```powershell
aws s3 mb s3://btgpactual-deploy-$(aws sts get-caller-identity --query Account --output text) --region us-east-1
```

O usa un bucket existente.

### 2. Construir y subir el JAR

```powershell
cd d:\Preparacion entrevistas coding\personalsoft\btgpactual

# Compilar
.\gradlew.bat bootJar

# Subir (reemplaza BUCKET por tu bucket)
aws s3 cp build/libs/btgpactual-0.0.1-SNAPSHOT.jar s3://BUCKET/btgpactual/app.jar
```

> El JAR se genera en `build/libs/`. El nombre depende de `settings.gradle` y `version` en `build.gradle`.

### 3. Desplegar el stack CloudFormation

```powershell
aws cloudformation create-stack `
  --stack-name btgpactual-backend `
  --template-body file://cloudformation/backend-stack.yaml `
  --parameters `
    ParameterKey=JarS3Bucket,ParameterValue=TU-BUCKET `
    ParameterKey=JarS3Key,ParameterValue=btgpactual/app.jar `
    ParameterKey=MongoDbUri,ParameterValue="mongodb+srv://user:pass@cluster.mongodb.net/btgpactual"
```

O por archivo de parámetros `params.json`:

```json
[
  {"ParameterKey": "JarS3Bucket", "ParameterValue": "tu-bucket"},
  {"ParameterKey": "JarS3Key", "ParameterValue": "btgpactual/app.jar"},
  {"ParameterKey": "MongoDbUri", "ParameterValue": "mongodb+srv://..."}
]
```

```powershell
aws cloudformation create-stack --stack-name btgpactual-backend `
  --template-body file://cloudformation/backend-stack.yaml `
  --parameters file://cloudformation/params.json
```

### 4. Verificar

El despliegue tarda ~5-10 minutos. Para ver el estado:

```powershell
aws cloudformation describe-stacks --stack-name btgpactual-backend --query "Stacks[0].Outputs"
```

La URL de la API estará en `ApplicationUrl`.

---

## Actualizar versión

1. Compilar: `.\gradlew.bat bootJar`
2. Subir nuevo JAR a S3
3. Crear nueva versión en Elastic Beanstalk y desplegar, o usar:

```powershell
aws elasticbeanstalk create-application-version `
  --application-name btgpactual-backend `
  --version-label v2 `
  --source-bundle S3Bucket=TU-BUCKET,S3Key=btgpactual/app.jar

aws elasticbeanstalk update-environment `
  --application-name btgpactual-backend `
  --environment-name prod `
  --version-label v2
```

---

## Eliminar stack

```powershell
aws cloudformation delete-stack --stack-name btgpactual-backend
```
