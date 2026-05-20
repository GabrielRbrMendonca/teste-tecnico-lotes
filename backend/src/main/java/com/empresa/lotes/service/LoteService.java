package com.empresa.lotes.service;

import com.empresa.lotes.dto.DocumentoResponse;
import com.empresa.lotes.dto.LoteRequest;
import com.empresa.lotes.dto.LoteResponse;
import com.empresa.lotes.dto.PageResponse;
import com.empresa.lotes.exception.LoteNaoEncontradoException;
import com.empresa.lotes.exception.TransicaoInvalidaException;
import com.empresa.lotes.model.Documento;
import com.empresa.lotes.model.Lote;
import com.empresa.lotes.model.StatusLote;
import com.empresa.lotes.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoteService {

    @Autowired
    private LoteRepository repository;

    public PageResponse listar(StatusLote status, String operador, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Lote> resultado;

        if (status != null && operador != null) {
            resultado = repository.findByStatusAndOperador(status, operador, pageable);
        } else if (status != null) {
            resultado = repository.findByStatus(status, pageable);
        } else if (operador != null) {
            resultado = repository.findByOperador(operador, pageable);
        } else {
            resultado = repository.findAll(pageable);
        }

        List<LoteResponse> content = resultado.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        return new PageResponse(content, resultado.getTotalElements(),
                resultado.getTotalPages(), page, size);
    }

    public LoteResponse criar(LoteRequest req) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<LoteRequest>> violations = validator.validate(req);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Lote lote = new Lote();
        lote.setOperador(req.getOperador());
        lote.setProcesso(req.getProcesso());

        List<Documento> documentos = req.getDocumentos().stream()
                .map(d -> {
                    Documento doc = new Documento();
                    doc.setTipo(d.getTipo());
                    doc.setNome(d.getNome());
                    doc.setLote(lote);
                    return doc;
                })
                .collect(Collectors.toList());

        lote.setDocumentos(documentos);
        return toResponse(repository.save(lote));
    }

    public LoteResponse atualizarStatus(Long id, StatusLote novoStatus) {
        Lote lote = repository.findById(id)
                .orElseThrow(() -> new LoteNaoEncontradoException(id));

        if (!lote.getStatus().canTransitionTo(novoStatus)) {
            throw new TransicaoInvalidaException(lote.getStatus(), novoStatus);
        }

        lote.setStatus(novoStatus);
        return toResponse(repository.save(lote));
    }

    private LoteResponse toResponse(Lote lote) {
        LoteResponse response = new LoteResponse();
        response.setId(lote.getId());
        response.setOperador(lote.getOperador());
        response.setProcesso(lote.getProcesso());
        response.setStatus(lote.getStatus());
        response.setDataCriacao(lote.getDataCriacao());

        List<DocumentoResponse> docs = lote.getDocumentos().stream()
                .map(d -> new DocumentoResponse(d.getId(), d.getTipo(), d.getNome()))
                .collect(Collectors.toList());
        response.setDocumentos(docs);

        return response;
    }
}
