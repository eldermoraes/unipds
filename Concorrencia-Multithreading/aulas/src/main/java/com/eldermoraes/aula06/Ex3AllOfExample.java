package com.eldermoraes.aula06;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Ex3AllOfExample {

    static CompletableFuture<String> downloadData(String source) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int delay = 1000 + (int) (Math.random() * 1000);
                Thread.sleep(delay);
                System.out.println("Dados de " + source + " baixados em " + delay + "ms.");
            } catch (Exception e) {}
            return "Dados de " + source;
        });
    }

    public static void main(String[] args) {
        List<String> sources = List.of("API_1", "API_2", "API_3", "API_4");

        List<CompletableFuture<String>> futures = sources.stream()
                .map(Ex3AllOfExample::downloadData)
                .collect(Collectors.toList());

        // CompletableFuture.allOf retorna CompletableFuture<Void>.
        // Ele serve apenas para sinalizar que todas as tarefas terminaram.
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        // Para obter os resultados, precisamos processar a lista original de futures
        // após a conclusão do allDoneFuture.
        CompletableFuture<List<String>> allResultsFuture = allDoneFuture.thenApply(v ->
                futures.stream()
                        .map(CompletableFuture::join) // join() é seguro aqui porque sabemos que todos já terminaram.
                        .collect(Collectors.toList())
        );

        List<String> results = allResultsFuture.join();
        System.out.println("\nTodos os downloads concluídos. Resultados:");
        results.forEach(System.out::println);
    }
}