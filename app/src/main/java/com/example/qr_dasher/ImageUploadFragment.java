package com.example.qr_dasher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
/**
 * A DialogFragment for uploading images from either the device's camera or gallery.
 * Provides options to capture a new image or select an existing image.
 */

public class ImageUploadFragment extends DialogFragment {
   /**
    * Constant for requesting image capture.
    */
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    /**
     * Constant for requesting image selection from gallery.
     */
    private static final int REQUEST_IMAGE_PICK = 2;

    private Button captureImageButton, selectImageButton, deleteImageButton;
    private Bitmap capturedImageBitmap;
    private ImageUploadListener imageUploadListener;
    /**
     * Interface for communicating image upload events to the hosting activity.
     */
    public interface ImageUploadListener {
        /**
         * Callback method triggered when an image is uploaded.
         *
         * @param imageBitmap The uploaded image bitmap.
         */
        void onImageUpload(Bitmap imageBitmap);
    }
    /**
     * Creates the view of the ImageUploadFragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState This fragment's previously saved state, or null if it has no saved state.
     * @return The inflated view.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_upload, container, false);
    }
    /**
     * Initializes the view components and sets up click listeners.
     *
     * @param view               The view returned by onCreateView().
     * @param savedInstanceState This fragment's previously saved state, or null if it has no saved state.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        captureImageButton = view.findViewById(R.id.capture_image_button);
        selectImageButton = view.findViewById(R.id.select_image_button);
        deleteImageButton = view.findViewById(R.id.delete_image_button);

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

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });
    }
    /**
     * Deletes the currently selected image.
     */
    private void deleteImage(){
        capturedImageBitmap = null;
        if (imageUploadListener != null) {
            imageUploadListener.onImageUpload(null); // Passing null to indicate no image
        }
    }
    /**
     * Initiates a request to capture an image using the device's camera.
     */
    private void dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            // Permission is already granted, start the camera intent
            startCameraIntent();
        }
    }

    // Constant for camera permission request
    private static final int REQUEST_CAMERA_PERMISSION = 101;

    // Start camera intent
    /**
     * Start camera intent
     */
    private void startCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Handle permission request result
    /**
     * Handle permission request result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start the camera intent
                startCameraIntent();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(requireContext(), "Camera permission is required to capture images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Initiates a request to select an image from the device's gallery.
     */
    private void dispatchSelectImageIntent() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(pickPhotoIntent, "Select Picture"), REQUEST_IMAGE_PICK);
    }
    /**
     * Handles the result of image capture or selection.
     *
     * @param requestCode The request code passed to startActivityForResult().
     * @param resultCode  The result code returned by the child activity.
     * @param data        The intent data returned by the child activity.
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                capturedImageBitmap = (Bitmap) extras.get("data");
                if (capturedImageBitmap != null) {
                    capturedImageBitmap = Picture.cropToCircle(capturedImageBitmap);
                    if (imageUploadListener != null) {
                        imageUploadListener.onImageUpload(capturedImageBitmap);
                    }
                }
            } else if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                try {
                    capturedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    if (capturedImageBitmap != null) {
                        capturedImageBitmap = Picture.cropToCircle(capturedImageBitmap);
                        if (imageUploadListener != null) {
                            imageUploadListener.onImageUpload(capturedImageBitmap);
                        }
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
