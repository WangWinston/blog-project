#!/bin/bash

# Backup script for Blog System
# Usage: ./backup.sh

set -e

APP_DIR="/usr/local/src/blog"
BACKUP_DIR="/usr/local/src/blog/backups"
DATE=$(date +%Y%m%d_%H%M%S)

mkdir -p $BACKUP_DIR

echo "Creating backup..."

# Backup MySQL
echo "Backing up MySQL..."
docker exec blog-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD:-root123} blog > $BACKUP_DIR/blog_$DATE.sql
gzip $BACKUP_DIR/blog_$DATE.sql

# Backup .env
cp $APP_DIR/.env $BACKUP_DIR/.env_$DATE

# Clean old backups (keep last 7 days)
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

echo "Backup completed: $BACKUP_DIR/blog_$DATE.sql.gz"
ls -la $BACKUP_DIR/