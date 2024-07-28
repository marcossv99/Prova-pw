package com.example.carros.repository;

import com.example.carros.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.modelo = :modelo")
    List<Produto> findByModelo(@Param("modelo") String modelo);

    @Query("SELECT p FROM Produto p WHERE p.isDeleted IS NULL")
    List<Produto> findNotDeleted();
}
