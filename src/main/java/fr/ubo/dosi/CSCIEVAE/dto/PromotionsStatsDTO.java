package fr.ubo.dosi.CSCIEVAE.dto;

import java.util.List;

import lombok.Data;

@Data
public class PromotionsStatsDTO
{
	private String AnneUniv;
	private String nomFormation;
	private List<ReponseEvaluationGraphesDTO> reponseEvaluations;

}
