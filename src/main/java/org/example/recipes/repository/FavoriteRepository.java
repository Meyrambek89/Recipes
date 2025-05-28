package org.example.recipes.repository;

import org.example.recipes.entity.Favorite;
import org.example.recipes.entity.Recipe;
import org.example.recipes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUser(User user);

    boolean existsByUserAndRecipe(User user, Recipe recipe);
}
