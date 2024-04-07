package com.example.qr_dasher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.ContextWrapper;

import java.io.ByteArrayOutputStream;


public class ShareQRFragment extends DialogFragment {



    private Button shareQRbutton, sharePromoQRbutton;
    private Bitmap attendeeQRcode, promoQRcode;
    private Boolean twoQRcodes;
    private String eventName;
    private static final String QR_BITMAP1 = "arg_qr_bitmap";
    private static final String QR_BITMAP2 = "arg_qr_bitmap2";
    private static final String ARG_TWO_QR_CODES = "arg_two_qr_codes";
    private static final String ARG_EVENT_NAME = "arg_event_name";


    public static ShareQRFragment newInstance(Bitmap qrCodeBitmap, String eventName) {
        ShareQRFragment fragment = new ShareQRFragment();
        Bundle args = new Bundle();
        args.putParcelable(QR_BITMAP1, qrCodeBitmap);
        args.putBoolean(ARG_TWO_QR_CODES, false);
        args.putString(ARG_EVENT_NAME,eventName);

        fragment.setArguments(args);
        return fragment;
    }


    public static ShareQRFragment newInstance(Bitmap qrCodeBitmap1, Bitmap qrCodeBitmap2,String eventName) {
        ShareQRFragment fragment = new ShareQRFragment();
        Bundle args = new Bundle();
        args.putParcelable(QR_BITMAP1, qrCodeBitmap1);
        args.putParcelable(QR_BITMAP2, qrCodeBitmap2);
        args.putBoolean(ARG_TWO_QR_CODES, true);
        args.putString(ARG_EVENT_NAME,eventName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_share_qr, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        attendeeQRcode = getArguments().getParcelable(QR_BITMAP1);

        twoQRcodes = getArguments().getBoolean(ARG_TWO_QR_CODES);

        eventName = getArguments().getString(ARG_EVENT_NAME);


        shareQRbutton = view.findViewById(R.id.share_qr);
        sharePromoQRbutton = view.findViewById(R.id.share_qr_promotional);

        if (!twoQRcodes){
            sharePromoQRbutton.setVisibility(View.GONE);

        }
        else{
            promoQRcode = getArguments().getParcelable(QR_BITMAP2);
            sharePromoQRbutton.setVisibility(View.VISIBLE);
        }

        shareQRbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(attendeeQRcode);

            }
        });

        sharePromoQRbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(promoQRcode);

            }
        });


    }
    private void shareImage(Bitmap bitmap) {
        if (bitmap != null) {
            Uri uri = bitmapToUri(bitmap);
            if (uri != null) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        }
    }

    private Uri bitmapToUri(Bitmap bitmap) {
        if (getContext() != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(requireContext().getContentResolver(), bitmap,eventName , null);
            return Uri.parse(path);
        }
        return null;
    }
    public void showFragment(FragmentManager fragmentManager) {
        if (isAdded()) {
            return;
        }
        show(fragmentManager, "ShareQRFragment");
    }

}
