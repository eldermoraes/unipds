package com.eldermoraes.aula02;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Ex3ScheduledTasksExample {

    public static void main(String[] args) throws InterruptedException {
        try (ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2)) {

            // Tarefa 1: Executar uma vez após um atraso de 3 segundos.
            scheduler.schedule(() -> {
                System.out.println("Tarefa de execução única executada às " + LocalTime.now());
            }, 3, TimeUnit.SECONDS);

            // Tarefa 2: Executar a cada 5 segundos, com um atraso inicial de 1 segundo.
            // scheduleAtFixedRate inicia a próxima execução no tempo previsto,
            // mesmo que a execução anterior tenha atrasado.
            scheduler.scheduleAtFixedRate(() -> {
                System.out.println("Tarefa periódica (fixed rate) executada às " + LocalTime.now());
                try {
                    Thread.sleep(1000); // Simula trabalho.
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, 1, 5, TimeUnit.SECONDS);

            // Tarefa 3: Executar com um atraso fixo de 5 segundos entre o fim de uma
            // execução e o início da próxima.
            scheduler.scheduleWithFixedDelay(() -> {
                System.out.println("Tarefa periódica (fixed delay) executada às " + LocalTime.now());
                try {
                    Thread.sleep(2000); // Simula trabalho.
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, 1, 5, TimeUnit.SECONDS);

            // Deixa o scheduler rodar por 20 segundos para observação.
            Thread.sleep(20000);
        } // shutdown() é chamado aqui.
    }
}