package org.example.recipes.service;

import org.example.recipes.entity.Comment;
import org.example.recipes.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment); // ✅ метод переименован
    }

    public List<Comment> getCommentsByRecipeId(Long recipeId) {
        return commentRepository.findByRecipeId(recipeId); // ✅ теперь доступен
    }



    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
