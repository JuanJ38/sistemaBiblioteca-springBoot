package com.biblioteca.export;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportarLibros(List<Libro> libros) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Libros");

            // Estilos
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle dataStyle = crearEstiloDato(workbook);
            CellStyle disponibleStyle = crearEstiloDisponible(workbook, true);
            CellStyle noDisponibleStyle = crearEstiloDisponible(workbook, false);

            // Título
            Row titulo = sheet.createRow(0);
            Cell tituloCell = titulo.createCell(0);
            tituloCell.setCellValue("REPORTE DE LIBROS - SISTEMA DE BIBLIOTECA");
            CellStyle tituloStyle = workbook.createCellStyle();
            Font tituloFont = workbook.createFont();
            tituloFont.setBold(true);
            tituloFont.setFontHeightInPoints((short) 14);
            tituloStyle.setFont(tituloFont);
            tituloCell.setCellStyle(tituloStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            // Encabezados
            Row encabezado = sheet.createRow(2);
            String[] columnas = {"ID", "Título", "Autor", "Disponible"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = encabezado.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int fila = 3;
            for (Libro libro : libros) {
                Row row = sheet.createRow(fila++);

                Cell idCell = row.createCell(0);
                idCell.setCellValue(libro.getId());
                idCell.setCellStyle(dataStyle);

                Cell tituloLibro = row.createCell(1);
                tituloLibro.setCellValue(libro.getTitulo());
                tituloLibro.setCellStyle(dataStyle);

                Cell autorCell = row.createCell(2);
                autorCell.setCellValue(libro.getAutor());
                autorCell.setCellStyle(dataStyle);

                Cell dispCell = row.createCell(3);
                dispCell.setCellValue(libro.isDisponible() ? "Disponible" : "No disponible");
                dispCell.setCellStyle(libro.isDisponible() ? disponibleStyle : noDisponibleStyle);
            }

            // Fila de total
            Row totalRow = sheet.createRow(fila + 1);
            Cell totalLabel = totalRow.createCell(0);
            totalLabel.setCellValue("Total de libros:");
            Cell totalValue = totalRow.createCell(1);
            totalValue.setCellValue(libros.size());

            // Autoajustar columnas
            // Anchos fijos — compatibles con Railway/Linux
            sheet.setColumnWidth(0, 8 * 256);    // ID
            sheet.setColumnWidth(1, 40 * 256);   // Título
            sheet.setColumnWidth(2, 30 * 256);   // Autor
            sheet.setColumnWidth(3, 15 * 256);   // Disponible

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] exportarPrestamos(List<Prestamo> prestamos) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Préstamos");

            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle dataStyle = crearEstiloDato(workbook);

            // Título
            Row titulo = sheet.createRow(0);
            Cell tituloCell = titulo.createCell(0);
            tituloCell.setCellValue("REPORTE DE PRÉSTAMOS ACTIVOS - SISTEMA DE BIBLIOTECA");
            CellStyle tituloStyle = workbook.createCellStyle();
            Font tituloFont = workbook.createFont();
            tituloFont.setBold(true);
            tituloFont.setFontHeightInPoints((short) 14);
            tituloStyle.setFont(tituloFont);
            tituloCell.setCellStyle(tituloStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

            // Encabezados
            Row encabezado = sheet.createRow(2);
            String[] columnas = {"ID", "Libro", "Usuario", "Fecha Préstamo"};
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = encabezado.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int fila = 3;
            for (Prestamo p : prestamos) {
                Row row = sheet.createRow(fila++);

                Cell idCell = row.createCell(0);
                idCell.setCellValue(p.getId());
                idCell.setCellStyle(dataStyle);

                Cell libroCell = row.createCell(1);
                libroCell.setCellValue(p.getLibro().getTitulo());
                libroCell.setCellStyle(dataStyle);

                Cell usuarioCell = row.createCell(2);
                usuarioCell.setCellValue(p.getUsuario().getNombre());
                usuarioCell.setCellStyle(dataStyle);

                Cell fechaCell = row.createCell(3);
                fechaCell.setCellValue(p.getFechaPrestamo().toString());
                fechaCell.setCellStyle(dataStyle);
            }

            // Total
            Row totalRow = sheet.createRow(fila + 1);
            totalRow.createCell(0).setCellValue("Total préstamos activos:");
            totalRow.createCell(1).setCellValue(prestamos.size());

            // Anchos fijos — compatibles con Railway/Linux
            sheet.setColumnWidth(0, 8 * 256);    // ID
            sheet.setColumnWidth(1, 40 * 256);   // Libro
            sheet.setColumnWidth(2, 30 * 256);   // Usuario
            sheet.setColumnWidth(3, 20 * 256);   // Fecha Préstamo

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_TEAL.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloDato(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloDisponible(Workbook workbook, boolean disponible) {
        CellStyle style = crearEstiloDato(workbook);
        style.setFillForegroundColor(disponible ?
                IndexedColors.LIGHT_GREEN.getIndex() :
                IndexedColors.ROSE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
}
