package com.example.carros.service;

import com.example.carros.model.Produto;
import com.example.carros.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAllNotDeleted() {
        return produtoRepository.findByIsDeletedNull();
    }

    // Método para encontrar um produto pelo ID
    public Produto findById(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    // Método para deletar um produto logicamente
    public void deletarProduto(Long id) {
        Produto produto = findById(id);
        if (produto != null) {
            produto.setIsDeleted(LocalDateTime.now()); // Define a data e hora atuais para soft-delete
            produtoRepository.save(produto);
        }
    }

    public Produto save(Produto produto) {
        if (produto.getImagemUri() == null || produto.getImagemUri().isEmpty()) {
            produto.setImagemUri("default-image-uri"); // Defina um valor padrão adequado
        }
        return produtoRepository.save(produto);
    }

    public void update(Produto produto) {
        if (produtoRepository.existsById(produto.getId())) {
            produtoRepository.save(produto); // Atualiza o produto
        }
    }

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    // Método para encontrar produtos por categoria (modelo)
    public List<Produto> findByCategoria(String categoria) {
        return produtoRepository.findAll().stream()
                .filter(produto -> produto.getModelo().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    // Método para obter todas as categorias (modelos) únicas
    public List<String> findAllCategorias() {
        return produtoRepository.findAll().stream()
                .map(Produto::getModelo)
                .distinct()
                .collect(Collectors.toList());
    }
}
