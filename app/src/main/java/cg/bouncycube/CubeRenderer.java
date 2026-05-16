package cg.bouncycube;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.lang.Math;

public class CubeRenderer implements GLSurfaceView.Renderer {
    private Cube mCube;

    private float mBackgroundTimer = 0.0f;
    private float mTransY = 0.0f;
    private float mAngle = 0.0f;

    public CubeRenderer() {
        mCube = new Cube();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Fundalul își va schimba nuanțele pe măsură ce timpul (mBackgroundTimer) trece
        float redBg = (float) Math.sin(mBackgroundTimer) * 0.5f + 0.5f;
        float greenBg = (float) Math.cos(mBackgroundTimer * 0.7f) * 0.5f + 0.5f;
        gl.glClearColor(redBg * 0.3f, greenBg * 0.3f, 0.4f, 1.0f); // Nuanțe ambientale plăcute
        mBackgroundTimer += 0.02f;
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        // 1. MAI ÎNTÂI TRANSLAȚIA (Mută cubul pe Y și Z)
        gl.glTranslatef(0.0f, (float) Math.sin(mTransY) * 0.4f, -20.0f);

        // 2. APOI ROTAȚIILE (Îl învârt pe axa lui proprie)
        gl.glRotatef(mAngle, 0.2f, 1.0f, 0.0f);      // Rotație pe Y cu viteză normală
        gl.glRotatef(mAngle * 1.5f, 1.0f, 0.0f, 0.3f); // Rotație pe X mai rapidă + înclinare pe Z

        mTransY += 0.075f;
        mAngle += 0.8f;

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mCube.draw(gl);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        // Calcule avansate pentru Frustum (Câmpul de vizualizare 3D)
        //float fieldOfView = 30.0f / 57.3f; // 30 grade în radiani
        float fieldOfView = 10.0f / 57.3f; // Schimbat din 30 în 10
        float aspectRatio = (float) width / (float) height;
        float zNear = 0.1f;
        float zFar = 1000.0f;
        float size = zNear * (float) (Math.tan((double) (fieldOfView / 2.0f)));

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-size, size, -size / aspectRatio, size / aspectRatio, zNear, zFar);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        gl.glClearColor(0, 0, 0, 0); // Fundal negru
        gl.glEnable(GL10.GL_CULL_FACE);   // Ascunde fețele din spate (optimizare)
        gl.glCullFace(GL10.GL_FRONT);
        gl.glShadeModel(GL10.GL_SMOOTH);  // Culori fluide între puncte
        gl.glEnable(GL10.GL_DEPTH_TEST);  // Testul de adâncime (Z-buffer)
    }
}