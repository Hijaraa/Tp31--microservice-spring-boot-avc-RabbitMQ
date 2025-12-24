# Tp31-Microservice - RabbitMQ Messaging

Ce projet contient deux mini-projets Spring Boot exploitant RabbitMQ (AMQP) pour échanger des messages entre microservices.

## 📋 Table des matières

- [Vue d'ensemble](#vue-densemble)
- [Prérequis](#prérequis)
- [Architecture](#architecture)
- [Mini-projet 1 : Messagerie JSON](#mini-projet-1--messagerie-json)
- [Mini-projet 2 : Messagerie + MySQL](#mini-projet-2--messagerie--mysql)
- [Installation et Démarrage](#installation-et-démarrage)
- [Tests](#tests)
- [Structure des projets](#structure-des-projets)

## 🎯 Vue d'ensemble

### Mini-projet 1 : Messagerie JSON
- **Producer** (Port 8123) → RabbitMQ (Exchange Topic) → **Consumer** (Port 8223) → Logs console
- Communication simple avec sérialisation JSON
- Exchange de type **Topic**

### Mini-projet 2 : Messagerie + MySQL
- **Producer** (Port 8081) → RabbitMQ (Exchange Direct) → **Consumer** → **MySQL** (Table user)
- Persistance des messages dans MySQL via Spring Data JPA
- Exchange de type **Direct**

## 🔧 Prérequis

- **JDK 17+**
- **Maven 3.6+**
- **RabbitMQ** en cours d'exécution (Docker recommandé)
  - Port AMQP : `5672`
  - Port Management UI : `15672`
- **MySQL** (pour le mini-projet 2)
  - Port : `3306`
- **Postman** ou **curl** (pour les tests)

## 🏗️ Architecture

```
┌─────────────────┐         ┌──────────┐         ┌─────────────────┐
│   Producer      │────────▶│ RabbitMQ │────────▶│    Consumer     │
│  (Port 8123)    │  JSON   │ Exchange │  JSON   │  (Port 8223)     │
│                 │         │  Topic   │         │  (Console Logs) │
└─────────────────┘         └──────────┘         └─────────────────┘

┌─────────────────┐         ┌──────────┐         ┌─────────────────┐
│   Producer      │────────▶│ RabbitMQ │────────▶│    Consumer     │
│  (Port 8081)    │  JSON   │ Exchange │  JSON   │  (Port 8223)     │
│                 │         │  Direct  │         │  + MySQL DB      │
└─────────────────┘         └──────────┘         └─────────────────┘
```

## 📦 Mini-projet 1 : Messagerie JSON

### Producer (spring-rabbitmq-producer)

**Port :** `8123`  
**Exchange :** `topic.exchange` (Type: Topic)  
**Queue :** `user.queue`  
**Routing Key :** `user.routing.key`

#### Endpoints REST

- **POST** `/api/users/publish` - Publier un message User
- **GET** `/api/users/health` - Vérification de santé

#### Exemple de requête (curl)

```bash
curl -X POST http://localhost:8123/api/users/publish \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "firstName": "Oussama",
    "lastName": "Test",
    "email": "oussama@example.com"
  }'
```

**Réponse attendue :**
```
User message sent to RabbitMQ successfully!
```

#### Exemple de requête (Postman)

**URL :** `http://localhost:8123/api/users/publish`  
**Method :** `POST`  
**Headers :**
```
Content-Type: application/json
```

**Body (raw JSON) :**
```json
{
  "id": 1,
  "firstName": "Oussama",
  "lastName": "Test",
  "email": "oussama@example.com"
}
```

### Consumer (spring-rabbitmq-consumer)

**Port :** `8223`  
**Queue :** `user.queue`

Le consumer écoute automatiquement les messages et les affiche dans la console :

```
========================================
Message reçu depuis RabbitMQ:
ID: 1
Prénom: Oussama
Nom: Test
Email: oussama@example.com
========================================
```

## 💾 Mini-projet 2 : Messagerie + MySQL

### Producer (messaging-producer)

**Port :** `8081`  
**Exchange :** `direct.exchange` (Type: Direct)  
**Queue :** `user.mysql.queue`  
**Routing Key :** `user.mysql.routing.key`

#### Endpoints REST

- **POST** `/api/users/publish` - Publier un message User
- **GET** `/api/users/health` - Vérification de santé

#### Exemple de requête (curl)

```bash
curl -X POST http://localhost:8081/api/users/publish \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "firstName": "Hajar",
    "lastName": "Rabbit",
    "email": "hajar@example.com"
  }'
```

#### Exemple de requête (Postman)

**URL :** `http://localhost:8081/api/users/publish`  
**Method :** `POST`  
**Headers :**
```
Content-Type: application/json
```

**Body (raw JSON) :**
```json
{
  "id": 1,
  "firstName": "Hajar",
  "lastName": "Rabbit",
  "email": "hajar@example.com"
}
```

**Réponse attendue :**
```
User message sent to RabbitMQ successfully!
```

### Consumer (messaging-consumer)

**Port :** `8223`  
**Queue :** `user.mysql.queue`  
**Base de données :** MySQL (`messaging_db`)

Le consumer :
1. Reçoit les messages depuis RabbitMQ
2. Les affiche dans la console
3. **Les persiste dans MySQL** via Spring Data JPA

#### Logs console attendus

```
========================================
Message reçu depuis RabbitMQ:
ID: 1
Prénom: Hajar
Nom: Rabbit
Email: hajar@example.com
Utilisateur sauvegardé dans MySQL avec l'ID: 1
========================================
```

#### Structure de la table MySQL

```sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);
```

## 🚀 Installation et Démarrage

### 1. Démarrer RabbitMQ (Docker)

```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

**Interface de gestion :** http://localhost:15672  
**Identifiants par défaut :** `guest` / `guest`

### 2. Démarrer MySQL (Docker - pour mini-projet 2)

```bash
docker run -d --name mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=messaging_db \
  mysql:8.0
```

### 3. Lancer les applications

#### Mini-projet 1

**Terminal 1 - Producer :**
```bash
cd spring-rabbitmq-producer
./mvnw spring-boot:run
# ou sur Windows: mvnw.cmd spring-boot:run
```

**Terminal 2 - Consumer :**
```bash
cd spring-rabbitmq-consumer
./mvnw spring-boot:run
# ou sur Windows: mvnw.cmd spring-boot:run
```

#### Mini-projet 2

**Terminal 1 - Producer :**
```bash
cd messaging-producer
./mvnw spring-boot:run
# ou sur Windows: mvnw.cmd spring-boot:run
```

**Terminal 2 - Consumer :**
```bash
cd messaging-consumer
./mvnw spring-boot:run
# ou sur Windows: mvnw.cmd spring-boot:run
```

## 🧪 Tests

### Test du Mini-projet 1

1. **Démarrer RabbitMQ**
2. **Lancer le Consumer** (port 8223)
3. **Lancer le Producer** (port 8123)
4. **Envoyer une requête :**

```bash
curl -X POST http://localhost:8123/api/users/publish \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com"
  }'
```

5. **Vérifier les logs du Consumer** - Le message doit apparaître dans la console
6. **Vérifier RabbitMQ Management UI** (http://localhost:15672)
   - Exchange `topic.exchange` créé
   - Queue `user.queue` créée
   - Messages dans la queue

### Test du Mini-projet 2

1. **Démarrer RabbitMQ et MySQL**
2. **Lancer le Consumer** (port 8223)
3. **Lancer le Producer** (port 8081)
4. **Envoyer une requête :**

```bash
curl -X POST http://localhost:8081/api/users/publish \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "firstName": "Hajar",
    "lastName": "Rabbit",
    "email": "hajar@example.com"
  }'
```

5. **Vérifier les logs du Consumer** - Le message doit apparaître avec confirmation de sauvegarde
6. **Vérifier MySQL :**

```sql
SELECT * FROM messaging_db.user;
```

7. **Vérifier RabbitMQ Management UI**
   - Exchange `direct.exchange` créé
   - Queue `user.mysql.queue` créée

## 📁 Structure des projets

### spring-rabbitmq-producer
```
spring-rabbitmq-producer/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/hajar/rabbitmicro/
│       │       ├── SpringRabbitmqProducerApplication.java
│       │       ├── config/
│       │       │   └── RabbitMQConfig.java
│       │       ├── controller/
│       │       │   └── UserController.java
│       │       ├── model/
│       │       │   └── User.java
│       │       └── service/
│       │           └── RabbitMQProducerService.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### spring-rabbitmq-consumer
```
spring-rabbitmq-consumer/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/hajar/rabbitmicro/
│       │       ├── SpringRabbitmqConsumerApplication.java
│       │       ├── config/
│       │       │   └── RabbitMQConfig.java
│       │       ├── listener/
│       │       │   └── RabbitMQConsumer.java
│       │       └── model/
│       │           └── User.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### messaging-producer
```
messaging-producer/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/hajar/messaging/
│       │       ├── MessagingProducerApplication.java
│       │       ├── config/
│       │       │   └── RabbitMQConfig.java
│       │       ├── controller/
│       │       │   └── UserController.java
│       │       ├── model/
│       │       │   └── User.java
│       │       └── service/
│       │           └── RabbitMQProducerService.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### messaging-consumer
```
messaging-consumer/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/hajar/messaging/
│       │       ├── MessagingConsumerApplication.java
│       │       ├── config/
│       │       │   └── RabbitMQConfig.java
│       │       ├── listener/
│       │       │   └── RabbitMQConsumer.java
│       │       ├── model/
│       │       │   └── User.java (@Entity)
│       │       └── repository/
│       │           └── UserRepository.java
│       └── resources/
│           └── application.properties
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## ⚙️ Configuration

### RabbitMQ Configuration

Tous les projets utilisent la configuration par défaut :
```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### MySQL Configuration (messaging-consumer)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/messaging_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## 📊 RabbitMQ Management UI

Accéder à l'interface de gestion : http://localhost:15672

### Vérifications

1. **Exchanges :**
   - `topic.exchange` (Type: topic) - Mini-projet 1
   - `direct.exchange` (Type: direct) - Mini-projet 2

2. **Queues :**
   - `user.queue` - Mini-projet 1
   - `user.mysql.queue` - Mini-projet 2

3. **Bindings :**
   - `topic.exchange` → `user.queue` (routing key: `user.routing.key`)
   - `direct.exchange` → `user.mysql.queue` (routing key: `user.mysql.routing.key`)

## 🎓 Objectifs d'apprentissage

✅ Déclarer dynamiquement un exchange, une queue et un binding depuis Spring Boot  
✅ Publier un message via REST (Producer) et consommer via @RabbitListener (Consumer)  
✅ Observer les échanges et compteurs dans l'interface RabbitMQ  
✅ Sérialiser/désérialiser en JSON avec Jackson2JsonMessageConverter  
✅ Persister un message consommé dans MySQL via Spring Data JPA  

## 📝 Notes

- Les ports peuvent être modifiés dans les fichiers `application.properties`
- Les noms d'exchanges, queues et routing keys sont configurables
- Pour MySQL, ajuster les identifiants dans `application.properties` selon votre configuration
- Les fichiers Maven wrapper (`mvnw`, `mvnw.cmd`) permettent d'exécuter Maven sans installation préalable

## 👤 Auteur

**Hajar**  
Projet TP31 - Microservices avec RabbitMQ

## 📄 Licence

Ce projet est à des fins éducatives.

