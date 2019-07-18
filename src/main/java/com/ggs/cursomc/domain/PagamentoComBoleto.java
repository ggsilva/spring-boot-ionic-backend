package com.ggs.cursomc.domain;

import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class PagamentoComBoleto extends Pagamento {

	private static final long serialVersionUID = -2820967032936495921L;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dataVencimento;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dataPagamento;
	
	public Date getDataVencimento() {
		return dataVencimento;
	}
	
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

}
