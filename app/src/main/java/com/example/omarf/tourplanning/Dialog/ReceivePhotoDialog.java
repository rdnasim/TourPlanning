package com.example.omarf.tourplanning.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.omarf.tourplanning.FirebaseHelper;
import com.example.omarf.tourplanning.Model.Event;
import com.example.omarf.tourplanning.Model.EventPic;
import com.example.omarf.tourplanning.R;
import com.example.omarf.tourplanning.databinding.ReceivePhotoDialogBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.STORAGE_SERVICE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

/**
 * Created by omarf on 2/13/2017.
 */

public class ReceivePhotoDialog extends DialogFragment {
    private static final String TAG = "receive_dialog";
    private String mCurrentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ReceivePhotoDialogBinding mBinding;
    private Uri mPhotoUri;
    private String mEventKey;
    private  DatabaseReference mShowingDataRef;
    private EventPic mShowingEventPic;


    public void setmEventKey(String mEventKey) {
        this.mEventKey = mEventKey;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.receive_photo_dialog,
                null, false);



        if (mShowingEventPic!=null){
            mBinding.momentDetailsEditText.setText(mShowingEventPic.getPicDetails());
            Picasso.with(getActivity()).load(mShowingEventPic.getPicUrl()).into(mBinding.photoCaptureImageView);
            return new AlertDialog.Builder(getActivity())
                    .setView(mBinding.getRoot())
                    .setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mShowingDataRef.setValue(null);
                        }
                    })
                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String details=mBinding.momentDetailsEditText.getText().toString();
                            if (details.isEmpty()){
                                details=" ";
                            }
                          mShowingDataRef.child("picDetails").setValue(details);
                        }
                    })
                    .create();
        }
        else {
            captureImage();
            return new AlertDialog.Builder(getActivity())
                    .setView(mBinding.getRoot())
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uploadPictoFirebase();
                        }
                    })
                    .create();
        }


        }




    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile=null;

            try {

                photoFile=createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile!=null){
                 mPhotoUri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,mPhotoUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }



        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
/*
            //    Bundle extra = data.getExtras();
                Log.i(TAG,mCurrentPhotoPath);
                Uri uri= Uri.parse(mCurrentPhotoPath);
                Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                if (imageBitmap==null)
                    Toast.makeText(getActivity(), "sorry", Toast.LENGTH_SHORT).show();
                mBinding.photoCaptureImageView.setImageBitmap(imageBitmap);*/

//                Picasso.with(getActivity()).load(uri).into(mBinding.photoCaptureImageView);

                setPic();

            }
        }
    }

    private void uploadPictoFirebase() {
        Uri fileUri= Uri.fromFile(new File(mCurrentPhotoPath));
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userEmail= user.getEmail();
        StorageReference reference= FirebaseHelper.getReference().child(userEmail).child(fileUri.getLastPathSegment());
        UploadTask uploadTask=reference.putFile(fileUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri=taskSnapshot.getDownloadUrl();
                String photoDetails=mBinding.momentDetailsEditText.getText().toString();
                if (photoDetails.isEmpty())
                {
                  photoDetails=" ";
                }
                Log.i(TAG,downloadUri.toString());
               // String userId=user.getUid();

                DatabaseReference dbRef= FirebaseDatabase.getInstance()
                        .getReference(FirebaseHelper.EVENT_PIC_REF)
                        .child(mEventKey);

                EventPic eventPic=new EventPic(downloadUri.toString(),photoDetails);
                 dbRef.push().setValue(eventPic);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                Log.i(TAG,"Failed");
            }
        });
    }

    private File createImageFile() throws IOException {
        String timeStapm = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG_" + timeStapm + "_";
        File storageDir=getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File newImage=File.createTempFile(fileName,".jpg",storageDir);
        mCurrentPhotoPath=newImage.getAbsolutePath();
        return  newImage;
    }


    public void setPic(){
        int targetW=mBinding.photoCaptureImageView.getWidth();
        int targetH=mBinding.photoCaptureImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mBinding.photoCaptureImageView.setImageBitmap(bitmap);

    }

    public void setEventPic(EventPic eventPic, DatabaseReference reference) {
        mShowingEventPic=eventPic;
        mShowingDataRef=reference;
    }
}
