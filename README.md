# co-scraper-services

## Description
The `co-scraper-services` repository contains multiple microservices designed to handle different aspects of a web scraping solution. These services include customer management, product management, store management, and more, all orchestrated using Kubernetes and Docker.

## Installation
To set up and run the services locally, follow these steps:

1. **Clone the repository:**
    ```bash
    git clone https://github.com/clarkFransje/co-scraper-services.git
    cd co-scraper-services
    ```

2. **Build and run using Docker Compose:**
    ```bash
    docker-compose up --build
    ```

## Usage
Each service is designed to run independently and can be accessed via their respective endpoints. Here is a brief overview of how to interact with each service:

- **Customer Service:**
    Handles customer-related operations.
    ```bash
    http://localhost:8081/customers
    ```

- **Product Service:**
    Manages product-related data.
    ```bash
    http://localhost:8082/products
    ```

- **Store Service:**
    Manages store-related operations.
    ```bash
    http://localhost:8083/stores
    ```

- **Customer-Product Service:**
    Manages interactions between customers and products.
    ```bash
    http://localhost:8084/customer-products
    ```

## Services Overview
### Customer Service
This service handles all CRUD operations related to customers.

### Product Service
This service is responsible for managing product data and interactions.

### Store Service
This service manages the store data and related operations.

### Customer-Product Service
This service handles the relationships and interactions between customers and products.

### Kubernetes Configs
Configuration files for deploying the services in a Kubernetes cluster are located in the `k8s-configs` directory.

## Contributing
Contributions are welcome! Please fork the repository and create a pull request with your changes. Ensure that your code follows the project's coding guidelines and includes necessary tests.

1. Fork the repository
2. Create a new branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Create a pull request

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

Feel free to customize this template based on the specifics of your project and additional details you might want to include.
