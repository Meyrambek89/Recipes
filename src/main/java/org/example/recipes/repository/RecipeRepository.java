package org.example.recipes.repository;

import org.example.recipes.entity.Category;
import org.example.recipes.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByCategory(Category category);
    List<Recipe> findByCategory_Name(String name); // ✅ по названию категории
    List<Recipe> findByTitleContainingIgnoreCase(String keyword);
    List<Recipe> findByIngredientsContainingIgnoreCase(String ingredient); // ✅ добавили
    List<Recipe> findByAuthorUsername(String username); // правильное имя поля
    List<Recipe> findByApprovedFalse();
    List<Recipe> findByApprovedTrue();

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    List<Recipe> findByIngredientContaining(@Param("ingredient") String ingredient);

    List<Recipe> findByCategory_NameIgnoreCase(String categoryName);

    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%')) AND LOWER(r.category.name) = LOWER(:category)")
    List<Recipe> findByIngredientAndCategory(@Param("ingredient") String ingredient, @Param("category") String category);
}

