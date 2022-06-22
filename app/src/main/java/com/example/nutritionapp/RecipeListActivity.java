package com.example.nutritionapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nutritionapp.model.MyFireStorageHandler;
import com.example.nutritionapp.model.MyFireStoreHandler;
import com.example.nutritionapp.model.RecipeItem;
import com.google.gson.Gson;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private final String TAG = "RecipeListActivity";

    RecyclerView rvRecipes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allrecipes);
        rvRecipes = findViewById(R.id.rvRecipes);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        MyFireStoreHandler.getAllRecipes(new MyFireStoreHandler.RecipesCallback() {
            @Override
            public void onResponse(List<RecipeItem> recipeItems) {
                RecipesAdapter adapter = new RecipesAdapter(RecipeListActivity.this, recipeItems);
                rvRecipes.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(RecipeListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.addRecipeBtn)
                .setOnClickListener(v -> startActivity(new Intent(RecipeListActivity.this, AddRecipeActivity.class)));

        if (getIntent() != null) {
            if (!getIntent().getBooleanExtra("admin", true))
                findViewById(R.id.addRecipeBtn).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //MyFireStoreHandler.removeRecipesListener();
    }

    class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {

        Context context;
        private List<RecipeItem> recipeItems;

        public RecipesAdapter(Context context, List<RecipeItem> recipeItems) {
            this.context = context;
            this.recipeItems = recipeItems;
        }

        @NonNull
        @Override
        public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecipesViewHolder(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recipe_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {
            RecipeItem recipe = recipeItems.get(position);
            holder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return recipeItems.size();
        }

        class RecipesViewHolder extends RecyclerView.ViewHolder {

            TextView moveToInstructions, recipeType, recipeName;
            ImageView recipeImage;

            public RecipesViewHolder(@NonNull View itemView) {
                super(itemView);
                this.recipeImage = itemView.findViewById(R.id.recipeItemImage);
                this.moveToInstructions = itemView.findViewById(R.id.moveToRecipeIns);
                this.recipeType = itemView.findViewById(R.id.recipeItemType);
                this.recipeName = itemView.findViewById(R.id.recipeItemName);
            }

            public void bind(RecipeItem recipeItem) {
                this.moveToInstructions.setOnClickListener(v -> {
                    Gson g = new Gson();
                    String toJson = g.toJson(recipeItem);
                    Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
                    intent.putExtra("recipe", toJson);
                    startActivity(intent);
                });
                this.recipeName.setText(recipeItem.getName());
                this.recipeType.setText(recipeItem.getType());
                if (recipeItem.getImage()==null || !recipeItem.getImage().equals("none")) {
                    String pictureInChatMsg = "images/recipes/" + recipeItem.getImage();
                    MyFireStorageHandler.downloadPictureIntoImageview(context, pictureInChatMsg,
                            this.recipeImage, new MyFireStorageHandler.RequestCallback() {
                                @Override
                                public void OnResponse(MyFireStorageHandler.EnumStatus status, String message, double uploadProgress) {
                                    if (status == MyFireStorageHandler.EnumStatus.SUCCESS) {
                                        Log.d(TAG, "Pic downloaded. pic: " + pictureInChatMsg);
                                    } else {
                                        Log.e(TAG, "Failed to download pic. pic: " + pictureInChatMsg);
                                    }
                                }
                            });
                }
            }
        }
    }
}
