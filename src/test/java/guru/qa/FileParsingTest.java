package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.sun.source.tree.AssertTree;
import guru.qa.domain.Staff;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileParsingTest {
    String jsonClassPath = "src/test/resources/Staff.json";
    String zipClassPath = "src/test/resources/ZipFiles.zip";

    @Test
    void parsePdfTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("PDF.pdf");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        PDF pdf = new PDF(inputStream);
        assertThat(pdf.text).contains("Почему популярны файлы PDF?");
    }

    @Test
    void parseXlsxTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("XLSX.xlsx");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        XLS xls = new XLS(inputStream);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(2)
                .getCell(0)
                .getStringCellValue()).contains("Петров");
    }

    @Test
    void parseCsvTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("CSV.csv");
        try (InputStream inputStream = zipFiles.getInputStream(zipEntry);
             CSVReader csv = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> content = csv.readAll();
            assertThat(content.get(3)).contains("Вучич;550000");
        }
    }
    @Test
    void parseJsonTest() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        Staff staff = mapper.readValue(Paths.get(jsonClassPath).toFile(), Staff.class);
         assertThat(staff.name).isEqualTo("Артем");
    }
}

