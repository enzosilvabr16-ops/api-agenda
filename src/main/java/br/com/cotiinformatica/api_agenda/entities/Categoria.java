package br.com.cotiinformatica.api_agenda.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categorias")
@Setter
@Getter
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @OneToMany(mappedBy = "categoria")
    private List<Tarefa> tarefas;

}
