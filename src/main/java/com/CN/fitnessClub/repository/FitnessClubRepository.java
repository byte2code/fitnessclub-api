package com.CN.fitnessClub.repository;

import com.CN.fitnessClub.model.FitnessClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FitnessClubRepository extends JpaRepository<FitnessClub, Long> {
}
