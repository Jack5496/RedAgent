package com.redagent.world;

import java.util.Random;

public class DiamondSquareGenerator {
    private final int size;
    public final float initialCorner;
    private final float initialVariance;
    private final float varianceDivisor;
    private final Random random;

    //range is 0f to 100f
    //format: genArray[x][y]
    private float[][] genArray;

    /**
     * @param size            The size of the square 2d array generated.
     *                        Size must be a (power of 2) + 1
     * @param initialCorner   The value to initialize the 4 corners to.
     *                        All generation will be based on this values
     * @param initialVariance The initial variance. The center cell is positively or
     *                        negatively augmented by a random number from 0 to initialVariance.
     *                        <p/>
     *                        A larger initial variance means a less smooth map.
     * @param varianceDivisor Variance is divided by this value each recursion.
     *                        <p/>
     *                        A larger variance divisor means the variance decreases
     *                        faster as you move outward
     * @param seed            Seed for the random number generator
     */
    public DiamondSquareGenerator(int size, float initialCorner, float initialVariance,
                                  float varianceDivisor, long seed) {

        this.size = size;
        this.initialCorner = initialCorner;
        this.initialVariance = initialVariance;
        this.varianceDivisor = varianceDivisor;
        this.random = new Random(seed);
    }
    
    public float[][] generate(float topLeft, float topRight, float bottomLeft, float bottomRight){
    	genArray = new float[size][size];

        //init corners
        genArray[0][0] = bottomLeft;
        genArray[0][size - 1] = topLeft;
        genArray[size - 1][0] = bottomRight;
        genArray[size - 1][size - 1] = topRight;

        //the actual generation
        generateRecursive(0, 0, size - 1, size - 1, initialVariance);

        return genArray;
    }

    public float[][] generate() {
        return generate(initialCorner,initialCorner,initialCorner,initialCorner);
    }

    /**
     * does recursive generation
     * on the square of genArray specified by startX, startY, endX, endY
     *
     * @param variance the amount to augment the center cell by (in + and - directions)
     */
    private void generateRecursive(int startX, int startY, int endX, int endY, float variance) {
        //return if square is too small to operate on (algorithm is finished)
        if (endX - startX < 2) {
            return;
        }

        //calculate center coordinates
        int centerX = startX + ((endX - startX) / 2);
        int centerY = startY + ((endY - startY) / 2);

        //set center cell to average of four corners
        float centerAverage = average(
                getGenArray(startX, startY),
                getGenArray(startX, endY),
                getGenArray(endX, startY),
                getGenArray(endX, endY)
        );

        //augment center cell by random between +variance and -variance
        genArray[centerX][centerY] = centerAverage +
                ((random.nextFloat() * variance * 2) - variance);

        int halfDistance = centerX - startX;

        //set the midpoint of each side of the square to an average of the points around it
        averageAround(centerX, startY, halfDistance);
        averageAround(centerX, endY, halfDistance);
        averageAround(startX, centerY, halfDistance);
        averageAround(endX, centerY, halfDistance);

        float newVariance = variance / varianceDivisor;

        //recurse for the 4 quadrants of this square
        generateRecursive(startX, startY, centerX, centerY, newVariance);
        generateRecursive(centerX, startY, endX, centerY, newVariance);
        generateRecursive(startX, centerY, centerX, endY, newVariance);
        generateRecursive(centerX, centerY, endX, endY, newVariance);
    }

    /**
     * sets the point x,y to the average of the points around it (in diamond arrangement)
     */
    private void averageAround(int x, int y, int distanceAway) {
        genArray[x][y] = average(
                getGenArray(x + distanceAway, y),
                getGenArray(x - distanceAway, y),
                getGenArray(x, y + distanceAway),
                getGenArray(x, y - distanceAway)
        );
    }

    /**
     * @return the genArray at x,y or initialCorner if out of bounds
     */
    private float getGenArray(int x, int y) {
        //use same tests for x and y because genArray is assumed to be square
        if ((x > 0) && (y > 0) && (x < genArray.length) && (y < genArray.length)) {
            //in bounds
            return genArray[x][y];
        } else {
            //out of bounds
            //set to corner value
            return initialCorner;
        }
    }

    /**
     * averages passed in numbers
     */
    private float average(float... numbers) {
        float sum = 0f;
        for (float number : numbers) {
            sum = sum + number;
        }
        return sum / numbers.length;
    }
}