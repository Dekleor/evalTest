package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

class EmployeTest {

    @Test
    void testGetNombreAnneeEncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }

    @Test
    void testGetNbAnneesAncienneteDateEmbauchePassee(){
        //Given
        //Date d'embauche 10 ans dans le passé
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(10));
        //employe.setDateEmbauche(LocalDate.of(2012, 4, 26)); //Pas bon...
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 10
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(10);
    }

    @Test
    void testGetNbAnneesAncienneteDateEmbaucheFuture(){
        //Given
        //Date d'embauche 2 ans dans le futur
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }

    @Test
    void testGetNbAnneesAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);
        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }

    @ParameterizedTest
    @CsvSource({
            "'M12345',0,1,1.0,1700.0",
            "'M12345',2,1,1.0,1900.0",
            "'M12345',0,2,1.0,1700.0",
            "'M12345',0,1,0.5,850.0",
            "'C12345',0,1,1.0,1000",
            "'C12345',5,1,1.0,1500",
            "'C12345',2,1,1.0,1200",
            "'C12345',0,2,1.0,2300.0",
            "'C12345',3,2,1.0,2600.0",
            "'C12345',0,1,0.5,500",
            ",0,1,1.0,1000.",
            "'C12345',0,,1.0,1000"

    })
    void testGetPrimeAnnuelManagerPerformanceBasePleinTemps(
        String matricule,
        Integer nbAnneesAnciennete,
        Integer performance,
        Double tauxActivite,
        Double prime
    ){
        //Given
        Employe employe = new Employe("Manage","Manager",matricule,LocalDate.now().minusYears(nbAnneesAnciennete),2500d,performance,tauxActivite);

        //When
        Double primeObtenue = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeObtenue).isEqualTo(prime);
    }

    @ParameterizedTest
    @CsvSource( {
            "-10, 1800",
            "-60, 800"
    })
    void testAugmenterSalaireError(double pourcentage, double expectedSalary) throws EmployeException
    {
        //Given
        Employe employe = new Employe("Doe", "John", "C123456", LocalDate.now(), 2000d, 1, 1.0);

        //When
        Throwable thrown = Assertions.catchThrowable(()-> {
           employe.augmenterSalaire(pourcentage);
        });

        //Then
        Assertions.assertThat(thrown).isInstanceOf(EmployeException.class).hasMessageContaining("Il est illégal de faire baisser le salaire");
    }

    @ParameterizedTest
    @CsvSource( {
            "0, 2000",
            "10, 2200",
            "20, 2400",
            "30, 2600",
            "40, 2800",
            "50, 3000"
    })
    void testAugmenterSalaireWithoutError(double pourcentage, double expectedSalary) throws EmployeException {
        //Given
        Employe employe = new Employe("Doe", "John", "C123456", LocalDate.now(), 2000d, 1, 1.0);

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(expectedSalary);
    }

    @ParameterizedTest
    @CsvSource({
            "2019-01-01, 8, 1.0",
            "2021-01-01, 10, 1.0",
            "2022-01-01, 5, 0.5",
            "2032-01-01, 6, 0.5",
            "2044-01-01, 10, 1.0"
    })
    void testGetNbRtt(LocalDate date, int expectedRtt, double tempsPartiel){
        //Given
        Employe employe = new Employe("Doe", "John", "C123456", LocalDate.now(), 2000d, 1, tempsPartiel);

        //When
        employe.getNbRtt(date);

        //Then
        Assertions.assertThat(employe.getNbRtt(date)).isEqualTo(expectedRtt);
    }
}
