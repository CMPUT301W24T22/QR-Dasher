// Generated by view binder compiler. Do not edit!
package com.example.qr_dasher.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public final class ActivityCreateEventOrganizerBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button CreateEvent;

  @NonNull
  public final TextView CreateEventHeading;

  @NonNull
  public final EditText details;

  @NonNull
  public final Button displayQRcodes;

  @NonNull
  public final EditText eventName;

  @NonNull
  public final Button generatePromotionalQR;

  @NonNull
  public final Button generateQR;

  @NonNull
  public final ImageView promotionalQR;

  @NonNull
  public final ImageView qrCode;

  private ActivityCreateEventOrganizerBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button CreateEvent, @NonNull TextView CreateEventHeading, @NonNull EditText details,
      @NonNull Button displayQRcodes, @NonNull EditText eventName,
      @NonNull Button generatePromotionalQR, @NonNull Button generateQR,
      @NonNull ImageView promotionalQR, @NonNull ImageView qrCode) {
    this.rootView = rootView;
    this.CreateEvent = CreateEvent;
    this.CreateEventHeading = CreateEventHeading;
    this.details = details;
    this.displayQRcodes = displayQRcodes;
    this.eventName = eventName;
    this.generatePromotionalQR = generatePromotionalQR;
    this.generateQR = generateQR;
    this.promotionalQR = promotionalQR;
    this.qrCode = qrCode;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityCreateEventOrganizerBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityCreateEventOrganizerBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_create_event_organizer, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityCreateEventOrganizerBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.CreateEvent;
      Button CreateEvent = ViewBindings.findChildViewById(rootView, id);
      if (CreateEvent == null) {
        break missingId;
      }

      id = R.id.CreateEventHeading;
      TextView CreateEventHeading = ViewBindings.findChildViewById(rootView, id);
      if (CreateEventHeading == null) {
        break missingId;
      }

      id = R.id.details;
      EditText details = ViewBindings.findChildViewById(rootView, id);
      if (details == null) {
        break missingId;
      }

      id = R.id.displayQRcodes;
      Button displayQRcodes = ViewBindings.findChildViewById(rootView, id);
      if (displayQRcodes == null) {
        break missingId;
      }

      id = R.id.eventName;
      EditText eventName = ViewBindings.findChildViewById(rootView, id);
      if (eventName == null) {
        break missingId;
      }

      id = R.id.generatePromotionalQR;
      Button generatePromotionalQR = ViewBindings.findChildViewById(rootView, id);
      if (generatePromotionalQR == null) {
        break missingId;
      }

      id = R.id.generateQR;
      Button generateQR = ViewBindings.findChildViewById(rootView, id);
      if (generateQR == null) {
        break missingId;
      }

      id = R.id.promotionalQR;
      ImageView promotionalQR = ViewBindings.findChildViewById(rootView, id);
      if (promotionalQR == null) {
        break missingId;
      }

      id = R.id.qrCode;
      ImageView qrCode = ViewBindings.findChildViewById(rootView, id);
      if (qrCode == null) {
        break missingId;
      }

      return new ActivityCreateEventOrganizerBinding((ConstraintLayout) rootView, CreateEvent,
          CreateEventHeading, details, displayQRcodes, eventName, generatePromotionalQR, generateQR,
          promotionalQR, qrCode);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}