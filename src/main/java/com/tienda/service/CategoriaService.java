package com.tienda.service;

import com.tienda.domain.Categoria;
import com.tienda.domain.Producto;
import com.tienda.domain.dto.CategoriaCantidadDTO;
import com.tienda.repository.CategoriaRepository;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final FirebaseStorageService firebaseStorageService;

    public CategoriaService(CategoriaRepository categoriaRepository, FirebaseStorageService firebaseStorageService) {
        this.categoriaRepository = categoriaRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        categoria = categoriaRepository.save(categoria);
        if (!imagenFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile, "categoria", categoria.getIdCategoria());
                categoria.setRutaImagen(rutaImagen);
                categoriaRepository.save(categoria);
            } catch (IOException e) {
            }
        }
    }

    @Transactional
    public void delete(Integer idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new IllegalArgumentException("La categoría con ID " + idCategoria + " no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar la categoria. Tiene datos asociados.", e);
        }
    }

    @Transactional(readOnly = true)
    public List<CategoriaCantidadDTO> consultaCategoriaDerivada(Long cantidadMinProductos, String textoDescripcion) {
        List<Categoria> categorias = categoriaRepository
                .findByActivoTrueAndDescripcionContainingIgnoreCaseOrderByDescripcionAsc(textoDescripcion);

        return categorias.stream()
                .map(c -> new CategoriaCantidadDTO(
                        c.getIdCategoria(),
                        c.getDescripcion(),
                        c.getActivo(),
                        contarProductosActivos(c.getProductos())))
                .filter(c -> c.getCantidadProductos() >= cantidadMinProductos)
                .sorted(Comparator.comparing(CategoriaCantidadDTO::getCantidadProductos).reversed())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CategoriaCantidadDTO> consultaCategoriaJPQL(Long cantidadMinProductos, String textoDescripcion) {
        return categoriaRepository.consultaCategoriaJPQL(cantidadMinProductos, textoDescripcion);
    }

    @Transactional(readOnly = true)
    public List<CategoriaCantidadDTO> consultaCategoriaSQL(Long cantidadMinProductos, String textoDescripcion) {
        List<Object[]> datos = categoriaRepository.consultaCategoriaSQL(cantidadMinProductos, textoDescripcion);

        return datos.stream().map(fila -> {
            Integer idCategoria = ((Number) fila[0]).intValue();
            String descripcion = String.valueOf(fila[1]);
            Boolean activo = convertirBoolean(fila[2]);
            Long cantidad = ((Number) fila[3]).longValue();
            return new CategoriaCantidadDTO(idCategoria, descripcion, activo, cantidad);
        }).toList();
    }

    private Long contarProductosActivos(List<Producto> productos) {
        if (productos == null) {
            return 0L;
        }

        return productos.stream()
                .filter(p -> p != null && p.isActivo())
                .count();
    }

    private Boolean convertirBoolean(Object valor) {
        if (valor instanceof Boolean b) {
            return b;
        }
        if (valor instanceof Number n) {
            return n.intValue() != 0;
        }
        return Boolean.parseBoolean(String.valueOf(valor));
    }
}
