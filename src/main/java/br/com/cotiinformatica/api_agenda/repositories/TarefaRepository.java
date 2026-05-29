package br.com.cotiinformatica.api_agenda.repositories;

import br.com.cotiinformatica.api_agenda.entities.Categoria;
import br.com.cotiinformatica.api_agenda.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TarefaRepository extends JpaRepository <Tarefa, Integer>{

    //metodo ra consultar todas as tarefas de um usuario
    List<Tarefa> findByUsuarioId(UUID usuarioId);

    //consultar uma tarefa de um determinado usuario atráves do id
    Optional<Tarefa> findByIdAndUsuarioId(Integer id, UUID usuarioId);
}
