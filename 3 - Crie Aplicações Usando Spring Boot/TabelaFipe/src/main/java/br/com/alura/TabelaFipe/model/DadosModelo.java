package br.com.alura.TabelaFipe.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DadosModelo(
        @JsonAlias("codigo") Integer codigo,
        @JsonAlias("nome") String nomeModelo
) {
}
