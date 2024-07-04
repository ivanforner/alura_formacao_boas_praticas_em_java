package br.com.alura.screenmatch.Principal;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;


public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=9613a928";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();

    public Principal (SerieRepository repository){
        this.repository = repository;
    }

    public void exibeMenu() {
        int opcao = -1;

        while (opcao != 0) {
            String menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar séries por título
                5 - Buscar séries por ator
                6 - Buscar Top 5 Séries
                7 - Buscar series por categoria
                8 - Buscar series por temporadas
                
                0 - Sair
                """;

            System.out.println(menu);

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao){
                case 1:
                    buscarSerieWeb();
                    break;

                case 2:
                    buscarEpisodiosWeb();
                    break;

                case 3:
                    listarSeriesBuscadas();
                    break;

                case 4:
                    buscarSeriePorTitulo();
                    break;

                case 5:
                    buscarSeriesPorAtor();
                    break;

                case 6:
                    buscarTop5Series();
                    break;

                case 7:
                    buscarSeriesPorCategoria();
                    break;

                case 8:
                    buscarSeriesPorTemporadas();
                    break;

                default:
                    System.out.println("Saindo...");
            }
        }
    }

    private void listarSeriesBuscadas() {
//        series = dadosSeries.stream()
//                .map(Serie::new)
//                .collect(Collectors.toList());

        series = repository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dadosSerie = getDadosSerie();
        Serie serie = new Serie(dadosSerie);
        repository.save(serie);
        System.out.println(serie);
    }

    private void buscarEpisodiosWeb() {
        listarSeriesBuscadas();

        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(' ', '+') + "&season=" + i + APIKEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            repository.save(serieEncontrada);

        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        String nomeSerie = scanner.nextLine();

        String uri = ENDERECO + nomeSerie.replace(' ', '+') + APIKEY;
        String json = consumoApi.obterDados(uri);

        return conversor.obterDados(json, DadosSerie.class);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha a série pelo título: ");
        var nomeSerie = scanner.nextLine();

        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da serie: " + serieBuscada);
        } else {
            System.out.println("Serie não encontrada!");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Qual o nome para busca? ");
        String nomeAtor = scanner.nextLine();

        System.out.println("Avaliações a partir de qual valor? ");
        double avaliacao = scanner.nextDouble();

        List<Serie> seriesEcontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);

        System.out.println("Series em que " + nomeAtor + " trabalhou");

        seriesEcontradas.forEach(
                s -> System.out.printf("Título: %s, avaliação %s\n", s.getTitulo(), s.getAvaliacao())
        );
    }

    private void  buscarTop5Series() {
        List<Serie> seriesTop = repository.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(
                s -> System.out.printf("Título: %s, avaliação %s\n", s.getTitulo(), s.getAvaliacao())
        );
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Qual categoria?");
        var categoriaUsuario = scanner.nextLine();
        Categoria categoria = Categoria.fromStringPortugues(categoriaUsuario);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriesPorTemporadas() {
        System.out.println("Até quantas temporadas? ");
        Integer totalTemporadas = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Avaliações a partir de qual valor? ");
        double avaliacao = scanner.nextDouble();

        List<Serie> seriesPorTemporadas = repository
                .findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas, avaliacao);

        seriesPorTemporadas.forEach(System.out::println);
    }
}
