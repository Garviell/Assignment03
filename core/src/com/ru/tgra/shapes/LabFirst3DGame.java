package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;

import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.utils.BufferUtils;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {

	private FloatBuffer matrixBuffer;

	private int renderingProgramID;
	private int vertexShaderID;
	private int fragmentShaderID;

	private int positionLoc;
	private int normalLoc;

	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int projectionMatrixLoc;

	private int colorLoc;
	
	private Camera cam;
	private Camera orthoCam;
	
	private Maze maze;
	private ThingOne thingOne;
	
	private float deltaTime;
	private float angle;
	private float fov = 90.0f;

	//private ModelMatrix modelMatrix;

	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(this);

		String vertexShaderString;
		String fragmentShaderString;

		vertexShaderString = Gdx.files.internal("shaders/simple3D.vert").readString();
		fragmentShaderString =  Gdx.files.internal("shaders/simple3D.frag").readString();

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

		positionLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_position");
		Gdx.gl.glEnableVertexAttribArray(positionLoc);

		normalLoc				= Gdx.gl.glGetAttribLocation(renderingProgramID, "a_normal");
		Gdx.gl.glEnableVertexAttribArray(normalLoc);

		modelMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_modelMatrix");
		viewMatrixLoc			= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_viewMatrix");
		projectionMatrixLoc	= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_projectionMatrix");

		colorLoc				= Gdx.gl.glGetUniformLocation(renderingProgramID, "u_color");

		Gdx.gl.glUseProgram(renderingProgramID);

/*
		float[] mm = new float[16];

		mm[0] = 1.0f; mm[4] = 0.0f; mm[8] = 0.0f; mm[12] = 0.0f;
		mm[1] = 0.0f; mm[5] = 1.0f; mm[9] = 0.0f; mm[13] = 0.0f;
		mm[2] = 0.0f; mm[6] = 0.0f; mm[10] = 1.0f; mm[14] = 0.0f;
		mm[3] = 0.0f; mm[7] = 0.0f; mm[11] = 0.0f; mm[15] = 1.0f;

		modelMatrixBuffer = BufferUtils.newFloatBuffer(16);
		modelMatrixBuffer.put(mm);
		modelMatrixBuffer.rewind();

		Gdx.gl.glUniformMatrix4fv(modelMatrixLoc, 1, false, modelMatrixBuffer);
*/
		//COLOR IS SET HERE
		Gdx.gl.glUniform4f(colorLoc, 0.7f, 0.2f, 0, 1);

		BoxGraphic.create(positionLoc, normalLoc);
		SphereGraphic.create(positionLoc, normalLoc);
		SincGraphic.create(positionLoc);
		CoordFrameGraphic.create(positionLoc);

		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		ModelMatrix.main = new ModelMatrix();
		ModelMatrix.main.loadIdentityMatrix();
		ModelMatrix.main.setShaderMatrix(modelMatrixLoc);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		//OrthographicProjection3D(-2, 2, -2, 2, 1, 100);
		//PerspctiveProjection3D();

		//Camera
		cam = new Camera(viewMatrixLoc, projectionMatrixLoc);
		cam.look(new Point3D(-1.0f, 0.08f, -1.0f), new Point3D(0,0.0f,0), new Vector3D(0,0.8f,0));
		cam.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
		
		orthoCam = new Camera(viewMatrixLoc, projectionMatrixLoc);
		orthoCam.orthographicProjection(-4, 4, -4, 4, 3.0f, 100);
		
		maze = new Maze();
		thingOne = new ThingOne();
		
	}

	private void input()
	{
		
	}
	
	private void update()
	{
		if(ModelMatrix.main.alive)
		{
			deltaTime = Gdx.graphics.getDeltaTime();
	
			angle += 180.0f * deltaTime;
	
			//do all updates to the game
			
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				cam.yaw(-90.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				cam.yaw(90.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
				cam.pitch(-90.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				cam.pitch(90.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.A)) {
				cam.slide(-2.0f * deltaTime, 0, 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.D)) {
				cam.slide(2.0f * deltaTime, 0, 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.W)) {
				cam.slide(0, 0, -2.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.S)) {
				cam.slide(0, 0, 2.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.R)) {
				cam.slide(0, 3.0f * deltaTime, 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.F)) {
				cam.slide(0, -3.0f * deltaTime, 0);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
				cam.roll(-45.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.E)) {
				cam.roll(45.0f * deltaTime);
			}
			if(Gdx.input.isKeyPressed(Input.Keys.T)) {
				fov -= 30.0f * deltaTime;
			}
			if(Gdx.input.isKeyPressed(Input.Keys.G)) {
				fov += 30.0f * deltaTime;
			}
			
			maze.checkCollision(cam, deltaTime);
	
		}
		else
		{
			ModelMatrix.main.alive = true;
			ModelMatrix.main.setShaderMatrix();
			
			try {
				Thread.sleep(3000);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void display()
	{
		if(ModelMatrix.main.alive)
		{
			//do all actual drawing and rendering here
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			
			for(int viewNum = 0; viewNum < 2; viewNum++)
			{
				if(viewNum == 0)
				{
					Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
					cam.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
					cam.setShaderMatrices();
					drawFloor();
//					drawCeeling();
					
				}
				else
				{
					Gdx.gl.glViewport(0, 0, 200, 200);
					orthoCam.look(new Point3D(cam.eye.x, 20.0f, cam.eye.z), cam.eye, new Vector3D(0,0,-1));
					orthoCam.setShaderMatrices();
				}
	
				ModelMatrix.main.loadIdentityMatrix();
				
				
				ModelMatrix.main.pushMatrix();
				
				
				maze.draw(colorLoc, deltaTime);
				ModelMatrix.main.popMatrix();
				
				thingOne.display(colorLoc, deltaTime);
				
				Gdx.gl.glUniform4f(colorLoc, 1.0f, 0, 0, 1.0f);
				
				maze.displayDoors(colorLoc, deltaTime);
				
				
				// Mini-map
				if(viewNum == 1)
				{
					Gdx.gl.glUniform4f(colorLoc, 1.0f, 0.3f, 0.1f, 1.0f);
					
					ModelMatrix.main.pushMatrix();
					ModelMatrix.main.addTranslation(cam.eye.x, cam.eye.y, cam.eye.z);
					ModelMatrix.main.addScale(0.25f, 0.25f, 0.25f);
					ModelMatrix.main.setShaderMatrix();
					SphereGraphic.drawSolidSphere();
					ModelMatrix.main.popMatrix();
				}
			}
		}
		else
		{
			cam.look(new Point3D(-1.0f, 0.08f, -1.0f), new Point3D(0,0.0f,0), new Vector3D(0,0.8f,0));
			cam.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
			cam.setShaderMatrices();

			Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		}
	}

	@Override
	public void render () {
		
		input();
		//put the code inside the update and display methods, depending on the nature of the code
		update();
		display();

	}
	
	public void drawFloor()
	{
		Gdx.gl.glUniform4f(colorLoc, 0.333333f, 0.333333f, 0.333333f, 1.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(10.0f, -0.5f, 10.0f);
		ModelMatrix.main.addScale(23.0f, 0.01f, 23.0f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}
	
	public void drawCeiling()
	{
		Gdx.gl.glUniform4f(colorLoc, 0.5f, 0.3f, 1.0f, 1.0f);
		ModelMatrix.main.pushMatrix();
		ModelMatrix.main.addTranslation(10.0f, 0.5f, 10.0f);
		ModelMatrix.main.addScale(20.2f, 0.1f, 20.2f);
		ModelMatrix.main.setShaderMatrix();
		BoxGraphic.drawSolidCube();
		ModelMatrix.main.popMatrix();
	}



	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}


}