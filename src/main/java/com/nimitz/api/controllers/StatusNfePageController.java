package com.nimitz.api.controllers;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nimitz.api.models.HistoricoStatus;
import com.nimitz.api.repository.HistoricoStatusRepository;

@Component
public class StatusNfePageController {

	@Autowired
	HistoricoStatusRepository repository;

	private static final String URL_NFE_DISPONIBILIDADE = "https://www.nfe.fazenda.gov.br/portal/disponibilidade.aspx";

	private static final String ID_TABLE_STATUS = "ctl00_ContentPlaceHolder1_gdvDisponibilidade2";

	public List<HistoricoStatus> capturaHistoricosFromPage() throws Exception {
		List<HistoricoStatus> historicos = new ArrayList<HistoricoStatus>();
		Document nfePage = Jsoup.connect(URL_NFE_DISPONIBILIDADE).get();
		Elements listaHistoricosHtml = nfePage.getElementById(ID_TABLE_STATUS).getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");
		listaHistoricosHtml.remove(0); // REMOVE CABEÇALHO DA TABELA POIS NÃO NOS INTERRESSA

		for (Element linhaStatus : listaHistoricosHtml) {
			HistoricoStatus historico = new HistoricoStatus();
			historico.setAutorizador(linhaStatus.getElementsByIndexEquals(0).text());
			historico.setData(System.currentTimeMillis());
			historico.setStatus(getStatusByUrlmagem(linhaStatus));
			historicos.add(historico);
		}

		return historicos;
	}

	private short getStatusByUrlmagem(Element linhaStatus) {
		String urlImagemStatus = linhaStatus.getElementsByTag("td").get(5).child(0).attr("src");
		switch (urlImagemStatus) {
		case "imagens/bola_verde_P.png":
			return 1;
		case "imagens/bola_amarela_P.png":
			return 2;
		default:
			return 3;
		}
	}

	public List<HistoricoStatus> salvaHistoricosPeriodicamente() throws Exception {
		List<HistoricoStatus> historicos = capturaHistoricosFromPage();
		repository.saveAll(historicos);
		return historicos;
	}
}
