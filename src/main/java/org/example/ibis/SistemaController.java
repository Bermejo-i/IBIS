package org.example.ibis;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class SistemaController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/pagos")
    public String pagos() {
        return "pagos";
    }
    @GetMapping("/ventas")
    public String ventas() {
        return "ventas";
    }
    @GetMapping("/alumnos")
    public String alumnos() {
        return "alumnos";
    }
    @GetMapping("/cursos")
    public String cursos() {
        return "cursos";
    }
    @GetMapping("/practicas")
    public String practicas() {
        return "practicas";
    }
    @GetMapping("/horarios")
    public String horarios() {
        return "horarios";
    }
    @GetMapping("/asistencias")
    public String asistencias() {
        return "asistencias";
    }
    @GetMapping("/registro")
    public String mostrarFormularioPago() {
        return "registro";
    }
}