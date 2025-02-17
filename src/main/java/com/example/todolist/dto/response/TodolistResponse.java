package com.example.todolist.dto.response;

import com.example.todolist.model.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodolistResponse {
    private Long id;
    private String title;
    private String description;
    private String username;
    private CategoryData category;
    private boolean isCompleted;
    private String imagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    public TodolistResponse(){
        this.category = new CategoryData();
    }
    public void setCategoryId(Long id){
        this.category.setId(id);
    }
    public void setCategoryName(String name){
        this.category.setName(name);
    }
}

@Data
class CategoryData{
    private Long id;
    private String name;
}
