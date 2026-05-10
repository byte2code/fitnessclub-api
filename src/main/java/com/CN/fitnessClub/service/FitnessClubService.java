package com.CN.fitnessClub.service;

import com.CN.fitnessClub.dto.FitnessClubDto;
import com.CN.fitnessClub.exception.FitnessClubNotFoundException;
import com.CN.fitnessClub.exception.UserNotFoundException;
import com.CN.fitnessClub.model.FitnessClub;
import com.CN.fitnessClub.model.User;
import com.CN.fitnessClub.repository.FitnessClubRepository;
import com.CN.fitnessClub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FitnessClubService {

    @Autowired
    private FitnessClubRepository fitnessClubRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public List<FitnessClub> getAllFitnessClub() {
        return fitnessClubRepository.findAll();
    }

    public FitnessClub getFitnessClubId(Long id) {
        return fitnessClubRepository.findById(id).orElseThrow(() -> new FitnessClubNotFoundException("Gym not found with id: " + id));
    }

    public boolean deleteFitnessClubById(Long id) {
        if(fitnessClubRepository.existsById(id)){
            fitnessClubRepository.deleteById(id);
            return true;
        }else
            return false;
    }

    public void updateFitnessClub(FitnessClubDto fitnessClubDto, Long id) {
                FitnessClub existingFitnessClub = getFitnessClubId(id);
                existingFitnessClub.setAddress(fitnessClubDto.getAddress());
                existingFitnessClub.setFacilities(fitnessClubDto.getFacilities());
                existingFitnessClub.setName(fitnessClubDto.getName());
                existingFitnessClub.setMembers(fitnessClubDto.getMembers());
                existingFitnessClub.setContactNo(fitnessClubDto.getContactNo());
                existingFitnessClub.setMembershipPlans(fitnessClubDto.getMembershipPlans());
                fitnessClubRepository.save(existingFitnessClub);
        }

    public void createFitnessClub(FitnessClubDto fitnessClubDto) {
        FitnessClub fitnessClub = FitnessClub.builder().name(fitnessClubDto.getName()).membershipPlans(fitnessClubDto.getMembershipPlans())
                .address(fitnessClubDto.getAddress()).facilities(fitnessClubDto.getFacilities())
                .contactNo(fitnessClubDto.getContactNo()).members(fitnessClubDto.getMembers()).build();
       fitnessClubRepository.save(fitnessClub);

    }

    public void addMember(Long userId, Long gymId) {
            User user = userService.getUserById(userId);
            FitnessClub fitnessClub = getFitnessClubId(gymId);
            List<User> members= fitnessClub.getMembers();
            members.add(user);
            fitnessClub.setMembers(members);
            user.setFitnessClub(fitnessClub);
            fitnessClubRepository.save(fitnessClub);
            userRepository.save(user);
    }

    public void deleteMember(Long userId, Long gymId) {
            User user = userService.getUserById(userId);
            FitnessClub fitnessClub = getFitnessClubId(gymId);
            if (fitnessClub.getMembers().contains(user)) {
                user.setFitnessClub(null);
                fitnessClub.getMembers().remove(user);
                fitnessClubRepository.save(fitnessClub);
                userRepository.save(user);
            }
    }
}
