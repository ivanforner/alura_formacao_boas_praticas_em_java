package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

// A anotação JsonAlias lê a chave json e escreve a chave que você define
// A anotação JsonProperty lê e escreve a propriedade da maneira como ela vem da API.
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") int totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao) {
}
