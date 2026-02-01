package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;

import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.RoomDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/hotels/{hotelId}/rooms")
public class RoomAdminController {

    @Autowired
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@PathVariable (name = "hotelId") Long id, @RequestBody RoomDTO roomDTO){
        RoomDTO room = roomService.createNewRoom(id,roomDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getRoomByHotelId(@PathVariable (name = "hotelId") Long id){
        List<RoomDTO> room = roomService.getAllRoomInHotel(id);
        return ResponseEntity.ok(room);
    }
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable (name = "roomId") Long id){
        RoomDTO room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }
    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void>  deleteRoomById(@PathVariable (name = "roomId") Long id){
         roomService.deleteRoomById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{roomId}")
    public ResponseEntity<RoomDTO> updateRoomById(@PathVariable (name = "roomId") Long roomId,
                                                  @RequestBody RoomDTO roomDTO,
                                                  @PathVariable Long hotelId){
        RoomDTO room = roomService.updateRoomById(roomId,hotelId,roomDTO);
        return ResponseEntity.ok(room);
    }




}
