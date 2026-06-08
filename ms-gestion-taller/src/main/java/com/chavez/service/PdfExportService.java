package com.chavez.service;

import com.chavez.dto.TallerDTO;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfExportService {

    public byte[] exportarTalleresPDF(List<TallerDTO> talleres) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Reporte de Talleres - AcademyHub",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Instructor");
            table.addCell("Cupo");
            table.addCell("Inscritos");

            for (TallerDTO t : talleres) {
                table.addCell(String.valueOf(t.getId()));
                table.addCell(t.getNombre());
                String instrName = t.getInstructor() != null
                        ? String.valueOf(t.getInstructor().getOrDefault("nombre", "N/A"))
                        : "N/A";
                table.addCell(instrName);
                table.addCell(String.valueOf(t.getCupo()));
                table.addCell(String.valueOf(
                        t.getAlumnos() != null ? t.getAlumnos().size() : 0));
            }

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total de talleres: " + talleres.size(),
                    FontFactory.getFont(FontFactory.HELVETICA, 10)));

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error generando PDF: " + e.getMessage());
        }

        return baos.toByteArray();
    }
}
