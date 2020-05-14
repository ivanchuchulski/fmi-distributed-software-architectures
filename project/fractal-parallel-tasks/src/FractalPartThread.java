import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;

import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Vector;

public class FractalPartThread extends Thread {
	final private int index;
	private final BufferedImage bufferedImage;
	private final Vector<Boolean> tasks;

	public FractalPartThread(int index, BufferedImage bufferedImage, Vector<Boolean> tasks) {
		this.index = index;
		this.bufferedImage = bufferedImage;
		this.tasks = tasks;
	}

	@Override
	public void run() {
        long threadStartTimestamp = Calendar.getInstance().getTimeInMillis();

        if (!Main.quietOutput) {
            System.out.printf("Thread-%d started.%n", index);
        }

        while (true) {
            int rowAvailable = findFirstAvailableTask();

            if (rowAvailable != -1) {
                markTaskAsTaken(rowAvailable);

                System.out.printf("thread %d working on row %d%n", index, rowAvailable);

                int yPixel = rowAvailable;

                for (int xPixel = 0; xPixel < Main.width; xPixel++) {
                    double realValue = mapPixelWidthToRealAxis(xPixel, Main.width, Main.minReal, Main.maxReal);
                    double imaginaryValue = mapPixelHeightToImaginaryAxis(yPixel, Main.height, Main.minImaginary, Main.maxImaginary);

                    int numberOfSteps = calculateNumberOfSteps(new Complex(realValue, imaginaryValue));

//    				printInfoAboutPoint(xPixel, yPixel, realValue, imaginaryValue, r);
//    				int color = blackAndWhiteOutput(bufferedImage, xPixel, yPixel, r);

                    int color = getIterationColor(bufferedImage, xPixel, yPixel, numberOfSteps);

                    drawPixel(bufferedImage, xPixel, yPixel, color);
                }
            }
            else {
                long threadFinishTimeStamp = System.currentTimeMillis();
                long threadOverallTime = threadFinishTimeStamp - threadStartTimestamp;

                if (!Main.quietOutput) {
                    System.out.printf("Thread-%d stopped%n", index);
                    System.out.printf("Thread-%d execution time was (millis): %d%n", index, threadOverallTime);
                }
                return;
            }
        }
    }

    private synchronized void markTaskAsTaken(int rowAvailable) {
        tasks.setElementAt(true, rowAvailable);
    }

    /**
     *
     * @return
     */
    private synchronized int findFirstAvailableTask() {
		return tasks.indexOf(false);
	}

	/**
	 *
	 * @param constant point on the complex plane to check whether is in the fractal set
	 * @return number of iterations until the sequence becomes unbounded and goes to infinity,
	 * if 0 is returned, it means the sequence remains bound and the point is in the fractal set
	 */
	private static int calculateNumberOfSteps(Complex constant) {
		final Complex z0 = new Complex(0.0, 0.0);
		Complex zPrevious = z0;
		Complex zIteration = null;
		Double realPartOfZPrevious = null;

		// maybe tweak maxIterations, 1000 is to much, maybe 500 or little bit less
		int iterations = 0;
		final int maxIterations = 500;

		for (int i = 0; i < maxIterations; i++) {
			zIteration = calculateIterationTerm(zPrevious, constant);
			zPrevious = zIteration;

			realPartOfZPrevious = zPrevious.getReal();

			if (realPartOfZPrevious.isInfinite() || realPartOfZPrevious.isNaN()) {
				iterations = i;
				break;
			}
		}

//		System.out.println("iter: " + iterations + "; " + zIteration.getReal() + ", " + zIteration.getImaginary());

		return iterations;
	}

	private static Complex calculateIterationTerm(Complex z, Complex constant) {
//		formula project num 16 : F(Z) = C*e^(-Z) + Z^2
		// Complex minusZ = z.multiply(-1);
		// Complex exponentRaisedToTheMinusZ = minusZ.exp();
		// Complex zSquared = z.multiply(z);

		// return (constant.multiply(exponentRaisedToTheMinusZ)).add(zSquared);


//		formula project num 17 : F(Z) = e^(cos(C*Z))
		Complex constantTimesZ = constant.multiply(z);
		Complex cosine = constantTimesZ.cos();

		return cosine.exp();

//		formula project num 19 : F(Z) = e^(Z^2 * C)
//		Complex zSquared = z.multiply(z);
//		Complex zSquaredTimesConstant = zSquared.multiply(constant);
//
//		return zSquaredTimesConstant.exp();
	}

	private static void printInfoAboutPoint(int xPixel, int yPixel, double realValue, double imaginaryValue, int r) {
		System.out.printf("(%.9f, %.9f) to (%3d, %3d) => %d\n", realValue, imaginaryValue, xPixel, yPixel, r);
	}

	private static double mapPixelWidthToRealAxis(int x, int width, double minReal,double maxReal) {
		double range = maxReal - minReal;

		return x * (range / width) + minReal;
	}

	private static double mapPixelHeightToImaginaryAxis(int y, int height, double minImaginary, double maxImaginary) {
		double range = maxImaginary - minImaginary;

		return y * (range / height) + minImaginary;
	}

	private static int blackAndWhiteOutput(BufferedImage bufferedImage, int xPixel, int yPixel, int iterations) {
		if (iterations == 0) {
			return 0x000000;
		}
		else {
			return 0xffffff;
		}
	}

	private static int getIterationColor(BufferedImage bufferedImage, int xPixel, int yPixel, int iterations) {
		if (iterations == 0) {
			return 0x00ff00;
		}
		else if (iterations <= 10) {
			return 0xFFFFFF;
		}
		else if (iterations == 11) {
			return 0x0000ff;
		}
		else if (iterations == 12) {
			return 0x0000ee;
		}
		else if (iterations == 13) {
			return 0x0000dd;
		}
		else if (iterations == 14) {
			return 0x0000cc;
		}
		else if (iterations == 15) {
			return 0x0000bb;
		}
		else if (iterations == 16) {
			return 0x0000aa;
		}
		else if (iterations == 17) {
			return 0x000099;
		}
		else if (iterations == 18) {
			return 0x000088;
		}
		else if (iterations == 19) {
			return 0x000077;
		}
		else if (iterations == 20) {
			return 0x000066;
		}
		else if (iterations <= 30) {
			return 0x666600;
		}
		else if (iterations <= 40) {
			return 0x777700;
		}
		else if (iterations <= 50) {
			return 0x888800;
		}
		else if (iterations <= 100) {
			return 0x999900;
		}
		else if (iterations <= 150) {
			return 0xaaaa00;
		}
		else if (iterations <= 200) {
			return 0xbbbb00;
		}
		else if (iterations <= 250) {
			return 0xcccc00;
		}
		else if (iterations <= 300) {
			return 0xdddd00;
		}
		else {
			return 0xeeee00;
		}
	}

//	maybe make the method synchronized
	private void drawPixel(BufferedImage bufferedImage, int xPixel, int yPixel, int color) {
		bufferedImage.setRGB(xPixel, yPixel, color);
	}
}
