package com.campusconnect.controller;


import com.campusconnect.dto.ClubDto;
import com.campusconnect.dto.EventDto;
import com.campusconnect.entities.Club;
import com.campusconnect.services.ClubService;
import com.campusconnect.services.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @PostMapping("/register")
    private ResponseEntity<ClubDto> createClub(@RequestBody ClubDto clubDto)
    {
        ClubDto clubDto1 = clubService.createClub(clubDto);
        return new ResponseEntity<ClubDto>(clubDto1, HttpStatus.CREATED);
    }


    @GetMapping("/allclub")
    private ResponseEntity<List<ClubDto>> getAllCLub()
    {
        List<ClubDto> allClubs = clubService.getAllClub()
                .stream()
                .map(club -> modelMapper.map(club,ClubDto.class))
                .collect(Collectors.toList());

        return new ResponseEntity<List<ClubDto>>(allClubs,HttpStatus.OK);
    }


    @GetMapping("/{club_id}")
    private ResponseEntity<ClubDto> getClubById(@PathVariable("club_id") Long club_id)
    {
        ClubDto club = clubService.getClubById(club_id);

        return new ResponseEntity<ClubDto>(club,HttpStatus.OK);
    }

    @GetMapping("/{club_name}/{club_password}")
    private ResponseEntity<?> loginClub(@PathVariable("club_name") String club_name,@PathVariable("club_password") String club_password)
    {
        ClubDto clubDto = clubService.loginClub(club_name,club_password);

        return new ResponseEntity<Long>(clubDto.getClubId(),HttpStatus.ACCEPTED);
    }


}