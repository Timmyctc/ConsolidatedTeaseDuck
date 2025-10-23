package com.luv2code.springboot.consolidatedteaseduck.controller;

import com.luv2code.springboot.consolidatedteaseduck.service.ReadingAggregateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/readings")
public class ReadingAggregateController {

    private ReadingAggregateService service;

    public ReadingAggregateController(ReadingAggregateService service);
}
