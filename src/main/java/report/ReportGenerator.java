package report;

import model.Tweet;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.function.BiConsumer;

public class ReportGenerator {
    public final BiConsumer<List<Tweet>, String> guardarTweetsLimpios = (tweets, rutaSalida) -> {
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
    };

    public final BiConsumer<String, String> guardarResumenEstadisticas = (resumen, rutaSalida) -> {
        try {
            Path p = Paths.get(rutaSalida);
            Files.createDirectories(p.getParent());
            Files.writeString(p, resumen);
        } catch (IOException e) {
            System.err.println("Error guardando resumen: " + e.getMessage());
        }
    };
}
