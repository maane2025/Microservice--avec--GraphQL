# Web Service GraphQL - Bank Account Management

## Description du Projet

Ce projet est un micro-service de gestion de comptes bancaires développé avec Spring Boot. Il expose deux types d'API :
- **API REST** classique pour les opérations CRUD
- **API GraphQL** pour des requêtes flexibles et optimisées

Le projet suit les bonnes pratiques de développement avec une architecture en couches (Controller, Service, Repository) et utilise des DTOs avec des Mappers pour la séparation des préoccupations.


## Technologies Utilisées

- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Data JPA** - Couche de persistance
- **Spring Data REST** - Exposition automatique des repositories
- **Spring GraphQL** - API GraphQL
- **H2 Database** - Base de données en mémoire
- **Lombok** - Réduction du code boilerplate
- **SpringDoc OpenAPI** - Documentation Swagger
- **Maven** - Gestion des dépendances

## Structure du Projet

```
src/main/java/com/mmaane/glsid/webservicegraphql/
├── controller/
│   ├── BankAccountController.java          # REST Controller
│   └── BankAccountGraphQLController.java   # GraphQL Controller
├── dto/
│   ├── ReceiveAccount.java                 # DTO pour les réponses
│   ├── RequestAccount.java                 # DTO pour les requêtes
│   └── UpdateBankAccountRequest.java       # DTO pour les mises à jour
├── entity/
│   └── BankAccount.java                    # Entité JPA
├── mapper/
│   └── BankAccountMapper.java              # Mapper Entity <-> DTO
├── repository/
│   └── BankAccountRepo.java                # Repository Spring Data
├── service/
│   ├── BankAccountService.java             # Interface Service
│   └── BankAccountImpl.java                # Implémentation Service
└── WebServiceGraphQlApplication.java       # Classe principale

src/main/resources/
└── graphql/
    └── schema.graphqls                     # Schéma GraphQL
```

## Entités

### BankAccount (Entité JPA)

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String number;
    private String owner;
}
```

### DTOs

**ReceiveAccount** - DTO pour les réponses :
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveAccount {
    private Long id;
    private String name;
    private String number;
    private String owner;
}
```

**RequestAccount** - DTO pour les requêtes :
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAccount {
    private Long id;
    private String name;
    private String number;
    private String owner;
}
```

## API REST

### REST Controller

```java
@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public List<ReceiveAccount> getAllAccounts() {
        return bankAccountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ReceiveAccount getAccountById(@PathVariable Long id) {
        return bankAccountService.getAccountById(id);
    }

    @PostMapping
    public ReceiveAccount addAccount(@RequestBody RequestAccount requestAccount) {
        return bankAccountService.addAccount(requestAccount)
                .orElseThrow(() -> new RuntimeException("Failed to save account"));
    }

    @DeleteMapping("/{id}")
    public boolean deleteAccount(@PathVariable Long id) {
        return bankAccountService.deleteAccount(id);
    }
}
```

### Endpoints REST

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/accounts` | Récupérer tous les comptes |
| GET | `/api/accounts/{id}` | Récupérer un compte par ID |
| POST | `/api/accounts` | Créer un nouveau compte |
| DELETE | `/api/accounts/{id}` | Supprimer un compte |

### Exemple de requête REST (POST)

```json
POST http://localhost:8080/api/accounts
Content-Type: application/json

{
    "name": "Compte Courant",
    "number": "123456789",
    "owner": "John Doe"
}
```

## API GraphQL

### GraphQL Controller

```java
@Controller
@RequestMapping("/graphql")
@RequiredArgsConstructor
public class BankAccountGraphQLController {
    private final BankAccountService bankAccountService;

    @QueryMapping
    public List<ReceiveAccount> getAllAccounts() {
        return bankAccountService.getAllAccounts();
    }

    @MutationMapping
    public ReceiveAccount addAccount(@Argument RequestAccount bankAccount) {
        return bankAccountService.addAccount(bankAccount)
                .orElseThrow(() -> new RuntimeException("Save failed"));
    }
}
```

### Schéma GraphQL

```graphql
type ReceiveAccount {
    id: ID
    name: String
    number: String
    owner: String
}

type Query {
    getAllAccounts: [ReceiveAccount]
    getAccountById(id: ID): ReceiveAccount
}

type Mutation {
    addAccount(bankAccount: RequestAccount): ReceiveAccount
}

input RequestAccount {
    name: String
    number: String
    owner: String
}
```

### Exemples de requêtes GraphQL

#### Query - Récupérer tous les comptes

```graphql
query {
    getAllAccounts {
        id
        name
        number
        owner
    }
}
```

#### Query - Récupérer un compte par ID

```graphql
query {
    getAccountById(id: "1") {
        id
        name
        owner
    }
}
```

#### Mutation - Créer un nouveau compte

```graphql
mutation {
    addAccount(bankAccount: {
        name: "Compte Épargne"
        number: "987654321"
        owner: "Jane Smith"
    }) {
        id
        name
        number
        owner
    }
}
```

## Couche Service

### Interface BankAccountService

```java
public interface BankAccountService {
    Optional<ReceiveAccount> addAccount(RequestAccount requestAccount);
    boolean deleteAccount(Long id);
    ReceiveAccount updateAccount(Long id, RequestAccount requestAccount);
    List<ReceiveAccount> getAllAccounts();
    ReceiveAccount getAccountById(Long id);
}
```

### Implémentation BankAccountImpl

```java
@Service
public class BankAccountImpl implements BankAccountService {

    BankAccountRepo bankAccountRepo;
    BankAccountMapper bankAccountMapper;

    public BankAccountImpl(BankAccountRepo bankAccountRepo, BankAccountMapper bankAccountMapper) {
        this.bankAccountRepo = bankAccountRepo;
        this.bankAccountMapper = bankAccountMapper;
    }

    @Override
    public List<ReceiveAccount> getAllAccounts() {
        return bankAccountRepo.findAll()
                .stream()
                .map(bankAccountMapper::toDto)
                .toList();
    }

    @Override
    public ReceiveAccount getAccountById(Long id) {
        return bankAccountRepo.findById(id)
                .map(bankAccountMapper::toDto)
                .orElse(null);
    }

    @Override
    public Optional<ReceiveAccount> addAccount(RequestAccount requestAccount) {
        BankAccount bankAccount = bankAccountMapper.toEntity(requestAccount);
        bankAccountRepo.save(bankAccount);
        ReceiveAccount receiveAccount = bankAccountMapper.toDto(bankAccount);
        return Optional.ofNullable(receiveAccount);
    }

    @Override
    public boolean deleteAccount(Long id) {
        if (bankAccountRepo.existsById(id)) {
            bankAccountRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public ReceiveAccount updateAccount(Long id, RequestAccount requestAccount) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepo.findById(id);
        if (bankAccountOptional.isPresent()) {
            BankAccount bankAccount = bankAccountOptional.get();
            bankAccount.setName(requestAccount.getName());
            bankAccount.setNumber(requestAccount.getNumber());
            bankAccount.setOwner(requestAccount.getOwner());
            bankAccountRepo.save(bankAccount);
            return bankAccountMapper.toDto(bankAccount);
        }
        return null;
    }
}
```

## Repository

```java
@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
}
```

## Démarrage du Projet

### Prérequis
- Java 17 ou supérieur
- Maven 3.6+

### Installation et Exécution

1. Cloner le projet
```bash
git clone <repository-url>
cd Web-service-GraphQL
```

2. Compiler le projet
```bash
mvn clean install
```

3. Lancer l'application
```bash
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

## Documentation et Tests

### Swagger UI
Accéder à la documentation Swagger :
```
http://localhost:8080/swagger-ui.html
```

### GraphQL Playground
Accéder à l'interface GraphQL :
```
http://localhost:8080/graphiql
```

### H2 Console
Accéder à la console H2 :
```
http://localhost:8080/h2-console
```

---

## By

**Mmaane Glsid**