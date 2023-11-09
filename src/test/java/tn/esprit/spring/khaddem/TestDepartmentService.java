package tn.esprit.spring.khaddem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.khaddem.entities.Departement;
import tn.esprit.spring.khaddem.repositories.DepartementRepository;
import tn.esprit.spring.khaddem.services.DepartementServiceImpl;


import java.util.Optional;
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Slf4j
@SpringBootTest
 class TestDepartmentService {
    @InjectMocks
    private DepartementServiceImpl departementService;
    @Mock
    private DepartementRepository departementRepository;
    @Test
    @Order(0)
    void testAddDepartement() {
        // Arrange
        Departement ajoutDepartement= new Departement();
        Mockito.when(departementRepository.save(ajoutDepartement)).thenReturn(ajoutDepartement);

        // Act
        Departement result = departementService.addDepartement(ajoutDepartement);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(ajoutDepartement, result);
    }
    @Test
    @Order(2)
    void testRetrieveDepartment() {
        // Arrange
        Integer idDepartement = 1;
        Departement departement = new Departement();
        Mockito.when(departementRepository.findById(idDepartement)).thenReturn(Optional.of(departement));

        // Act
        Departement result = departementService.retrieveDepartement(idDepartement);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(departement, result);
    }
    @Test
    @Order(1)
    void testUpdateDepartment() {
        // Arrange
        Departement updatedDepartment = new Departement();

        // Act
        Departement result = departementService.updateDepartement(updatedDepartment);

        // Assert
        Mockito.verify(departementRepository, Mockito.times(1)).save(updatedDepartment);
        Assertions.assertEquals(updatedDepartment, result);
    }






}
