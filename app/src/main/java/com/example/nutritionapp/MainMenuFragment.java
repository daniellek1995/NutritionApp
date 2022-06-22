package com.example.nutritionapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nutritionapp.model.App;
import com.example.nutritionapp.model.MenuItem;

import java.util.List;

public class MainMenuFragment extends Fragment {

    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        ListView list = view.findViewById(R.id.fragment_MenuListView);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        String fullName = sharedPreferences.getString("fullName", "guest");

        boolean displayAdminMenu = email.equalsIgnoreCase(App.getAdminEmail());
        List<MenuItem> dataToDisplay = MenuItem.generateItemList(displayAdminMenu);

        MyAdapter adapter = new MyAdapter(dataToDisplay);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "row #" + position + "was selected");
                MenuItem item = dataToDisplay.get(position);
                String activityName = item.getActivityName();

                Class<?> c = null;
                try {
                    c = Class.forName(activityName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

               // Navigation.findNavController(view).navigate(MainMenuFragmentDirections.actionMainMenuFragmentToInformationListFragment());

            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    class MyAdapter extends BaseAdapter {
        private List<MenuItem> data;

        MyAdapter(List<MenuItem> itemsToDisplay) {
            this.data = itemsToDisplay;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_row, null);
                Log.d("TAG", "creating new view for row" + position);
            } else {
                Log.d("TAG", "reusing old view for row" + position);
            }

            MenuItem item = data.get(position);
            String itemText = item.getText();

            TextView text = convertView.findViewById(R.id.listrow_text);
            ImageView img = convertView.findViewById(R.id.listrow_img);

            text.setText(itemText);
            img.setImageResource(item.getImg());

            return convertView;
        }
    }
}