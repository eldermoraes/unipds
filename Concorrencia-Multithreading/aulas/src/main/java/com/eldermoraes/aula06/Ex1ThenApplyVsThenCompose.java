package com.eldermoraes.aula06;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ex1ThenApplyVsThenCompose {

    record User(int id, String name) {}
    record Profile(int userId, String details) {}

    static CompletableFuture<User> getUserById(int id) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Buscando usuário " + id + "...");
            return new User(id, "Usuário " + id);
        });
    }

    static CompletableFuture<Profile> getProfileForUser(User user) {
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Buscando perfil para " + user.name() + "...");
            return new Profile(user.id(), "Detalhes do perfil...");
        });
    }

    public static void main(String[] args) {
        // Uso incorreto com thenApply para encadear operações assíncronas
        CompletableFuture<CompletableFuture<Profile>> nestedFuture =
                getUserById(101).thenApply(user -> getProfileForUser(user));
        System.out.println("Tipo de retorno com thenApply: " + nestedFuture.getClass().getSimpleName());

        // Uso correto com thenCompose para "achatar" o resultado
        CompletableFuture<Profile> flatFuture =
                getUserById(102).thenCompose(user -> getProfileForUser(user));
        System.out.println("Tipo de retorno com thenCompose: " + flatFuture.getClass().getSimpleName());

        Profile profile = flatFuture.join(); // Bloqueia para obter o resultado final
        System.out.println("Perfil obtido: " + profile);
    }
}