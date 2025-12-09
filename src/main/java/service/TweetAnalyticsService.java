package service;

import model.Tweet;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class TweetAnalyticsService {

    public final Function<Function<Tweet, Tweet>, Function<List<Tweet>, List<Tweet>>> transformar
            = transformacion -> tweets -> tweets.stream()
                    .map(transformacion)
                    .collect(Collectors.toList());

    public final BiFunction<Function<Tweet, Tweet>, Consumer<Tweet>, Consumer<List<Tweet>>> crearProcesador
            = (transform, consumer) -> tweets -> tweets.stream()
                    .map(transform)
                    .forEach(consumer);

    public final BiFunction<List<Tweet>, String, Double> calcularPromedioLongitud
            = (tweets, sentimiento) -> tweets.stream()
                    .filter(t -> t.getSentimiento() != null && t.getSentimiento().equalsIgnoreCase(sentimiento))
                    .mapToInt(t -> t.getTexto() != null ? t.getTexto().length() : 0)
                    .average()
                    .orElse(0.0);

    public final Function<List<Tweet>, Map<String, Long>> contarTweetsPorSentimiento
            = tweets -> tweets.stream()
                    .filter(t -> t.getSentimiento() != null)
                    .collect(Collectors.groupingBy(t -> t.getSentimiento().toLowerCase(), Collectors.counting()));
}
