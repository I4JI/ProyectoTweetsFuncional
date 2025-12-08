/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package report;

import model.Tweet;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class ReportGenerator {

    public void guardarTweetsLimpios(List<Tweet> tweets, String rutaSalida){
        try {
            Path p = Paths.get(rutaSalida);
            Files.createDirectories(p.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(p)) {
                for (Tweet t : tweets) {
                    bw.write(t.toString());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error guardando tweets limpios: " + e.getMessage());
        }
    }

    public void guardarResumenEstadisticas(String resumen, String rutaSalida){
        try {
            Path p = Paths.get(rutaSalida);
            Files.createDirectories(p.getParent());
            Files.writeString(p, resumen);
        } catch (IOException e) {
            System.err.println("Error guardando resumen: " + e.getMessage());
        }
    }
}
