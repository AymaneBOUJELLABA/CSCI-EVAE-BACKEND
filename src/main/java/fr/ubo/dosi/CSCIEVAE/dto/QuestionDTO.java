package fr.ubo.dosi.CSCIEVAE.dto;

import fr.ubo.dosi.CSCIEVAE.entity.Qualificatif;
import lombok.Data;

@Data
public class QuestionDTO {

    private Long idQuestion;
    private String type;
    private Long noEnseignant;
    private String intitule;
    private Long order;
    private Qualificatif qualificatif;

}