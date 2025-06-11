package org.example.ibis.repository;

import org.example.ibis.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Consultas derivadas
    List<Pago> findByTurno(String turno);
    List<Pago> findByTurnoAndNombre(String turno, String nombre);
    List<Pago> findByTurnoAndNombreAndMes(String turno, String nombre, String mes);

    // Consultas personalizadas
    @Query("SELECT DISTINCT p.turno FROM Pago p")
    List<String> findDistinctTurnos();

    @Query("SELECT DISTINCT p.nombre FROM Pago p WHERE p.turno = ?1")
    List<String> findNombresByTurno(String turno);

    @Query("SELECT p.mes FROM Pago p WHERE p.turno = ?1 AND p.nombre = ?2")
    List<String> findMesesPagados(String turno, String nombre);

    @Query("SELECT p.turno, SUM(p.monto) FROM Pago p GROUP BY p.turno")
    List<Object[]> sumMontoByTurno();

    @Query("SELECT p.medioPago, COUNT(p) FROM Pago p GROUP BY p.medioPago")
    List<Object[]> countByMedioPago();
}