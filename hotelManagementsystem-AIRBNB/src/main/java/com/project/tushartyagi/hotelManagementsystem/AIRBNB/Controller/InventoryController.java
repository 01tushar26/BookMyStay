package com.project.tushartyagi.hotelManagementsystem.AIRBNB.Controller;


import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.InventoryDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.DTO.InventoryUpdateRequestDTO;
import com.project.tushartyagi.hotelManagementsystem.AIRBNB.Services.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Inventory Management APIs",
        description = "Administrative APIs for managing room inventory availability, stock updates, and room allocation tracking."
)
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/admin/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping(path = "rooms/{roomId}")
    public ResponseEntity<List<InventoryDTO>> getAllInventoryByRoom(@PathVariable (name = "roomId") Long id){

        List<InventoryDTO> dtoList = inventoryService.getAllInventoryByRoom(id);
        return ResponseEntity.ok(dtoList);

    }
    @PatchMapping(path = "rooms/{roomId}")
    public ResponseEntity<Void> UpdateInventoryByRoom(@PathVariable (name = "roomId") Long id,
                                                      @RequestBody InventoryUpdateRequestDTO updateRequestDTO){

        inventoryService.UpdateInventoryByRoom(id,updateRequestDTO);
        return ResponseEntity.noContent().build();

    }
}
