package com.eldermoraes.aula06;

import java.util.concurrent.CompletableFuture;

public class Ex4ErrorHandlingExample {

    public static void main(String[] args) {
        // 1. Usando exceptionally() para fornecer um valor de fallback (como um 'catch')
        CompletableFuture<String> futureWithFallback = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Falha na simulação!");
            }
            return "Sucesso!";
        }).exceptionally(ex -> {
            System.err.println("Ocorreu um erro: " + ex.getMessage());
            return "Valor de Fallback"; // Retorna um valor padrão em caso de erro.
        });

        System.out.println("Resultado com fallback: " + futureWithFallback.join());
        System.out.println("---");

        // 2. Usando handle() para processar sucesso ou falha (como um 'finally' que retorna valor)
        CompletableFuture<String> futureHandled = CompletableFuture.supplyAsync(() -> {
            if (Math.random() > 0.5) {
                throw new RuntimeException("Falha na simulação!");
            }
            return "Sucesso!";
        }).handle((result, ex) -> {
            if (ex!= null) {
                System.err.println("Ocorreu um erro: " + ex.getMessage());
                return "Resultado do Erro Processado";
            }
            return "Resultado de Sucesso Processado: " + result;
        });

        System.out.println("Resultado com handle: " + futureHandled.join());
    }
}