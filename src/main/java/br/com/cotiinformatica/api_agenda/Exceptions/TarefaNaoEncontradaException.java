package br.com.cotiinformatica.api_agenda.Exceptions;

public class TarefaNaoEncontradaException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Tarefa não encontrada.";
    }
}
