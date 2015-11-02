package com.ru.tgra.mazerunner.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.nio.FloatBuffer;


public class Shader {
    private int lc;
    private int renderingProgramID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private int positionLoc;
    private int normalLoc;
    private int uvLoc;
    private int globalAmbientLoc;
    private int eyePosLoc;
    private boolean usesDiffuseTexture = false;
    private int usesDiffuseTexLoc;
    private int diffuseTextureLoc;

    private int lightPosLoc[];
    private int lightDirLoc[];
    private int lightFocLoc[];
    private int lightDifLoc[];
    private int constantAttLoc[];
    private int linearAttLoc[];
    private int quadraticAttLoc[];

    private int materialDifLoc;
    private int materialShineLoc;
    private int materialSpecularLoc;
    private int materialEmiLoc;

    private int modelMatrixLoc;
    private int viewMatrixLoc;
    private int projectionMatrixLoc;


    /*
     * Initializes the vert and fragment shader.  We are mainly using the fragment shader for the light though.
     */
    public Shader(){
        //Arrays to have multiple lights without needing to add functions
        lc = 7;
        lightPosLoc = new int[lc];
        lightDirLoc = new int[lc];
        lightFocLoc = new int[lc];
        lightDifLoc = new int[lc];
        constantAttLoc = new int[lc];
        linearAttLoc = new int[lc];
        quadraticAttLoc = new int[lc];
        String vertexShaderString;
        String fragmentShaderString;

        vertexShaderString = Gdx.files.internal("shaders/fragmentLighting3D.vert").readString();
        fragmentShaderString = Gdx.files.internal("shaders/fragmentLighting3D.frag").readString();

        vertexShaderID = Gdx.gl.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderID = Gdx.gl.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        Gdx.gl.glShaderSource(vertexShaderID, vertexShaderString);
        Gdx.gl.glShaderSource(fragmentShaderID, fragmentShaderString);

        Gdx.gl.glCompileShader(vertexShaderID);
        Gdx.gl.glCompileShader(fragmentShaderID);
		System.out.println("Vertex shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(vertexShaderID));
		System.out.println("Fragment shader compile messages:");
		System.out.println(Gdx.gl.glGetShaderInfoLog(fragmentShaderID));
        Gdx.gl.glGetError();	// Use glGetShadeGetInfoLoc for more detailed errors.

        renderingProgramID = Gdx.gl.glCreateProgram();

        Gdx.gl.glAttachShader(renderingProgramID, vertexShaderID);
        Gdx.gl.glAttachShader(renderingProgramID, fragmentShaderID);

        Gdx.gl.glLinkProgram(renderingProgramID);

        positionLoc = Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
        Gdx.gl.glEnableVertexAttribArray(positionLoc);

        normalLoc = Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
        Gdx.gl.glEnableVertexAttribArray(normalLoc);
        uvLoc = Gdx.gl.glGetAttribLocation(renderingProgramID, "a_uv");
        Gdx.gl.glEnableVertexAttribArray(uvLoc);


        modelMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
        viewMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");

        projectionMatrixLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

        usesDiffuseTexLoc       = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_usesDiffuseTexture");
        diffuseTextureLoc       = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_diffuseTexture");
        eyePosLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_eyePosition");
        materialDifLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialDiffuse");
        materialShineLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialShininess");
        materialSpecularLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialSpecular");
        globalAmbientLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_globalAmbient");
        materialEmiLoc = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_materialEmission");
        for (int i = 0; i < lc; ++i){
            lightPosLoc[i] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightPosition[" + i + "]");
            lightDifLoc[i] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDiffuse[" + i + "]");
            lightDirLoc[i] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightDirection[" + i + "]");
            lightFocLoc[i] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_lightFocus[" + i + "]");
            constantAttLoc[i] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_constantAttenuation[" + i + "]");
            linearAttLoc[i] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_linearAttenuation[" + i + "]");
            quadraticAttLoc[i] = Gdx.gl.glGetUniformLocation(renderingProgramID, "u_quadraticAttenuation[" + i + "]");

        }
        Gdx.gl.glUseProgram(renderingProgramID);
        Gdx.gl.glUniform1f(usesDiffuseTexLoc, 0.0f);
    }

        public void setDiffuseTexture(Texture tex)
    {
        if(tex == null)
        {
            Gdx.gl.glUniform1f(usesDiffuseTexLoc, 0.0f);
            Gdx.gl.glUniform1i(diffuseTextureLoc, 0);
            usesDiffuseTexture = false;
        }
        else
        {
            tex.bind(0);
            Gdx.gl.glUniform1i(diffuseTextureLoc, 0);
            Gdx.gl.glUniform1f(usesDiffuseTexLoc, 1.0f);
            usesDiffuseTexture = true;

            Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_S, GL20.GL_REPEAT);
            Gdx.gl.glTexParameteri(GL20.GL_TEXTURE_2D, GL20.GL_TEXTURE_WRAP_T, GL20.GL_REPEAT);
        }
    }

    public boolean usesTextures()
    {
        return (usesDiffuseTexture/* || usesSpecularTexture ... etc.*/);
    }
    public void setEyePosition(float x, float y, float z, float w){
        Gdx.gl.glUniform4f(eyePosLoc, x, y, z, w);
    }

    public void setShininess(float shine){
        Gdx.gl.glUniform1f(materialShineLoc, shine);
    }

    public void setMaterialSpecular(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialSpecularLoc, r, g, b, a);
    }

    public void setGlobalAmbient(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(globalAmbientLoc, r, g, b, a);
    }

    public void setMaterialDiffuse(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialDifLoc, r, g, b, a);
    }

    public void setMaterialEmission(float r, float g, float b, float a){
        Gdx.gl.glUniform4f(materialEmiLoc, r, g, b, a);
    }

    public void setLightPosition(float x, float y, float z, float w, int num){
        Gdx.gl.glUniform4f(lightPosLoc[num], x, y, z, w);
    }

    public void setLightDirection(float x, float y, float z, float w, int num){
        Gdx.gl.glUniform4f(lightDirLoc[num], x, y, z, w);
    }

    public void setFocus(float focus, int num){
        Gdx.gl.glUniform1f(lightFocLoc[num], focus);
    }

    public void setConstantAtt(float att, int num){
        Gdx.gl.glUniform1f(constantAttLoc[num], att);
    }

    public void setLinearAtt(float att, int num){
        Gdx.gl.glUniform1f(linearAttLoc[num], att);
    }

    public void setQuadraticAtt(float att, int num){
        Gdx.gl.glUniform1f(quadraticAttLoc[num], att);
    }

    public void setLightDiffuse(float r, float g, float b, float a, int num){
        Gdx.gl.glUniform4f(lightDifLoc[num], r, g, b, a);
    }

    public int getVertexPointer(){
        return positionLoc;
    }

    public int getNormalPointer(){
        return normalLoc;
    }

    public int getUVPointer(){
        return uvLoc;
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
