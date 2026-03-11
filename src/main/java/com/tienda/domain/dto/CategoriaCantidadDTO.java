package com.tienda.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaCantidadDTO {

    private Integer idCategoria;
    private String descripcion;
    private Boolean activo;
    private Long cantidadProductos;
}
