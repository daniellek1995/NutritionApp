// Generated by view binder compiler. Do not edit!
package com.example.nutritionapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.nutritionapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMenuScreenOldBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout MenuList;

  @NonNull
  public final ListView MenuListView;

  @NonNull
  public final Button btnStartChat;

  @NonNull
  public final FragmentContainerView navHostFragment;

  @NonNull
  public final Toolbar toolBar;

  private ActivityMenuScreenOldBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout MenuList, @NonNull ListView MenuListView,
      @NonNull Button btnStartChat, @NonNull FragmentContainerView navHostFragment,
      @NonNull Toolbar toolBar) {
    this.rootView = rootView;
    this.MenuList = MenuList;
    this.MenuListView = MenuListView;
    this.btnStartChat = btnStartChat;
    this.navHostFragment = navHostFragment;
    this.toolBar = toolBar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMenuScreenOldBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMenuScreenOldBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_menu_screen_old, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMenuScreenOldBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout MenuList = (ConstraintLayout) rootView;

      id = R.id.MenuListView;
      ListView MenuListView = ViewBindings.findChildViewById(rootView, id);
      if (MenuListView == null) {
        break missingId;
      }

      id = R.id.btnStartChat;
      Button btnStartChat = ViewBindings.findChildViewById(rootView, id);
      if (btnStartChat == null) {
        break missingId;
      }

      id = R.id.nav_host_fragment;
      FragmentContainerView navHostFragment = ViewBindings.findChildViewById(rootView, id);
      if (navHostFragment == null) {
        break missingId;
      }

      id = R.id.toolBar;
      Toolbar toolBar = ViewBindings.findChildViewById(rootView, id);
      if (toolBar == null) {
        break missingId;
      }

      return new ActivityMenuScreenOldBinding((ConstraintLayout) rootView, MenuList, MenuListView,
          btnStartChat, navHostFragment, toolBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
