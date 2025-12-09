package app;

import model.Tweet;
import report.ReportGenerator;
import service.TweetAnalyticsService;
import service.TweetLoader;

import java.util.*;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Main {
    public static void main(String[] args) {
        String rutaCsv = "data/tweets.csv";
        String outTweets = "output/tweets_limpios.txt";
        String outResumen = "output/resumen_estadisticas.txt";

        Supplier<List<Tweet>> lector = TweetLoader.crearLector.apply(rutaCsv);

        TweetAnalyticsService analytics = new TweetAnalyticsService();
        ReportGenerator report = new ReportGenerator();

        Function<Tweet, Tweet> limpiar = t -> {
            String txt = t.getTexto() == null ? "" : t.getTexto()
                    .replaceAll("@\\S+", "")
                    .replaceAll("#\\S+", "")
                    .replaceAll("\\s+", " ")
                    .trim()
                    .toUpperCase();
            return new Tweet(t.getId(), t.getEntidad(), t.getSentimiento(), txt);
        };
        List<Tweet> tweetsLimpios = new ArrayList<>();
        Consumer<Tweet> consumidor = t -> {
            tweetsLimpios.add(t);
            System.out.println(t);
        };

        Consumer<List<Tweet>> procesador = analytics.crearProcesador.apply(limpiar, consumidor);

        Runnable pipeline = () -> {
            List<Tweet> raw = lector.get();
            System.out.println("Tweets leídos: " + raw.size());

            procesador.accept(raw);

            double promPos = analytics.calcularPromedioLongitud.apply(tweetsLimpios, "positive");
            double promNeg = analytics.calcularPromedioLongitud.apply(tweetsLimpios, "negative");
            Map<String, Long> conteos = analytics.contarTweetsPorSentimiento.apply(tweetsLimpios);

            StringBuilder resumen = new StringBuilder();
            resumen.append("Promedio longitud (positive): ").append(String.format("%.2f", promPos)).append("\n");
            resumen.append("Promedio longitud (negative): ").append(String.format("%.2f", promNeg)).append("\n");
            resumen.append("Conteo por sentimiento:\n");
            conteos.forEach((k,v) -> resumen.append("  ").append(k).append(": ").append(v).append("\n"));

            report.guardarTweetsLimpios.accept(tweetsLimpios, outTweets);
            report.guardarResumenEstadisticas.accept(resumen.toString(), outResumen);
        };

        pipeline.run();
    }
}
