package com.campusconnect.controller;


import com.campusconnect.dto.ClubDto;
import com.campusconnect.dto.EventDto;
import com.campusconnect.entities.Admin;
import com.campusconnect.repositories.AdminRepo;
import com.campusconnect.repositories.ClubRepo;
import com.campusconnect.services.AdminService;
import com.campusconnect.services.ClubService;
import com.campusconnect.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    private AdminService adminService;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Autowired
    private ClubRepo clubRepo;

    @PostMapping("/register")
    private ResponseEntity<ClubDto> createClub(@RequestBody ClubDto clubDto)
    {
        System.out.println(clubDto.getClubEmail());
        Admin admin = new Admin();
        admin.setClubEmail(clubDto.getClubEmail());
        admin.setClubStatus("pending");
        adminRepo.save(admin);
        ClubDto clubDto1 = clubService.createClub(clubDto);
        return new ResponseEntity<ClubDto>(clubDto1, HttpStatus.CREATED);
    }

    @PutMapping("/updateclub/{clubId}")
    private ResponseEntity<ClubDto> updateClub(@RequestBody ClubDto clubDto, @PathVariable Long clubId){
        ClubDto clubDto1 = this.clubService.updateClub(clubDto,clubId);
        return new ResponseEntity<>(clubDto1,HttpStatus.OK);

    }

    @DeleteMapping("/deleteClub/{clubId}")
    private  void deleteClub(@PathVariable Long clubId){
        this.clubService.deleteClub(clubId);
    }

    @GetMapping("/allclub")
    private ResponseEntity<List<ClubDto>> getAllCLub()
    {
        List<ClubDto> allClubs = adminService.getClubEmails().stream()
                .filter(admin -> admin.getClubStatus().equals("accepted"))
                .map(admin -> modelMapper.map(clubRepo.findClubByClubEmail(admin.getClubEmail()),ClubDto.class  ))
                .collect(Collectors.toList());

        return new ResponseEntity<List<ClubDto>>(allClubs,HttpStatus.OK);
    }


    @GetMapping("/{club_id}")
    private ResponseEntity<ClubDto> getClubById(@PathVariable("club_id") Long club_id)
    {
        ClubDto club = clubService.getClubById(club_id);

        return new ResponseEntity<ClubDto>(club,HttpStatus.OK);
    }

    @GetMapping("/{club_email}/{club_password}")
    private ResponseEntity<?> loginClub(@PathVariable("club_email") String club_email,@PathVariable("club_password") String club_password)
    {
        ClubDto clubDto = clubService.loginClub(club_email,club_password);

        Admin admin = adminRepo.findAdminByClubEmail(clubDto.getClubEmail());

        if(admin.getClubStatus() == "pending")
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(clubDto.getClubId(),HttpStatus.ACCEPTED);
    }

    @PostMapping("/image/upload/{clubId}")
    public ResponseEntity<ClubDto> uploadlogo(
            @RequestParam("logo")MultipartFile image,
            @PathVariable Long clubId
            ) throws IOException {
        ClubDto clubDto = this.clubService.getClubById(clubId);
        String fileName = this.fileService.uploadImage(path,image);
        clubDto.setLogo(fileName);
        ClubDto clubDto1 = this.clubService.updateClub(clubDto,clubId);
        return  new ResponseEntity<ClubDto>(clubDto1,HttpStatus.OK);
    }

    @GetMapping(value = "/logo/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void ShowLogo(@PathVariable("image") String image,
                         HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResources(path,image);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}