package com.example.carros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.util.Date;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private double preco;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private String imagemUri;

    @Column(nullable = false)
    private int estoque; // Novo campo para estoque

    @Column
    private Date isDeleted;

    @PrePersist
    public void prePersist() {
        if (imagemUri == null || imagemUri.isEmpty()) {
            imagemUri = "default-image-uri"; // Defina um valor padrão adequado
        }
    }

    public Produto() {
    }

    public Produto(String nome, String descricao, double preco, String marca, int ano, String modelo, String imagemUri,
            int estoque) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.marca = marca;
        this.ano = ano;
        this.modelo = modelo;
        this.imagemUri = imagemUri;
        this.estoque = estoque;
        this.isDeleted = null; // Valor padrão para isDeleted é null
    }

    // Getters e setters para todos os campos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getImagemUri() {
        return imagemUri;
    }

    public void setImagemUri(String imagemUri) {
        this.imagemUri = imagemUri;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public Date getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Date isDeleted) {
        this.isDeleted = isDeleted;
    }
}
