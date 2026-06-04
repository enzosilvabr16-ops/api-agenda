package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.Exceptions.CategoriaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.Exceptions.TarefaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.components.PublisherComponent;
import br.com.cotiinformatica.api_agenda.dtos.CategoriaResponse;
import br.com.cotiinformatica.api_agenda.dtos.NotificacaoDto;
import br.com.cotiinformatica.api_agenda.dtos.TarefaRequest;
import br.com.cotiinformatica.api_agenda.dtos.TarefaResponse;
import br.com.cotiinformatica.api_agenda.entities.Tarefa;
import br.com.cotiinformatica.api_agenda.enums.Prioridade;

import br.com.cotiinformatica.api_agenda.repositories.CategoriaRepository;
import br.com.cotiinformatica.api_agenda.repositories.TarefaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PublisherComponent publisherComponent;

    @Autowired
    private ObjectMapper objectMapper;

    //Serviço para realizar o cadastro da tarefa
    @Transactional
    public TarefaResponse cadastrar(TarefaRequest request, UUID usuarioId) {

        //Buscar a categoria no banco de dados através do ID
        var categoria = categoriaRepository
                .findByIdAndUsuarioId(request.categoriaId(), usuarioId)
                .orElseThrow(CategoriaNaoEncontradaException::new);

        //Capturar os dados da tarefa
        var tarefa = new Tarefa();
        tarefa.setTitulo(request.titulo());
        tarefa.setData(request.data());
        tarefa.setHora(request.hora());
        tarefa.setPrioridade(Prioridade.valueOf(request.prioridade()));
        tarefa.setFinalizado(request.finalizado());
        tarefa.setCategoria(categoria); //Relacionamento da tarefa com a sua categoria
        tarefa.setUsuarioId(usuarioId); //Associar a tarefa ao usuário logado

        //Salvar a tarefa no banco de dados
        tarefaRepository.save(tarefa);

        //Enviar para a mensageria
        enviarParaMensageria(tarefa);

        //Retornar os dados da resposta
        return toResponse(tarefa);
    }

    public TarefaResponse atualizar(Integer id, TarefaRequest request, UUID usuarioId) {

        //Buscar no banco de dados a tarefa attravés do ID
        var tarefa = tarefaRepository
                .findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(TarefaNaoEncontradaException::new);

        //Buscar no banco de dados a categoria através do ID
        var categoria = categoriaRepository
                .findByIdAndUsuarioId(request.categoriaId(), usuarioId)
                .orElseThrow(CategoriaNaoEncontradaException::new);

        //Modificar os dados da tarefa
        tarefa.setTitulo(request.titulo());
        tarefa.setData(request.data());
        tarefa.setHora(request.hora());
        tarefa.setPrioridade(Prioridade.valueOf(request.prioridade()));
        tarefa.setFinalizado(request.finalizado());
        tarefa.setCategoria(categoria); //Relacionamento da tarefa com a sua categoria

        //Atualizando os dados da tarefa no banco de dados
        tarefaRepository.save(tarefa);

        //Retornar os dados da resposta
        return toResponse(tarefa);
    }

    //Método para excluir uma tarefa
    public TarefaResponse excluir(Integer id, UUID usuarioId) {

        //Buscar no banco de dados a tarefa attravés do ID
        var tarefa = tarefaRepository
                .findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(TarefaNaoEncontradaException::new);

        //Excluir a tarefa
        tarefaRepository.delete(tarefa);

        //Retornar os dados da resposta
        return toResponse(tarefa);
    }

    //Método para consultar as tarefas por datas e por usuário
    public List<TarefaResponse> consultar(LocalDate dataInicio, LocalDate dataFim, UUID usuarioId) {

        //Executando a consulta no banco de dados
        var tarefas = tarefaRepository.findByUsuarioIdAndDataBetween(usuarioId, dataInicio, dataFim);

        //Retornar os dados da resposta
        return tarefas.stream()
                .map(this::toResponse)
                .toList();
    }

    //Metodo pra obter 1 tarefa atraves do id
    public TarefaResponse obterPorId(Integer id, UUID usuarioId) {

        var tarefa = tarefaRepository
                .findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(TarefaNaoEncontradaException::new);
        return toResponse(tarefa);
    }


    private TarefaResponse toResponse(Tarefa tarefa) {
        return new TarefaResponse(
                tarefa.getId(), //Id da tarefa
                tarefa.getTitulo(), //Título da tarefa
                tarefa.getData(), //Data da tarefa,
                tarefa.getHora(), //Hora da tarefa
                tarefa.getPrioridade().toString(), //Prioridade da tarefa
                tarefa.getFinalizado(), //Finalizado?
                new CategoriaResponse(
                        tarefa.getCategoria().getId(), //Id da categoria
                        tarefa.getCategoria().getNome() //Nome da categoria
                )
        );
    }

    private void enviarParaMensageria(Tarefa tarefa) {
        try {

            //Copiar as informações da tarefa para o DTO (NotificacaoDto)
            var notificacao = new NotificacaoDto(
                    tarefa.getUsuarioId(),
                    "usuario@email.com", //Provisório!
                    tarefa.getTitulo(),
                    tarefa.getData().toString(),
                    tarefa.getHora().toString(),
                    tarefa.getPrioridade().toString(),
                    tarefa.getFinalizado(),
                    tarefa.getCategoria().getNome()
            );

            //Serializar os dados em JSON
            var json = objectMapper.writeValueAsString(notificacao);

            //Enviando para a mensageria
            publisherComponent.sendMessage(json);
        }
        catch(Exception e) {
            System.out.println("Falha ao enviar mensagem: " + e.getMessage());
        }
    }
}
