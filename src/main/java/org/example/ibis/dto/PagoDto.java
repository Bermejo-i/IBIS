package org.example.ibis.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data

public class PagoDto {
    public String turno;
    public String nombre;
    public String mes;
    public double monto;
    public String medioPago ;

}