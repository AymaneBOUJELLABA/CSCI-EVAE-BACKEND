package fr.ubo.dosi.CSCIEVAE.services;

import fr.ubo.dosi.CSCIEVAE.dto.EvaluationDTO;
import fr.ubo.dosi.CSCIEVAE.dto.QuestionDTO;
import fr.ubo.dosi.CSCIEVAE.dto.StatReponseQuestionDTO;
import fr.ubo.dosi.CSCIEVAE.dto.StatRubriqueDTO;
import fr.ubo.dosi.CSCIEVAE.dto.RubriqueDTO;
import fr.ubo.dosi.CSCIEVAE.dto.StatEvaluationDTO;
import fr.ubo.dosi.CSCIEVAE.entity.Evaluation;
import fr.ubo.dosi.CSCIEVAE.entity.QuestionEvaluation;
import fr.ubo.dosi.CSCIEVAE.entity.ReponseQuestion;
import fr.ubo.dosi.CSCIEVAE.entity.Rubrique;
import fr.ubo.dosi.CSCIEVAE.entity.RubriqueEvaluation;

import java.util.List;

public interface EvaluationService {

    List<Evaluation> getAllEvalutions();

    Evaluation getEvalutionParCodeUeAndAnneeUniv(String codeUe, String anneeUniv);

    Evaluation getEvalutionParId(Long id);

    List<RubriqueDTO> getRubriqueEvaluation(Long idEvaluation);
    List<QuestionDTO> getQuestionRubriqueForEvaluation(Long idRubrique);
    List<Rubrique> getRubriquesForEvaluationCreation();
    List<StatRubriqueDTO> getStatRubriques(Long idEvaluation);
    StatEvaluationDTO getStatEvaluation(Long idEvaluation);


    EvaluationDTO createEvaluation(EvaluationDTO evaluationDTO);

    void setQuestionsEvaluationForRubsEval(List<RubriqueEvaluation> rubriqueEvaluations);

    void associerRubriquesToEvaluation(Evaluation finalEva, List<RubriqueDTO> rubriquesDto);

    EvaluationDTO updateRubriquesEvaluationOrder(EvaluationDTO evaluationDTO);

    List<QuestionDTO> getQuestionsEvalForRubriqueEval(Long idRubEval);

    Evaluation publierEvaluation(Evaluation evaluation);
}
