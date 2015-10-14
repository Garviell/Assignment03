package com.ru.tgra.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.nio.FloatBuffer;

/**
 * Created by andri on 10/13/15.
 */
public class Shader {
    private int renderingProgramID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int positionLoc;
    private int normalLoc;
    private int lightPosLoc;
    private int lightDirLoc;
    private int lightFocLoc;
    private int lightDifLoc;
    private int materialDifLoc;
    private int materialShineLoc;
    private int materialSpecularLoc;
    private int materialEmiLoc;
    private int globalAttLoc;
    private int linearAttLoc;
    private int quadraticAttLoc;

    private int modelMatrixLoc;
    private int viewMatrixLoc;
    private int projectionMatrixLoc;
    private int globalAmbientLoc;

    private int eyePosLoc;

    Shader(){
        String vertexShaderString;
        String fragmentShaderString;

        vertexShaderString = Gdx.files.internal("shaders/vertexLighting.vert").readString();
        fragmentShaderString = Gdx.files.internal("shaders/vertexLighting.frag").readString();

        vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
        Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

        Gdx.gl.glCompileShader(vertexShaderID);
        Gdx.gl.glCompileShader(fragmentShaderID);

        renderingProgramID = Gdx.gl.glCreateProgram();

        Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
        Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);

        Gdx.gl.glLinkProgram(renderingProgramID);

        positionLoc = Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
        Gdx.gl.glEnableVertexAttribArray(positionLoc);

        normalLoc = Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
        Gdx.gl.glEnableVertexAttribArray(normalLoc);

        modelMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
        viewMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
        projectionMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

        eyePosLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");
        lightPosLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition");
        lightDifLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDiffuse");
        materialDifLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
        materialShineLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");
        materialSpecularLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
        globalAmbientLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");
        materialEmiLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");
        lightDirLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDirection");
        lightFocLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightFocus");
        globalAttLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAttenuation");
        linearAttLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_linearAttenuation");
        quadraticAttLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_quadraticAttenuation");

        Gdx.gl.glUseProgram(renderingProgramID);
    }

    public void setMaterialDiffuse(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialDifLoc, r, g, b, a);
    }



    public void setLightPosition(float x, float y, float z, float w){
        Gdx.gl.glUniform4f(lightPosLoc, x, y, z, w);
    }

    public void setLightDirection(float x, float y, float z, float w){
        Gdx.gl.glUniform4f(lightDirLoc, x, y, z, w);
    }

    public void setFocus(float shine){
        Gdx.gl.glUniform1f(lightFocLoc, shine);
    }

    public void setGlobalAtt(float shine){
        Gdx.gl.glUniform1f(globalAttLoc, shine);
    }

    public void setLinearAtt(float shine){
        Gdx.gl.glUniform1f(linearAttLoc, shine);
    }

    public void setQuadraticAtt(float shine){
        Gdx.gl.glUniform1f(quadraticAttLoc, shine);
    }

    public void setMaterialEmission(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialEmiLoc, r, g, b, a);
    }

    public void setEyePosition(float x, float y, float z, float w){
        Gdx.gl.glUniform4f(eyePosLoc, x, y, z, w);
    }

    public void setLightDiffuse(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(lightDifLoc, r, g, b, a);
    }

    public void setMaterialSpecular(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialSpecularLoc, r, g, b, a);
    }

    public void setGlobalAmbient(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(globalAmbientLoc, r, g, b, a);
    }

    public void setShininess(float shine){
        Gdx.gl.glUniform1f(materialShineLoc, shine);
    }

    public int getVertexPointer(){
        return positionLoc;
    }

    public int getNormalPointer(){
        return normalLoc;
    }

    public void setModelMatrix(FloatBuffer matrix){
        Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, matrix);
    }


    public void setViewMatrix(FloatBuffer matrix){
        Gdx.gl.glUniformMatrix4fv(viewMatrixLoc, 1, false, matrix);
    }


    public void setProjectionMatrix(FloatBuffer matrix){
        Gdx.gl.glUniformMatrix4fv(projectionMatrixLoc, 1, false, matrix);
    }
}
