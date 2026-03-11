package com.tienda.controller;

import com.tienda.service.CategoriaService;
import com.tienda.service.ProductoService;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ConsultaController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        cargarModeloBase(model);
        return "/consultas/listado";
    }

    @PostMapping("/producto/derivada")
    public String consultaProductoDerivada(
            @RequestParam BigDecimal precioMin,
            @RequestParam BigDecimal precioMax,
            @RequestParam Integer existenciasMin,
            @RequestParam String descripcionCategoria,
            Model model) {

        cargarModeloBase(model);

        var productos = productoService.consultaProductoDerivada(
                precioMin, precioMax, existenciasMin, descripcionCategoria);

        model.addAttribute("productos", productos);
        model.addAttribute("moduloConsulta", "producto");
        model.addAttribute("tipoConsulta", "Derivada");
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("existenciasMin", existenciasMin);
        model.addAttribute("descripcionCategoria", descripcionCategoria);

        return "/consultas/listado";
    }

    @PostMapping("/producto/jpql")
    public String consultaProductoJPQL(
            @RequestParam BigDecimal precioMin,
            @RequestParam BigDecimal precioMax,
            @RequestParam Integer existenciasMin,
            @RequestParam String descripcionCategoria,
            Model model) {

        cargarModeloBase(model);

        var productos = productoService.consultaProductoJPQL(
                precioMin, precioMax, existenciasMin, descripcionCategoria);

        model.addAttribute("productos", productos);
        model.addAttribute("moduloConsulta", "producto");
        model.addAttribute("tipoConsulta", "JPQL");
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("existenciasMin", existenciasMin);
        model.addAttribute("descripcionCategoria", descripcionCategoria);

        return "/consultas/listado";
    }

    @PostMapping("/producto/sql")
    public String consultaProductoSQL(
            @RequestParam BigDecimal precioMin,
            @RequestParam BigDecimal precioMax,
            @RequestParam Integer existenciasMin,
            @RequestParam String descripcionCategoria,
            Model model) {

        cargarModeloBase(model);

        var productos = productoService.consultaProductoSQL(
                precioMin, precioMax, existenciasMin, descripcionCategoria);

        model.addAttribute("productos", productos);
        model.addAttribute("moduloConsulta", "producto");
        model.addAttribute("tipoConsulta", "SQL Nativa");
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);
        model.addAttribute("existenciasMin", existenciasMin);
        model.addAttribute("descripcionCategoria", descripcionCategoria);

        return "/consultas/listado";
    }

    @PostMapping("/categoria/derivada")
    public String consultaCategoriaDerivada(
            @RequestParam Long cantidadMinProductos,
            @RequestParam String textoDescripcion,
            Model model) {

        cargarModeloBase(model);

        var categoriasConsulta = categoriaService.consultaCategoriaDerivada(
                cantidadMinProductos, textoDescripcion);

        model.addAttribute("categoriasConsulta", categoriasConsulta);
        model.addAttribute("moduloConsulta", "categoria");
        model.addAttribute("tipoConsulta", "Derivada");
        model.addAttribute("cantidadMinProductos", cantidadMinProductos);
        model.addAttribute("textoDescripcion", textoDescripcion);

        return "/consultas/listado";
    }

    @PostMapping("/categoria/jpql")
    public String consultaCategoriaJPQL(
            @RequestParam Long cantidadMinProductos,
            @RequestParam String textoDescripcion,
            Model model) {

        cargarModeloBase(model);

        var categoriasConsulta = categoriaService.consultaCategoriaJPQL(
                cantidadMinProductos, textoDescripcion);

        model.addAttribute("categoriasConsulta", categoriasConsulta);
        model.addAttribute("moduloConsulta", "categoria");
        model.addAttribute("tipoConsulta", "JPQL");
        model.addAttribute("cantidadMinProductos", cantidadMinProductos);
        model.addAttribute("textoDescripcion", textoDescripcion);

        return "/consultas/listado";
    }

    @PostMapping("/categoria/sql")
    public String consultaCategoriaSQL(
            @RequestParam Long cantidadMinProductos,
            @RequestParam String textoDescripcion,
            Model model) {

        cargarModeloBase(model);

        var categoriasConsulta = categoriaService.consultaCategoriaSQL(
                cantidadMinProductos, textoDescripcion);

        model.addAttribute("categoriasConsulta", categoriasConsulta);
        model.addAttribute("moduloConsulta", "categoria");
        model.addAttribute("tipoConsulta", "SQL Nativa");
        model.addAttribute("cantidadMinProductos", cantidadMinProductos);
        model.addAttribute("textoDescripcion", textoDescripcion);

        return "/consultas/listado";
    }

    private void cargarModeloBase(Model model) {
        if (!model.containsAttribute("productos")) {
            model.addAttribute("productos", new ArrayList<>());
        }
        if (!model.containsAttribute("categoriasConsulta")) {
            model.addAttribute("categoriasConsulta", new ArrayList<>());
        }

        model.addAttribute("precioMin", model.containsAttribute("precioMin") ? model.getAttribute("precioMin") : null);
        model.addAttribute("precioMax", model.containsAttribute("precioMax") ? model.getAttribute("precioMax") : null);
        model.addAttribute("existenciasMin", model.containsAttribute("existenciasMin") ? model.getAttribute("existenciasMin") : null);
        model.addAttribute("descripcionCategoria", model.containsAttribute("descripcionCategoria") ? model.getAttribute("descripcionCategoria") : "");

        model.addAttribute("cantidadMinProductos", model.containsAttribute("cantidadMinProductos") ? model.getAttribute("cantidadMinProductos") : null);
        model.addAttribute("textoDescripcion", model.containsAttribute("textoDescripcion") ? model.getAttribute("textoDescripcion") : "");

        if (!model.containsAttribute("moduloConsulta")) {
            model.addAttribute("moduloConsulta", "");
        }
        if (!model.containsAttribute("tipoConsulta")) {
            model.addAttribute("tipoConsulta", "");
        }
    }
}

    
    
    
    

