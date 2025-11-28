package com.locadora.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "alugueis")
public class Aluguel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String status;

    private int quilometragemInicial;
    private Integer quilometragemFinal;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Veiculo veiculo;

    public Aluguel() {}

    public Aluguel(LocalDate dataInicio, LocalDate dataFim, String status,
                   int quilometragemInicial, Usuario usuario, Veiculo veiculo) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.quilometragemInicial = quilometragemInicial;
        this.usuario = usuario;
        this.veiculo = veiculo;
    }

    public Long getId() { return id; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getQuilometragemInicial() { return quilometragemInicial; }
    public void setQuilometragemInicial(int quilometragemInicial) { this.quilometragemInicial = quilometragemInicial; }

    public Integer getQuilometragemFinal() { return quilometragemFinal; }
    public void setQuilometragemFinal(Integer quilometragemFinal) { this.quilometragemFinal = quilometragemFinal; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }
}
