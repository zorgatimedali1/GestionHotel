package com.hotelmaster.hotelmaster.service.user;

import com.hotelmaster.hotelmaster.entities.AppUser;

import java.util.List;

public interface IAppUserService {
    void createReceptionist(String username, String password, String nomComplet);
    void updateReceptionist(Long id, String username, String nomComplet, boolean actif);
    void changePassword(Long id, String newPassword);
    void deleteUser(Long id);
    AppUser getUser(Long id);
    List<AppUser> getAllReceptionists();
    boolean usernameExists(String username);
}
