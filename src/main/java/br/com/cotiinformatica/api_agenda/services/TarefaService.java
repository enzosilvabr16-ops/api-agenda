package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.Exceptions.CategoriaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.components.PublisherComponent;
import br.com.cotiinformatica.api_agenda.dtos.*;
import br.com.cotiinformatica.api_agenda.entities.Categoria;
import br.com.cotiinformatica.api_agenda.entities.Tarefa;
import br.com.cotiinformatica.api_agenda.enums.Prioridade;
import br.com.cotiinformatica.api_agenda.repositories.CategoriaRepository;
import br.com.cotiinformatica.api_agenda.repositories.TarefaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public TarefaResponse cadastrar(TarefaRequest request, UUID usuarioId) {
        var categoria = categoriaRepository.findByIdAndUsuarioId(request.categoriaId(), usuarioId)
                .orElseThrow(CategoriaNaoEncontradaException::new);

        var tarefa = new Tarefa();
        tarefa.setTitulo(request.titulo());
        tarefa.setData(request.data());
        tarefa.setHora(request.hora());
        tarefa.setPrioridade(Prioridade.valueOf(request.prioridade()));
        tarefa.setFinalizado(request.finalizado());
        tarefa.setCategoria(categoria);
        tarefa.setUsuarioId(usuarioId);
    //salvar as tarefas no bd
        tarefaRepository.save(tarefa);

        //enviar pra messageria
        enviarParaMensageria(tarefa);

        //retornar os dados da response
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

            var notificacao = new NotificacaoDto(
                tarefa.getUsuarioId(),
                    "usuario@gmail.com",
                tarefa.getTitulo(),
                tarefa.getData().toString(),
                tarefa.getHora().toString(),
                tarefa.getPrioridade().toString(),
                tarefa.getFinalizado(),
                tarefa.getCategoria().getNome()
            );

            var json = objectMapper.writeValueAsString(notificacao);

            publisherComponent.sendMessage(json);

        } catch (Exception e) {
            System.out.println("Falha ao enviar mensagem: " + e.getMessage());;
        }
    }
}


