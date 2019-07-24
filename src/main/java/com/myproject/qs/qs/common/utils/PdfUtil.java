package com.myproject.qs.qs.common.utils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.myproject.qs.qs.domainobject.QuestionDo;
import com.myproject.qs.qs.domainobject.ResultDetailDo;
import com.myproject.qs.qs.domainobject.SectionDo;
import com.thoughtworks.xstream.XStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import javax.xml.XMLConstants;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.springframework.core.io.Resource;
import java.io.*;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;


public class PdfUtil {

    public static byte[] exportPdf(ResultDetailDo resultDetailDo) {
        if (resultDetailDo == null) {
            return new byte[0];
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            String xslTemplate = getPdfTemplate();
            if (!StringUtils.isEmpty(xslTemplate)) {
                String resultXml = parseDoToXML(resultDetailDo);

                StreamResult htmlOutput = getHTMLOutput(resultXml, xslTemplate);

                convertHTMLToPDF(byteArrayOutputStream, htmlOutput);

                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            return new byte[0];
        }
        return new byte[0];
    }

    private static void convertHTMLToPDF(ByteArrayOutputStream byteArrayOutputStream, StreamResult htmlOutput) throws DocumentException, IOException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlOutput.getWriter().toString());
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont("templates/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream, true, 1);
    }

    private static String parseDoToXML(ResultDetailDo resultDetailDo) {
        XStream xStream = new XStream();
        xStream.alias("ResultDetailDo", ResultDetailDo.class);
        xStream.alias("SectionDo", SectionDo.class);
        xStream.alias("QuestionDo", QuestionDo.class);
        xStream.alias("String", String.class);
        return xStream.toXML(resultDetailDo);
    }

    private static StreamResult getHTMLOutput(String resultXml, String xslTemplate) throws TransformerException {
        Source xmlSource = new StreamSource(new StringReader(resultXml));
        Source xsltSource = new StreamSource(new StringReader(xslTemplate));
        StreamResult htmlOutput = new StreamResult(new StringWriter());
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        Transformer transformer = factory.newTransformer(xsltSource);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(xmlSource, htmlOutput);
        return htmlOutput;
    }

    private static String getPdfTemplate() throws IOException {
        Resource resource = new ClassPathResource("templates/pdfTemplate.xml");
        File file = resource.getFile();
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }

}
