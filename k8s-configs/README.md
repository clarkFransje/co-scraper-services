# Kubernetes Configuration for AKS Environment

## Overview
This repository contains Kubernetes configurations for the AKS environment, including scaling and ingress service details.

## Highlights

### Horizontal Pod Autoscaler (HPA)
- **File:** `hpa-services.yaml`
- **Purpose:** Automatically scales the number of pods based on CPU/memory usage.
- **Configuration:** Defines target CPU utilization and minimum/maximum pod limits.

### Ingress Service
- **Directory:** `ingress/`
- **Purpose:** Manages external access to the services within the cluster.
- **Features:**
  - **Routing:** Directs traffic to appropriate services based on host/path rules.
  - **TLS Support:** Secures connections using TLS certificates.

## Directory Structure
- **config-maps:** Contains ConfigMap definitions.
- **deployments:** Includes deployment manifests for various services.
- **ingress:** Configuration files for ingress services.
- **network-policies:** Defines network policies for security.
- **services:** Service definitions to expose deployments.

## Usage
1. Apply ConfigMaps: `kubectl apply -f config-maps/`
2. Deploy services: `kubectl apply -f deployments/`
3. Set up ingress: `kubectl apply -f ingress/`
4. Apply network policies: `kubectl apply -f network-policies/`
5. Configure HPA: `kubectl apply -f hpa-services.yaml`
