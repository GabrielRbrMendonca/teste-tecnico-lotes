package com.empresa.lotes;

import com.empresa.lotes.dto.DocumentoRequest;
import com.empresa.lotes.dto.LoteRequest;
import com.empresa.lotes.dto.LoteResponse;
import com.empresa.lotes.exception.LoteNaoEncontradoException;
import com.empresa.lotes.exception.TransicaoInvalidaException;
import com.empresa.lotes.model.Lote;
import com.empresa.lotes.model.StatusLote;
import com.empresa.lotes.repository.LoteRepository;
import com.empresa.lotes.service.LoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    LoteRepository repository;

    @InjectMocks
    LoteService service;

    @Test
    void deveCriarLoteComStatusPendente() {
        LoteRequest req = new LoteRequest(
                "joao.silva",
                "ABERTURA_CONTA",
                Arrays.asList(new DocumentoRequest("RG", "rg_frente.jpg"))
        );
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        LoteResponse res = service.criar(req);

        assertEquals(StatusLote.PENDENTE, res.getStatus());
    }

    @Test
    void deveRetornar400QuandoOperadorAusente() {
        LoteRequest req = new LoteRequest(
                null,
                "ABERTURA_CONTA",
                Arrays.asList(new DocumentoRequest("RG", "rg.jpg"))
        );

        assertThrows(ConstraintViolationException.class, () -> service.criar(req));
    }

    @Test
    void deveRetornar400QuandoDocumentosVazios() {
        LoteRequest req = new LoteRequest(
                "joao.silva",
                "ABERTURA_CONTA",
                Arrays.asList()
        );

        assertThrows(ConstraintViolationException.class, () -> service.criar(req));
    }

    @Test
    void deveRetornar422AoAlterarLoteExportado() {
        Lote lote = new Lote();
        lote.setStatus(StatusLote.EXPORTADO);
        when(repository.findById(1L)).thenReturn(Optional.of(lote));

        assertThrows(TransicaoInvalidaException.class,
                () -> service.atualizarStatus(1L, StatusLote.REJEITADO));
    }

    @Test
    void deveRetornar404ParaLoteInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(LoteNaoEncontradoException.class,
                () -> service.atualizarStatus(999L, StatusLote.REJEITADO));
    }
}
