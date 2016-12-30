package com.duy.databaseservice.custom_view;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Duy on 19/7/2016
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private android.hardware.Camera camera;
    private SurfaceHolder surfaceHolder;

    public CameraView(Context context, Camera camera) {
        super(context);
        this.camera = camera;

        this.camera.setDisplayOrientation(90);
        //get the holder and set this class as the callback, so we can get camera data here
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //before changing the application orientation, you need to stop the preview, rotate and then start it again
        if (surfaceHolder.getSurface() == null)//check if the surface is ready to receive camera data
            return;

        try {
            camera.stopPreview();
        } catch (Exception e) {
            //this will happen when you are trying the camera if it's not running
        }

        //now, recreate the camera preview
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//our app has only one screen, so we'll destroy the camera in the surface
        //if you are unsing with more screens, please move this code your activity
        try {
            camera.stopPreview();
            camera.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseCam() {
        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeCam() {
        try {
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
