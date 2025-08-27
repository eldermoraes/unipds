package com.eldermoraes.aula07;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ex3ParallelStreamBadUseCaseStateful {

    public static void main(String[] args) {
        List<Integer> resultList = new ArrayList<>();

        // TENTATIVA INCORRETA de coletar resultados.
        // A ordem de execução não é garantida e múltiplas threads
        // modificarão a lista não-thread-safe simultaneamente.
        IntStream.range(0, 1000).parallel().forEach(i -> {
            resultList.add(i);
        });

        // O tamanho da lista provavelmente não será 1000.
        System.out.println("Tamanho da lista (incorreto): " + resultList.size());

        // A FORMA CORRETA é usar coletores thread-safe.
        List<Integer> correctList = IntStream.range(0, 1000)
                .parallel()
                .boxed()
                .collect(Collectors.toList());

        System.out.println("Tamanho da lista (correto): " + correctList.size());
    }
}