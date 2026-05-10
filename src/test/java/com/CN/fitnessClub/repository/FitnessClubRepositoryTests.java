package com.CN.fitnessClub.repository;

import com.CN.fitnessClub.model.FitnessClub;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Testing FitnessClubRepository")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FitnessClubRepositoryTests {
    @Autowired
    FitnessClubRepository fitnessClubRepository;
    FitnessClub savedClub = new FitnessClub();

    @BeforeEach
    void setup(){
        FitnessClub fitnessClub =  new FitnessClub();
        fitnessClub.setName("club 999");
        fitnessClub.setContactNo(99999L);
        fitnessClub.setAddress("NA");
        savedClub = fitnessClubRepository.save(fitnessClub);
    }

    @Test
    @Order(1)
    @DisplayName("Testing method getAllFitnessClub()")
    public void shouldTestGetAllFitnessClub() {
        List<FitnessClub> resultList = fitnessClubRepository.findAll();
        Assertions.assertThat(resultList)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedClub));
    }

    @Test
    @Order(2)
    @DisplayName("Testing method getFitnessClubId()")
    public void shouldTestGetFitnessClubId() {
        Optional<FitnessClub> fitnessList = fitnessClubRepository.findById(savedClub.getId());
        Assertions.assertThat(fitnessList)
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(savedClub));
    }

    @Test
    @Order(3)
    @DisplayName("Testing method updateFitnessClub()")
    public void shouldTestUpdateFitnessClub() {
        savedClub.setName("Updated Club");
        savedClub.setAddress("Updated Address");
        FitnessClub updatedClub = fitnessClubRepository.save(savedClub);
        Assertions.assertThat(updatedClub)
                .usingRecursiveComparison()
                .isEqualTo(savedClub);
    }

    @Test
    @Order(4)
    @DisplayName("Testing method deleteFitnessClubById()")
    public void shouldTestDeleteFitnessClubById() {
        fitnessClubRepository.deleteById(savedClub.getId());
        Assertions.assertThat(fitnessClubRepository.findById(savedClub.getId())).isEmpty();
    }

    @Test
    @Order(5)
    @DisplayName("Testing method createFitnessClub()")
    public void shouldTestCreateFitnessClub() {
        FitnessClub newFitnessClub = new FitnessClub();
        newFitnessClub.setName("New Fit Club");
        newFitnessClub.setContactNo(898989L);
        newFitnessClub.setAddress("NA");
        newFitnessClub.setMembershipPlans("Extra");
        FitnessClub outResult = fitnessClubRepository.save(newFitnessClub);
        Assertions.assertThat(outResult)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newFitnessClub);

    }
    @AfterEach
    void dsetup(){
        fitnessClubRepository.deleteAll();
    }

}
