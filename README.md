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

---

### Alta disponibilidade - criando um cluster

O objetivo é criar instâncias em um cluster, para que, caso haja falha em uma instância, as demais possam recuperar as mensagens das filas em que essa isntância por algum motivo, deixou de funcionar. Dessa forma, aplicamos o conceito de alta disponibilidade em nosso sistema, evitando a perca de mensagens.

- criar uma network no docker, para que as instâncias possam se comunicar:

```
docker network create alura
```

- criar as instâncias, em diferentes portas:

```
docker run -d --rm --net alura --hostname rabbit1 --name rabbit1 -p 8085:15672 -e RABBITMQ_ERLANG_COOKIE=alura_secret rabbitmq:3.10-management
```

```
docker run -d --rm --net alura --hostname rabbit2 --name rabbit2 -p 8086:15672 -e RABBITMQ_ERLANG_COOKIE=alura_secret rabbitmq:3.10-management
```

```
docker run -d --rm --net alura --hostname rabbit3 --name rabbit3 -p 8087:15672 -e RABBITMQ_ERLANG_COOKIE=alura_secret rabbitmq:3.10-management
```

*sobre a variável de ambiente RABBITMQ_ERLANG_COOKIE*

>Setar todos os contâiners com o mesmo Erlang Cookie necessário para a comunicação e espelhamento das mensagens. Então, para que as instâncias se comuniquem dentro do cluster, o cookie seja o mesmo para todas.

- unindo os contâiners **rabbit2** e **rabbit3** ao contâiner **rabbit1**:

    - parando o contâiner rabbit2
        ```
        docker exec -it rabbit2 rabbitmqctl stop_app
        ```
        
    - resetando as configurações do rabbit2
        ```
        docker exec -it rabbit2 rabbitmqctl reset
        ```

    - unindo o contâiner rabbit2 ao rabbit1
        ```
        docker exec -it rabbit2 rabbitmqctl join_cluster rabbit@rabbit1
        ```
        
    - startando o contâiner rabbit2
        ```
        docker exec -it rabbit2 rabbitmqctl start_app
        ```
o mesmo se aplica ao contâiner rabbit3:

```
docker exec -it rabbit3 rabbitmqctl stop_app
docker exec -it rabbit3 rabbitmqctl reset
docker exec -it rabbit3 rabbitmqctl join_cluster rabbit@rabbit1
docker exec -it rabbit3 rabbitmqctl start_app
```

visualizar os resultados esperados:

```
http://localhost:8085
```

---

Neste ponto, se criarmos uma fila no node rabbit2, por exemplo, e postarmos uma mensagem nessa fila, veremos que, se pararmos essa instância e depois, startarmos ela novamente, a mensagem foi perdida, pois ainda as demais instâncias não estão configuradas. Para resolver esse problema, basta adicionar a policy **HA mode**:

- Na interface do RabbitMQ -> Admin -> Policies
    - Adicionar:
    
    ![image](https://user-images.githubusercontent.com/61791877/210120799-62769b29-adf9-4622-9eab-e4e37a0ffb22.png)

```
Add / update a policy:

Name: ha
Pattern: .*
Apply to: Exchanges and queues
Priority: vazio
Definition: ha mode = all
```

- Para testar, basta publicar a mensagem na fila, e depois, dar um stop no contâiner com ```docker exec -it rabbit2 rabbitmqctl stop_app```. Veja que o **rabbit1** assumiu a mensagem que havia sido publica na fila do contâiner **rabbit2**

![image](https://user-images.githubusercontent.com/61791877/210120871-b79b6c17-1ef0-49d5-9587-09f68309c6ab.png)

