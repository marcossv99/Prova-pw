package com.example.carros.service;

import com.example.carros.model.Produto;
import com.example.carros.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> findAllNotDeleted() {
        return produtoRepository.findNotDeleted();
    }

    public Produto findById(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public void softDelete(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow();
        produto.setIsDeleted(LocalDateTime.now());
        produtoRepository.save(produto);
    }

    public Produto save(Produto produto) {
        if (produto.getImagemUri() == null || produto.getImagemUri().isEmpty()) {
            produto.setImagemUri("default-image-uri");
        }
        return produtoRepository.save(produto);
    }

    public void update(Produto produto) {
        produtoRepository.save(produto);
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public List<Produto> findByCategoria(String categoria) {
        return produtoRepository.findByModelo(categoria);
    }

    public List<String> findAllCategorias() {
        return produtoRepository.findAll().stream()
                .map(Produto::getModelo)
                .distinct()
                .collect(Collectors.toList());
    }
}
