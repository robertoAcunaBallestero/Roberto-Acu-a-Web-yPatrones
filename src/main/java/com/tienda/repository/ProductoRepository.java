package com.tienda.repository;

import com.tienda.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    public List<Producto> findByActivoTrue();

    
    public List<Producto> findByActivoTrueAndPrecioBetweenAndExistenciasGreaterThanAndCategoria_ActivoTrueAndCategoria_DescripcionContainingIgnoreCaseOrderByPrecioAsc(
            BigDecimal precioMin,
            BigDecimal precioMax,
            Integer existenciasMin,
            String descripcionCategoria
    );

    
    @Query("""
           SELECT p
           FROM Producto p
           JOIN p.categoria c
           WHERE p.activo = true
             AND p.precio BETWEEN :precioMin AND :precioMax
             AND p.existencias > :existenciasMin
             AND c.activo = true
             AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcionCategoria, '%'))
           ORDER BY p.precio ASC
           """)
    public List<Producto> consultaProductoJPQL(
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("existenciasMin") Integer existenciasMin,
            @Param("descripcionCategoria") String descripcionCategoria
    );


    @Query(value = """
            SELECT p.*
            FROM producto p
            INNER JOIN categoria c ON p.id_categoria = c.id_categoria
            WHERE p.activo = true
              AND p.precio BETWEEN :precioMin AND :precioMax
              AND p.existencias > :existenciasMin
              AND c.activo = true
              AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :descripcionCategoria, '%'))
            ORDER BY p.precio ASC
            """, nativeQuery = true)
    public List<Producto> consultaProductoSQL(
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax,
            @Param("existenciasMin") Integer existenciasMin,
            @Param("descripcionCategoria") String descripcionCategoria
    );
}