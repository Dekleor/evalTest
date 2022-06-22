package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @AfterEach
    @BeforeEach
    public void clearDB(){
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatriculeWithoutEmploye(){
        //given

        //when
        String matricule = employeRepository.findLastMatricule();
        //then
        Assertions.assertThat(matricule).isNull();
    }

    @Test
    public void testFindLastMatriculeWith1Employe(){
        //given
        Employe manager = new Employe("Doe","John","M00007", LocalDate.now(),2500d,2,1.0);
        employeRepository.save(manager);

        //when
        String matricule = employeRepository.findLastMatricule();
        //then
        Assertions.assertThat(matricule).isEqualTo("00007");
    }

    public void testFindLastMatriculeWith3Employe(){
        //given
        Employe manager = new Employe("Doe","John","M00007", LocalDate.now(),2500d,2,1.0);
        Employe tech = new Employe("Doe","John","T01000", LocalDate.now(),2500d,2,1.0);
        Employe commercial = new Employe("Doe","John","C01001", LocalDate.now(),2500d,2,1.0);
        employeRepository.saveAll(Arrays.asList(manager,tech,commercial));

        //when
        String matricule = employeRepository.findLastMatricule();
        //then
        Assertions.assertThat(matricule).isEqualTo("01001");
    }
}
