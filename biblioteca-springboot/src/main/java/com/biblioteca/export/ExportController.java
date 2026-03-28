package com.biblioteca.export;

import com.biblioteca.service.LibroService;
import com.biblioteca.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exportar")
@Tag(name = "Exportar", description = "Endpoints para exportar datos en Excel y PDF")
public class ExportController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private PdfExportService pdfExportService;

    // ========================
    // EXCEL
    // ========================

    @GetMapping("/libros/excel")
    @Operation(summary = "Exportar lista de libros a Excel")
    public ResponseEntity<byte[]> exportarLibrosExcel() {
        try {
            byte[] data = excelExportService.exportarLibros(libroService.obtenerTodos());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=libros.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/prestamos/excel")
    @Operation(summary = "Exportar préstamos activos a Excel")
    public ResponseEntity<byte[]> exportarPrestamosExcel() {
        try {
            byte[] data = excelExportService.exportarPrestamos(prestamoService.obtenerActivos());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prestamos.xlsx")
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // ========================
    // PDF
    // ========================

    @GetMapping("/libros/pdf")
    @Operation(summary = "Exportar lista de libros a PDF")
    public ResponseEntity<byte[]> exportarLibrosPdf() {
        try {
            byte[] data = pdfExportService.exportarLibros(libroService.obtenerTodos());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=libros.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/prestamos/pdf")
    @Operation(summary = "Exportar préstamos activos a PDF")
    public ResponseEntity<byte[]> exportarPrestamosPdf() {
        try {
            byte[] data = pdfExportService.exportarPrestamos(prestamoService.obtenerActivos());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=prestamos.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
