package service;


import model.Tweet;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.Supplier;

public class TweetLoader {

    // Supplier que al invocar get() lee el CSV y devuelve lista de Tweet
    public static Supplier<List<Tweet>> crearLectorTweets(String rutaArchivo){
        return () -> {
            List<Tweet> lista = new ArrayList<>();
            try (BufferedReader br = Files.newBufferedReader(Paths.get(rutaArchivo))) {
                String linea = br.readLine(); // posible encabezado
                while ((linea = br.readLine()) != null) {
                    // dividir en 4 partes: id, entidad, sentimiento, texto (texto puede contener comas)
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
}
