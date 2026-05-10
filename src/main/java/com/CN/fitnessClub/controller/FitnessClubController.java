package com.CN.fitnessClub.controller;

import com.CN.fitnessClub.dto.FitnessClubDto;
import com.CN.fitnessClub.model.FitnessClub;
import com.CN.fitnessClub.service.FitnessClubService;
import com.CN.fitnessClub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/fitnessClub")
public class FitnessClubController {

    @Autowired
    private FitnessClubService fitnessClubService;

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public List<FitnessClub> getAllFitnessClub() {
        return fitnessClubService.getAllFitnessClub();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public FitnessClub getFitnessClubId(@PathVariable Long id){
        return fitnessClubService.getFitnessClubId(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void createFitnessClub(@RequestBody FitnessClubDto fitnessClubDto) {
         fitnessClubService.createFitnessClub(fitnessClubDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void updateFitnessClub(@RequestBody FitnessClubDto fitnessClubDto, @PathVariable Long id){
         fitnessClubService.updateFitnessClub(fitnessClubDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFitnessClub(@PathVariable Long id){
         fitnessClubService.deleteFitnessClubById(id);
    }

    @PostMapping("/addMember")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void addMember(@PathParam("userId") Long userId, @PathParam("fitnessClubId") Long fitnessClubId) {
         fitnessClubService.addMember(userId, fitnessClubId);
    }

    @DeleteMapping("/deleteMember")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMember(@PathParam("userId") Long userId, @PathParam("fitnessClubId") Long fitnessClubId) {
         fitnessClubService.deleteMember(userId, fitnessClubId);
    }

}
