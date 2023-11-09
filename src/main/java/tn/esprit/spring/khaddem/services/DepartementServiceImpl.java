package tn.esprit.spring.khaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.khaddem.entities.Departement;
import tn.esprit.spring.khaddem.entities.Universite;
import tn.esprit.spring.khaddem.repositories.DepartementRepository;
import tn.esprit.spring.khaddem.repositories.UniversiteRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class DepartementServiceImpl implements IDepartementService{
    @Autowired
    DepartementRepository departementRepository;
    @Autowired
    UniversiteRepository universiteRepository;
    @Override
    public List<Departement> retrieveAllDepartements() {
        return departementRepository.findAll();
    }

    @Override
    public Departement addDepartement(Departement d) {
        departementRepository.save(d);
        return d;
    }

    @Override
    public Departement updateDepartement(Departement d) {
        departementRepository.save(d);
        return d;
    }


    @Override
    public Departement retrieveDepartement(Integer idDepart) {
        Optional<Departement> departementOptional = departementRepository.findById(idDepart);
        if (departementOptional.isPresent()) {
            return departementOptional.get();
        }
        // Handle the case when the value is not present
        return null; // Or throw an exception or handle the error accordingly
    }


    @Override
    public List<Departement> retrieveDepartementsByUniversite(Integer idUniversite) {
        Optional<Universite> universiteOptional = universiteRepository.findById(idUniversite);
        if (universiteOptional.isPresent()) {
            Universite universite = universiteOptional.get();
            return universite.getDepartements();
        }
        // Handle the case when the value is not present
        return Collections.emptyList(); // Or throw an exception or handle the error accordingly
    }

}
