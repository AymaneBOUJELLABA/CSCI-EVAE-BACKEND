package fr.ubo.dosi.CSCIEVAE.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.ubo.dosi.CSCIEVAE.dto.EtudiantDTO;
import fr.ubo.dosi.CSCIEVAE.dto.EvaluationDTO;
import fr.ubo.dosi.CSCIEVAE.dto.PromotionsStatsDTO;
import fr.ubo.dosi.CSCIEVAE.dto.QuestionDTO;
import fr.ubo.dosi.CSCIEVAE.dto.QuestionReponseInfoDTO;
import fr.ubo.dosi.CSCIEVAE.dto.ReponseEvaluationDTO;
import fr.ubo.dosi.CSCIEVAE.dto.ReponseEvaluationGraphesDTO;
import fr.ubo.dosi.CSCIEVAE.dto.ReponseQuestionDTO;
import fr.ubo.dosi.CSCIEVAE.dto.ReponseRubriqueDTO;
import fr.ubo.dosi.CSCIEVAE.dto.RubriqueGraphesDTO;
import fr.ubo.dosi.CSCIEVAE.entity.Etudiant;
import fr.ubo.dosi.CSCIEVAE.entity.Evaluation;
import fr.ubo.dosi.CSCIEVAE.entity.Qualificatif;
import fr.ubo.dosi.CSCIEVAE.entity.ReponseEvaluation;
import fr.ubo.dosi.CSCIEVAE.entity.ReponseQuestion;
import fr.ubo.dosi.CSCIEVAE.entity.Rubrique;
import fr.ubo.dosi.CSCIEVAE.repository.EtudiantRepository;
import fr.ubo.dosi.CSCIEVAE.repository.PromotionRepository;
import fr.ubo.dosi.CSCIEVAE.repository.QualificatifRepository;
import fr.ubo.dosi.CSCIEVAE.repository.ReponseEvaluationRepository;
import fr.ubo.dosi.CSCIEVAE.repository.ReponseQuestionRepository;
import fr.ubo.dosi.CSCIEVAE.utils.DataMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ReponseEvaluationServiceImpl implements ReponseEvaluationService
{
	
	private static DataMapper mapper = new DataMapper();
	
	@Autowired
	ReponseEvaluationRepository reponseEvalRepo;
	@Autowired
	EvaluationService evaluationService;
	@Autowired
	EtudiantRepository etudiantRepo;
	@Autowired
	ReponseQuestionRepository reponseQuestionRepository;
	@Autowired
	RubriqueService rubriqueService;
	@Autowired
	QualificatifRepository qualifRepo;
	
	@Override
	public List<ReponseEvaluationDTO> getAllReponseEvaluations()
	{
		try
		{
			List<ReponseEvaluation> rDB = reponseEvalRepo.findAll();
			log.info("____Number of ReponseEvals____"+rDB.size());
			List<ReponseEvaluationDTO> r = new ArrayList<ReponseEvaluationDTO>();
			
			for(ReponseEvaluation i : rDB)
			{				
				ReponseEvaluationDTO n = this.populateDTOfromReponseEvaluation(i);
				r.add(n);
			}	
			return r;
			
		}catch(Exception e)
		{
			log.error("___ERROR get all reponse evaluations",e);
			return null;
		}
	}

	@Override
	public List<ReponseEvaluationDTO> getAllReponseEvaluationsByUeAndAnneUniv(String codeUe,String anneeUniv)
	{
		try
		{
			List<ReponseEvaluation> repsEval = reponseEvalRepo.findAllByCodeUeAndAnneUniv(codeUe, anneeUniv);
			List<ReponseEvaluationDTO> output = repsEval.stream().map(rep -> 
				this.populateDTOfromReponseEvaluation(rep)
			).collect(Collectors.toList());
			
			return output;
		}catch(Exception e)
		{
			log.error("___Erreur get All reponse evaluation By UE __ =>" + e);
		}
		return null;
	}

	@Override
	public ReponseEvaluationDTO addReponseEvaluation(ReponseEvaluation entity)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Etudiant getEtudiantFromResponseEvaluation(Long idReponseEvaluation)
	{
		try {
			log.info("__Chercher l'etudiant liée à l'id réponse evaluation  :"+idReponseEvaluation+" ___");
			Etudiant etd = etudiantRepo.findByReponseEvaluation(idReponseEvaluation);
			log.info("__Etudiant trouvé : " + etd);
			return etd;
		}catch(Exception e)
		{
			log.error("__Impossible de trouver l'etudiant =>" + e.getMessage());
			return null;
		}
	}
	
	@Override
	public List<QuestionReponseInfoDTO> getQuestionReponseAllInfo(Long idReponseEvaluation)
	{
		try
		{
			System.out.println("-------------- GETTING QUESTION RESPONSES OF EVALUATION");
			List<Object[]> r = reponseQuestionRepository.findAllReponseQuestionInfo(idReponseEvaluation);
			
			List<QuestionReponseInfoDTO> result = new ArrayList<QuestionReponseInfoDTO>();
			r.forEach(item->
			{
				QuestionReponseInfoDTO i = QuestionReponseInfoDTO.builder()
							.idQuestion(((BigDecimal) item[0]).longValue())
							.type((String) item[1])
							.noEnseignant((String) item[2])
							.idQualificatif(((BigDecimal) item[3]).longValue())
							.intitule((String) item[4])
							.positionnement(((BigDecimal) item[5]).longValue())
							.noEtudiant(((String) item[6]))
							.idReponseEvaluation(((BigDecimal) item[7]).longValue())
							.idRubriqueEvaluation(((BigDecimal) item[8]).longValue())
							.idQuestionEvaluation(((BigDecimal) item[9]).longValue())
							.ordre(((BigDecimal) item[10]).longValue())
							.build();
				result.add(i);
			});
			
			return result;
			
		}catch(Exception e)
		{
			log.error("____ERREUR!! getQuestionAllInfo=>"+e);
			return null;
		}
		
	}

	@Override
	public ReponseEvaluationDTO populateDTOfromReponseEvaluation(ReponseEvaluation repEval)
	{
		try
		{
			ReponseEvaluationDTO item = new ReponseEvaluationDTO();
			log.info("___searching for Etudiant of this ReponseEvaluation "+repEval.getIdReponseEvaluation()+"......");
			Etudiant etd = this.getEtudiantFromResponseEvaluation(repEval.getIdReponseEvaluation());
			log.info("__searching for Eval associer à cette reponse evaluation....");
			Evaluation eval = evaluationService.getEvalutionParId(repEval.getIdEvaluation());
			
			log.info("___searching for les questions de cette réponse....");
			List<QuestionReponseInfoDTO> rQuestions = this.getQuestionReponseAllInfo(repEval.getIdReponseEvaluation());
			List<ReponseRubriqueDTO> rubriques = new ArrayList<ReponseRubriqueDTO>();
			
			log.info("___Associations des rubriques avec les réponses Questions.....");
			rQuestions.forEach(question->
			{
				boolean rubExists = false;
				
				for(ReponseRubriqueDTO j : rubriques)
				{
					if(j.getIdRubriqueEvaluation().equals(question.getIdRubriqueEvaluation()))
						rubExists = true;
				}
				
				ReponseRubriqueDTO rub = rubriques.stream()
											.filter(el -> el.getIdRubriqueEvaluation().equals(question.getIdRubriqueEvaluation()))
											.findFirst()
											.orElse(new ReponseRubriqueDTO());
				//if rub exists
				List<ReponseQuestionDTO> rubQsts = rubExists ? rub.getQuestions() : new ArrayList<ReponseQuestionDTO>();
				
				//add question to rub
				QuestionDTO qDTO = new QuestionDTO();
				qDTO.setIdQuestion(question.getIdQuestion());
				qDTO.setIntitule(question.getIntitule());
				qDTO.setNoEnseignant(
						question.getNoEnseignant() == null || question.getNoEnseignant().isEmpty() ? null:
						Long.parseLong(question.getNoEnseignant()));
				qDTO.setOrder(question.getOrdre());
				if(qualifRepo.findById(question.getIdQualificatif()).isPresent())
					qDTO.setQualificatif(qualifRepo.findById(question.getIdQualificatif()).get());
				
				rubQsts.add(
						new ReponseQuestionDTO(
								question.getIdQuestionEvaluation(),
								question.getIdReponseEvaluation(),
								question.getPositionnement(),
								qDTO)
						);
				
				rub.setQuestions(rubQsts);
				
				Rubrique rubInfo = rubriqueService.getRubriqueByIdRubriqueEvaluation(question.getIdReponseEvaluation()); //RubriqueInfo
				rub.setRubriqueinfo(rubInfo);
				rub.setIdRubriqueEvaluation(question.getIdRubriqueEvaluation());
				
				rubriques.add(rub);
			});
			
			item.setEtudiant(etd);
			item.setEvaluation(mapper.evaluationMapperToDTO(eval));
			item.setIdReponseEvaluation(repEval.getIdReponseEvaluation());
			item.setCommentaire(repEval.getCommentaire());
			item.setRubriques(rubriques);
			log.info("____Opération términé avec succes!! resultat : \n" + item);
			return item;
		}catch(Exception e)
		{
			log.error("__ERROR while populating for Reponse Evaluation DTO____ => "+e);
			return null;
		}
		
	}

	@Override
	public EvaluationDTO getEvaluationFromResponseEvaluation(Long idReponseEvaluation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReponseQuestion> getAllQuestionReponseByIdReponseEvaluation(Long idReponseEvaluation) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Autowired
	PromotionRepository promoRepo;
	@Override
	public List<PromotionsStatsDTO> getAllPromotionsGraphesData()
	{
		try
		{
			List<ReponseEvaluationDTO> allData = this.getAllReponseEvaluations();
			
			
			List<PromotionsStatsDTO> result = allData.stream().map(data ->
			{
				PromotionsStatsDTO promo = new PromotionsStatsDTO();
				List<ReponseEvaluationGraphesDTO> reponsesEvaluations = new ArrayList<ReponseEvaluationGraphesDTO>();
				
				
				
				
				promo.setAnneUniv(null);
				promo.setNomFormation(null);
				promo.setReponseEvaluations(null);
				
				return promo;
			}).collect(Collectors.toList());
			
			
			
			return result;
			
		}catch(Exception e)
		{
			log.error("___ ERROR GETTING ALL RESPONSES EVALUATION FOR GRAPHES____ => "+ e);
			return null;
		}
	}
	
	public List<RubriqueGraphesDTO> calculerMoyenneRub(List<ReponseRubriqueDTO> rubriques)
	{
		try
		{
			List<RubriqueGraphesDTO> rubG = rubriques.stream().map(rub ->
			{
				Double sum = 0d;
				for(ReponseQuestionDTO q : rub.getQuestions())
				{
					sum += q.getPositionnement();
				}
				
				return new RubriqueGraphesDTO(rub.getRubriqueinfo().getDesignation(),sum/rub.getQuestions().size());				
			}).collect(Collectors.toList());
			
			return rubG;
			
		}catch(Exception e)
		{
			log.error("___ERROR calculer moyenne Rubriques ... => " + e);
			return null;
		}
		
	}
}
