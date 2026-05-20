package com.empresa.lotes;

import com.empresa.lotes.dto.LoteResponse;
import com.empresa.lotes.dto.PageResponse;
import com.empresa.lotes.exception.LoteNaoEncontradoException;
import com.empresa.lotes.exception.TransicaoInvalidaException;
import com.empresa.lotes.model.StatusLote;
import com.empresa.lotes.service.LoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.empresa.lotes.controller.LoteController.class)
class LoteControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LoteService service;

    @Test
    void deveListarLotesComPaginacaoPadrao() throws Exception {
        LoteResponse lote = loteResponse(1L, "joao", "ABERTURA_CONTA", StatusLote.PENDENTE);
        PageResponse page = new PageResponse(Collections.singletonList(lote), 1L, 1, 0, 10);

        when(service.listar(null, null, 0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/lotes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content[0].operador").value("joao"));
    }

    @Test
    void deveListarLotesFiltrandoPorStatus() throws Exception {
        LoteResponse lote = loteResponse(2L, "maria", "ENCERRAMENTO", StatusLote.EXPORTADO);
        PageResponse page = new PageResponse(Collections.singletonList(lote), 1L, 1, 0, 10);

        when(service.listar(eq(StatusLote.EXPORTADO), isNull(), eq(0), eq(10))).thenReturn(page);

        mockMvc.perform(get("/api/lotes").param("status", "EXPORTADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("EXPORTADO"));
    }

    @Test
    void deveAtualizarStatusComSucesso() throws Exception {
        LoteResponse lote = loteResponse(1L, "joao", "ABERTURA_CONTA", StatusLote.EXPORTADO);
        when(service.atualizarStatus(1L, StatusLote.EXPORTADO)).thenReturn(lote);

        Map<String, String> body = new HashMap<>();
        body.put("status", "EXPORTADO");

        mockMvc.perform(patch("/api/lotes/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("EXPORTADO"));
    }

    @Test
    void deveRetornar422AoAtualizarStatusInvalido() throws Exception {
        when(service.atualizarStatus(eq(1L), any()))
                .thenThrow(new TransicaoInvalidaException(StatusLote.EXPORTADO, StatusLote.REJEITADO));

        Map<String, String> body = new HashMap<>();
        body.put("status", "REJEITADO");

        mockMvc.perform(patch("/api/lotes/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void deveRetornar404ParaLoteInexistente() throws Exception {
        when(service.atualizarStatus(eq(999L), any()))
                .thenThrow(new LoteNaoEncontradoException(999L));

        Map<String, String> body = new HashMap<>();
        body.put("status", "EXPORTADO");

        mockMvc.perform(patch("/api/lotes/999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").exists());
    }

    private LoteResponse loteResponse(Long id, String operador, String processo, StatusLote status) {
        LoteResponse r = new LoteResponse();
        r.setId(id);
        r.setOperador(operador);
        r.setProcesso(processo);
        r.setStatus(status);
        r.setDataCriacao(LocalDateTime.now());
        r.setDocumentos(Arrays.asList());
        return r;
    }
}
