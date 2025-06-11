package org.example.ibis.service;

import java.util.List;
import java.util.Map;

public interface PagoService {

    List<String> obtenerTurnos();

    List<String> obtenerNombresPorTurno(String turno);

    List<String> obtenerMesesPendientes(String turno, String nombre);

    boolean registrarPago(String turno, String nombre, String mes, double monto, String medioPago);

    Map<String, Double> obtenerResumenPagosPorTurno();

    Map<String, Long> contarPagosPorMedioDePago();
}