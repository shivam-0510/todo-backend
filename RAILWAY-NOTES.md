# Railway Deployment Notes

## Docker Compose Files

This project has two docker-compose files:

1. **`docker-compose.yml`** - For local development
   - Includes Docker volumes for PostgreSQL data persistence
   - Use this for local development: `docker-compose up -d`

2. **`docker-compose.railway.yml`** - For Railway deployment
   - **No Docker volumes** (Railway handles persistence differently)
   - Railway automatically uses this file (configured in `railway.json`)
   - Railway may use ephemeral storage or Railway volumes for data persistence

## Why Two Files?

Railway has restrictions on Docker volumes in docker-compose files. Railway recommends:
- Using Railway's managed PostgreSQL service for production
- Or using Railway volumes (configured in Railway dashboard) instead of docker-compose volumes

For this deployment, we're using docker-compose with PostgreSQL containers, but without the volumes section to comply with Railway's requirements.

## Data Persistence on Railway

If you need persistent data storage on Railway:
1. **Option 1**: Use Railway's managed PostgreSQL service (recommended)
   - Add PostgreSQL service in Railway dashboard
   - Update connection strings in environment variables
   
2. **Option 2**: Use Railway volumes
   - Configure volumes in Railway dashboard
   - Mount them in your service configuration

3. **Option 3**: Current setup (ephemeral)
   - Data persists during service uptime
   - Data may be lost on service restart/redeploy
   - Suitable for development/testing

## Migration to Managed PostgreSQL (Recommended)

For production, consider migrating to Railway's managed PostgreSQL:

1. In Railway dashboard, add a PostgreSQL service
2. Railway will provide connection details
3. Update environment variables:
   - `SPRING_DATASOURCE_URL` - Use Railway PostgreSQL URL
   - `SPRING_DATASOURCE_USERNAME` - From Railway
   - `SPRING_DATASOURCE_PASSWORD` - From Railway
4. Remove `user-db` and `todo-db` services from docker-compose
5. Update service dependencies

## Current Configuration

- **Railway uses**: `docker-compose.railway.yml` (no volumes) - Railway will auto-detect this
- **Local uses**: `docker-compose.yml` (with volumes)
- **Note**: Railway auto-detects docker-compose files, no `railway.json` needed

## Deployment Options

### Option 1: Use docker-compose.railway.yml (Recommended)
1. Railway will auto-detect `docker-compose.railway.yml`
2. No volumes, Railway handles persistence

### Option 2: Rename for Railway
1. Temporarily rename `docker-compose.railway.yml` to `docker-compose.yml`
2. Deploy on Railway
3. Rename back after deployment (or keep separate branches)

### Option 3: Use docker-compose.yml
1. Railway will use `docker-compose.yml` but ignore volumes
2. Data persistence may be ephemeral

