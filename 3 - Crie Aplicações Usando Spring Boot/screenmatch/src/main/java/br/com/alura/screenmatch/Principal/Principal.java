package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;


public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=9613a928";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        System.out.println("Digite o nome da série:");
        var nomeSerie = scanner.nextLine();
        String uri = ENDERECO + nomeSerie.replace(' ', '+') + APIKEY;
        var json = consumoApi.obterDados(uri);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        //System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(' ', '+') + "&season=" + i + APIKEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

//        for (DadosTemporada temporada : temporadas){
//            System.out.println(temporada);
//        }

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (DadosEpisodio dadosEpisodio : episodiosTemporada) {
//                System.out.println(dadosEpisodio.titulo());
//            }
//        }

        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList()); // a diferença entre usar assim e o toList() é que o toList() devolve uma lista imutável
//

//        System.out.println("Lista com os 10 melhores episódios: ");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro Filtro (N/A) " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);

//        System.out.println();

        // Funções Lambdas equivalentes:
        // temporadas.forEach(t -> System.out.println(t));
        // temporadas.forEach(System.out::println);

        /*List<String> nomes = Arrays.asList("Jaque", "Iasmin", "Rodrigo", "Nico");
        nomes.stream()
                .sorted()
                .filter(n -> n.startsWith("N"))
                //.map(n -> n.toUpperCase())
                .map(String::toUpperCase)
                .forEach(System.out::println);*/

//        System.out.println("Lista de todos os episódios com suas respectivas temporadas.");

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(e -> new Episodio(t.numero(), e)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Digite um trecho do título do episódio");
        String trechoTitulo = scanner.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        System.out.println(episodioBuscado);

        if (episodioBuscado.isPresent()) {
            System.out.println("Episodio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episodio não encontrado");
        }

//
//        System.out.println();
//
//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        var ano = scanner.nextInt();
//        scanner.nextLine(); //Toda vez que usamos um nextint, precisamos usar um nextLine na sequência pq as vezes buga
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> {
//                    System.out.println(
//                            "Temporada: " + e.getTemporada() +
//                                    ", Episodio: " + e.getTitulo() +
//                                    ", Data lançamento: " + e.getDataLancamento().format(formatter)
//                    );
//                });

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Media: " + est.getAverage());
        System.out.println("Melhor episodio: " + est.getMax());
        System.out.println("Pior episodio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());

    }
}
