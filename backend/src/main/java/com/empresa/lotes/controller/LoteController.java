package com.empresa.lotes.controller;

import com.empresa.lotes.dto.LoteRequest;
import com.empresa.lotes.dto.LoteResponse;
import com.empresa.lotes.dto.PageResponse;
import com.empresa.lotes.dto.StatusRequest;
import com.empresa.lotes.model.StatusLote;
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

    @GetMapping
    public ResponseEntity<PageResponse> listar(
            @RequestParam(required = false) StatusLote status,
            @RequestParam(required = false) String operador,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.listar(status, operador, page, size));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<LoteResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid StatusRequest request) {
        return ResponseEntity.ok(service.atualizarStatus(id, request.getStatus()));
    }
}
