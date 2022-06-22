package com.example.nutritionapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class StatusFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private StatusFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private StatusFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static StatusFragmentArgs fromBundle(@NonNull Bundle bundle) {
    StatusFragmentArgs __result = new StatusFragmentArgs();
    bundle.setClassLoader(StatusFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("email of progress owner")) {
      String emailOfProgressOwner;
      emailOfProgressOwner = bundle.getString("email of progress owner");
      if (emailOfProgressOwner == null) {
        throw new IllegalArgumentException("Argument \"email of progress owner\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("email of progress owner", emailOfProgressOwner);
    } else {
      __result.arguments.put("email of progress owner", "none");
    }
    if (bundle.containsKey("fullname of progress owner")) {
      String fullnameOfProgressOwner;
      fullnameOfProgressOwner = bundle.getString("fullname of progress owner");
      if (fullnameOfProgressOwner == null) {
        throw new IllegalArgumentException("Argument \"fullname of progress owner\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("fullname of progress owner", fullnameOfProgressOwner);
    } else {
      __result.arguments.put("fullname of progress owner", "none");
    }
    return __result;
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

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
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
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    StatusFragmentArgs that = (StatusFragmentArgs) object;
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
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getEmailOfProgressOwner() != null ? getEmailOfProgressOwner().hashCode() : 0);
    result = 31 * result + (getFullnameOfProgressOwner() != null ? getFullnameOfProgressOwner().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StatusFragmentArgs{"
        + "emailOfProgressOwner=" + getEmailOfProgressOwner()
        + ", fullnameOfProgressOwner=" + getFullnameOfProgressOwner()
        + "}";
  }

  public static class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(StatusFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder() {
    }

    @NonNull
    public StatusFragmentArgs build() {
      StatusFragmentArgs result = new StatusFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setEmailOfProgressOwner(@NonNull String emailOfProgressOwner) {
      if (emailOfProgressOwner == null) {
        throw new IllegalArgumentException("Argument \"email of progress owner\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("email of progress owner", emailOfProgressOwner);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setFullnameOfProgressOwner(@NonNull String fullnameOfProgressOwner) {
      if (fullnameOfProgressOwner == null) {
        throw new IllegalArgumentException("Argument \"fullname of progress owner\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("fullname of progress owner", fullnameOfProgressOwner);
      return this;
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
  }
}
