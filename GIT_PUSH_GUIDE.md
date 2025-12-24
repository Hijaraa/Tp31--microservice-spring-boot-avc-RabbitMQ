# Guide pour pousser le projet vers GitHub

## Étapes pour pousser le projet vers GitHub

### 1. Initialiser Git (si pas déjà fait)

```bash
cd C:\Users\Orha6\Tp31-Microservice
git init
```

### 2. Ajouter le remote GitHub

```bash
git remote add origin https://github.com/Hijaraa/Tp31--microservice-spring-boot-avc-RabbitMQ.git
```

Si le remote existe déjà, vous pouvez le mettre à jour :
```bash
git remote set-url origin https://github.com/Hijaraa/Tp31--microservice-spring-boot-avc-RabbitMQ.git
```

Vérifier le remote :
```bash
git remote -v
```

### 3. Vérifier le fichier .gitignore

Assurez-vous qu'un fichier `.gitignore` existe à la racine avec le contenu suivant :

```
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# IDE
.idea/
*.iml
*.iws
*.ipr
.vscode/
.classpath
.project
.settings/
bin/

# OS
.DS_Store
Thumbs.db

# Logs
*.log
```

### 4. Ajouter tous les fichiers

```bash
git add .
```

Ou ajouter fichier par fichier :
```bash
git add README.md
git add spring-rabbitmq-producer/
git add spring-rabbitmq-consumer/
git add messaging-producer/
git add messaging-consumer/
```

### 5. Faire un commit

```bash
git commit -m "Initial commit: Add RabbitMQ microservices projects"
```

### 6. Pousser vers GitHub

Si c'est la première fois (branche main) :
```bash
git branch -M main
git push -u origin main
```

Si le dépôt existe déjà avec des commits :
```bash
git pull origin main --allow-unrelated-histories
# Résoudre les conflits si nécessaire
git push -u origin main
```

## Commandes complètes (copier-coller)

```bash
# Se placer dans le répertoire du projet
cd C:\Users\Orha6\Tp31-Microservice

# Initialiser Git (si nécessaire)
git init

# Ajouter le remote
git remote add origin https://github.com/Hijaraa/Tp31--microservice-spring-boot-avc-RabbitMQ.git
# Ou si existe déjà :
# git remote set-url origin https://github.com/Hijaraa/Tp31--microservice-spring-boot-avc-RabbitMQ.git

# Ajouter tous les fichiers
git add .

# Faire le commit
git commit -m "Initial commit: Add RabbitMQ microservices projects

- Add spring-rabbitmq-producer (port 8123, Topic exchange)
- Add spring-rabbitmq-consumer (port 8223)
- Add messaging-producer (port 8081, Direct exchange)
- Add messaging-consumer (port 8223, MySQL persistence)
- Add comprehensive README.md"

# Pousser vers GitHub
git branch -M main
git push -u origin main
```

## Si vous rencontrez des erreurs

### Erreur : "remote origin already exists"

```bash
git remote remove origin
git remote add origin https://github.com/Hijaraa/Tp31--microservice-spring-boot-avc-RabbitMQ.git
```

### Erreur : "Updates were rejected"

Le dépôt distant contient des fichiers que vous n'avez pas localement :

```bash
git pull origin main --allow-unrelated-histories
# Résoudre les conflits si nécessaire
git add .
git commit -m "Merge remote-tracking branch 'origin/main'"
git push -u origin main
```

### Erreur d'authentification

Si GitHub demande vos identifiants :
1. Utilisez un **Personal Access Token** (PAT) au lieu du mot de passe
2. Créez un PAT : GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
3. Sélectionnez les scopes : `repo`
4. Utilisez le token comme mot de passe lors du push

Ou configurez Git Credential Manager :
```bash
git config --global credential.helper wincred
```

## Vérification après le push

Vérifiez sur GitHub : https://github.com/Hijaraa/Tp31--microservice-spring-boot-avc-RabbitMQ

Vous devriez voir :
- ✅ README.md
- ✅ spring-rabbitmq-producer/
- ✅ spring-rabbitmq-consumer/
- ✅ messaging-producer/
- ✅ messaging-consumer/

