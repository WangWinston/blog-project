#!/bin/bash

# Quick deploy script - restart services after updating files
# Usage: ./quick-deploy.sh

set -e

APP_DIR="/usr/local/src/blog"

echo "Starting quick deployment..."

cd $APP_DIR

# Stop services
docker compose down

# Restart services
docker compose up -d

# Wait and check
sleep 5
docker compose ps

echo ""
echo "Deployment completed!"
echo "Check logs: docker compose logs -f"