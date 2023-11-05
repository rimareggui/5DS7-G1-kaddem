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

    public void evoluerEquipes(){
        log.info("debut methode evoluerEquipes");
        // t1 : recuperer date a linstant t1
        List<Equipe> equipes = equipeRepository.findAll();
        log.debug("nombre equipes : "+equipes.size());
        for (Equipe equipe : equipes) {
            if (equipe.getEtudiants()!=null && !equipe.getEtudiants().isEmpty()) {
                log.debug("vérification niveau équipe");
            if ((equipe.getNiveau().equals(Niveau.JUNIOR)) || (equipe.getNiveau().equals(Niveau.SENIOR))) {
                List<Etudiant> etudiants = equipe.getEtudiants();

                    Integer nbEtudiantsAvecContratsActifs = 0;
                    for (Etudiant etudiant : etudiants) {
                     log.debug("in for etudiants");

                        List<Contrat> contrats = contratRepository.findByEtudiantIdEtudiant(etudiant.getIdEtudiant())  ;
                        for (Contrat contrat : contrats) {
                            log.debug("in contrat");

                            long differenceTime = contrat.getDateFinContrat().getTime() - contrat.getDateDebutContrat().getTime();
                            long differenceYears = (differenceTime / (1000l * 60 * 60 * 24 * 365));
                            log.debug("difference_In_Years: " + differenceYears);

                            if ((contrat.getArchived() == null || !contrat.getArchived() ) && (differenceYears > 1)) {
                                nbEtudiantsAvecContratsActifs++;
                                if (nbEtudiantsAvecContratsActifs >= 3) {
                                    break;
                                }
                            }
                        }


                    }
                    log.info("nbEtudiantsAvecContratsActifs: " + nbEtudiantsAvecContratsActifs);
                if (nbEtudiantsAvecContratsActifs >= 3) {
                    if (equipe.getNiveau().equals(Niveau.JUNIOR)) {
                        log.info("mise a jour du niveau de l equipe " + equipe.getNomEquipe() +
                                " du niveau " + equipe.getNiveau() + " vers le niveau supérieur SENIOR");
                        equipe.setNiveau(Niveau.SENIOR);
                    } else if (equipe.getNiveau().equals(Niveau.SENIOR)) {
                        log.info("mise a jour du niveau de l equipe " + equipe.getNomEquipe() +
                                " du niveau " + equipe.getNiveau() + " vers le niveau supérieur EXPERT");
                        equipe.setNiveau(Niveau.EXPERT);
                    }
                    equipeRepository.save(equipe);
                    // t2 : recuperer date a l'instant t2 : te=t2-t1
                    break;
                }

            }

            }
        }
        log.info("fin methode evoluerEquipes");
        // t2 : recuperer date a linstant t2 : te=t2-t1
    }

}
