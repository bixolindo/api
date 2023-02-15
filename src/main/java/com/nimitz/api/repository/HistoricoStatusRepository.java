package com.nimitz.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nimitz.api.models.HistoricoStatus;

@Repository
public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long>{

}
