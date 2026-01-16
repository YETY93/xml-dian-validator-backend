# 📄 XML Validator DIAN – Spring Boot

API REST desarrollada en **Spring Boot** para la **validación estructural de documentos XML DIAN (UBL 2.1)** contra esquemas **XSD oficiales**, con el objetivo de detectar errores antes del envío a la DIAN.

Este proyecto está pensado como:

* 🚀 **Producto SaaS inicial**
* 🧪 Herramienta de validación técnica
* 📚 Proyecto demostrativo para portafolio profesional

---

## 🎯 Objetivo del proyecto

Permitir a desarrolladores, empresas y proveedores tecnológicos:

* Validar la **estructura XML** de facturas electrónicas DIAN
* Obtener **mensajes de error claros**
* Reducir rechazos en el proceso de facturación electrónica
* Extender fácilmente a firma XML, persistencia y envío real a DIAN

---

## 🏗️ Arquitectura

Arquitectura en capas siguiendo buenas prácticas de Spring Boot:

```
com.yesidrangel.dian.xmlvalidator
├── controller        # Capa web (REST Controllers)
├── service           # Lógica de negocio (interfaces)
│   └── impl           # Implementaciones de servicios
├── domain
│   └── dto            # DTOs de request/response
├── util               # Utilidades (validación XSD)
├── exception          # Manejo global de errores
└── XmlValidatorApplication.java
```

---

## ⚙️ Tecnologías utilizadas

* **Java 17**
* **Spring Boot 3.5.9**
* **Maven**
* **Jakarta XML Validation**
* **Postman (testing)**
* **XSD UBL 2.1 (DIAN)**

---

## 📦 Funcionalidades actuales

✔ API REST funcional
✔ Endpoint de salud (`/health`)
✔ Validación XML contra XSD
✔ Retorno de errores estructurales
✔ Arquitectura preparada para escalar

---

## 🔌 Endpoints disponibles

### 🔍 Health check

```
GET /health
```

**Respuesta**

```
OK
```

---

### 📄 Validar XML DIAN

```
POST /api/xml/validate
```

#### Request

```json
{
  "xml": "<Invoice>...</Invoice>",
  "documentType": "INVOICE"
}
```

#### Response – XML válido

```json
{
  "valid": true,
  "errors": []
}
```

#### Response – XML inválido

```json
{
  "valid": false,
  "errors": [
    "cvc-complex-type.2.4.a: Invalid content was found..."
  ]
}
```

---

## ▶️ Cómo ejecutar el proyecto

### 1️⃣ Clonar repositorio

```bash
git clone https://github.com/YETY93/xml-dian-validator-backend.git
cd xml-dian-validator-backend
```

### 2️⃣ Compilar

```bash
mvn clean install
```

### 3️⃣ Ejecutar

```bash
mvn spring-boot:run
```

La aplicación quedará disponible en:

```
http://localhost:8080
```

---

## 🧪 Pruebas

* Pruebas manuales usando **Postman**
* Pruebas unitarias planeadas para fases futuras

---

## 🧭 Roadmap (próximas fases)

🔜 Selección automática de XSD según tipo de documento
🔜 Persistencia de validaciones (PostgreSQL)
🔜 Firma digital XML (XAdES)
🔜 Integración real con servicios DIAN
🔜 Frontend Angular para usuarios finales

---

## 👨‍💻 Autor

**Yesid Rangel**
Desarrollador Java
Experiencia en facturación electrónica DIAN

---

## 🌿 Flujo de desarrollo

Este proyecto usa **GitFlow**. Ver [CONTRIBUTING.md](CONTRIBUTING.md) para detalles.

---

## 📄 Licencia

Proyecto en fase inicial – uso académico y demostrativo.
