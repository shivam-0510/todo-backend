# Quick Deployment Guide

## Prerequisites

Before deploying, make sure you have:
- **Docker Desktop** installed and running (for Windows)
- At least 4GB of free RAM
- Ports 8080, 8081, 8082, 8761, 5432, 5433 available

## Step 1: Verify Docker is Running

Open PowerShell and run:
```powershell
docker --version
docker-compose --version
```

If you see version numbers, you're good to go!

## Step 2: Deploy Everything (One Command!)

Navigate to the backend directory and run:

```powershell
docker-compose up -d --build
```

This will:
- Build Docker images for all 4 microservices
- Start 2 PostgreSQL databases
- Start all services in the correct order
- Take about 5-10 minutes the first time

## Step 3: Check if Everything is Running

```powershell
docker-compose ps
```

You should see all services with status "Up" or "Up (healthy)".

## Step 4: View Logs (Optional)

To see what's happening:
```powershell
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f discovery-server
docker-compose logs -f user-service
docker-compose logs -f todo-service
docker-compose logs -f apigateway
```

Press `Ctrl+C` to exit log view.

## Step 5: Access Your Services

Once everything is running, you can access:

- **Eureka Dashboard (Service Discovery):** http://localhost:8761
- **API Gateway:** http://localhost:8080
- **User Service:** http://localhost:8081
- **Todo Service:** http://localhost:8082

## Common Commands

### Stop all services:
```powershell
docker-compose down
```

### Stop and remove all data (fresh start):
```powershell
docker-compose down -v
```

### Restart a specific service:
```powershell
docker-compose restart user-service
```

### Rebuild and restart everything:
```powershell
docker-compose up -d --build
```

## Troubleshooting

### Port Already in Use
If you get an error about ports being in use:
1. Check what's using the port: `netstat -ano | findstr :8080`
2. Stop the conflicting service or change the port in `docker-compose.yml`

### Services Not Starting
1. Check logs: `docker-compose logs [service-name]`
2. Make sure Docker Desktop is running
3. Ensure you have enough disk space

### Database Connection Issues
Wait a bit longer - databases need time to initialize. Check with:
```powershell
docker-compose logs user-db
docker-compose logs todo-db
```

## What's Running?

Your deployment includes:
- **Discovery Server** (Eureka) - Service registry
- **User Service** - User management
- **Todo Service** - Todo item management  
- **API Gateway** - Single entry point for all requests
- **PostgreSQL Databases** - Data storage

All services communicate through Docker's internal network.

