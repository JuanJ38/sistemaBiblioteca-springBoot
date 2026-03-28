package com.biblioteca.export;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class PdfExportService {

    private static final BaseColor COLOR_HEADER = new BaseColor(0, 150, 150);
    private static final BaseColor COLOR_FILA_PAR = new BaseColor(240, 248, 248);
    private static final BaseColor COLOR_DISPONIBLE = new BaseColor(212, 237, 218);
    private static final BaseColor COLOR_NO_DISPONIBLE = new BaseColor(248, 215, 218);

    public byte[] exportarLibros(List<Libro> libros) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Título
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, COLOR_HEADER);
        Paragraph titulo = new Paragraph("REPORTE DE LIBROS", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(5);
        document.add(titulo);

        // Subtítulo
        Font subFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.GRAY);
        Paragraph sub = new Paragraph("Sistema de Biblioteca — Generado: " + LocalDate.now(), subFont);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(20);
        document.add(sub);

        // Línea separadora
        LineSeparator separator = new LineSeparator();
        separator.setLineColor(COLOR_HEADER);
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE);

        // Tabla
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 4f, 3f, 2f});
        table.setSpacingBefore(10);

        // Encabezados
        agregarEncabezado(table, "ID");
        agregarEncabezado(table, "Título");
        agregarEncabezado(table, "Autor");
        agregarEncabezado(table, "Disponible");

        // Datos
        boolean parImpar = false;
        for (Libro libro : libros) {
            BaseColor bgColor = parImpar ? COLOR_FILA_PAR : BaseColor.WHITE;

            agregarCelda(table, String.valueOf(libro.getId()), bgColor, Element.ALIGN_CENTER);
            agregarCelda(table, libro.getTitulo(), bgColor, Element.ALIGN_LEFT);
            agregarCelda(table, libro.getAutor(), bgColor, Element.ALIGN_LEFT);

            BaseColor dispColor = libro.isDisponible() ? COLOR_DISPONIBLE : COLOR_NO_DISPONIBLE;
            String dispTexto = libro.isDisponible() ? "Sí" : "No";
            agregarCelda(table, dispTexto, dispColor, Element.ALIGN_CENTER);

            parImpar = !parImpar;
        }

        document.add(table);

        // Resumen
        document.add(Chunk.NEWLINE);
        Font resumenFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        long disponibles = libros.stream().filter(Libro::isDisponible).count();
        document.add(new Paragraph("Total de libros: " + libros.size(), resumenFont));
        document.add(new Paragraph("Disponibles: " + disponibles, resumenFont));
        document.add(new Paragraph("No disponibles: " + (libros.size() - disponibles), resumenFont));

        document.close();
        return out.toByteArray();
    }

    public byte[] exportarPrestamos(List<Prestamo> prestamos) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // Título
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, COLOR_HEADER);
        Paragraph titulo = new Paragraph("REPORTE DE PRÉSTAMOS ACTIVOS", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(5);
        document.add(titulo);

        Font subFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.GRAY);
        Paragraph sub = new Paragraph("Sistema de Biblioteca — Generado: " + LocalDate.now(), subFont);
        sub.setAlignment(Element.ALIGN_CENTER);
        sub.setSpacingAfter(20);
        document.add(sub);

        LineSeparator separator = new LineSeparator();
        separator.setLineColor(COLOR_HEADER);
        document.add(new Chunk(separator));
        document.add(Chunk.NEWLINE);

        // Tabla
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 4f, 3f, 2.5f});
        table.setSpacingBefore(10);

        agregarEncabezado(table, "ID");
        agregarEncabezado(table, "Libro");
        agregarEncabezado(table, "Usuario");
        agregarEncabezado(table, "Fecha Préstamo");

        boolean parImpar = false;
        for (Prestamo p : prestamos) {
            BaseColor bgColor = parImpar ? COLOR_FILA_PAR : BaseColor.WHITE;
            agregarCelda(table, String.valueOf(p.getId()), bgColor, Element.ALIGN_CENTER);
            agregarCelda(table, p.getLibro().getTitulo(), bgColor, Element.ALIGN_LEFT);
            agregarCelda(table, p.getUsuario().getNombre(), bgColor, Element.ALIGN_LEFT);
            agregarCelda(table, p.getFechaPrestamo().toString(), bgColor, Element.ALIGN_CENTER);
            parImpar = !parImpar;
        }

        document.add(table);

        document.add(Chunk.NEWLINE);
        Font resumenFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        document.add(new Paragraph("Total préstamos activos: " + prestamos.size(), resumenFont));

        document.close();
        return out.toByteArray();
    }

    private void agregarEncabezado(PdfPTable table, String texto) {
        Font font = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(COLOR_HEADER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        table.addCell(cell);
    }

    private void agregarCelda(PdfPTable table, String texto, BaseColor bgColor, int alignment) {
        Font font = new Font(Font.FontFamily.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(bgColor);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(6);
        table.addCell(cell);
    }
}
