package com.example.carros.controller;

import com.example.carros.model.Produto;
import com.example.carros.service.ProdutoService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/")
    public String listarProdutos(Model model, HttpServletResponse response, HttpSession session) {
        String visitaDataHora = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        Cookie cookie = new Cookie("visita", visitaDataHora);
        cookie.setMaxAge(24 * 60 * 60); // 24 horas em segundos
        cookie.setPath("/"); // Disponível em todo o site
        response.addCookie(cookie);

        List<Produto> produtos = produtoService.findAllNotDeleted();
        model.addAttribute("produtos", produtos);

        @SuppressWarnings("unchecked")
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        model.addAttribute("quantidadeItensCarrinho", carrinho.size());

        return "index";
    }

    @GetMapping("/index")
    public String listarProdutosIndex(Model model, HttpServletResponse response, HttpSession session) {
        String visitaDataHora = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        Cookie cookie = new Cookie("visita", visitaDataHora);
        cookie.setMaxAge(24 * 60 * 60); // 24 horas em segundos
        cookie.setPath("/"); // Disponível em todo o site
        response.addCookie(cookie);

        List<Produto> produtos = produtoService.findAllNotDeleted();
        model.addAttribute("produtos", produtos);

        @SuppressWarnings("unchecked")
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        model.addAttribute("quantidadeItensCarrinho", carrinho.size());

        return "index";
    }

    @GetMapping("/verCarrinho")
    public String verCarrinho(Model model, HttpSession session) {
        @SuppressWarnings("unchecked")
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }

        double valorTotal = 0.0;
        for (Produto produto : carrinho) {
            valorTotal += produto.getPreco();
        }

        model.addAttribute("carrinho", carrinho);
        model.addAttribute("valorTotal", valorTotal);
        model.addAttribute("quantidadeItensCarrinho", carrinho.size());

        return "verCarrinho";
    }

    @GetMapping("/editar")
    public String editarProduto(@RequestParam("id") Long id, Model model) {
        Produto produto = produtoService.findById(id);
        if (produto != null) {
            model.addAttribute("produto", produto); // Use "produto" here to match the template
            return "editarProduto";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/atualizarProduto")
    public String atualizarProduto(@Valid Produto produto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "editarProduto";
        }
        produtoService.update(produto);
        redirectAttributes.addFlashAttribute("message", "Produto atualizado com sucesso!");
        return "redirect:/index";
    }

    @GetMapping("/deletar")
    public String deletarProduto(@RequestParam("id") Long id) {
        produtoService.deletarProduto(id);
        return "redirect:/index";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("produto", new Produto());
        return "cadastro";
    }

    @PostMapping("/salvarProduto")
    public String salvarProduto(@Valid Produto produto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "cadastro";
        }

        if (produto.getImagemUri() == null || produto.getImagemUri().isEmpty()) {
            produto.setImagemUri("default-image-uri"); // Define um valor padrão adequado
        }

        produtoService.save(produto);
        redirectAttributes.addFlashAttribute("message", "Produto cadastrado com sucesso!");
        return "redirect:/index";
    }

    @GetMapping("/adicionarCarrinho")
    public String adicionarCarrinho(@RequestParam("id") Long id, HttpSession session) {
        Produto produto = produtoService.findById(id);

        if (produto != null) {
            @SuppressWarnings("unchecked")
            List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new ArrayList<>();
                session.setAttribute("carrinho", carrinho);
            }

            carrinho.add(produto);
        }

        return "redirect:/index";
    }

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpSession session, Model model) {
        @SuppressWarnings("unchecked")
        List<Produto> carrinho = (List<Produto>) session.getAttribute("carrinho");

        double valorTotal = 0.0;

        if (carrinho != null) {
            for (Produto produto : carrinho) {
                valorTotal += produto.getPreco();
                produto.setEstoque(produto.getEstoque() - 1);
                produtoService.update(produto);
            }
            session.removeAttribute("carrinho");
        }

        model.addAttribute("valorTotal", valorTotal);
        session.invalidate();

        return "redirect:/index";
    }

    @GetMapping("/produtos")
    public String listarProdutosCompleto(Model model) {
        model.addAttribute("produtos", produtoService.findAll());
        return "produtos";
    }

    @GetMapping("/produtosPorCategoria")
    public String listarProdutosPorCategoria(@RequestParam("categoria") String categoria, Model model) {
        List<Produto> produtos = produtoService.findByCategoria(categoria);
        model.addAttribute("produtos", produtos);
        model.addAttribute("categoriaSelecionada", categoria);
        model.addAttribute("categorias", produtoService.findAllCategorias());
        return "produtosPorCategoria";
    }

    @GetMapping("/verProduto")
    public String verProduto(@RequestParam("id") Long id, Model model) {
        Produto produto = produtoService.findById(id);
        if (produto != null) {
            model.addAttribute("produto", produto);
            return "descricao";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/{id}")
    public String getProduto(@PathVariable Long id, Model model) {
        Produto produto = produtoService.findById(id);
        if (produto == null) {
            model.addAttribute("error", "Produto não encontrado");
            return "error";
        }
        model.addAttribute("produto", produto);
        return "produto";
    }
}
