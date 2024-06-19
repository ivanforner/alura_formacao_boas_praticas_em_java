package br.com.alura.domain;

public class Abrigo {
    private long id;
    private String nome;
    private String telefone;
    private String email;
    private Pet[] pets;


    // Jackson pede que uma classe tenha um construtor default para poder deserealizar o objeto
    public Abrigo() {

    }

    public Abrigo(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Pet[] getPets() {
        return pets;
    }

    public String getNome() {
        return nome;
    }
}
