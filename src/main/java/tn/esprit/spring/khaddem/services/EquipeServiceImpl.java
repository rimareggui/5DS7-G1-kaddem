package tn.esprit.spring.khaddem.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.khaddem.entities.Contrat;
import tn.esprit.spring.khaddem.entities.Equipe;
import tn.esprit.spring.khaddem.entities.Etudiant;
import tn.esprit.spring.khaddem.entities.Niveau;
import tn.esprit.spring.khaddem.repositories.ContratRepository;
import tn.esprit.spring.khaddem.repositories.EquipeRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class EquipeServiceImpl implements IEquipeService{

    EquipeRepository equipeRepository;

    ContratRepository contratRepository;

    @Override
    public List<Equipe> retrieveAllEquipes() {
        return equipeRepository.findAll();
    }

    @Transactional
    public Equipe addEquipe(Equipe e) {
        equipeRepository.save(e);
        return e;
    }

    @Transactional
    public Equipe updateEquipe(Equipe updatedEquipe) {
        Optional<Equipe> existingEquipe = equipeRepository.findById(updatedEquipe.getIdEquipe());

        if (existingEquipe.isPresent()) {
            Equipe equipeToUpdate = existingEquipe.get();
            // Perform necessary updates on the existing entity
            equipeToUpdate.setNomEquipe(updatedEquipe.getNomEquipe());
            // Update other fields as needed

            equipeRepository.save(equipeToUpdate);
            return equipeToUpdate;
        } else {
            // Handle the case when the entity is not found
            // For instance, you can throw an exception or return null
            return null;
        }
    }


    @Override
    public Equipe retrieveEquipe(Integer idEquipe) {
        return   equipeRepository.findById(idEquipe).orElse(null);
    }

    public void evoluerEquipes() {
        log.info("debut methode evoluerEquipes");
        List<Equipe> equipes = equipeRepository.findAll();
        log.debug("nombre equipes : " + equipes.size());
        for (Equipe equipe : equipes) {
            if (equipe.getEtudiants() != null && !equipe.getEtudiants().isEmpty()) {
                log.debug("vérification niveau équipe");
                if (shouldUpdateEquipeNiveau(equipe)) {
                    updateEquipeNiveau(equipe);
                    equipeRepository.save(equipe);
                    break;
                }
            }
        }
        log.info("fin methode evoluerEquipes");
    }

    private boolean shouldUpdateEquipeNiveau(Equipe equipe) {
        if (equipe.getNiveau().equals(Niveau.JUNIOR) || equipe.getNiveau().equals(Niveau.SENIOR)) {
            return countActiveContrats(equipe) >= 3;
        }
        return false;
    }

    private int countActiveContrats(Equipe equipe) {
        int nbEtudiantsAvecContratsActifs = 0;
        for (Etudiant etudiant : equipe.getEtudiants()) {
            nbEtudiantsAvecContratsActifs += countActiveContratsForEtudiant(etudiant);
            if (nbEtudiantsAvecContratsActifs >= 3) {
                break;
            }
        }
        return nbEtudiantsAvecContratsActifs;
    }

    private int countActiveContratsForEtudiant(Etudiant etudiant) {
        int nbEtudiantsAvecContratsActifs = 0;
        List<Contrat> contrats = contratRepository.findByEtudiantIdEtudiant(etudiant.getIdEtudiant());
        for (Contrat contrat : contrats) {
            if (isContratActive(contrat)) {
                nbEtudiantsAvecContratsActifs++;
            }
        }
        return nbEtudiantsAvecContratsActifs;
    }

    private boolean isContratActive(Contrat contrat) {
        long differenceTime = contrat.getDateFinContrat().getTime() - contrat.getDateDebutContrat().getTime();
        long differenceYears = (differenceTime / (1000L * 60 * 60 * 24 * 365));
        log.debug("difference_In_Years: " + differenceYears);
        return (contrat.getArchived() == null || !contrat.getArchived()) && (differenceYears > 1);
    }

    private void updateEquipeNiveau(Equipe equipe) {
        if (equipe.getNiveau().equals(Niveau.JUNIOR)) {
            log.info("mise a jour du niveau de l equipe " + equipe.getNomEquipe() +
                    " du niveau " + equipe.getNiveau() + " vers le niveau supérieur SENIOR");
            equipe.setNiveau(Niveau.SENIOR);
        } else if (equipe.getNiveau().equals(Niveau.SENIOR)) {
            log.info("mise a jour du niveau de l equipe " + equipe.getNomEquipe() +
                    " du niveau " + equipe.getNiveau() + " vers le niveau supérieur EXPERT");
            equipe.setNiveau(Niveau.EXPERT);
        }
    }

}
