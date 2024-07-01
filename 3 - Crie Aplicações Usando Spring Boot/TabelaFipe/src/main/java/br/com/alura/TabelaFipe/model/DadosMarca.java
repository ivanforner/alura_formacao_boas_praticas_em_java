package br.com.alura.TabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosMarca(
        @JsonAlias("codigo") Integer codigo,
        @JsonAlias("nome") String descricao
) {
}
