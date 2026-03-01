#!/bin/bash

# Blog System Deployment Script for Ubuntu 24.04
# Installation directory: /usr/local/src/blog

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Configuration - All data stored in /usr/local/src/blog
APP_DIR="/usr/local/src/blog"
DOMAIN="${DOMAIN:-localhost}"
MYSQL_ROOT_PASSWORD="${MYSQL_ROOT_PASSWORD:-root123}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-blog123}"
JWT_SECRET="${JWT_SECRET:-blog-prod-secret-key-must-be-at-least-256-bits-long-for-hs256}"

echo "==================================="
echo "Blog System Deployment Script"
echo "Installation Directory: $APP_DIR"
echo "==================================="

# Check root
if [ "$EUID" -ne 0 ]; then
  echo -e "${RED}Please run as root${NC}"
  exit 1
fi

# Install Docker
install_docker() {
  echo -e "${YELLOW}Installing Docker...${NC}"

  if ! command -v docker &> /dev/null; then
    apt-get update
    apt-get install -y ca-certificates curl gnupg

    install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    chmod a+r /etc/apt/keyrings/docker.gpg

    echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

    apt-get update
    apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

    systemctl enable docker
    systemctl start docker

    echo -e "${GREEN}Docker installed successfully${NC}"
  else
    echo -e "${GREEN}Docker already installed${NC}"
  fi
}

# Create directories
create_directories() {
  echo -e "${YELLOW}Creating directories...${NC}"

  mkdir -p $APP_DIR
  mkdir -p $APP_DIR/web
  mkdir -p $APP_DIR/ssl

  echo -e "${GREEN}Created: $APP_DIR${NC}"
}

# Create .env file
create_env_file() {
  echo -e "${YELLOW}Creating .env file...${NC}"

  cat > $APP_DIR/.env << EOF
# MySQL Configuration
MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD
MYSQL_PASSWORD=$MYSQL_PASSWORD

# JWT Configuration
JWT_SECRET=$JWT_SECRET

# GitHub OAuth
GITHUB_CLIENT_ID=
GITHUB_CLIENT_SECRET=
GITHUB_REDIRECT_URI=https://$DOMAIN/api/auth/github/callback
EOF

  echo -e "${GREEN}.env file created${NC}"
}

# Configure firewall
configure_firewall() {
  echo -e "${YELLOW}Configuring firewall...${NC}"

  if command -v ufw &> /dev/null; then
    ufw --force enable
    ufw allow 22/tcp
    ufw allow 80/tcp
    ufw allow 443/tcp
    echo -e "${GREEN}Firewall configured${NC}"
  fi
}

# Print instructions
print_instructions() {
  echo ""
  echo -e "${GREEN}==================================="
  echo "Installation Complete!"
  echo "===================================${NC}"
  echo ""
  echo "All files will be stored in: $APP_DIR"
  echo ""
  echo "Next steps:"
  echo ""
  echo "1. Copy files to server:"
  echo "   scp docker-compose.yml root@$DOMAIN:$APP_DIR/"
  echo "   scp nginx.conf root@$DOMAIN:$APP_DIR/"
  echo "   scp init.sql root@$DOMAIN:$APP_DIR/"
  echo "   scp .env root@$DOMAIN:$APP_DIR/"
  echo "   scp blog-backend.jar root@$DOMAIN:$APP_DIR/"
  echo "   scp -r web/* root@$DOMAIN:$APP_DIR/web/"
  echo ""
  echo "2. Configure GitHub OAuth in $APP_DIR/.env"
  echo ""
  echo "3. Start services:"
  echo "   cd $APP_DIR && docker compose up -d"
  echo ""
  echo "4. View logs:"
  echo "   docker compose logs -f"
  echo ""
  echo "5. Stop services:"
  echo "   docker compose down"
  echo ""
}

# Main
main() {
  install_docker
  create_directories
  create_env_file
  configure_firewall
  print_instructions
}

main "$@"