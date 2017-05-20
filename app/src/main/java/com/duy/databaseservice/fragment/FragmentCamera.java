package com.duy.databaseservice.fragment;

import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.duy.databaseservice.FirebaseListener;
import com.duy.databaseservice.R;
import com.duy.databaseservice.custom_view.CameraView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

class FragmentCamera extends Fragment {
    public static String TAG = FragmentCamera.class.getName();
    private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };
    FirebaseStorage storage;
    private Camera camera;
    private FirebaseListener firebaseListener;
    private CameraView mCameraView;
    private StorageReference storageRef;
    private StorageReference mountainsRef;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.v(TAG, "Getting output media file");
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.v(TAG, "Error creating output file");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    }
                });
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
            }
        }
    };

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        } else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if (!folder_gui.exists()) {
                Log.v(TAG, "Creating folder: " + folder_gui.getAbsolutePath());
                folder_gui.mkdirs();
            }
            String date = new Date().toString();
            File outFile = new File(folder_gui, "AAhome_" + date + ".jpg");
            Log.v(TAG, "Returnng file: " + outFile.getAbsolutePath());
            return outFile;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseListener = new FirebaseListener(getActivity().getApplicationContext());
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://smarthome-f6176.appspot.com");
        mountainsRef = storageRef.child(firebaseListener.getUid());
        settingService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_camera, container, false);
        try {
            camera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }

        if (camera != null) {
            mCameraView = new CameraView(getActivity().getApplicationContext(), camera);//create a SurfaceView to show camera data
            FrameLayout camera_view = (FrameLayout) view.findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);//add the SurfaceView to the layout
        }
        Button btnTakePicture = (Button) view.findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camera != null) {
                    try {
                        camera.takePicture(shutterCallback, null, mPicture);
                        Thread.sleep(1000);
                        camera.startPreview();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });

        return view;
    }

    private void settingService() {

        String url = "users/" + firebaseListener.getUid() + "/" + "camera/";
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference(url);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue(Boolean.class) != null) {
                    boolean b = dataSnapshot.getValue(Boolean.class);
                    if (b) {
                        db.setValue(false);
                        try {
                            camera.takePicture(shutterCallback, null, mPicture);
                            Thread.sleep(1000);
                            camera.startPreview();
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
