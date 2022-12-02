# alura-food-rabbitmq

## Microsserviços na prática: mensageria com RabbitMQ


* docker-compose para o curso

```
version: "3.6"

services:
    rabbitmq:
        image: rabbitmq:3.10-management
        container_name: rabbitmq
        restart: always
        ports:
            - 5672:5672
            - 15672:15672
        volumes:
            - ./dados:/var/lib/rabbitmq/
        environment:
            - RABBITMQ_DEFAULT_USER=bruna
            - RABBITMQ_DEFAULT_PASS=alura
```
