package com.tienda.repository;

import com.tienda.domain.Categoria;
import com.tienda.domain.dto.CategoriaCantidadDTO;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    public List<Categoria> findByActivoTrue();


    public List<Categoria> findByActivoTrueAndDescripcionContainingIgnoreCaseOrderByDescripcionAsc(String textoDescripcion);

    @Query("""
           SELECT new com.tienda.domain.dto.CategoriaCantidadDTO(
               c.idCategoria,
               c.descripcion,
               c.activo,
               COUNT(p)
           )
           FROM Categoria c
           JOIN c.productos p
           WHERE c.activo = true
             AND p.activo = true
             AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :textoDescripcion, '%'))
           GROUP BY c.idCategoria, c.descripcion, c.activo
           HAVING COUNT(p) >= :cantidadMinProductos
           ORDER BY COUNT(p) DESC
           """)
    public List<CategoriaCantidadDTO> consultaCategoriaJPQL(
            @Param("cantidadMinProductos") Long cantidadMinProductos,
            @Param("textoDescripcion") String textoDescripcion
    );

    @Query(value = """
            SELECT 
                c.id_categoria,
                c.descripcion,
                c.activo,
                COUNT(p.id_producto) AS cantidad_productos
            FROM categoria c
            INNER JOIN producto p ON p.id_categoria = c.id_categoria
            WHERE c.activo = true
              AND p.activo = true
              AND LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :textoDescripcion, '%'))
            GROUP BY c.id_categoria, c.descripcion, c.activo
            HAVING COUNT(p.id_producto) >= :cantidadMinProductos
            ORDER BY COUNT(p.id_producto) DESC
            """, nativeQuery = true)
    public List<Object[]> consultaCategoriaSQL(
            @Param("cantidadMinProductos") Long cantidadMinProductos,
            @Param("textoDescripcion") String textoDescripcion
    );
}