package service;


import model.Tweet;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

public class TweetLoader {
    public static final Function<String, Supplier<List<Tweet>>> crearLector =
            ruta -> () -> {
                List<Tweet> lista = new ArrayList<>();
                try (BufferedReader br = Files.newBufferedReader(Paths.get(ruta))) {
                    String linea = br.readLine(); // posible encabezado
                    while ((linea = br.readLine()) != null) {
                        String[] partes = linea.split(",", 4);
                        if (partes.length < 4) continue;
                        String id = partes[0].trim().replaceAll("^\"|\"$", "");
                        String entidad = partes[1].trim().replaceAll("^\"|\"$", "");
                        String sentimiento = partes[2].trim().replaceAll("^\"|\"$", "");
                        String texto = partes[3].trim().replaceAll("^\"|\"$", "");
                        lista.add(new Tweet(id, entidad, sentimiento, texto));
                    }
                } catch (IOException e) {
                    System.err.println("Error leyendo archivo: " + e.getMessage());
                }
                return lista;
            };
}