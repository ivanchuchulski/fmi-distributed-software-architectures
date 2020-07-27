import org.apache.commons.math3.complex.Complex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.Callable;

public class AsynchronousStaticSlave implements Callable<HashMap<Pixel, Integer>> {
    final private int index;
    private final HashMap<Pixel, Integer> calculatedPixels;

    public AsynchronousStaticSlave(int index) {
        this.index = index;
        calculatedPixels = new HashMap<>();
    }

    @Override
    public HashMap<Pixel, Integer> call() {
        if (!Main.quietOutput) {
            System.out.printf("Thread-%d started.%n", index);
        }

        long threadStartTimestamp = Calendar.getInstance().getTimeInMillis();

        calculateImagePixelsColor();

        long threadFinishTimeStamp = System.currentTimeMillis();
        long threadOverallTime = threadFinishTimeStamp - threadStartTimestamp;

        if (!Main.quietOutput) {
            System.out.printf("Thread-%d stopped.%n", index);
            System.out.printf("Thread-%d execution time was (millis): %d%n", index, threadOverallTime);
        }

        return calculatedPixels;
    }

    private void calculateImagePixelsColor() {
        for (int yPixel = index; yPixel < Main.height; yPixel += Main.threadCount) {
            double imaginaryValue = mapPixelHeightToImaginaryAxis(yPixel);

            for (int xPixel = 0; xPixel < Main.width; xPixel++) {
                double realValue = mapPixelWidthToRealAxis(xPixel);

                int numberOfSteps = calculateNumberOfSteps(new Complex(realValue, imaginaryValue));

                int pointColor = getHSBToRGBColor(numberOfSteps);

                calculatedPixels.put(new Pixel(xPixel, yPixel), pointColor);
            }
        }
    }

    private static double mapPixelHeightToImaginaryAxis(int yPixel) {
        double range = Main.maxImaginary - Main.minImaginary;

        return yPixel * (range / Main.height) + Main.minImaginary;
    }

    private static double mapPixelWidthToRealAxis(int xPixel) {
        double range = Main.maxReal - Main.minReal;

        return xPixel * (range / Main.width) + Main.minReal;
    }

    /**
     * calculates the number of steps for the given point
     * @param complexPoint point on the complex plane to check whether is in the fractal set
     * @return number of iterations until the sequence becomes unbounded and goes to infinity,
     * if 0 is returned, it means the sequence for the point converges and the point is in the fractal set
     */
    private static int calculateNumberOfSteps(Complex complexPoint) {
        final Complex z0 = new Complex(0.0, 0.0);
        Complex zPrevious = z0;
        Complex zIteration = null;
        Double realPartOfZPrevious = null;

        int iterations = 0;
        final int maxIterations = Main.maxPointIterations;

        for (int i = 0; i < maxIterations; i++) {
            zIteration = calculateIterationTerm(zPrevious, complexPoint);
            zPrevious = zIteration;

            realPartOfZPrevious = zPrevious.getReal();

            if (realPartOfZPrevious.isInfinite() || realPartOfZPrevious.isNaN()) {
                iterations = i;
                break;
            }
        }

        return iterations;
    }

    /**
     * formula project num 17 : F(Z) = e^(cos(C*Z))
     *
     * @param z
     * @param constant
     * @return
     */
    public static Complex calculateIterationTerm(Complex z, Complex constant) {
        Complex constantTimesZ = constant.multiply(z);
        Complex cosine = constantTimesZ.cos();

        return cosine.exp();
    }

    private static int getHSBToRGBColor(int numberOfIterations) {
        if (numberOfIterations == 0 || numberOfIterations > Main.maxPointIterations) {
            return 0x000000; // black
        }
        else {
            return Color.HSBtoRGB((float) 128 * numberOfIterations / Main.maxPointIterations, 0.5f, 1);
        }
    }
}
