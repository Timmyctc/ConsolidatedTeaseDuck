package com.luv2code.springboot.consolidatedteaseduck.controller;

import com.luv2code.springboot.consolidatedteaseduck.dto.ReadingRequest;
import com.luv2code.springboot.consolidatedteaseduck.service.ReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReadingController {

    private ReadingService readingService;

    @PostMapping("/readings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void submitSensorReading(@Valid @RequestBody ReadingRequest request) {
        readingService
    }

    @GetMapping("/readings")
    public


}
