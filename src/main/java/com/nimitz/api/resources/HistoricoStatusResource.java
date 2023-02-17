package com.nimitz.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/status-atual")
	public List<HistoricoStatus> statusAtualServicoAllEstados() throws Exception {
		return controller.statusAtualServicoAllEstados();
	}

	@GetMapping("/status-atual/{uf}")
	public HistoricoStatus findByEstado(@PathVariable(value = "uf") String uf) throws Exception {
		return controller.findByEstado(uf);
	}

	@GetMapping("/status-atual/{dtInicio}/{dtFim}")
	public List<HistoricoStatus> findByDateBetween(@PathVariable(value = "dtInicio") String dtInicio,
			@PathVariable(value = "dtFim") String dtFim) throws Exception {
		return controller.findByDateBetween(dtInicio, dtFim);
	}

	@GetMapping("/pior-estado")
	public String findWorst() throws Exception {
		return controller.findWorst();
	}

	@PostMapping("/historico")
	public HistoricoStatus adicionaHistorico(@RequestBody HistoricoStatus historico) {
		return historicoStatusRepository.save(historico);
	}

}
