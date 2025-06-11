package org.example.ibis.service.impl;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.example.ibis.service.GoogleSheetsService;
import org.example.ibis.service.PagoService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GoogleSheetsServiceImpl implements PagoService, GoogleSheetsService {

    private final Sheets sheetsService;
    private static final String SPREADSHEET_ID = "1Wuk1LEI2Ua-43vm7pdqH3yYTt4canjyN97xdcThjm3I";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public GoogleSheetsServiceImpl(Sheets sheetsService) {
        this.sheetsService = sheetsService;
    }

    @Override
    public List<String> obtenerTurnos() {
        try {
            Spreadsheet spreadsheet = sheetsService.spreadsheets().get(SPREADSHEET_ID).execute();
            return spreadsheet.getSheets().stream()
                    .map(sheet -> sheet.getProperties().getTitle())
                    .filter(title -> !title.equalsIgnoreCase("REGISTRO_PAGOS"))
                    .filter(title -> !title.equalsIgnoreCase("NUEVO_PAGO"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> obtenerNombresPorTurno(String turno) {
        try {
            String range = String.format("'%s'!B4:B", turno);
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, range)
                    .execute();

            return response.getValues().stream()
                    .filter(row -> !row.isEmpty())
                    .map(row -> row.get(0).toString().trim())
                    .filter(nombre -> !nombre.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<String> obtenerMesesPendientes(String turno, String nombre) {
        try {
            // Obtener cabeceras de meses (fila 3, columnas D en adelante)
            String headerRange = String.format("'%s'!3:3", turno);
            ValueRange headerResponse = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, headerRange)
                    .execute();

            List<Object> headerRowRaw = headerResponse.getValues().get(0);
            List<String> meses = new ArrayList<>();
            for (int i = 3; i < headerRowRaw.size(); i++) {
                meses.add(headerRowRaw.get(i).toString().trim());
            }

            System.out.println("Meses detectados: " + meses);

            // Buscar fila del alumno
            String dataRange = String.format("'%s'!A4:Z", turno);
            ValueRange dataResponse = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, dataRange)
                    .execute();

            for (List<Object> row : dataResponse.getValues()) {
                if (row.size() > 1 && nombre.equalsIgnoreCase(row.get(1).toString().trim())) {
                    for (int i = 0; i < meses.size(); i++) {
                        int colNum = i + 3; // Desde la columna D (índice 3)
                        String valor = row.size() > colNum ? row.get(colNum).toString().trim() : "";
                        String mes = meses.get(i);
                        System.out.println("Mes: " + mes + " - Valor: '" + valor + "'");
                        if (!"X".equalsIgnoreCase(valor)) {
                            return Collections.singletonList(mes);
                        }
                    }
                    return Collections.emptyList(); // Todos los meses están pagados
                }
            }

            return Collections.emptyList(); // Alumno no encontrado
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean registrarPago(String turno, String nombre, String mes, double monto, String medioPago) {
        try {
            // Paso 1: Registrar en hoja de REGISTRO_PAGOS
            ValueRange appendBody = new ValueRange()
                    .setValues(List.of(List.of(
                            LocalDate.now().format(DATE_FORMATTER),
                            nombre,
                            turno,
                            mes,
                            monto,
                            medioPago
                    )));

            sheetsService.spreadsheets().values()
                    .append(SPREADSHEET_ID, "REGISTRO_PAGOS!A:F", appendBody)
                    .setValueInputOption("USER_ENTERED")
                    .execute();

            // Paso 2: Marcar como pagado en la hoja del turno
            String sheetName = turno;
            String nombreBuscado = nombre;
            String mesBuscado = mes;

            // Encontrar coordenadas de la celda a actualizar
            String headerRange = String.format("'%s'!3:3", sheetName);
            ValueRange headerResponse = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, headerRange)
                    .execute();

            int columnaMes = -1;
            for (int i = 3; i < headerResponse.getValues().get(0).size(); i++) {
                if (mesBuscado.equalsIgnoreCase(headerResponse.getValues().get(0).get(i).toString())) {
                    columnaMes = i;
                    break;
                }
            }
            if (columnaMes == -1) return false;

            String dataRange = String.format("'%s'!A4:Z", sheetName);
            ValueRange dataResponse = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, dataRange)
                    .execute();

            for (int rowNum = 0; rowNum < dataResponse.getValues().size(); rowNum++) {
                List<Object> row = dataResponse.getValues().get(rowNum);
                if (row.size() > 1 && nombreBuscado.equalsIgnoreCase(row.get(1).toString().trim())) {
                    // Actualizar celda con "X"
                    String cellRange = String.format("'%s'!%s%d",
                            sheetName,
                            getColumnLetter(columnaMes + 1), // +1 porque las columnas empiezan en 1
                            rowNum + 4); // +4 porque los datos empiezan en fila 4

                    ValueRange updateBody = new ValueRange()
                            .setValues(List.of(List.of("X")));

                    sheetsService.spreadsheets().values()
                            .update(SPREADSHEET_ID, cellRange, updateBody)
                            .setValueInputOption("RAW")
                            .execute();

                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, Double> obtenerResumenPagosPorTurno() {
        try {
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, "REGISTRO_PAGOS!A:F")
                    .execute();

            return response.getValues().stream()
                    .filter(row -> row.size() >= 5)
                    .collect(Collectors.groupingBy(
                            row -> row.get(2).toString(), // Turno (columna C)
                            Collectors.summingDouble(row -> Double.parseDouble(row.get(4).toString())) // Monto (columna E)
                    ));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    @Override
    public Map<String, Long> contarPagosPorMedioDePago() {
        try {
            ValueRange response = sheetsService.spreadsheets().values()
                    .get(SPREADSHEET_ID, "REGISTRO_PAGOS!A:F")
                    .execute();

            return response.getValues().stream()
                    .filter(row -> row.size() >= 6)
                    .collect(Collectors.groupingBy(
                            row -> row.get(5).toString(), // Medio de pago (columna F)
                            Collectors.counting()
                    ));
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    private String getColumnLetter(int columnIndex) {
        StringBuilder columnName = new StringBuilder();
        while (columnIndex > 0) {
            int remainder = (columnIndex - 1) % 26;
            columnName.insert(0, (char) ('A' + remainder));
            columnIndex = (columnIndex - 1) / 26;
        }
        return columnName.toString();
    }
}