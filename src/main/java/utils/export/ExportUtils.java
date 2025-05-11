package utils.export;

import entities.Fournisseur;
import entities.Materiel;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class ExportUtils {

    // ========================
    // EXPORT VERS EXCEL
    // ========================

    /**
     * Exporte une liste d'objets Materiel vers un fichier Excel.
     *
     * @param filePath Chemin du fichier Excel à générer
     * @param materiaux Liste des matériaux à exporter
     * @throws IOException Si l'écriture du fichier échoue
     */
    public static void exportToExcel(String filePath, List<Materiel> materiaux) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Matériels");

        int rowIdx = 0;
        Row headerRow = sheet.createRow(rowIdx++);
        Cell headerCell;

        headerCell = headerRow.createCell(0); headerCell.setCellValue("Nom");
        headerCell = headerRow.createCell(1); headerCell.setCellValue("Type");
        headerCell = headerRow.createCell(2); headerCell.setCellValue("Quantité Totale");
        headerCell = headerRow.createCell(3); headerCell.setCellValue("Quantité Disponible");
        headerCell = headerRow.createCell(4); headerCell.setCellValue("État");
        headerCell = headerRow.createCell(5); headerCell.setCellValue("Prix");

        for (Materiel m : materiaux) {
            Row dataRow = sheet.createRow(rowIdx++);
            dataRow.createCell(0).setCellValue(m.getNom_mat());
            dataRow.createCell(1).setCellValue(m.getType_mat());
            dataRow.createCell(2).setCellValue(m.getQte_tot());
            dataRow.createCell(3).setCellValue(m.getQte_disp());
            dataRow.createCell(4).setCellValue(m.getEtat());
            dataRow.createCell(5).setCellValue(m.getPrix());
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    /**
     * Exporte une liste d'objets Fournisseur vers un fichier Excel.
     *
     * @param filePath Chemin du fichier Excel à générer
     * @param fournisseurs Liste des fournisseurs à exporter
     * @throws IOException Si l'écriture du fichier échoue
     */
    public static void exportFournisseursToExcel(String filePath, List<Fournisseur> fournisseurs) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Fournisseurs");

        int rowIdx = 0;
        Row headerRow = sheet.createRow(rowIdx++);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nom");
        headerRow.createCell(2).setCellValue("Téléphone");

        for (Fournisseur f : fournisseurs) {
            Row dataRow = sheet.createRow(rowIdx++);
            dataRow.createCell(0).setCellValue(f.getId_fourn());
            dataRow.createCell(1).setCellValue(f.getNom_fourn());
            dataRow.createCell(2).setCellValue(f.getNum_fourn());
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    /**
     * Exporte les données d'une ResultSet vers un fichier Excel.
     *
     * @param filePath Chemin du fichier Excel à générer
     * @param resultSet Résultat de requête SQL contenant les données à exporter
     * @throws IOException Si l'écriture du fichier échoue
     * @throws SQLException Si la lecture du ResultSet échoue
     */
    public static void exportResultSetToExcel(String filePath, ResultSet resultSet) throws IOException, SQLException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export");

        int rowIdx = 0;

        // En-tête du tableau
        Row headerRow = sheet.createRow(rowIdx++);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            Cell headerCell = headerRow.createCell(i - 1);
            headerCell.setCellValue(metaData.getColumnName(i));
        }

        // Données
        while (resultSet.next()) {
            Row dataRow = sheet.createRow(rowIdx++);
            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                Cell cell = dataRow.createCell(i - 1);
                if (value != null) {
                    cell.setCellValue(value.toString());
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }

    /**
     * Ouvre un dialogue pour que l'utilisateur choisisse l'emplacement et le nom du fichier à exporter.
     *
     * @param stage La fenêtre principale (ou parente) pour centrer le dialogue
     * @param defaultFileName Le nom par défaut du fichier
     * @return Le fichier choisi, ou null si annulé
     */
    private File chooseExportFile(Stage stage, String defaultFileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les données");
        fileChooser.setInitialFileName(defaultFileName);

        // Filtre d'extensions (optionnel)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("Tous les fichiers", "*.*")
        );

        return fileChooser.showSaveDialog(stage);
    }
}