package org.example.recipes.controller;

import jakarta.servlet.http.HttpSession;
import org.example.recipes.entity.Category;
import org.example.recipes.entity.Recipe;
import org.example.recipes.entity.User;
import org.example.recipes.service.CategoryService;
import org.example.recipes.service.RecipeService;
import org.example.recipes.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final UserService userService; // <-- Добавили UserService
    private final CategoryService categoryService;

    public RecipeController(RecipeService recipeService, UserService userService, CategoryService categoryService) {
        this.recipeService = recipeService;
        this.userService = userService; // <-- Сохранили зависимость
        this.categoryService = categoryService;
    }



    @GetMapping("/recipes-data")
    @ResponseBody
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    @GetMapping("/{id}")
    public Recipe getRecipe(@PathVariable Long id) {
        return recipeService.getRecipeById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }

    @GetMapping("/my-recipes")
    public String showMyRecipes(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return "redirect:/login";

        String username = userDetails.getUsername();
        List<Recipe> recipes = recipeService.getRecipesByUsername(username);
        model.addAttribute("recipes", recipes);

        return "my_recipes";
    }

    @GetMapping("/my-recipes/new")
    public String showCreateRecipeForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "new_recipe";
    }

    @PostMapping("/my-recipes/new")
    public String createRecipe(@ModelAttribute Recipe recipe,
                               @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        // Найти пользователя по username (например, через UserService)
        User currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
        if (currentUser == null) {
            return "redirect:/login";
        }

        recipe.setAuthor(currentUser);
        recipe.setApproved(false); // или true, если не требуется модерация
        recipeService.saveRecipe(recipe);

        return "redirect:/my-recipes";
    }

    @GetMapping("/recipes/{id}")
    public String showRecipeDetails(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.getRecipeById(id).orElse(null);
        if (recipe == null) {
            return "redirect:/my-recipes";
        }

        model.addAttribute("recipe", recipe);
        return "recipe_details";
    }

    // Показать форму редактирования
    @GetMapping("/recipes/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Recipe recipe = recipeService.getRecipeById(id).orElse(null);
        if (recipe == null) {
            return "redirect:/my-recipes";
        }

        // Проверка: только автор может редактировать
        if (!recipe.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/my-recipes";
        }

        model.addAttribute("recipe", recipe);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit_recipe";
    }

    // Обработка формы редактирования
    @PostMapping("/recipes/edit/{id}")
    public String updateRecipe(@PathVariable Long id,
                               @ModelAttribute Recipe updatedRecipe,
                               @AuthenticationPrincipal UserDetails userDetails) {
        Recipe existing = recipeService.getRecipeById(id).orElse(null);
        if (existing == null) {
            return "redirect:/my-recipes";
        }

        // Только автор может редактировать
        if (!existing.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/my-recipes";
        }

        // Обновляем данные
        existing.setTitle(updatedRecipe.getTitle());
        existing.setDescription(updatedRecipe.getDescription());
        existing.setIngredients(updatedRecipe.getIngredients());
        existing.setImageUrl(updatedRecipe.getImageUrl());
        existing.setCategory(updatedRecipe.getCategory());

        recipeService.saveRecipe(existing);
        return "redirect:/recipes/" + id;
    }

    // Удаление рецепта
    @PostMapping("/recipes/delete/{id}")
    public String deleteRecipeById(@PathVariable Long id,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        Recipe recipe = recipeService.getRecipeById(id).orElse(null);
        if (recipe == null || !recipe.getAuthor().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/my-recipes";
        }

        recipeService.deleteRecipe(id);
        return "redirect:/my-recipes";
    }

    @GetMapping("/admin/recipe-requests")
    public String showPendingRecipes(Model model) {
        List<Recipe> pendingRecipes = recipeService.getPendingRecipes(); // approved = false
        model.addAttribute("pendingRecipes", pendingRecipes);
        return "admin_recipe_requests";
    }

    @PostMapping("/admin/recipes/approve/{id}")
    public String approveRecipe(@PathVariable Long id) {
        recipeService.approveRecipe(id);
        return "redirect:/admin/recipe-requests";
    }

    @PostMapping("/admin/recipes/delete/{id}")
    public String deleteRecipeByAdmin(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return "redirect:/admin/recipe-requests";
    }

    // --- Изменённый метод с поддержкой поиска и фильтрации ---
    @GetMapping("/recipes")
    public String showAllApprovedRecipes(Model model,
                                         @RequestParam(required = false) String ingredient,
                                         @RequestParam(required = false) String category) {
        List<Recipe> recipes = recipeService.searchAndFilter(ingredient, category);
        model.addAttribute("recipes", recipes);

        List<Category> categories = categoryService.getAllCategories(); // получаем все категории
        model.addAttribute("categories", categories);

        model.addAttribute("paramIngredient", ingredient);
        model.addAttribute("paramCategory", category);

        return "recipes";
    }

}
