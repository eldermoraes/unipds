package com.eldermoraes.aula06;

import java.util.concurrent.CompletableFuture;

public class Ex2ThenCombineExample {

    static CompletableFuture<String> getOrderHistory() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
            return "Histórico de Pedidos";
        });
    }

    static CompletableFuture<String> getShippingPreferences() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1200);
            } catch (Exception e) {}
            return "Preferências de Envio";
        });
    }

    public static void main(String[] args) {
        System.out.println("Iniciando busca de dados do cliente...");

        CompletableFuture<String> customerDataFuture =
                getOrderHistory().thenCombine(getShippingPreferences(), (history, prefs) -> {
                    return "Dados do Cliente:\n- " + history + "\n- " + prefs;
                });

        String customerData = customerDataFuture.join();
        System.out.println(customerData);
    }
}