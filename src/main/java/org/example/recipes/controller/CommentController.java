package org.example.recipes.controller;

import org.example.recipes.entity.Comment;
import org.example.recipes.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Comment> getCommentsByRecipe(@PathVariable Long recipeId) {
        return commentService.getCommentsByRecipeId(recipeId);
    }

    @PostMapping
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }
}
