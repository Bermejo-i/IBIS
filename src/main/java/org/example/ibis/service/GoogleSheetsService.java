package org.example.ibis.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public interface GoogleSheetsService {
    List<String> obtenerTurnos();
    List<String> obtenerNombresPorTurno(String turno);
    List<String> obtenerMesesPendientes(String turno, String nombre);
    boolean registrarPago(String turno, String nombre, String mes, double monto, String medioPago);
    Map<String, Double> obtenerResumenPagosPorTurno();
    Map<String, Long> contarPagosPorMedioDePago();
}