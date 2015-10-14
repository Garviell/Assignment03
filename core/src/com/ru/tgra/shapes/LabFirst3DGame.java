package com.ru.tgra.shapes;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class LabFirst3DGame extends ApplicationAdapter implements InputProcessor {

    private int colorLoc;

    private Player player;
    private Camera orthoCam;
    private Shader shader;

    private Maze maze;

    private float deltaTime;
    private float angle;
    private float fov = 90.0f;
    private int thingsLostWhenDeathOccurs = 2;
    //private ModelMatrix modelMatrix;

    @Override
    public void create() {
        shader = new Shader();

        Gdx.input.setInputProcessor(this);

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

        BoxGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
        SphereGraphic.create(shader.getVertexPointer(), shader.getNormalPointer());
        SincGraphic.create(shader.getVertexPointer());
        CoordFrameGraphic.create(shader.getVertexPointer());

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        ModelMatrix.main = new ModelMatrix();
        ModelMatrix.main.loadIdentityMatrix();
        shader.setModelMatrix(ModelMatrix.main.getMatrix());

        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

        //OrthographicProjection3D(-2, 2, -2, 2, 1, 100);
        //PerspctiveProjection3D();

        //Camera
        player = new Player(fov);
        orthoCam = new Camera();
        orthoCam.orthographicProjection(-4, 4, -4, 4, 3.0f, 100);

        maze = new Maze();
    }

    private void input() {

    }

    private void update() {
        if (player.isAlive()) {
            deltaTime = Gdx.graphics.getDeltaTime();

            angle += 180.0f * deltaTime;
            player.update(deltaTime);
            maze.checkCollision(player, deltaTime);

        } else {
            player.flipAlive();
            loseThings();
            shader.setModelMatrix(ModelMatrix.main.getMatrix());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void display() {
        if (player.isAlive()) {
            //do all actual drawing and rendering here
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            for (int viewNum = 0; viewNum < 2; viewNum++) {
                if (viewNum == 0) {
                    player.display(shader);
                    drawFloor();
                    //drawCeiling();

                } else {
                    Gdx.gl.glViewport(0, 0, 200, 200);
                    orthoCam.look(new Point3D(player.camera.eye.x, 35.0f, player.camera.eye.z), player.camera.eye, new Vector3D(0, 0, -1));
                    shader.setViewMatrix(orthoCam.getViewMatrix());
                    shader.setProjectionMatrix(orthoCam.getProjectionMatrix());
                }

                ModelMatrix.main.loadIdentityMatrix();

                shader.setLightPosition(player.camera.eye.x, player.camera.eye.y, player.camera.eye.z, 1.0f);
//                shader.setLightPosition(0.0f, 0.0f, 0.0f, 1.0f);
                shader.setLightDiffuse(1.0f, 1.0f, 1.0f, 1.0f);


                ModelMatrix.main.pushMatrix();


                maze.display(shader, deltaTime);
                ModelMatrix.main.popMatrix();


                shader.setMaterialDiffuse(1.0f, 0, 0, 1.0f);

                maze.displayDoors(shader, deltaTime);
                
                player.score.display(shader, deltaTime, player.camera);


                // Mini-map
                if (viewNum == 1) {
                    player.displayMap(shader);
                }
            }
        } else {
            player.camera.look(new Point3D(-1.0f, 0.08f, -1.0f), new Point3D(0, 0.0f, 0), new Vector3D(0, 0.8f, 0));
            player.camera.perspectiveProjection(fov, 1.0f, 0.1f, 80.0f);
            shader.setViewMatrix(player.camera.getViewMatrix());
            shader.setProjectionMatrix(player.camera.getProjectionMatrix());
            Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        }
    }

    @Override
    public void render() {
        input();
        //put the code inside the update and display methods, depending on the nature of the code
        update();
        display();

    }

    public void drawFloor() {
    	shader.setMaterialDiffuse(0.333333f, 0.333333f, 0.333333f, 1);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(10.0f, -0.5f, 10.0f);
        ModelMatrix.main.addScale(23.0f, 0.01f, 23.0f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }

    public void drawCeiling() {
    	shader.setMaterialDiffuse(0.5f, 0.3f, 1.0f, 1.0f);
        ModelMatrix.main.pushMatrix();
        ModelMatrix.main.addTranslation(10.0f, 0.5f, 10.0f);
        ModelMatrix.main.addScale(20.2f, 0.1f, 20.2f);
        shader.setModelMatrix(ModelMatrix.main.getMatrix());
        BoxGraphic.drawSolidCube();
        ModelMatrix.main.popMatrix();
    }
    
    public void loseThings()
    {
    	for(int i = 0; i < thingsLostWhenDeathOccurs; i++)
        {
            if(player.score.numScore > 0)
            {
            	player.score.removething();
            }
        }
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