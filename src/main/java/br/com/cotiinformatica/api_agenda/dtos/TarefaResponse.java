package br.com.cotiinformatica.api_agenda.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record TarefaResponse(
        Integer id,
        String titulo,
        LocalDate data,
        LocalTime hora,
        String prioridade,
        Boolean finalizado,
        CategoriaResponse categoria
) {
}
