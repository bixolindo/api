package com.nimitz.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimitz.api.controllers.StatusNfePageController;
import com.nimitz.api.models.HistoricoStatus;
import com.nimitz.api.repository.HistoricoStatusRepository;

@RestController
@RequestMapping(value = "/api")
public class HistoricoStatusResource {

	@Autowired
	private HistoricoStatusRepository historicoStatusRepository;

	@Autowired
	private StatusNfePageController controller;

	@GetMapping("/historicos")
	public List<HistoricoStatus> listaHistoricos() {
		return historicoStatusRepository.findAll();
	}

	@GetMapping("/salva-historicos")
	public void visualizaDados() throws Exception {
		controller.salvaHistoricosPeriodicamente();
	}

	@PostMapping("/historico")
	public HistoricoStatus adicionaHistorico(@RequestBody HistoricoStatus historico) {
		return historicoStatusRepository.save(historico);
	}

}
