package com.example.qr_dasher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ImageUploadFragment extends DialogFragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private Button captureImageButton, selectImageButton;
    private Bitmap capturedImageBitmap;
    private ImageUploadListener imageUploadListener;

    public interface ImageUploadListener {
        void onImageUpload(Bitmap imageBitmap);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        captureImageButton = view.findViewById(R.id.capture_image_button);
        selectImageButton = view.findViewById(R.id.select_image_button);

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchSelectImageIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchSelectImageIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(pickPhotoIntent, "Select Picture"), REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                capturedImageBitmap = (Bitmap) extras.get("data");
                if (imageUploadListener != null) {
                    imageUploadListener.onImageUpload(capturedImageBitmap);
                }
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                try {
                    capturedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    if (imageUploadListener != null) {
                        imageUploadListener.onImageUpload(capturedImageBitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setImageUploadListener(ImageUploadListener listener) {
        this.imageUploadListener = listener;
    }
}
