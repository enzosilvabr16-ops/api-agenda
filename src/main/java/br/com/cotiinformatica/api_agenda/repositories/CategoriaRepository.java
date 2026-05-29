package br.com.cotiinformatica.api_agenda.repositories;

import br.com.cotiinformatica.api_agenda.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    //consultar todas as categorias de um determinado suario
    List<Categoria> findByUsuarioId(UUID usuarioId);

    //consultar uma categoria de um determinado usuario atráves do id
    Optional<Categoria> findByIdAndUsuarioId(Integer id, UUID usuarioId);

}
