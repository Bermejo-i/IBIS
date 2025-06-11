package org.example.ibis.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String turno;
    private String mes;
    private double monto;
    private String medioPago;

    @Column(name = "fecha_pago")
    private LocalDate fecha;

    // Constructores
    public Pago() {}

    public Pago(String nombre, String turno, String mes, double monto, String medioPago) {
        this.nombre = nombre;
        this.turno = turno;
        this.mes = mes;
        this.monto = monto;
        this.medioPago = medioPago;
        this.fecha = LocalDate.now();
    }

    // Getters y Setters completos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(String medioPago) {
        this.medioPago = medioPago;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    // Método toString() para representación en String
    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", turno='" + turno + '\'' +
                ", mes='" + mes + '\'' +
                ", monto=" + monto +
                ", medioPago='" + medioPago + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}