// Generated by view binder compiler. Do not edit!
package com.example.qr_dasher.databinding;

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
import com.example.qr_dasher.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SendNotificationBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText notificationContent;

  @NonNull
  public final TextView notificationHeading;

  @NonNull
  public final TextView notificationTitle;

  @NonNull
  public final EditText notificationTitleText;

  @NonNull
  public final Button sendNotificationsButton;

  @NonNull
  public final TextView sendText;

  @NonNull
  public final View view;

  private SendNotificationBinding(@NonNull ConstraintLayout rootView,
      @NonNull EditText notificationContent, @NonNull TextView notificationHeading,
      @NonNull TextView notificationTitle, @NonNull EditText notificationTitleText,
      @NonNull Button sendNotificationsButton, @NonNull TextView sendText, @NonNull View view) {
    this.rootView = rootView;
    this.notificationContent = notificationContent;
    this.notificationHeading = notificationHeading;
    this.notificationTitle = notificationTitle;
    this.notificationTitleText = notificationTitleText;
    this.sendNotificationsButton = sendNotificationsButton;
    this.sendText = sendText;
    this.view = view;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SendNotificationBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SendNotificationBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.send_notification, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SendNotificationBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.notification_content;
      EditText notificationContent = ViewBindings.findChildViewById(rootView, id);
      if (notificationContent == null) {
        break missingId;
      }

      id = R.id.notification_heading;
      TextView notificationHeading = ViewBindings.findChildViewById(rootView, id);
      if (notificationHeading == null) {
        break missingId;
      }

      id = R.id.notification_title;
      TextView notificationTitle = ViewBindings.findChildViewById(rootView, id);
      if (notificationTitle == null) {
        break missingId;
      }

      id = R.id.notification_title_text;
      EditText notificationTitleText = ViewBindings.findChildViewById(rootView, id);
      if (notificationTitleText == null) {
        break missingId;
      }

      id = R.id.send_notifications_button;
      Button sendNotificationsButton = ViewBindings.findChildViewById(rootView, id);
      if (sendNotificationsButton == null) {
        break missingId;
      }

      id = R.id.send_text;
      TextView sendText = ViewBindings.findChildViewById(rootView, id);
      if (sendText == null) {
        break missingId;
      }

      id = R.id.view;
      View view = ViewBindings.findChildViewById(rootView, id);
      if (view == null) {
        break missingId;
      }

      return new SendNotificationBinding((ConstraintLayout) rootView, notificationContent,
          notificationHeading, notificationTitle, notificationTitleText, sendNotificationsButton,
          sendText, view);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}