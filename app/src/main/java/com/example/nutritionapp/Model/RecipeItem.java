package com.example.nutritionapp.model;

public class RecipeItem {
    {}

    private String id;
    private String name;
    private String instructions;
    private String type;
    private String image;

    public RecipeItem(String id, String name, String instructions, String type, String image) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.type = type;
        this.image = image;
    }

    public RecipeItem(){}

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImageUrl(String imageUrl) {
        this.image = image;
    }
}
