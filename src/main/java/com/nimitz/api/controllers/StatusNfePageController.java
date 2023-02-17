package com.nimitz.api.controllers;

import java.text.SimpleDateFormat;
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

	private static final int INDEX_CABECALHO = 0;

	private static final short BOM = 1;

	private static final short MEDIO = 2;

	private static final short RUIM = 3;

	public List<HistoricoStatus> capturaHistoricosFromPage() throws Exception {
		List<HistoricoStatus> historicos = new ArrayList<HistoricoStatus>();
		Document nfePage = Jsoup.connect(URL_NFE_DISPONIBILIDADE).get();
		Elements listaHistoricosHtml = nfePage.getElementById(ID_TABLE_STATUS).getElementsByTag("tbody").get(0)
				.getElementsByTag("tr");
		listaHistoricosHtml.remove(INDEX_CABECALHO); // REMOVE CABEÇALHO DA TABELA POIS NÃO NOS INTERRESSA

		for (Element linhaStatus : listaHistoricosHtml) {
			HistoricoStatus historico = new HistoricoStatus();
			historico.setAutorizador(linhaStatus.getElementsByIndexEquals(0).text().trim());
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
			return BOM;
		case "imagens/bola_amarela_P.png":
			return MEDIO;
		default:
			return RUIM;
		}
	}

	public List<HistoricoStatus> salvaHistoricosPeriodicamente() throws Exception {
		List<HistoricoStatus> historicos = capturaHistoricosFromPage();
		repository.saveAll(historicos);
		return historicos;
	}

	public List<HistoricoStatus> statusAtualServicoAllEstados() throws Exception {
		return repository.statusAtualServicoAllEstados();
	}

	public HistoricoStatus findByEstado(String uf) throws Exception {
		return repository.findByEstado(uf).get(0);
	}

	public List<HistoricoStatus> findByDateBetween(String dataInicio, String dataFim) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Long tsInicio = (dateFormat.parse(dataInicio)).getTime();
		Long tsFim = (dateFormat.parse(dataFim)).getTime();
		return repository.findByDateBetween(tsInicio, tsFim);
	}

	public String findWorst() throws Exception {
		String resultado = repository.findWorst().get(0);
		return resultado.substring(0, resultado.indexOf(","));

	}
}
