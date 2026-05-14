package br.com.cotiinformatica.api_agenda.Exceptions;

public class CategoriaNaoEncontradaException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Categoria não encontrada. Verifique o ID informado.";
    }
}
