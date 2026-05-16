package cg.bouncycube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class Cube {
    private FloatBuffer mFVertexBuffer;
    private ByteBuffer mColorBuffer;
    private ByteBuffer mTFan1;
    private ByteBuffer mTFan2;

    public Cube() {
        // Cele 8 puncte ale cubului (X, Y, Z)
        float vertices[] = {
                -1.0f,  1.0f,  1.0f,
                1.0f,  1.0f,  1.0f,
                1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,
                -1.0f,  1.0f, -1.0f,
                1.0f,  1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f
        };

        // Culori RGBA pentru fiecare din cele 8 puncte
        byte maxColor = (byte) 255;
        byte colors[] = {
                maxColor, 0,        0,        maxColor, // Colț 0: Roșu
                0,        maxColor, 0,        maxColor, // Colț 1: Verde
                0,        0,        maxColor, // Colț 2: Albastru
                maxColor, maxColor, 0,        maxColor, // Colț 3: Galben
                0,        maxColor, maxColor, maxColor, // Colț 4: Cyan
                maxColor, 0,        maxColor, maxColor, // Colț 5: Magenta
                maxColor, maxColor, maxColor, maxColor, // Colț 6: Alb
                (byte)128, (byte)128, (byte)128, maxColor  // Colț 7: Gri
        };

        // Primul grup de fețe (Triangle Fan 1)
        byte tFan1[] = {
                1,0,3,  1,3,2,  1,2,6,  1,6,5,  1,5,4,  1,4,0
        };

        // Al doilea grup de fețe (Triangle Fan 2)
        byte tFan2[] = {
                7,4,5,  7,5,6,  7,6,2,  7,2,3,  7,3,0,  7,0,4
        };

        // Alocare buffers de memorie nativă pentru GPU
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mFVertexBuffer = vbb.asFloatBuffer();
        mFVertexBuffer.put(vertices);
        mFVertexBuffer.position(0);

        mColorBuffer = ByteBuffer.allocateDirect(colors.length);
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mTFan1 = ByteBuffer.allocateDirect(tFan1.length);
        mTFan1.put(tFan1);
        mTFan1.position(0);

        mTFan2 = ByteBuffer.allocateDirect(tFan2.length);
        mTFan2.put(tFan2);
        mTFan2.position(0);
    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL11.GL_CW);
        gl.glVertexPointer(3, GL11.GL_FLOAT, 0, mFVertexBuffer); // 3 coordonate (X,Y,Z)
        gl.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 0, mColorBuffer);

        // Desenăm cele două grupuri de fețe texturate/colorate
        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 6 * 3, GL10.GL_UNSIGNED_BYTE, mTFan1);
        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, 6 * 3, GL10.GL_UNSIGNED_BYTE, mTFan2);
    }
}