import org.apache.commons.math3.complex.Complex;

import java.awt.image.BufferedImage;
import java.util.Calendar;

public class FractalPartThread extends Thread {
	final private int index;
	private final BufferedImage bufferedImage;
	private final int startingRow;
	private final int numberOfRows;

	public FractalPartThread(int index, BufferedImage bufferedImage, int startingRow, int numberOfRows) {
		this.index = index;
		this.bufferedImage = bufferedImage;
		this.startingRow = startingRow;
		this.numberOfRows = numberOfRows;
	}

	@Override
	public void run() {
		long threadStartTimestamp = Calendar.getInstance().getTimeInMillis();

		if (!Main.quietOutput){
			System.out.printf("Thread-%d started.%n", index);
		}
		for (int yPixel = startingRow; yPixel < index * numberOfRows; yPixel++) {
			double imaginaryValue = mapPixelHeightToImaginaryAxis(yPixel, Main.height, Main.minImaginary, Main.maxImaginary);

			for (int xPixel = 0; xPixel < Main.width; xPixel++) {
				double realValue = mapPixelWidthToRealAxis(xPixel, Main.width, Main.minReal, Main.maxReal);

				int numberOfSteps = calculateNumberOfSteps(new Complex(realValue, imaginaryValue));

//				printInfoAboutPoint(xPixel, yPixel, realValue, imaginaryValue, r);

				// blackAndWhiteOutput(bufferedImage, xPixel, yPixel, r);
				coloredOutput(bufferedImage, xPixel, yPixel, numberOfSteps);
			}
		}

		long threadFinishTimeStamp = System.currentTimeMillis();
		long threadOverallTime = threadFinishTimeStamp - threadStartTimestamp;

		if (!Main.quietOutput) {
			System.out.printf("Thread-%d stopped.%n", index);
			System.out.printf("Thread-%d execution time was (millis): %d%n", index, threadOverallTime);
		}
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

	private static void blackAndWhiteOutput(BufferedImage bufferedImage, int xPixel, int yPixel, int numberOfSteps) {
		if (numberOfSteps == 0) {
			bufferedImage.setRGB(xPixel, yPixel, 0x000000);
		}
		else {
			bufferedImage.setRGB(xPixel, yPixel, 0xffffff);
		}
	}

	private static void coloredOutput(BufferedImage bufferedImage, int xPixel, int yPixel, int numberOfSteps) {
		if (numberOfSteps == 0) {
			bufferedImage.setRGB(xPixel, yPixel, 0x00ff00);
		}
		else if (numberOfSteps <= 10) { // outside ... (rapid move)
			bufferedImage.setRGB(xPixel, yPixel, 0xFFFFFF);
		}
		else if (numberOfSteps == 11) {
			bufferedImage.setRGB(xPixel, yPixel, 0x0000ff);
		}
		else if (numberOfSteps == 12) {
			bufferedImage.setRGB(xPixel, yPixel, 0x0000ee);
		}
		else if (numberOfSteps == 13) {
			bufferedImage.setRGB(xPixel, yPixel, 0x0000dd);
		}
		else if (numberOfSteps == 14) {
			bufferedImage.setRGB(xPixel, yPixel, 0x0000cc);
		}
		else if (numberOfSteps == 15) {
			bufferedImage.setRGB(xPixel, yPixel, 0x0000bb);
		}
		else if (numberOfSteps == 16) {
			bufferedImage.setRGB(xPixel, yPixel, 0x0000aa);
		}
		else if (numberOfSteps == 17) {
			bufferedImage.setRGB(xPixel, yPixel, 0x000099);
		}
		else if (numberOfSteps == 18) {
			bufferedImage.setRGB(xPixel, yPixel, 0x000088);
		}
		else if (numberOfSteps == 19) {
			bufferedImage.setRGB(xPixel, yPixel, 0x000077);
		}
		else if (numberOfSteps == 20) {
			bufferedImage.setRGB(xPixel, yPixel, 0x000066);
		}
		else if (numberOfSteps <= 30) {
			bufferedImage.setRGB(xPixel, yPixel, 0x666600);
		}
		else if (numberOfSteps <= 40) {
			bufferedImage.setRGB(xPixel, yPixel, 0x777700);
		}
		else if (numberOfSteps <= 50) {
			bufferedImage.setRGB(xPixel, yPixel, 0x888800);
		}
		else if (numberOfSteps <= 100) {
			bufferedImage.setRGB(xPixel, yPixel, 0x999900);
		}
		else if (numberOfSteps <= 150) {
			bufferedImage.setRGB(xPixel, yPixel, 0xaaaa00);
		}
		else if (numberOfSteps <= 200) {
			bufferedImage.setRGB(xPixel, yPixel, 0xbbbb00);
		}
		else if (numberOfSteps <= 250) {
			bufferedImage.setRGB(xPixel, yPixel, 0xcccc00);
		}
		else if (numberOfSteps <= 300) {
			bufferedImage.setRGB(xPixel, yPixel, 0xdddd00);
		}
		else {
			bufferedImage.setRGB(xPixel, yPixel, 0xeeee00);
		}
	}

	public static int calculateNumberOfSteps(Complex c) {
		Complex z0 = new Complex(0.0, 0.0);
		Complex zPrevious = z0;
		Complex zIteration = null;
		Double realPartOfZPrevious = null;
		int steps = 0;

		final int maxIterations = 640;

		for (int i = 0; i < maxIterations; i++) {
			zIteration = calculateIterationTerm(zPrevious, c);
			zPrevious = zIteration;

			realPartOfZPrevious = zPrevious.getReal();

			if (realPartOfZPrevious.isInfinite() || realPartOfZPrevious.isNaN()) {
				steps = i;
				break;
			}
		}

//		System.out.println("s: " + steps + ", " + z_i.getReal() + ", " + z_i.getImaginary());

		return steps;
	}

	public static Complex calculateIterationTerm(Complex z, Complex constant) {
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
}
