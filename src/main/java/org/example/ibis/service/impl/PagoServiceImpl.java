package org.example.ibis.service.impl;
import org.example.ibis.service.GoogleSheetsService;
import org.example.ibis.service.PagoService;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PagoServiceImpl implements PagoService {
    private final GoogleSheetsService sheetsService;

    public PagoServiceImpl(GoogleSheetsService sheetsService) {
        this.sheetsService = sheetsService;
    }
    @Override
    public List<String> obtenerTurnos() {
        return sheetsService.obtenerTurnos();
    }

    @Override
    public List<String> obtenerNombresPorTurno(String turno) {
        return sheetsService.obtenerNombresPorTurno(turno);
    }

    @Override
    public List<String> obtenerMesesPendientes(String turno, String nombre) {
        return sheetsService.obtenerMesesPendientes(turno, nombre);
    }

    @Override
    public boolean registrarPago(String turno, String nombre, String mes, double monto, String medioPago) {
        return sheetsService.registrarPago(turno, nombre, mes, monto, medioPago);
    }

    @Override
    public Map<String, Double> obtenerResumenPagosPorTurno() {
        return sheetsService.obtenerResumenPagosPorTurno();
    }

    @Override
    public Map<String, Long> contarPagosPorMedioDePago() {
        return sheetsService.contarPagosPorMedioDePago();
    }
}