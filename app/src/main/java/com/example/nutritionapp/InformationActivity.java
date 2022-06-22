package com.example.nutritionapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nutritionapp.model.InformationDataItem;

import java.util.List;

public class InformationActivity extends AppCompatActivity {

    private List<InformationDataItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        data = InformationDataItem.generateItemList();

        RecyclerView listRV = findViewById(R.id.informationListRV);
        listRV.setHasFixedSize(true);
        listRV.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter adapter = new MyAdapter();
        listRV.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG", "row was clicked " + position);
            }
        });
    }

    interface OnItemClickListener {
        void onItemClick(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        ImageView img;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            text = itemView.findViewById(R.id.listrow_text);
            img = itemView.findViewById(R.id.listrow_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    listener.onItemClick(pos);
                }
            });
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            InformationDataItem item = data.get(position);
            holder.text.setText(item.getText());
            holder.img.setImageResource(item.getImg());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}