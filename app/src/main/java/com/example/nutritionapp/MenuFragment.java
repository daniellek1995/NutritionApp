package com.example.nutritionapp;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nutritionapp.model.App;
import com.example.nutritionapp.model.MenuItem;
import com.example.nutritionapp.viewModel.MenuViewModel;

import java.util.List;

public class MenuFragment extends Fragment {

    private MenuViewModel viewModel;

    private final String TAG = "MenuFragment";

    Button btnStartChat;
    String email;
    String fullName;
    String registeredAtDateStr;
    boolean isAdmin;

    NavHostFragment navHostFragment;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.menu_fragment, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(MenuViewModel.class);

        navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().
                                                findFragmentById(R.id.nav_host_fragment);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        fullName = sharedPreferences.getString("fullName", "no name");
        registeredAtDateStr = sharedPreferences.getString("registeredAt", "1900-01-01");

        btnStartChat = view.findViewById(R.id.btnStartChat2);
        ListView listViewMenuItems = view.findViewById(R.id.MenuListView2);

        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnStartChatClicked(v);
            }
        });

        isAdmin = App.isAdmin(email);
        List<MenuItem> dataToDisplay = MenuItem.generateItemList(isAdmin);
        if (isAdmin) {
            btnStartChat.setText("צ'אט עם לקוח");
        }

        MenuFragment.MyAdapter adapter = new MenuFragment.MyAdapter(dataToDisplay);
        listViewMenuItems.setAdapter(adapter);

        listViewMenuItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                if (activityName.equals("com.example.nutritionapp.ContactActivity")) {
                    Intent intent = new Intent(getActivity(), c);
                    if (isAdmin) {
                        intent.putExtra("title", "ניהול לקוחות");
                    }
                    else {
                        intent.putExtra("title", "אנשי קשר");
                    }
                    startActivity(intent);
                    return;
                }
                else if (activityName.equals("StatusFragment")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("email of progress owner", email);
                    bundle.putString("fullname of progress owner", fullName);
                    navHostFragment.getNavController().navigate(R.id.action_menuFragment_to_statusFragment, bundle);
                    return;
                }
                else if (activityName.equals("ProfileFragment")) {
                    Log.d("navv", "navigating now");
                    NavHostFragment navHostFragment = (NavHostFragment)getActivity().getSupportFragmentManager().
                            findFragmentById(R.id.nav_host_fragment);
                    navHostFragment.getNavController().navigate(R.id.action_menuFragment_to_profileFragment);
                    return;
                }

                else if(activityName.contains("RecipeListActivity")) {
                    Intent intent = new Intent(getActivity(), c);
                    intent.putExtra("admin",isAdmin);
                    startActivity(intent);
                    return;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void onBtnStartChatClicked(View view) {

        if (isAdmin) {
            Intent intent = new Intent(getActivity(), ContactActivity.class);
            intent.putExtra("title", "ניהול אנשי קשר");
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("email of chat owner", email);
            startActivity(intent);
        }
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

//    @Override
//    public boolean onSupportNavigateUp() {
//        return  Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigateUp();
//    }

//    public void onBtnNavigateClicked(View view) {
//        Log.d("navv", "navigating now");
//        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager().
//                                                findFragmentById(R.id.nav_host_fragment);
//        navHostFragment.getNavController().navigate(R.id.action_menuFragment_to_profileFragment);
//    }
}