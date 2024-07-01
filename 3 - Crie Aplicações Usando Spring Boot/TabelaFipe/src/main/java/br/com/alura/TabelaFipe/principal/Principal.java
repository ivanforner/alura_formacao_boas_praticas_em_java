package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.DadosAno;
import br.com.alura.TabelaFipe.model.DadosMarca;
import br.com.alura.TabelaFipe.model.DadosModelo;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final Map<String, String> opcoes = new HashMap<>();
    private final String API_URI = "https://parallelum.com.br/fipe/api/v1";

    public void exibeMenu() {
        opcoes.put("carro", "carros");
        opcoes.put("moto", "motos");
        opcoes.put("caminhão", "caminhoes");

        System.out.println("**** Opções ****");
        System.out.println("Carro\nMoto\nCaminhão\n");
        System.out.println("Digite uma das opções para consultar valores: ");

        var tipoVeiculo = scanner.nextLine();

        while (!opcoes.containsKey(tipoVeiculo.toLowerCase())) {
            System.out.println("Ops! Parece que você digirtou uma opção inválida. Tente novamente: ");
            tipoVeiculo = scanner.nextLine();
        }

        String pesquisaTipo = "/%s/marcas".formatted(opcoes.get(tipoVeiculo));
        var json = consumoApi.obterDados(API_URI + pesquisaTipo);

        List<DadosMarca> marcas = List.of(conversor.converterDados(json, DadosMarca[].class));

        marcas.stream()
                .sorted(Comparator.comparing(DadosMarca::codigo))
                .forEach(m -> System.out.printf("Cód: %s    Descrição: %s%n", m.codigo(), m.descricao()));

        // Consulta ao código da marca
        System.out.println("Informe o código da marca para consulta: ");
        Integer codMarca = scanner.nextInt();
        scanner.nextLine();

        String pesquisaMarca = "/%s/modelos".formatted(codMarca);
        json = consumoApi.obterDados(API_URI + pesquisaTipo + pesquisaMarca);

        Modelos modelos = conversor.converterDados(json, Modelos.class);
        List<DadosModelo> listaModelos = modelos.modelos().stream()
                .sorted(Comparator.comparing(DadosModelo::codigo))
                .collect(Collectors.toList());
        listaModelos.forEach(m -> System.out.printf("Cód: %s    Descrição: %s%n", m.codigo(), m.nomeModelo()));

        System.out.println("Digite um trecho do veículo para consulta: ");
        String trechoVeiculo = scanner.nextLine();

        listaModelos.stream()
                .filter(m -> m.nomeModelo().toLowerCase().contains(trechoVeiculo.toLowerCase()))
                .forEach(m -> System.out.printf("Cód: %s    Descrição: %s%n", m.codigo(), m.nomeModelo()));

        System.out.println("Digite o código do modelo para consultar valores: ");
        Integer codigoModelo = scanner.nextInt();
        scanner.nextLine();

        String pesquisaModelo = "/%s/anos".formatted(codigoModelo);
        json = consumoApi.obterDados(API_URI + pesquisaTipo + pesquisaMarca + pesquisaModelo);
        List<DadosAno> listaDadosAno = List.of(conversor.converterDados(json, DadosAno[].class));

        List<Veiculo> veiculos = new ArrayList<>();

        for (DadosAno ano : listaDadosAno) {
            String pesquisaAno = "/%s".formatted(ano.codigo());

            try {
                json = consumoApi.obterDados(API_URI + pesquisaTipo + pesquisaMarca + pesquisaModelo + pesquisaAno);
                Veiculo veiculo = conversor.converterDados(json, Veiculo.class);
                veiculos.add(veiculo);
            } catch (NullPointerException ex) {
                System.out.println(ex.getMessage());
            }
        }

        veiculos.forEach(System.out::println);

    }
}
