package com.eldermoraes.aula03;

import com.eldermoraes.aula03.Ex2CopyOnWriteListenerExample.EventListener;
import com.eldermoraes.aula03.Ex2CopyOnWriteListenerExample.Notifier;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
public class copyOnWriteListener {
  // Interface para o listener.
  interface EventListener {
    void onEvent(String event);
  }

  // Classe que notifica os listeners.
  static class Notifier {
    private final List<EventListener> listeners = new CopyOnWriteArrayList<>();

    public void addListener( EventListener listener) {
      listeners.add(listener);
      System.out.println("Listener adicionado. Lista atual: " + listeners.size());
    }

    public void notifyListeners(String event) {
      System.out.println("Notificando " + listeners.size() + " listeners sobre o evento: " + event);
      // A iteração é segura e não lança ConcurrentModificationException.
      for (EventListener listener : listeners) {
        listener.onEvent(event);
        try {
          // Simula um trabalho de notificação.
          Thread.sleep(10);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Notifier notifier = new Notifier();
    notifier.addListener(event -> System.out.println("Listener A recebeu: " + event));
    notifier.addListener(event -> System.out.println("Listener B recebeu: " + event));

    try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
      // Thread 1: Notifica os listeners continuamente.
      executor.submit(() -> {
        for (int i = 0; i < 5; i++) {
          notifier.notifyListeners("tread 1 = Evento " + i);
        }
      });

  //     Thread 2: Adiciona um novo listener enquanto a notificação está ocorrendo.
      executor.submit(() -> {
        try {
          Thread.sleep(50); // Espera um pouco para a notificação começar.
          notifier.addListener(event -> System.out.println("tread 2 = Listener C (novo) recebeu: " + event));
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      });
      executor.shutdown();
      executor.awaitTermination(10, TimeUnit.SECONDS);
    }
  }
}