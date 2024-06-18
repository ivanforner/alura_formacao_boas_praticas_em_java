package br.com.alura.domain;

public class Pet {
    private long id;
    private String tipo;
    private String nome;
    private String raca;
    private int idade;
    private String cor;
    private float peso;

    public Pet(long id, String tipo, String nome, String raca, int idade) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
    }

    public Pet(String tipo, String nome, String raca, int idade, String cor, float peso) {
        this.tipo = tipo;
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.cor = cor;
        this.peso = peso;
    }

    @Override
    public String toString() {
        return this.getId() +" - " +this.getTipo() +" - " +this.getNome() +" - " +this.getRaca() +" - " +this.getIdade() +" ano(s)";
    }

    public long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public int getIdade() {
        return idade;
    }
}
