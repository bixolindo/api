package com.nimitz.api.schedule;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.nimitz.api.controllers.StatusNfePageController;

@Configuration
@EnableScheduling
public class ScheduleStatus {

	@Autowired
	StatusNfePageController statusNfePageController;

	@Scheduled(fixedDelay = 300000)
	public void baixarStatusNfe() {
		try {
			statusNfePageController.salvaHistoricosPeriodicamente();
		} catch (Exception e) {
			System.out.println("Erro ao coletar dados as " + new Date());
			e.printStackTrace();
		}
	}
}
