package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.LongStream;

public class InfoController {

    @Value("${server.port}")

    @GetMapping("/port")
    public ResponseEntity<Integer> getPort() {
        return ResponseEntity.ok(8080);
    }

    @GetMapping("/parallel-sum")
    public ResponseEntity<Long> getParallelSum() {
        long sum = LongStream.rangeClosed(1, 1_000_000)
                .parallel() // параллельная обработка
                .sum();
        return ResponseEntity.ok(sum);
    }
}
