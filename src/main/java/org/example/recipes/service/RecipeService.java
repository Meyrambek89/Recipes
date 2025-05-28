package org.example.recipes.service;

import org.example.recipes.entity.Recipe;
import org.example.recipes.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> getRecipesByCategory(String categoryName) {
        return recipeRepository.findByCategory_Name(categoryName); // ✅ исправлено
    }

    public List<Recipe> searchByIngredient(String ingredient) {
        return recipeRepository.findByIngredientsContainingIgnoreCase(ingredient); // ✅ теперь есть
    }

    public List<Recipe> getRecipesByUsername(String username){
        return recipeRepository.findByAuthorUsername(username);
    }

    public List<Recipe> getPendingRecipes() {
        return recipeRepository.findByApprovedFalse();
    }

    public void approveRecipe(Long id) {
        recipeRepository.findById(id).ifPresent(recipe -> {
            recipe.setApproved(true);
            recipeRepository.save(recipe);
        });
    }

    public List<Recipe> getAllApprovedRecipes() {
        return recipeRepository.findByApprovedTrue();
    }

    // --- НОВЫЙ МЕТОД для поиска по ингредиентам и фильтрации по категориям вместе ---
    public List<Recipe> searchAndFilter(String ingredient, String category) {
        boolean ingredientEmpty = (ingredient == null || ingredient.isBlank());
        boolean categoryEmpty = (category == null || category.isBlank());

        if (ingredientEmpty && categoryEmpty) {
            return getAllApprovedRecipes(); // возвращаем все одобренные рецепты
        }

        if (ingredientEmpty) {
            return recipeRepository.findByCategory_NameIgnoreCase(category);
        }

        if (categoryEmpty) {
            return recipeRepository.findByIngredientsContainingIgnoreCase(ingredient);
        }

        // Поиск с фильтрацией сразу
        return recipeRepository.findByIngredientAndCategory(ingredient, category);
    }
}

