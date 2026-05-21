package br.com.cotiinformatica.api_agenda.dtos;

import java.util.UUID;

public record NotificacaoDto(
        UUID usuarioId,
        String usuarioEmail,
        String tituloTarefa,
        String dataTarefa,
        String horaTarefa,
        String prioridadeTarefa,
        Boolean finalizado,
        String nomeCategoria
) {
}
