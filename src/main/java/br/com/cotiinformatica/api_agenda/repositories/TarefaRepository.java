package br.com.cotiinformatica.api_agenda.repositories;

import br.com.cotiinformatica.api_agenda.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {

    /*
        Método para consultar todas as tarefas de um determinado usuário
        dentro de um período de datas
     */
    List<Tarefa> findByUsuarioIdAndDataBetween(
            UUID usuarioId,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    /*
        Método para consultar uma tarefa de um determinado usuário através do ID
     */
    Optional<Tarefa> findByIdAndUsuarioId(Integer id, UUID usuarioId);
}