package com.example.nutritionapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class MenuFragmentDirections {
  private MenuFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionMenuFragmentToProfileFragment() {
    return new ActionOnlyNavDirections(R.id.action_menuFragment_to_profileFragment);
  }

  @NonNull
  public static ActionMenuFragmentToStatusFragment actionMenuFragmentToStatusFragment() {
    return new ActionMenuFragmentToStatusFragment();
  }

  public static class ActionMenuFragmentToStatusFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionMenuFragmentToStatusFragment() {
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionMenuFragmentToStatusFragment setEmailOfProgressOwner(
        @NonNull String emailOfProgressOwner) {
      if (emailOfProgressOwner == null) {
        throw new IllegalArgumentException("Argument \"email of progress owner\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("email of progress owner", emailOfProgressOwner);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionMenuFragmentToStatusFragment setFullnameOfProgressOwner(
        @NonNull String fullnameOfProgressOwner) {
      if (fullnameOfProgressOwner == null) {
        throw new IllegalArgumentException("Argument \"fullname of progress owner\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("fullname of progress owner", fullnameOfProgressOwner);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("email of progress owner")) {
        String emailOfProgressOwner = (String) arguments.get("email of progress owner");
        __result.putString("email of progress owner", emailOfProgressOwner);
      } else {
        __result.putString("email of progress owner", "none");
      }
      if (arguments.containsKey("fullname of progress owner")) {
        String fullnameOfProgressOwner = (String) arguments.get("fullname of progress owner");
        __result.putString("fullname of progress owner", fullnameOfProgressOwner);
      } else {
        __result.putString("fullname of progress owner", "none");
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_menuFragment_to_statusFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getEmailOfProgressOwner() {
      return (String) arguments.get("email of progress owner");
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getFullnameOfProgressOwner() {
      return (String) arguments.get("fullname of progress owner");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionMenuFragmentToStatusFragment that = (ActionMenuFragmentToStatusFragment) object;
      if (arguments.containsKey("email of progress owner") != that.arguments.containsKey("email of progress owner")) {
        return false;
      }
      if (getEmailOfProgressOwner() != null ? !getEmailOfProgressOwner().equals(that.getEmailOfProgressOwner()) : that.getEmailOfProgressOwner() != null) {
        return false;
      }
      if (arguments.containsKey("fullname of progress owner") != that.arguments.containsKey("fullname of progress owner")) {
        return false;
      }
      if (getFullnameOfProgressOwner() != null ? !getFullnameOfProgressOwner().equals(that.getFullnameOfProgressOwner()) : that.getFullnameOfProgressOwner() != null) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (getEmailOfProgressOwner() != null ? getEmailOfProgressOwner().hashCode() : 0);
      result = 31 * result + (getFullnameOfProgressOwner() != null ? getFullnameOfProgressOwner().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionMenuFragmentToStatusFragment(actionId=" + getActionId() + "){"
          + "emailOfProgressOwner=" + getEmailOfProgressOwner()
          + ", fullnameOfProgressOwner=" + getFullnameOfProgressOwner()
          + "}";
    }
  }
}
