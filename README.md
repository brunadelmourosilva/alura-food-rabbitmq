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

---

### Plugin shovel

Consiste em uma das soluções para mover mensagens da fila dlq para outras filas, por exemplo.

Para ativá-lo, basta utilizar o comando no contâiner docker: ``` rabbitmq-plugins enable rabbitmq_shovel rabbitmq_shovel_management ```

>Existem dois tipos de shovel: estático e dinâmico. O estático é o que é declarado via arquivo de configuração e deve ser criado/atualizado antes que o nó seja inicializado para ter as configurações válidas. O dinâmico é definido através de parâmetros em tempo de execução. Recomenda-se que seja priorizada a utilização do tipo dinâmico para facilitar a administração e evitar a necessidade de reiniciar as instâncias do RabbitMQ.

- Link para ativar shovel estático: https://www.rabbitmq.com/configure.html#config-file

- Link para ativar shovel dinâmico: https://www.rabbitmq.com/shovel-dynamic.html

