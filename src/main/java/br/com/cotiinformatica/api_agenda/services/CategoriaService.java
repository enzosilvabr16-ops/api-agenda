package br.com.cotiinformatica.api_agenda.services;

import br.com.cotiinformatica.api_agenda.Exceptions.CategoriaNaoEncontradaException;
import br.com.cotiinformatica.api_agenda.dtos.CategoriaRequest;
import br.com.cotiinformatica.api_agenda.dtos.CategoriaResponse;
import br.com.cotiinformatica.api_agenda.entities.Categoria;
import br.com.cotiinformatica.api_agenda.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    //Serviço para cadastrar categoria
    public CategoriaResponse cadastrar(CategoriaRequest request, UUID usuarioId) {

        //Criando um objeto da entidade 'Categoria'
        var categoria = new Categoria();
        categoria.setNome(request.nome());
        categoria.setUsuarioId(usuarioId);

        //Salvar a categoria no banco de dados
        categoriaRepository.save(categoria);

        //Retornar os dados da resposta
        return toResponse(categoria);
    }

    //Serviço para atualizar categoria
    public CategoriaResponse atualizar(Integer id, CategoriaRequest request, UUID usuarioId) {

        //Buscar a categoria no banco de dados através do ID
        var categoria = categoriaRepository
                .findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(CategoriaNaoEncontradaException::new);

        //Alterar os dados da categoria
        categoria.setNome(request.nome());

        //Salvar no banco de dados
        categoriaRepository.save(categoria);

        //Retornar os dados da resposta
        return toResponse(categoria);
    }

    //Serviço para excluir uma categoria
    public CategoriaResponse excluir(Integer id, UUID usuarioId) {

        //Buscar a categoria no banco de dados através do ID
        var categoria = categoriaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(CategoriaNaoEncontradaException::new);

        //Excluir a categoria
        categoriaRepository.delete(categoria);

        //Retornar os dados da resposta
        return toResponse(categoria);
    }

    //Serviço para consultar todas as categorias do banco de dados
    public List<CategoriaResponse> consultar(UUID usuarioId) {

        //Consultando todas as categorias cadastradas
        var categorias = categoriaRepository.findByUsuarioId(usuarioId);

        //Copiar todos os objetos da primeira lista para uma nova lista do DTO
        //Convertendo cada objeto 'Categoria' em um objeto 'CategoriaResponse'
        return categorias.stream()
                .map(this::toResponse)
                .toList();
    }

    //Serviço para obter 1 categoria através do ID
    public CategoriaResponse obterPorId(Integer id, UUID usuarioId) {

        //buscar 1 categoria no banco de dados atraves do ID
        var categoria = categoriaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(CategoriaNaoEncontradaException::new);

        //Retornar os dados da resposta
        return toResponse(categoria);
    }

    //Método privado para retornar os dados do objeto CategoriaResponse
    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNome()
        );
    }

}
