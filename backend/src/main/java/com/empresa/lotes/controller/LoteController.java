package com.empresa.lotes.controller;

import com.empresa.lotes.dto.LoteRequest;
import com.empresa.lotes.dto.LoteResponse;
import com.empresa.lotes.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    @Autowired
    private LoteService service;

    @PostMapping
    public ResponseEntity<LoteResponse> criar(@RequestBody @Valid LoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }
}
