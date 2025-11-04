package com.ecommerce.agriconnectke.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.dto.UserDTO;
import com.ecommerce.agriconnectke.models.User;
import com.ecommerce.agriconnectke.services.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsersDTO();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserDTO user = userService.getUserDTOById(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(userId, userDetails);
            UserDTO userDTO = new UserDTO(updatedUser);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{userId}/profile")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long userId,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String businessType) {
        try {
            User updatedUser = userService.updateUserProfile(userId, fullName, county, businessType);
            UserDTO userDTO = new UserDTO(updatedUser);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        try {
            List<UserDTO> users = userService.getUsersDTOByRole(role);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<UserDTO>> getAllDrivers() {
        List<UserDTO> drivers = userService.getAllDrivers().stream()
                .map(UserDTO::new)
                .toList();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/farmers")
    public ResponseEntity<List<UserDTO>> getAllFarmers() {
        List<UserDTO> farmers = userService.getAllFarmers().stream()
                .map(UserDTO::new)
                .toList();
        return ResponseEntity.ok(farmers);
    }

    @GetMapping("/buyers")
    public ResponseEntity<List<UserDTO>> getAllBuyers() {
        List<UserDTO> buyers = userService.getAllBuyers().stream()
                .map(UserDTO::new)
                .toList();
        return ResponseEntity.ok(buyers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserDTO>> searchUsers(@RequestParam String q) {
        try {
            List<UserDTO> users = userService.searchUsersDTO(q);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/stats/count")
    public ResponseEntity<?> getUserCount() {
        try {
            long totalUsers = userService.getTotalUsers();
            long farmers = userService.getUsersCountByRole("FARMER");
            long buyers = userService.getUsersCountByRole("BUYER");
            long drivers = userService.getUsersCountByRole("DRIVER");
            long admins = userService.getUsersCountByRole("ADMIN");

            var stats = new UserStats(totalUsers, farmers, buyers, drivers, admins);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/is-driver")
    public ResponseEntity<Boolean> isDriver(@PathVariable Long userId) {
        boolean isDriver = userService.isDriver(userId);
        return ResponseEntity.ok(isDriver);
    }

    @GetMapping("/{userId}/is-farmer")
    public ResponseEntity<Boolean> isFarmer(@PathVariable Long userId) {
        boolean isFarmer = userService.isFarmer(userId);
        return ResponseEntity.ok(isFarmer);
    }

    @GetMapping("/{userId}/is-buyer")
    public ResponseEntity<Boolean> isBuyer(@PathVariable Long userId) {
        boolean isBuyer = userService.isBuyer(userId);
        return ResponseEntity.ok(isBuyer);
    }

    // Stats DTO
    private static class UserStats {
        private final long totalUsers;
        private final long farmers;
        private final long buyers;
        private final long drivers;
        private final long admins;

        public UserStats(long totalUsers, long farmers, long buyers, long drivers, long admins) {
            this.totalUsers = totalUsers;
            this.farmers = farmers;
            this.buyers = buyers;
            this.drivers = drivers;
            this.admins = admins;
        }

        public long getTotalUsers() { return totalUsers; }
        public long getFarmers() { return farmers; }
        public long getBuyers() { return buyers; }
        public long getDrivers() { return drivers; }
        public long getAdmins() { return admins; }
    }
}