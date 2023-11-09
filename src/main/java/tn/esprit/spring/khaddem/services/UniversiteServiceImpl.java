package tn.esprit.spring.khaddem.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.spring.khaddem.entities.Departement;
import tn.esprit.spring.khaddem.entities.Universite;
import tn.esprit.spring.khaddem.repositories.DepartementRepository;
import tn.esprit.spring.khaddem.repositories.UniversiteRepository;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UniversiteServiceImpl implements  IUniversiteService{

   private final  UniversiteRepository universiteRepository;

    private final DepartementRepository departementRepository;
    public UniversiteServiceImpl (UniversiteRepository universiteRepository,DepartementRepository departementRepository) {
        this.universiteRepository = universiteRepository;
        this.departementRepository = departementRepository;
    }
    @Override
    public List<Universite> retrieveAllUniversites() {
        return universiteRepository.findAll();
    }

    @Override
    public Universite addUniversite(Universite u) {
        log.debug("u :"+u.getNomUniv());
        universiteRepository.save(u);
        return u;
    }

    @Override
    public Universite updateUniversite(Universite u) {
        universiteRepository.save(u);
        return u;
    }

    @Override
    public Universite retrieveUniversite(Integer idUniversite) {
        Optional<Universite> universiteOptional = universiteRepository.findById(idUniversite);
        if (universiteOptional.isPresent()) {
            return universiteOptional.get();
        }
        // Handle the case when the value is not present
        return null; // Or throw an exception or handle the error accordingly
    }


    @Transactional
    public void assignUniversiteToDepartement(Integer universiteId, Integer departementId) {
        Universite universite =universiteRepository.findById(universiteId).get();
        Departement departement=departementRepository.findById(departementId).get();
        universite.getDepartements().add(departement);
        log.info("departements number "+universite.getDepartements().size());
    }
}
