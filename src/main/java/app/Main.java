

package app;


import model.Tweet;
import report.ReportGenerator;
import service.TweetAnalyticsService;
import service.TweetLoader;

import java.util.*;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        String rutaCsv = "data/tweets.csv"; // ruta esperada
        String outTweets = "output/tweets_limpios.txt";
        String outResumen = "output/resumen_estadisticas.txt";

        Supplier<List<Tweet>> lector = TweetLoader.crearLectorTweets(rutaCsv);
        TweetAnalyticsService analytics = new TweetAnalyticsService();
        ReportGenerator report = new ReportGenerator();

        // función de transformación: limpiar texto (quitar @, #, espacios extra) y pasar a mayúsculas
        java.util.function.Function<Tweet, Tweet> limpiar = t -> {
            String txt = t.getTexto();
            if (txt == null) txt = "";
            txt = txt.replaceAll("@\\S+","")    // eliminar menciones
                     .replaceAll("#\\S+","")    // eliminar hashtags
                     .replaceAll("\\s+"," ")    // espacios múltiples -> uno
                     .trim()
                     .toUpperCase();            // pasar a mayúsculas (según enunciado)
            return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), txt);
        };

        // Consumer que acumula tweets limpios en una lista y los muestra por consola
        List<Tweet> tweetsLimpios = new ArrayList<>();
        java.util.function.Consumer<Tweet> consumidor = t -> {
            tweetsLimpios.add(t);
            System.out.println(t);
        };

        // Runnable principal que ejecuta pipeline: lectura -> transformación -> análisis -> reporte
        Runnable pipelinePrincipal = () -> {
            List<Tweet> raw = lector.get();
            System.out.println("Tweets leídos: " + raw.size());

            // procesar (transformar + consumir)
            analytics.procesarTweets(raw, limpiar, consumidor);

            // estadísticas
            double promPos = analytics.calcularPromedioLongitud(tweetsLimpios, "positive");
            double promNeg = analytics.calcularPromedioLongitud(tweetsLimpios, "negative");
            Map<String, Long> conteos = analytics.contarTweetsPorSentimiento(tweetsLimpios);

            StringBuilder resumen = new StringBuilder();
            resumen.append("Promedio longitud (positive): ").append(String.format("%.2f", promPos)).append("\n");
            resumen.append("Promedio longitud (negative): ").append(String.format("%.2f", promNeg)).append("\n");
            resumen.append("Conteo por sentimiento:\n");
            conteos.forEach((k,v) -> resumen.append("  ").append(k).append(": ").append(v).append("\n"));

            // guardar resultados
            report.guardarTweetsLimpios(tweetsLimpios, outTweets);
            report.guardarResumenEstadisticas(resumen.toString(), outResumen);
        };

        // Ejecuta pipeline
        pipelinePrincipal.run();
    }
}
