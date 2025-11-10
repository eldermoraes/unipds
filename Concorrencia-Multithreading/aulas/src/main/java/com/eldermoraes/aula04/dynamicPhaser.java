package com.eldermoraes.aula04;

import com.eldermoraes.aula04.Ex4DynamicPhaserExample.Task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
public class dynamicPhaser {


  static class Task implements Runnable {

    private final int id;
    private final Phaser phaser;

    Task(int id, Phaser phaser) {
      this.id = id;
      this.phaser = phaser;
      phaser.register(); // Registra a tarefa como um participante.
      System.out.println(
          "Tarefa " + id + " registrada. Participantes: " + phaser.getRegisteredParties());
    }

    @Override
    public void run() {
      System.out.println("Tarefa " + id + " na Fase " + phaser.getPhase() + ".");
      phaser.arriveAndAwaitAdvance(); // Chega e espera pelos outros.

      // Apenas algumas tarefas continuam para a próxima fase.
      if (id % 2 == 0) {
        System.out.println("Tarefa " + id + " continuando para a Fase " + phaser.getPhase() + ".");
        phaser.arriveAndAwaitAdvance();
        System.out.println("Tarefa " + id + " concluiu todas as fases.");
        phaser.arriveAndDeregister(); // Conclui e se desregistra.
      } else {
        System.out.println("Tarefa " + id + " concluindo e se desregistrando na Fase " + phaser.getPhase() + ".");
        phaser.arriveAndDeregister(); // Chega, se desregistra e não espera.
      }
    }
  }

  public static void main(String[] args) {
    Phaser phaser = new Phaser(1); // 1 para a thread principal.

    try (ExecutorService executor = Executors.newCachedThreadPool()) {
      for (int i = 0; i < 4; i++) {
        executor.submit(new Task(i, phaser));
      }
      System.out.println("Aguardando todas as tarefas concluírem a Fase 0...");
      phaser.arriveAndAwaitAdvance();
      System.out.println("Fase 0 concluída. Participantes restantes: " + phaser.getRegisteredParties());

      System.out.println("Aguardando tarefas restantes concluírem a Fase 1...");
      phaser.arriveAndAwaitAdvance();
      System.out.println("Fase 1 concluída. Participantes restantes: " + phaser.getRegisteredParties());

      phaser.arriveAndDeregister(); // Thread principal se desregistra.
      System.out.println("Phaser terminado: " + phaser.isTerminated());
    }
  }
}
