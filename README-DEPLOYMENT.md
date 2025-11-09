# Deployment Guide - Todo App Backend

This guide explains how to deploy the Todo App backend microservices using Docker and Jenkins.

## Architecture Overview

The application consists of 4 microservices:
- **Discovery Server** (Eureka) - Port 8761
- **API Gateway** - Port 8080
- **User Service** - Port 8081
- **Todo Service** - Port 8082

Each service uses PostgreSQL databases for data persistence.

## Prerequisites

1. **Docker** (version 20.10 or higher)
2. **Docker Compose** (version 2.0 or higher)
3. **Jenkins** (with Docker plugin installed)
4. **Maven** (for building, or use Maven wrapper)
5. **JDK 17**

## Local Deployment with Docker Compose

### Quick Start

1. **Build and start all services:**
   ```bash
   docker-compose up -d --build
   ```

2. **Check service status:**
   ```bash
   docker-compose ps
   ```

3. **View logs:**
   ```bash
   # All services
   docker-compose logs -f
   
   # Specific service
   docker-compose logs -f discovery-server
   docker-compose logs -f apigateway
   docker-compose logs -f user-service
   docker-compose logs -f todo-service
   ```

4. **Stop services:**
   ```bash
   docker-compose down
   ```

5. **Stop and remove volumes (clean slate):**
   ```bash
   docker-compose down -v
   ```

### Access Services

- **Eureka Dashboard:** http://localhost:8761
- **API Gateway:** http://localhost:8080
- **User Service:** http://localhost:8081
- **Todo Service:** http://localhost:8082

## Jenkins CI/CD Setup

### 1. Install Required Jenkins Plugins

- Docker Pipeline Plugin
- Docker Plugin
- Maven Integration Plugin
- Pipeline Plugin

### 2. Configure Jenkins Credentials

1. Go to **Jenkins Dashboard** → **Manage Jenkins** → **Credentials**
2. Add the following credentials:

   **Docker Registry Credentials:**
   - ID: `docker-registry-credentials`
   - Username: Your Docker registry username
   - Password: Your Docker registry password

   **Docker Registry URL (Optional):**
   - ID: `docker-registry-url`
   - Value: Your Docker registry URL (e.g., `docker.io`, `registry.example.com:5000`)

### 3. Configure Jenkins Tools

1. Go to **Manage Jenkins** → **Global Tool Configuration**
2. Configure:
   - **JDK 17** (name: `JDK17`)
   - **Maven** (name: `Maven`)

### 4. Create Jenkins Pipeline

1. **Create New Item** → **Pipeline**
2. **Pipeline Definition:** Select "Pipeline script from SCM"
3. **SCM:** Git
4. **Repository URL:** Your Git repository URL
5. **Script Path:** `Jenkinsfile`
6. **Save**

### 5. Pipeline Stages

The Jenkins pipeline includes:

1. **Checkout** - Clones the repository
2. **Build** - Compiles all microservices using Maven
3. **Test** - Runs unit tests
4. **Build Docker Images** - Creates Docker images for all services
5. **Push Docker Images** - Pushes images to registry (on main/master/develop branches)
6. **Deploy** - Deploys using docker-compose (on main/master branches)

## Manual Docker Build

### Build Individual Services

```bash
# Discovery Server
docker build -t todo-backend/discovery-server:latest -f discovery-server/Dockerfile .

# API Gateway
docker build -t todo-backend/apigateway:latest -f apigateway/Dockerfile .

# User Service
docker build -t todo-backend/user-service:latest -f user-service/Dockerfile .

# Todo Service
docker build -t todo-backend/todo-service:latest -f todo-service/Dockerfile .
```

### Run Individual Containers

```bash
# Discovery Server
docker run -d -p 8761:8761 --name discovery-server todo-backend/discovery-server:latest

# User Service (requires discovery-server and database)
docker run -d -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=docker \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/user-db \
  -e EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE=http://host.docker.internal:8761/eureka \
  --name user-service todo-backend/user-service:latest
```

## Environment Variables

### Docker Compose Environment Variables

You can customize the deployment by creating a `.env` file:

```env
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password
DISCOVERY_SERVER_PORT=8761
API_GATEWAY_PORT=8080
USER_SERVICE_PORT=8081
TODO_SERVICE_PORT=8082
```

Then update `docker-compose.yml` to use these variables.

## Troubleshooting

### Services Not Starting

1. **Check logs:**
   ```bash
   docker-compose logs [service-name]
   ```

2. **Verify network connectivity:**
   ```bash
   docker network inspect todo-network
   ```

3. **Check database connectivity:**
   ```bash
   docker-compose exec user-db psql -U postgres -d user-db
   ```

### Port Conflicts

If ports are already in use, modify the port mappings in `docker-compose.yml`:

```yaml
ports:
  - "8762:8761"  # Change host port from 8761 to 8762
```

### Database Connection Issues

Ensure:
- Database containers are healthy before services start
- Service names match in `application-docker.yaml` files
- Network configuration is correct

### Eureka Service Registration

If services don't register with Eureka:
- Check that `discovery-server` is running and healthy
- Verify `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE` environment variable
- Check service logs for connection errors

## Production Considerations

1. **Use Environment Variables:** Don't hardcode passwords in docker-compose.yml
2. **Use Secrets Management:** Use Docker secrets or external secret management
3. **Health Checks:** Implement proper health check endpoints
4. **Monitoring:** Add monitoring tools (Prometheus, Grafana)
5. **Logging:** Set up centralized logging (ELK stack)
6. **SSL/TLS:** Configure HTTPS for production
7. **Resource Limits:** Set CPU and memory limits in docker-compose.yml
8. **Backup Strategy:** Implement database backup procedures

## Scaling Services

To scale services:

```bash
# Scale user-service to 3 instances
docker-compose up -d --scale user-service=3

# Scale todo-service to 2 instances
docker-compose up -d --scale todo-service=2
```

Note: Ensure your load balancer (API Gateway) is configured to handle multiple instances.

## Cleanup

```bash
# Stop and remove containers
docker-compose down

# Remove containers, networks, and volumes
docker-compose down -v

# Remove all images
docker-compose down --rmi all
```

## Support

For issues or questions, please check:
- Service logs: `docker-compose logs [service-name]`
- Eureka dashboard: http://localhost:8761
- Docker status: `docker ps -a`

