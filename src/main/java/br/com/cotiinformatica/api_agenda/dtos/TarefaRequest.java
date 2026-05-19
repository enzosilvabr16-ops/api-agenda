package br.com.cotiinformatica.api_agenda.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

public record TarefaRequest(
        String titulo,
        LocalDate data,
        LocalTime hora,
        String prioridade,
        Boolean finalizado,
        Integer categoriaId
) {
}
