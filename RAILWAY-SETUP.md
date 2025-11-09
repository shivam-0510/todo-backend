# Railway Setup Instructions

## Quick Fix for Railway Deployment

Railway auto-detects docker-compose files. To deploy:

### Option 1: Use docker-compose.railway.yml (Recommended)

1. **Before deploying**, temporarily rename the file:
   ```bash
   # In the backend directory
   mv docker-compose.yml docker-compose.local.yml
   mv docker-compose.railway.yml docker-compose.yml
   ```

2. **Deploy on Railway:**
   - Railway will auto-detect `docker-compose.yml`
   - All services will deploy correctly
   - No volumes (Railway handles persistence)

3. **After deployment** (optional - for local development):
   ```bash
   mv docker-compose.yml docker-compose.railway.yml
   mv docker-compose.local.yml docker-compose.yml
   ```

### Option 2: Keep Both Files (Alternative)

1. Railway will try to auto-detect `docker-compose.yml` first
2. If you want Railway to use `docker-compose.railway.yml`, you can:
   - Rename it to `docker-compose.yml` before pushing to GitHub
   - Or configure it in Railway dashboard (if supported)

### Option 3: Use docker-compose.yml Directly

Railway will use `docker-compose.yml` but will ignore the volumes section automatically. This works but data persistence may be ephemeral.

## Recommended Approach

**For Railway deployment, use `docker-compose.railway.yml`:**

1. The file has no volumes (Railway-compliant)
2. All other configurations are identical
3. Simply rename it to `docker-compose.yml` before deploying

## What Changed

- ❌ Removed `railway.json` (was causing Railway to parse docker-compose as Dockerfile)
- ✅ Railway now auto-detects docker-compose files
- ✅ `docker-compose.railway.yml` is ready for Railway (no volumes)
- ✅ `docker-compose.yml` is ready for local development (with volumes)

## Next Steps

1. Rename `docker-compose.railway.yml` to `docker-compose.yml`
2. Push to GitHub
3. Deploy on Railway
4. Railway will auto-detect and deploy all services

