// Generated by view binder compiler. Do not edit!
package com.example.qr_dasher.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.qr_dasher.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class EventSignUpPageBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView signUpDetails;

  @NonNull
  public final TextView signUpName;

  @NonNull
  public final ImageView signUpPoster;

  @NonNull
  public final TextView signUpTime;

  @NonNull
  public final Button signupButton;

  private EventSignUpPageBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView signUpDetails, @NonNull TextView signUpName,
      @NonNull ImageView signUpPoster, @NonNull TextView signUpTime, @NonNull Button signupButton) {
    this.rootView = rootView;
    this.signUpDetails = signUpDetails;
    this.signUpName = signUpName;
    this.signUpPoster = signUpPoster;
    this.signUpTime = signUpTime;
    this.signupButton = signupButton;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static EventSignUpPageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static EventSignUpPageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.event_sign_up_page, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static EventSignUpPageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.signUp_Details;
      TextView signUpDetails = ViewBindings.findChildViewById(rootView, id);
      if (signUpDetails == null) {
        break missingId;
      }

      id = R.id.signUp_Name;
      TextView signUpName = ViewBindings.findChildViewById(rootView, id);
      if (signUpName == null) {
        break missingId;
      }

      id = R.id.signUp_poster;
      ImageView signUpPoster = ViewBindings.findChildViewById(rootView, id);
      if (signUpPoster == null) {
        break missingId;
      }

      id = R.id.signUp_Time;
      TextView signUpTime = ViewBindings.findChildViewById(rootView, id);
      if (signUpTime == null) {
        break missingId;
      }

      id = R.id.signup_button;
      Button signupButton = ViewBindings.findChildViewById(rootView, id);
      if (signupButton == null) {
        break missingId;
      }

      return new EventSignUpPageBinding((ConstraintLayout) rootView, signUpDetails, signUpName,
          signUpPoster, signUpTime, signupButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}