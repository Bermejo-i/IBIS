package org.example.ibis.controller;

import org.example.ibis.dto.PagoDto;
import org.example.ibis.service.PagoService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoServiceBd;
    private final PagoService pagoServiceSheets;
    private final boolean usarGoogleSheets = true; // ← cambia esto cuando termines la migración

    public PagoController(
            @Qualifier("pagoServiceImpl") PagoService pagoServiceBd,
            @Qualifier("googleSheetsServiceImpl") PagoService pagoServiceSheets
    ) {
        this.pagoServiceBd = pagoServiceBd;
        this.pagoServiceSheets = pagoServiceSheets;
    }

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrarPago(@RequestBody PagoDto dto) {
        boolean resultado = getServicioActivo().registrarPago(
                dto.turno, dto.nombre, dto.mes, dto.monto, dto.medioPago
        );

        if (resultado) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Pago registrado exitosamente"
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "No se pudo registrar el pago"
            ));
        }
    }

    private PagoService getServicioActivo() {
        return usarGoogleSheets ? pagoServiceSheets : pagoServiceBd;
    }
    @GetMapping("/turnos")
    public ResponseEntity<List<String>> obtenerTurnos() {
        return ResponseEntity.ok(pagoServiceSheets.obtenerTurnos());
    }

    @GetMapping("/nombres")
    public ResponseEntity<List<String>> obtenerNombres(@RequestParam String turno) {
        return ResponseEntity.ok(pagoServiceSheets.obtenerNombresPorTurno(turno));
    }

    @GetMapping("/meses")
    public ResponseEntity<List<String>> obtenerMesesPendientes(
            @RequestParam String turno,
            @RequestParam String nombre) {
        return ResponseEntity.ok(pagoServiceSheets.obtenerMesesPendientes(turno, nombre));
    }
    // Nuevos endpoints para reportes
    @GetMapping("/resumen-turnos")
    public ResponseEntity<Map<String, Double>> obtenerResumenPorTurno() {
        return ResponseEntity.ok(pagoServiceSheets.obtenerResumenPagosPorTurno());
    }

    @GetMapping("/resumen-medios-pago")
    public ResponseEntity<Map<String, Long>> obtenerResumenMediosPago() {
        return ResponseEntity.ok(pagoServiceSheets.contarPagosPorMedioDePago());
    }

    // Endpoint adicional para verificar conexión
    @GetMapping("/test-conexion")
    public ResponseEntity<String> testConexion() {
        try {
            List<String> turnos = pagoServiceSheets.obtenerTurnos();
            return ResponseEntity.ok("Conexión exitosa. Turnos encontrados: " + turnos.size());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error de conexión: " + e.getMessage());
        }
    }
}