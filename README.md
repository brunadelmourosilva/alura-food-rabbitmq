# alura-food-rabbitmq

## Microsserviços na prática: mensageria com RabbitMQ


* docker run

```
# latest RabbitMQ 3.11
docker run -d --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.11-management
```

* docker-compose

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

* instância para EC2 da AWS
 
```
54.175.14.152
```
---

* RabbitMQ Simulator

```
http://tryrabbitmq.com/
```

---

### Estrutura para o envio e recebimento de mensagens

![image](https://user-images.githubusercontent.com/61791877/206879510-52d7e4d0-1039-4362-91bc-6e598df2a04a.png)


