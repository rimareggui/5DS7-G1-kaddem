package tn.esprit.spring.khaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import tn.esprit.spring.khaddem.entities.Departement;
import tn.esprit.spring.khaddem.services.IDepartementService;

import java.util.List;

@RestController
@RequestMapping("/departement")
@AllArgsConstructor
public class DepartementRestController {

    private final  IDepartementService departementService;
    // http://localhost:8089/Kaddem/departement/retrieve-all-departements
    @GetMapping("/retrieve-all-departements")
    @ResponseBody
    public List<Departement> getDepartements() {
        return departementService.retrieveAllDepartements();
    }


    // http://localhost:8089/Kaddem/departement/retrieve-departement/8
    @GetMapping("/retrieve-departement/{departement-id}")
    @ResponseBody
    public Departement retrieveDepartement(@PathVariable("departement-id") Integer departementId) {
        return departementService.retrieveDepartement(departementId);
    }

    // http://localhost:8089/Kaddem/departement/add-departement
    @PostMapping("/add-departement")
    @ResponseBody
    public Departement addDepartement(@RequestBody Departement d) {
        departementService.addDepartement(d);
        return d;
    }

    // http://localhost:8089/Kaddem/departement/update-departement
    @PutMapping("/update-departement")
    @ResponseBody
    public Departement updateDepartement(@RequestBody Departement departement) {
        return departementService.updateDepartement(departement);
    }



    // http://localhost:8089/Kaddem/departement/retrieveDepartementsByUniversite/1
    @GetMapping("/retrieveDepartementsByUniversite/{idUniversite}")
    @ResponseBody
    public List<Departement> retrieveDepartementsByUniversite(@PathVariable("idUniversite") Integer idUniversite) {
        return departementService.retrieveDepartementsByUniversite(idUniversite);
    }



}
