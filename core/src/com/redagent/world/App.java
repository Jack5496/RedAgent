/// Copyright Ian Parberry, September 2013.
///
/// This file is made available under the GNU All-Permissive License.
///
/// Copying and distribution of this file, with or without modification,
/// are permitted in any medium without royalty provided the copyright
/// notice and this notice are preserved.  This file is offered as-is,
/// without any warranty.
///
/// Created by Ian Parberry, September 2013.
/// Demo by Pablo Nuñez.
/// Last updated January 31, 2014.

package amortized2dnoise;

//import aurelienribon.accessors.SpriteAccessor;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class App extends ApplicationAdapter implements InputProcessor {
	
	/**********************************************************************************
	 * Change this values for adjustments 
	**********************************************************************************/
	
	//Size of each cell
	public static int cellSize = 1024; 	
	
	//Number of cells
	public static int worldSize = 100; 	
	
	//Alter this value for different results
	public static int octave0 = 2; 		
	public static int octave1 = 8; 		

	//Altering will make it look more like continents or islands.
	public static float seaLevel = 0.1f;  
	
	//How much sand in the shore is created
	public static float sandAmount = 0.1f;
	
	private float cameraSpeed = 6.0f;
	private float zoomSensitivity = 1.0f;
	private float cameraInitialZoom = 40.0f;
	private float m_fboScaler = 1.0f; // m_fboScaler increase or decrease the antialiasing quality
	/************************************************************************************/
	
	private OrthographicCamera camera;
	private SpriteBatch batch;
	public static Amortized2DNoise noise; // = new Amortized2DNoise(Amortized2DNoise.CELLSIZE2D);
	static Random rand = new Random();
		
	private boolean m_fboEnabled = true;
	private FrameBuffer m_fbo = null;
	private TextureRegion m_fboRegion = null;
	private Matrix4 normalProjection = new Matrix4();
		
	private Cell cells[][] = new Cell[worldSize][worldSize];
	BitmapFont font;
	
	@Override
	public void create() {

		camera = new OrthographicCamera(48, 32);
		camera.update();
		batch = new SpriteBatch();

		//randnum.setSeed((long)Gdx.graphics.getDeltaTime()* 32000);
		noise = new Amortized2DNoise(Amortized2DNoise.CELLSIZE2D);
		
		camera.zoom = cameraInitialZoom;
		camera.position.set(cellSize*worldSize / 2, cellSize*worldSize / 2, 0);
		camera.update();
		
		Gdx.input.setInputProcessor(this);
		
		normalProjection.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		
		font = new BitmapFont(Gdx.files.internal("smallFont.fnt"), false);
		
	}
	
	public Vector2 getCellNumberFromCameraCorner(int x, int y) {
		Vector3 p = new Vector3((camera.viewportWidth * 1) * camera.zoom,(camera.viewportHeight * 1) * camera.zoom,0);
		
		float vw = p.x / 2;
		float vh = p.y / 2;
		
		float ccX = (camera.position.x+(vw*x)) / (Amortized2DNoise.CELLSIZE2D); 
		float ccY = (camera.position.y+(vh*y)) / (Amortized2DNoise.CELLSIZE2D);
		
		 return new Vector2(ccX,ccY);
	}
	
	public void processCells() {
		
		Vector2 cellTopRight = getCellNumberFromCameraCorner(1,1);
		Vector2 cellBottomLeft = getCellNumberFromCameraCorner(-1,-1);
		
		int ablX = (int)cellBottomLeft.x;
		int atrX = (int)cellTopRight.x;
		
		int ablY = (int)cellBottomLeft.y;
		int atrY = (int)cellTopRight.y;

		for(int y = ablY; y <= atrY; y++) {
			for(int x = ablX; x <= atrX; x++) {
				if(x >= 0 && y >= 0) {
					if(cells[x][y] != null) {
						cells[x][y].render(batch);
					} else {
						Cell cell = new Cell();
						cell.create(x,y);
						cells[x][y] = cell;
					}
					
					if(cells[x][y].x < ablX || cells[x][y].x > atrX || cells[x][y].y < ablY || cells[x][y].y > atrY) {
						cells[x][y].dispose();
						cells[x][y] = null;
					}
					
				}
			}
		}

	}
	
	public void renderfbo()
	{
		//m_fboScaler = 1.0f;
		m_fboEnabled = true;
		
	    int width = Gdx.graphics.getWidth();
	    int height = Gdx.graphics.getHeight();
	    
	    if(m_fboEnabled)      // enable or disable the supersampling
	    {                  
	        if(m_fbo == null)
	        {
	            // m_fboScaler increase or decrease the antialiasing quality
	            m_fbo = new FrameBuffer(Format.RGB565, (int)(width * m_fboScaler), (int)(height * m_fboScaler), false);
	            m_fboRegion = new TextureRegion(m_fbo.getColorBufferTexture());
	            m_fboRegion.flip(false, true);
	        }

	        m_fbo.begin();
	    }
	    
	    // this is the main render function
	    renderMap();
	     
	    if(m_fbo != null)
	    {
	        m_fbo.end();
	        batch.begin();
	        //batch.setShader(shader);
	        batch.setProjectionMatrix(normalProjection);
	        batch.draw(m_fboRegion, 0, 0, width, height);
	        
	        font.draw(batch, " FPS: " + Integer.toString(Gdx.graphics.getFramesPerSecond()) + " - Use WASD keys to move, scroll mouse to zoom", 0, height - 0);
	        
	        font.draw(batch, "Created by Ian Parberry - Demo by Pablo Nunez - http://larc.unt.edu/ian/research/amortizednoise/", 0, 30);
	        
	        batch.end();
	    }

	}
	
	@Override
	public void render() {
		renderfbo();
	}
	
	public void renderMap() {
		
		moveCamera();
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		processCells();
		
		batch.end();
	}
	
	private void moveCamera() {
		if(Gdx.input.isKeyPressed(Input.Keys.D))
			camera.position.x = camera.position.x + cameraSpeed;
		if(Gdx.input.isKeyPressed(Input.Keys.A))
			camera.position.x = camera.position.x - cameraSpeed;
		if(Gdx.input.isKeyPressed(Input.Keys.W))
			camera.position.y = camera.position.y + cameraSpeed;
		if(Gdx.input.isKeyPressed(Input.Keys.S))
			camera.position.y = camera.position.y - cameraSpeed;
		
		camera.update(true);
	}
	
	@Override 
	public void dispose() {
		noise.dispose();
		font.dispose();
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
		camera.zoom = camera.zoom + amount * zoomSensitivity;
		if(camera.zoom < 0.1f)
			camera.zoom = 0.1f;
	
		camera.update();
		return false;
	}
}