package br.com.alura.TabelaFipe.service;

public interface IConverteDados {
    public <T> T converterDados(String json, Class<T> classe);
}
