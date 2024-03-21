package com.campusconnect.controller;


import com.campusconnect.dto.ClubDto;
import com.campusconnect.dto.EventCardDto;
import com.campusconnect.dto.StudentDto;
import com.campusconnect.entities.Club;
import com.campusconnect.services.ClubService;
import com.campusconnect.services.impl.ClubServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/club")
@CrossOrigin("*")
public class ClubController
{
    @Autowired
    private ClubService clubService;

    @Autowired
    @Qualifier("modelMapper")
    private ModelMapper modelMapper;



    @PostMapping("/register")
    private ResponseEntity<?> createClub(@RequestBody ClubDto clubDto)
    {
        System.out.println(clubDto.getClub_name());
        ClubDto clubDto1 = this.clubService.createClub(clubDto);
        return new ResponseEntity<>(clubDto1, HttpStatus.CREATED);
    }


    @GetMapping("/AllClub")
    private ResponseEntity<List<ClubDto>> getAllCLub()
    {
        List<ClubDto> allClubs = clubService.getAllClub()
                .stream()
                .map(club -> modelMapper.map(club,ClubDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<List<ClubDto>>(allClubs,HttpStatus.OK);
    }


    @GetMapping("/{club_id}")
    private ResponseEntity<Club> getClubById(@PathVariable("club_id") Long club_id)
    {
        Club club = clubService.getClubById(club_id);

        return new ResponseEntity<Club>(club,HttpStatus.OK);
    }
}