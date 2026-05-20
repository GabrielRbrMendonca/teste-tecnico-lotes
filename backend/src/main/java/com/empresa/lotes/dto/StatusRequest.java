package com.empresa.lotes.dto;

import com.empresa.lotes.model.StatusLote;

import javax.validation.constraints.NotNull;

public class StatusRequest {

    @NotNull(message = "status é obrigatório")
    private StatusLote status;

    public StatusLote getStatus() { return status; }
    public void setStatus(StatusLote status) { this.status = status; }
}
