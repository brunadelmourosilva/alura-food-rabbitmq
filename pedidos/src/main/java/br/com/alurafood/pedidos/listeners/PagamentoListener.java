package br.com.alurafood.pedidos.listeners;

import br.com.alurafood.pedidos.messages.PagamentoMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PagamentoListener {



    @RabbitListener(queues = "#{'${queue.pagamentos.pedido}'}")
    public void recebeMensagem(@Payload PagamentoMessage pagamentoMessage) {
        String mensagem = """
                Dados do pagamento: %s
                Número do pedido: %s
                Valor R$: %s
                Status: %s
                     """
                .formatted(
                pagamentoMessage.getId(),
                pagamentoMessage.getPedidoId(),
                pagamentoMessage.getValor(),
                pagamentoMessage.getStatus());

        System.out.println("Recebi a mensagem " + mensagem);
    }
}
