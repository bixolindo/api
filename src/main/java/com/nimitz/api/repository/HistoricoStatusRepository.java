package com.nimitz.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.nimitz.api.models.HistoricoStatus;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {

	@Query(value = "SELECT DISTINCT ON (autorizador) * FROM historico_status ORDER BY autorizador, data DESC", nativeQuery = true)
	List<HistoricoStatus> statusAtualServicoAllEstados();

	@Query(value = "SELECT h FROM HistoricoStatus h WHERE h.autorizador = ?1 ORDER BY h.data DESC", countQuery = "1")
	List<HistoricoStatus> findByEstado(String autorizador);

	@Query("SELECT h FROM HistoricoStatus h WHERE h.data BETWEEN ?1 AND ?2")
	List<HistoricoStatus> findByDateBetween(Long start, Long end);

	@Query(value = "SELECT autorizador, COUNT(*) FROM  historico_status WHERE status = 3 GROUP BY autorizador ORDER BY count desc", nativeQuery = true, countQuery = "1")
	List<String> findWorst();
}
