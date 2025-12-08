/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Tweet;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class TweetAnalyticsService {

    // Transformar lista usando Function<Tweet,Tweet>
    public List<Tweet> transformarTweets(List<Tweet> tweets, Function<Tweet, Tweet> transformacion){
        return tweets.stream()
                     .map(transformacion)
                     .collect(Collectors.toList());
    }

    // Procesar con Function + Consumer (pipeline)
    public void procesarTweets(List<Tweet> tweets, Function<Tweet, Tweet> transformacion, Consumer<Tweet> accionFinal){
        tweets.stream()
              .map(transformacion)
              .forEach(accionFinal);
    }

    // Promedio de longitud del texto para un sentimiento dado
    public double calcularPromedioLongitud(List<Tweet> tweets, String sentimiento){
        return tweets.stream()
                     .filter(t -> t.getSentimiento() != null && t.getSentimiento().equalsIgnoreCase(sentimiento))
                     .mapToInt(t -> t.getTexto() != null ? t.getTexto().length() : 0)
                     .average()
                     .orElse(0.0);
    }

    // Conteo por sentimiento
    public Map<String, Long> contarTweetsPorSentimiento(List<Tweet> tweets){
        return tweets.stream()
                     .filter(t -> t.getSentimiento() != null)
                     .collect(Collectors.groupingBy(t -> t.getSentimiento().toLowerCase(), Collectors.counting()));
    }
}
