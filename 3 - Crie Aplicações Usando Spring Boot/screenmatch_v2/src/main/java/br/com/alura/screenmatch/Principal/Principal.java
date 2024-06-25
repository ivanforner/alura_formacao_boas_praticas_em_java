package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=9613a928";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        String menu = """
                1 - Buscar séries
                2 - Buscar episódios
                
                0 - Sair
                """;

        System.out.println(menu);
        var opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao){
            case 1:
                buscarSerieWeb();
                break;

            case 2:
                buscarEpisodiosWeb();
                break;

            default:
                System.out.println("Saindo...");
        }

    }

    private void buscarSerieWeb() {
        DadosSerie dadosSerie = getDadosSerie();
        System.out.println(dadosSerie);
    }

    private void buscarEpisodiosWeb() {
        DadosSerie dadosSerie = getDadosSerie();
        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            var json = consumoApi.obterDados(ENDERECO + dadosSerie.Titulo().replace(' ', '+') + "&season=" + i + APIKEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(
                System.out::println
        );
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        String nomeSerie = scanner.nextLine();

        String uri = ENDERECO + nomeSerie.replace(' ', '+') + APIKEY;
        String json = consumoApi.obterDados(uri);

        return conversor.obterDados(json, DadosSerie.class);
    }
}
