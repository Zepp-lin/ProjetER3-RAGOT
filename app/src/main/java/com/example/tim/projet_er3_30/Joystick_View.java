package com.example.tim.projet_er3_30;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


public class Joystick_View extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    //Declarations
    private float thirdX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;

    private void setupDimensions() {    //Presetting the dimensions of the Joystick
        thirdX = getWidth() - (getWidth() / 3);
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 4;
        hatRadius = Math.min(getWidth(), getHeight()) / 6;
    }

    public Joystick_View(Context context) {

        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public Joystick_View(Context context, AttributeSet attributes, int style) {

        super(context, attributes, style);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    public Joystick_View(Context context, AttributeSet attributes) {

        super(context, attributes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    private void drawJoystick(float newX, float newY) {

        if(getHolder().getSurface().isValid()) {

            Canvas myCanvas = this.getHolder().lockCanvas(); //Stuff to draw
            Paint colors = new Paint();
            myCanvas.drawARGB(255, 100, 100, 100); // Clear the background

            //Draw the base
            colors.setARGB(255, 80, 80, 255); //Set the base color
            myCanvas.drawCircle(thirdX, centerY, baseRadius, colors); //Size and position of the base

            //Draw the joystick hat
            colors.setARGB(255, 120, 120, 255); //Change the joystick hat color
            myCanvas.drawCircle(newX, newY, hatRadius , colors); //Draw the hat

            getHolder().unlockCanvasAndPost(myCanvas); //Write the new drawing to the SurfaceView
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setupDimensions();
        drawJoystick(thirdX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }


    public boolean onTouch(View v, MotionEvent e) {

        if(v.equals(this)) {

            if(e.getAction() != e.ACTION_UP) {

                float displacement = (float) Math.sqrt((Math.pow(e.getX() - thirdX, 2)) + Math.pow(e.getY() - centerY, 2));

                if(displacement < baseRadius) {

                    drawJoystick(e.getX(), e.getY());
                    joystickCallback.onJoystickMoved((e.getX() - thirdX)/baseRadius, (e.getY() - centerY)/baseRadius, getId());
                }
                else {

                    float ratio = baseRadius / displacement;
                    float constrainedX = thirdX + (e.getX() - thirdX) * ratio;
                    float constrainedY = centerY + (e.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((constrainedX-thirdX)/baseRadius, (constrainedY-centerY)/baseRadius, getId());
                }
            }
            else {

                drawJoystick(thirdX, centerY);
                joystickCallback.onJoystickMoved(0,0,getId());
            }
        }

        return true;
    }

    public interface JoystickListener {

        void onJoystickMoved(float xPercent, float yPercent, int id);
    }


}
