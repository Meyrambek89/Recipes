package org.example.recipes.controller;

import org.example.recipes.entity.Favorite;
import org.example.recipes.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/favorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {

        this.favoriteService = favoriteService;
    }

    @GetMapping("/user/{userId}")
    public List<Favorite> getFavoritesByUser(@PathVariable Long userId) {
        return  favoriteService.getFavoritesByUserId(userId);
    }

    @PostMapping
    public Favorite addFavorite(@RequestBody Favorite favorite)  {
        return favoriteService.addFavorite(favorite);
    }

    @DeleteMapping("/{id}")
    public void removeFavorite(@PathVariable Long id) {


        favoriteService.removeFavorite(id);
    }
}
