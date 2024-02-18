package com.nextreleaseproblem.model;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Data
@Configuration
public class GeradorPDF {

    private final TemplateEngine templateEngine;

    public GeradorPDF(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdfFromTemplate(String templateName, Context context) throws Exception {
        String renderedHtml = templateEngine.process(templateName, context);

        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(renderedHtml);
            renderer.layout();
            renderer.createPDF(outputStream);
            return ((ByteArrayOutputStream) outputStream).toByteArray();
        }
    }
}
