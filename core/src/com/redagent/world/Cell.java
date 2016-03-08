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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Cell {
	
	public int x;
	public int y;
	Texture tex;
	TextureRegion textureReg;
	
	public void create(int _x, int _y) {
		
		x = _x;
		y = _y;
		
		float[][] cell2 = new float[App.cellSize][App.cellSize];
		for (int i = 0; i < App.cellSize; i++)
			for (int j = 0; j < App.cellSize; j++)
				cell2[i][j] = 0.0f;
		
		tex = App.noise.Generate2DNoise(cell2, App.octave0,App.octave1,y,x);
		textureReg = new TextureRegion(tex);
		textureReg.flip(false, true);
	}
	
	public void render(SpriteBatch batch) {
		if(tex != null)
			batch.draw(textureReg, x * App.cellSize, y * App.cellSize);
		
	}
	
	public void dispose() {
		tex.dispose();
	}
	
}
