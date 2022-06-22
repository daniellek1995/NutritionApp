// Generated by view binder compiler. Do not edit!
package com.example.nutritionapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.nutritionapp.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityStatusBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnSaveWeight;

  @NonNull
  public final Button btnShowProgress;

  @NonNull
  public final EditText etEnterFatPercentage;

  @NonNull
  public final EditText etUserWeight;

  @NonNull
  public final TextView textView;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final TextView tvEnterFatPercentage;

  @NonNull
  public final TextView tvStatusFullName;

  @NonNull
  public final TextView tvStatusSaveWeight;

  private ActivityStatusBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnSaveWeight,
      @NonNull Button btnShowProgress, @NonNull EditText etEnterFatPercentage,
      @NonNull EditText etUserWeight, @NonNull TextView textView, @NonNull TextView textView2,
      @NonNull TextView tvEnterFatPercentage, @NonNull TextView tvStatusFullName,
      @NonNull TextView tvStatusSaveWeight) {
    this.rootView = rootView;
    this.btnSaveWeight = btnSaveWeight;
    this.btnShowProgress = btnShowProgress;
    this.etEnterFatPercentage = etEnterFatPercentage;
    this.etUserWeight = etUserWeight;
    this.textView = textView;
    this.textView2 = textView2;
    this.tvEnterFatPercentage = tvEnterFatPercentage;
    this.tvStatusFullName = tvStatusFullName;
    this.tvStatusSaveWeight = tvStatusSaveWeight;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityStatusBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityStatusBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_status, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityStatusBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnSaveWeight;
      Button btnSaveWeight = ViewBindings.findChildViewById(rootView, id);
      if (btnSaveWeight == null) {
        break missingId;
      }

      id = R.id.btnShowProgress;
      Button btnShowProgress = ViewBindings.findChildViewById(rootView, id);
      if (btnShowProgress == null) {
        break missingId;
      }

      id = R.id.etEnterFatPercentage;
      EditText etEnterFatPercentage = ViewBindings.findChildViewById(rootView, id);
      if (etEnterFatPercentage == null) {
        break missingId;
      }

      id = R.id.etUserWeight;
      EditText etUserWeight = ViewBindings.findChildViewById(rootView, id);
      if (etUserWeight == null) {
        break missingId;
      }

      id = R.id.textView;
      TextView textView = ViewBindings.findChildViewById(rootView, id);
      if (textView == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      id = R.id.tvEnterFatPercentage;
      TextView tvEnterFatPercentage = ViewBindings.findChildViewById(rootView, id);
      if (tvEnterFatPercentage == null) {
        break missingId;
      }

      id = R.id.tvStatusFullName;
      TextView tvStatusFullName = ViewBindings.findChildViewById(rootView, id);
      if (tvStatusFullName == null) {
        break missingId;
      }

      id = R.id.tvStatusSaveWeight;
      TextView tvStatusSaveWeight = ViewBindings.findChildViewById(rootView, id);
      if (tvStatusSaveWeight == null) {
        break missingId;
      }

      return new ActivityStatusBinding((ConstraintLayout) rootView, btnSaveWeight, btnShowProgress,
          etEnterFatPercentage, etUserWeight, textView, textView2, tvEnterFatPercentage,
          tvStatusFullName, tvStatusSaveWeight);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
