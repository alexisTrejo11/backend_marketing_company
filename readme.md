# Marketing Company Backend - CRM & Marketing Management System

[![Java](https://img.shields.io/badge/Java-23-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![GraphQL](https://img.shields.io/badge/GraphQL-API-E10098.svg)](https://graphql.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)

## 📋 Overview

A comprehensive enterprise-grade backend system for managing **Customer Relationship Management (CRM)** and **Marketing Campaigns**. This system provides a complete solution for tracking customer interactions, managing deals, orchestrating marketing campaigns, and analyzing performance metrics through a modern GraphQL API.

### Key Features

- 🔐 **Authentication & Authorization** - JWT-based secure authentication with role-based access control
- 👥 **Customer Management** - Complete customer company profiles with contact persons and contracts
- 💼 **CRM Module** - Opportunities, quotes, deals, tasks, and interaction tracking
- 📊 **Marketing Campaigns** - Full campaign lifecycle management with multi-channel support
- 📈 **Analytics & Metrics** - Real-time campaign performance and ROI tracking
- 🧪 **A/B Testing** - Marketing experiment management and analysis
- 🔌 **GraphQL API** - Type-safe, flexible, and efficient API
- 🐳 **Docker Ready** - Containerized deployment with Docker Compose
- 📝 **Audit Logging** - Comprehensive audit trail for all operations

## 🏗️ Architecture

This project follows **Hexagonal Architecture** (Ports & Adapters) with **Domain-Driven Design (DDD)** principles.

```
src/main/java/at/backend/MarketingCompany/
├── account/              # Authentication & User Management
│   ├── auth/            # JWT authentication, sessions
│   └── user/            # User management
├── config/              # Application configuration
│   ├── cors/           # CORS policies
│   ├── logging/        # Audit logging
│   ├── ratelimit/      # API rate limiting
│   ├── GraphQLConfig   # GraphQL setup
│   ├── RedisConfig     # Cache configuration
│   └── SecurityConfig  # Security policies
├── crm/                 # CRM Business Domain
│   ├── deal/           # Closed contracts
│   ├── interaction/    # Customer interactions
│   ├── opportunity/    # Sales opportunities
│   ├── quote/          # Price quotations
│   ├── servicePackage/ # Service offerings
│   └── tasks/          # CRM tasks
├── customer/            # Customer Management
│   └── core/           # Customer domain logic
├── marketing/           # Marketing Domain
│   ├── ab_test/        # A/B testing
│   ├── activity/       # Campaign activities
│   ├── asset/          # Marketing assets
│   ├── attribution/    # Attribution tracking
│   ├── campaign/       # Campaign management
│   ├── channel/        # Marketing channels
│   ├── interaction/    # Campaign interactions
│   ├── metric/         # Performance metrics
│   └── target/         # Target audiences
└── shared/              # Shared components
    ├── domain/         # Base domain entities
    ├── dto/            # Data transfer objects
    ├── exception/      # Exception handling
    └── graphql/        # GraphQL utilities
```

### Architecture Layers

Each domain module follows this structure:

```
module/
├── core/                  # Domain Layer (Business Logic)
│   ├── domain/           # Entities, Value Objects, Aggregates
│   ├── application/      # Use Cases, Services
│   └── port/            # Interfaces (Input/Output Ports)
└── adapter/              # Infrastructure Layer
    ├── input/           # Controllers (GraphQL Resolvers)
    └── output/          # Repositories, External Services
```

**Benefits:**

- **Clean separation** of business logic from infrastructure
- **Testable** - Core logic independent of frameworks
- **Flexible** - Easy to swap implementations
- **Maintainable** - Clear boundaries and responsibilities

## 🗄️ Database Schema

### Core Tables

| Module             | Tables                                                                                                                                          | Description                            |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------- |
| **Authentication** | `users`, `user_roles`, `auth_sessions`                                                                                                          | User authentication and authorization  |
| **Customer**       | `customer_companies`, `contact_persons`, `company_addresses`, `contracts`                                                                       | Customer data management               |
| **CRM**            | `opportunities`, `service_packages`, `quotes`, `deals`, `tasks`, `interactions`                                                                 | Sales pipeline and customer engagement |
| **Marketing**      | `marketing_campaigns`, `marketing_channels`, `campaign_activities`, `campaign_metrics`, `campaign_attributions`, `marketing_assets`, `ab_tests` | Campaign management and analytics      |

### Database Migration

This project uses **Flyway** for version-controlled database migrations:

```
src/main/resources/db/migration/
├── V1__users_schema.sql              # User authentication
├── V2__customer_companies_schema.sql  # Customer management
├── V3__opportunities_service_package_schema.sql
├── V4__crm_deals_schema.sql          # CRM deals
├── V5__crm_quotes_schema.sql         # Price quotes
├── V6__crm_tasks_interactions_schema.sql
├── V7__crm_indexes.sql               # Performance indexes
├── V8__insert_crm_demo_data.sql      # Sample data
├── V9__comment_views.sql             # Views
├── V10__marketing_management_table.sql # Marketing tables
└── V11__inset_marketing_management_demo_data.sql
```

## 🚀 Quick Start

### Prerequisites

- **Java 23** (JDK)
- **Gradle 8.11+**
- **Docker & Docker Compose** (for containerized deployment)
- **PostgreSQL 16** (if running locally)
- **Redis 7** (if running locally)

### 1. Clone Repository

```bash
git clone <repository-url>
cd backend_marketing_company
```

### 2. Environment Configuration

Create a `.env` file in the project root:

```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=marketing_company_db
DB_USERNAME=postgres
DB_PASSWORD=postgres

# PostgreSQL Docker Config
POSTGRES_DB=marketing_company_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT Configuration
JWT_SECRET=your-secret-key-change-this-in-production
JWT_ACCESS_TOKEN_EXPIRATION=3600000
JWT_REFRESH_TOKEN_EXPIRATION=2592000000

# Application Configuration
SPRING_PROFILES_ACTIVE=dev
```

### 3. Run with Docker (Recommended)

```bash
# Build and start all services
docker-compose up -d

# Check logs
docker-compose logs -f app

# Stop services
docker-compose down
```

The application will be available at:

- **GraphQL API**: http://localhost:8080/graphql
- **GraphiQL IDE**: http://localhost:8080/graphiql

### 4. Run Locally (Development)

```bash
# Start PostgreSQL and Redis with Docker
docker-compose up -d postgres redis

# Run application
./gradlew bootRun

# Or build and run JAR
./gradlew bootJar
java -jar build/libs/*.jar
```

## 🔌 API Documentation

### GraphQL Endpoint

**URL:** `http://localhost:8080/graphql`

**GraphiQL IDE:** `http://localhost:8080/graphiql`

### Schema Organization

```
graphql/
├── schema.graphqls           # Base types (Query, Mutation, Subscription)
├── common/                   # Shared types
│   ├── scalars.graphqls     # Custom scalars (Date, DateTime, etc.)
│   ├── pagination.graphqls  # Pagination types
│   └── common-types.graphqls
├── account/                  # Authentication
│   ├── auth.graphqls        # Login, signup, refresh
│   └── user.graphqls        # User management
├── crm/                      # CRM operations
│   ├── company.graphqls
│   ├── opportunity.graphqls
│   ├── quote.graphqls
│   ├── deal.graphqls
│   ├── task.graphqls
│   └── interaction.graphqls
└── marketing/                # Marketing operations
    ├── campaign.graphqls
    ├── channel.graphqls
    ├── activity.graphql
    ├── metric.graphql
    ├── attribution.graphql
    ├── asset.graphql
    ├── ab-test.graphql
    └── target.graphql
```

### Example Queries

#### Authentication

```graphql
# Sign Up
mutation {
  signUp(
    input: {
      email: "user@example.com"
      password: "SecurePass123"
      firstName: "John"
      lastName: "Doe"
      gender: MALE
      dateOfBirth: "1990-01-15"
      phoneNumber: "+1234567890"
    }
  ) {
    accessToken
    refreshToken
    user {
      id
      email
      firstName
      lastName
    }
  }
}

# Login
mutation {
  login(input: { email: "user@example.com", password: "SecurePass123" }) {
    accessToken
    refreshToken
    user {
      id
      email
    }
  }
}
```

#### CRM Operations

```graphql
# Get Opportunities
query {
  opportunities(page: 0, size: 10) {
    content {
      id
      title
      status
      estimatedValue
      expectedCloseDate
      customerCompany {
        companyName
      }
    }
    totalElements
  }
}

# Create Deal
mutation {
  createDeal(
    input: {
      opportunityId: "123"
      servicePackageIds: ["1", "2"]
      startDate: "2026-01-15"
    }
  ) {
    id
    status
    finalAmount
  }
}
```

#### Marketing Campaigns

```graphql
# Get Campaigns
query {
  campaigns(filters: { status: ACTIVE }, page: 0, size: 10) {
    content {
      id
      name
      campaignType
      status
      budget {
        totalBudget
        spentAmount
        remainingBudget
      }
      metrics {
        totalImpressions
        totalClicks
        conversions
        roi
      }
    }
  }
}

# Create Campaign
mutation {
  createCampaign(
    input: {
      name: "Summer Sale 2026"
      description: "Promotional campaign"
      campaignType: CONVERSION
      totalBudget: 50000.00
      startDate: "2026-06-01"
      endDate: "2026-08-31"
      channelIds: ["1", "2", "3"]
    }
  ) {
    id
    name
    status
  }
}
```

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport

# Run specific test class
./gradlew test --tests "CustomerCompanyTest"
```

## 📊 Technologies

### Core Technologies

- **Java 23** - Latest LTS Java version
- **Spring Boot 3.4.2** - Application framework
- **Spring Data JPA** - Data persistence
- **Hibernate** - ORM
- **PostgreSQL 16** - Primary database
- **Redis 7** - Caching layer
- **Flyway** - Database migrations

### API & GraphQL

- **Spring for GraphQL** - GraphQL integration
- **GraphQL Java Tools** - Schema-first GraphQL
- **GraphQL Extended Scalars** - Custom scalar types

### Security

- **Spring Security** - Authentication & authorization
- **JWT (JJWT)** - Token-based authentication
- **BCrypt** - Password hashing

### Development Tools

- **Lombok** - Reduce boilerplate code
- **Gradle** - Build automation
- **Docker & Docker Compose** - Containerization
- **Testcontainers** - Integration testing

## 📁 Project Structure

```
backend_marketing_company/
├── src/
│   ├── main/
│   │   ├── java/at/backend/MarketingCompany/  # Source code
│   │   └── resources/
│   │       ├── application.yml                 # Main config
│   │       ├── application-test.yml           # Test config
│   │       ├── logback-spring.xml             # Logging config
│   │       ├── db/migration/                  # Flyway migrations
│   │       └── graphql/                       # GraphQL schemas
│   └── test/                                   # Test code
├── build.gradle                                # Build configuration
├── settings.gradle                             # Gradle settings
├── dockerfile                                  # Docker image definition
├── docker-compose.yml                          # Multi-container setup
├── .env                                        # Environment variables
├── README.md                                   # This file
├── ARCHITECTURE.md                             # Architecture details
├── API.md                                      # API documentation
└── DEPLOYMENT.md                               # Deployment guide
```

## 📚 Additional Documentation

- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Detailed architecture and design patterns
- **[API.md](API.md)** - Complete GraphQL API reference
- **[DEPLOYMENT.md](DEPLOYMENT.md)** - Production deployment guide
- **[ENTITIES.md](ENTITIES.md)** - Database schema and entity documentation

## 🔒 Security Features

- **JWT Authentication** - Secure token-based auth
- **Password Encryption** - BCrypt hashing
- **Role-Based Access Control** - Fine-grained permissions
- **Rate Limiting** - API request throttling
- **CORS Configuration** - Cross-origin request handling
- **SQL Injection Protection** - Parameterized queries
- **Audit Logging** - Complete audit trail

## 🌐 Environment Profiles

- **`dev`** - Development environment
- **`test`** - Testing environment
- **`docker`** - Docker container environment
- **`prod`** - Production environment

Configure via `SPRING_PROFILES_ACTIVE` environment variable.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Contact & Support

For questions and support, please contact the development team.

---

**Built with ❤️ using Spring Boot and GraphQL**
