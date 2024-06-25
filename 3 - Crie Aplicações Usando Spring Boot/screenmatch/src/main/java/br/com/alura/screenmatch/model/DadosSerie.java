package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
<<<<<<< HEAD

// A anotação JsonAlias lê a chave json e escreve a chave que você define
// A anotação JsonProperty lê e escreve a propriedade da maneira como ela vem da API.
public record DadosSerie(@JsonAlias("Title") String titulo,
=======
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String Titulo,
>>>>>>> 46eaa4838beafbabd707fce0c0155eb2d6932a96
                         @JsonAlias("totalSeasons") int totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao) {
}
