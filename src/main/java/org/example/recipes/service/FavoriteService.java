package org.example.recipes.service;

import org.example.recipes.entity.Favorite;
import org.example.recipes.entity.User;
import org.example.recipes.repository.FavoriteRepository;
import org.example.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    public Favorite saveFavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }


    private UserRepository  userRepository;

    public List<Favorite> getFavoritesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найдена"));
        return favoriteRepository.findByUser(user);
    }


    public Favorite addFavorite(Favorite favorite) {

        return favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long id) {

        favoriteRepository.deleteById(id);
    }

}
