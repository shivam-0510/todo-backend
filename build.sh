#!/bin/bash

# Build script for all microservices
# Usage: ./build.sh [service-name]
# If no service name is provided, all services will be built

set -e

SERVICES=("discovery-server" "apigateway" "user-service" "todo-service")
SERVICE_NAME=$1

build_service() {
    local service=$1
    echo "Building $service..."
    docker build -t todo-backend/${service}:latest -f ${service}/Dockerfile .
    echo "$service built successfully!"
}

if [ -z "$SERVICE_NAME" ]; then
    echo "Building all services..."
    for service in "${SERVICES[@]}"; do
        build_service "$service"
    done
    echo "All services built successfully!"
else
    if [[ " ${SERVICES[@]} " =~ " ${SERVICE_NAME} " ]]; then
        build_service "$SERVICE_NAME"
    else
        echo "Error: Unknown service '$SERVICE_NAME'"
        echo "Available services: ${SERVICES[*]}"
        exit 1
    fi
fi

