package tn.esprit.spring.khaddem.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.khaddem.entities.*;
import tn.esprit.spring.khaddem.repositories.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j

public class EtudiantServiceImpl implements IEtudiantService{

    private final EtudiantRepository etudiantRepository;

    private final DepartementRepository departementRepository;

    private final ContratRepository contratRepository;

    private final EquipeRepository equipeRepository;
    public EtudiantServiceImpl (EtudiantRepository etudiantRepository,ContratRepository contratRepository,EquipeRepository equipeRepository, DepartementRepository departementRepository) {
        this.etudiantRepository = etudiantRepository;
        this.departementRepository = departementRepository;
        this.contratRepository = contratRepository;
        this.equipeRepository = equipeRepository;

    }
    @Override
    public List<Etudiant> retrieveAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant addEtudiant(Etudiant e) {
        etudiantRepository.save(e);
        return e;
    }

    @Override
    public Etudiant updateEtudiant(Etudiant updatedEtudiant) {
        Optional<Etudiant> existingEtudiant = etudiantRepository.findById(updatedEtudiant.getIdEtudiant());

        if (existingEtudiant.isPresent()) {
            Etudiant etudiantToUpdate = existingEtudiant.get();
            // Perform necessary updates on the existing entity
            etudiantToUpdate.setNomE(updatedEtudiant.getNomE());
            // Update other fields as needed

            etudiantRepository.save(etudiantToUpdate);
            return etudiantToUpdate;
        } else {
            // Handle the case when the entity is not found
            // For instance, you can throw an exception or return null
            return null;
        }
    }


    @Override
    public Etudiant retrieveEtudiant(Integer idEtudiant) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findById(idEtudiant);
        if (etudiantOptional.isPresent()) {
            return etudiantOptional.orElse(null);
        }
        // Handle the case when the value is not present
        return null; // Or throw an exception or handle the error accordingly
    }


    @Override
    public void removeEtudiant(Integer idEtudiant) {
     etudiantRepository.deleteById(idEtudiant);
    }

    @Override
    public void assignEtudiantToDepartement(Integer etudiantId, Integer departementId) {
        Optional<Etudiant> etudiantOptional = etudiantRepository.findById(etudiantId);
        Optional<Departement> departementOptional = departementRepository.findById(departementId);

        if (etudiantOptional.isPresent() && departementOptional.isPresent()) {
            Etudiant e = etudiantOptional.get();
            Departement d = departementOptional.get();
            e.setDepartement(d);
            etudiantRepository.save(e);
        } else {
            // Handle the case when the value is not present
            // For instance, you can throw an exception or handle the error accordingly
        }
    }


    @Override
    public List<Etudiant> findByDepartementIdDepartement(Integer idDepartement) {
        return etudiantRepository.findByDepartementIdDepartement(idDepartement);
    }

    @Override
    public List<Etudiant> findByEquipesNiveau(Niveau niveau) {
        return etudiantRepository.findByEquipesNiveau(niveau);
    }

    @Override
    public List<Etudiant> retrieveEtudiantsByContratSpecialite(Specialite specialite) {
        return etudiantRepository.retrieveEtudiantsByContratSpecialite(specialite);
    }

    @Override
    public List<Etudiant> retrieveEtudiantsByContratSpecialiteSQL(String specialite) {
        return etudiantRepository.retrieveEtudiantsByContratSpecialiteSQL(specialite);
    }

    @Transactional
    public Etudiant addAndAssignEtudiantToEquipeAndContract(Etudiant e, Integer idContrat, Integer idEquipe) {
        Contrat contrat = contratRepository.findById(idContrat).orElse(null);
        Equipe equipe=equipeRepository.findById(idEquipe).orElse(null);
        Etudiant etudiant= etudiantRepository.save(e);
        log.info("contrat: "+contrat.getSpecialite());
        log.info("equipe: "+equipe.getNomEquipe());
        log.info("etudiant: "+etudiant.getNomE()+" "+etudiant.getPrenomE()+" "+etudiant.getOp());
        List<Equipe> equipesMisesAjour = new ArrayList<>();
        contrat.setEtudiant(etudiant);
        if(etudiant.getEquipes()!=null) {
            equipesMisesAjour=etudiant.getEquipes();
        }
        equipesMisesAjour.add(equipe);
        log.info("taille apres ajout : "+equipesMisesAjour.size());
        etudiant.setEquipes(equipesMisesAjour);


        return e;
    }

    @Override
    public List<Etudiant> getEtudiantsByDepartement(Integer idDepartement) {
        Optional<Departement> departementOptional = departementRepository.findById(idDepartement);
        if (departementOptional.isPresent()) {
            Departement departement = departementOptional.get();
            return departement.getEtudiants();
        }
        // Handle the case when the value is not present
        return Collections.emptyList(); // Or throw an exception or handle the error accordingly
    }



}
