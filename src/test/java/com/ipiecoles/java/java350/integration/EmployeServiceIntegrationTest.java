package com.ipiecoles.java.java350.integration;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class EmployeServiceIntegrationTest {

    @Autowired
    EmployeService employeService;

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void clearDB(){
        employeRepository.deleteAll();
    }

    @Test
    void testEmbaucheEmployeWith1Employe() throws EmployeException {
        //given
        Employe manager = new Employe("BESIAT","Ivan","M00001", LocalDate.now(),2500d,2,1.0);
        employeRepository.save(manager);
        //when
        employeService.embaucheEmploye("Besiat", "Ivan", Poste.COMMERCIAL, NiveauEtude.DOCTORAT, 0.5);
        //then
        Employe employeTest = employeRepository.findByMatricule("C00002");
        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00002");
        Assertions.assertThat(employeTest.getMatricule()).isEqualTo("C00002");
        Assertions.assertThat(employeTest.getNom()).isEqualTo("Besiat");
        Assertions.assertThat(employeTest.getPrenom()).isEqualTo("Ivan");
        Assertions.assertThat(employeTest.getSalaire()).isEqualTo(1293.04);
        Assertions.assertThat(employeTest.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employeTest.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employeTest.getTempsPartiel()).isEqualTo((0.5));
    }

    @Test
    void testCalculPerformanceCommercial() throws EmployeException {
        // given
        Employe commercial = new Employe("Doe", "John", "C00001", LocalDate.now(), 2500d, 2, 1.0);
        employeRepository.save(commercial);
        Long caTraite = 987654321L;
        Long objectifCa = 987132456L;

        // when
        employeService.calculPerformanceCommercial(commercial.getMatricule(), caTraite, objectifCa);

        // then
        Employe employeTest = employeRepository.findByMatricule("C00001");
        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00001");
        Assertions.assertThat(caTraite).isPositive().isNotNull();
        Assertions.assertThat(objectifCa).isPositive().isNotNull();
        Assertions.assertThat(employeTest.getMatricule()).isNotNull();
        Assertions.assertThat(employeTest.getMatricule()).startsWith("C");

        //Assertions.assertThat(caTraite).isGreaterThanOrEqualTo((long) (objectifCa*0.8)).isLessThan((long) (objectifCa*0.95)).isGreaterThanOrEqualTo((long) (objectifCa*0.95)).isLessThanOrEqualTo((long) (objectifCa*1.05)).isLessThanOrEqualTo((long) (objectifCa*1.2)).isGreaterThan((long) (objectifCa*1.05)).isGreaterThanOrEqualTo((long) (objectifCa*1.2));
    }

    @Test
    void testAvgPerformanceWhereMatriculeStartsWith() throws EmployeException {
        // given
        Employe commercial = new Employe("Doe", "John", "C00001", LocalDate.now(), 2500d, 2, 1.0);
        employeRepository.save(commercial);
        int perfMoyenne = 1;

        //when
        employeRepository.avgPerformanceWhereMatriculeStartsWith("c");

        // then
        Employe employeTest = employeRepository.findByMatricule("C00001");
        Assertions.assertThat(employeRepository.findLastMatricule()).isEqualTo("00001");
        Assertions.assertThat(perfMoyenne).isLessThan(employeTest.getPerformance());
    }
}
