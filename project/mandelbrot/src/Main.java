import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.Complex;

public class Main {
	public static void main(String[] args) {
		int width = 640;
		int height = 480;

		int minReal = -2;
		int maxReal = 2;

		int minImaginary = -2;
		int maxImaginary = 2;

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.fillRect(0, 0, width - 1, height - 1);

		for (int yPixel = 0; yPixel < height; yPixel++) {
			double imaginaryValue = mapPixelHeightToImaginaryAxis(yPixel, height, minImaginary, maxImaginary);

			for (int xPixel = 0; xPixel < width; xPixel++) {
				double realValue = mapPixelWidthToRealAxis(xPixel, width, minReal, maxReal);

				int r = z_check(new Complex(realValue, imaginaryValue));

				printInfoAboutPoint(xPixel, yPixel, realValue, imaginaryValue, r);

				blackAndWhiteOutput(bufferedImage, xPixel, yPixel, r);
				// coloredOutput(bufferedImage, xPixel, yPixel, r);
			}
		}

		graphics2D.setColor(Color.GRAY);
		graphics2D.drawRect(0, 0, width - 2, height - 2);

		try {
			 ImageIO.write(bufferedImage, "PNG", new File("image-outputs/bw-test.png"));
			// ImageIO.write(bufferedImage, "PNG", new File("image-outputs/colored-test.png"));
		}
		catch (IOException exception) {
			exception.printStackTrace();
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

	private static void blackAndWhiteOutput(BufferedImage bufferedImage, int xPixel, int yPixel, int r) {
		if (r == 0) { // inside ...
			bufferedImage.setRGB(xPixel, yPixel, 0x000000);
		}
		else {
			bufferedImage.setRGB(xPixel, yPixel, 0xffffff);
		}
	}

	private static void coloredOutput(BufferedImage bufferedImage, int px_scr, int py_scr, int r) {
		if (r == 0) { // inside ...
			bufferedImage.setRGB(px_scr, py_scr, 0x00ff00);
		}
		else if (r <= 10) { // outside ... (rapid move)
			bufferedImage.setRGB(px_scr, py_scr, 0xFFFFFF);
			// close to inside cases ...
			// } else if (10 < r && r <= 50) {
			// bi.setRGB(px_scr, py_scr, 0x0033EE);
		}
		else if (r == 11) {
			bufferedImage.setRGB(px_scr, py_scr, 0x0000ff);
		}
		else if (r == 12) {
			bufferedImage.setRGB(px_scr, py_scr, 0x0000ee);
		}
		else if (r == 13) {
			bufferedImage.setRGB(px_scr, py_scr, 0x0000dd);
		}
		else if (r == 14) {
			bufferedImage.setRGB(px_scr, py_scr, 0x0000cc);
		}
		else if (r == 15) {
			bufferedImage.setRGB(px_scr, py_scr, 0x0000bb);
		}
		else if (r == 16) {
			bufferedImage.setRGB(px_scr, py_scr, 0x0000aa);
		}
		else if (r == 17) {
			bufferedImage.setRGB(px_scr, py_scr, 0x000099);
		}
		else if (r == 18) {
			bufferedImage.setRGB(px_scr, py_scr, 0x000088);
		}
		else if (r == 19) {
			bufferedImage.setRGB(px_scr, py_scr, 0x000077);
		}
		else if (r == 20) {
			bufferedImage.setRGB(px_scr, py_scr, 0x000066);
		}
		else if (20 < r && r <= 30) {
			bufferedImage.setRGB(px_scr, py_scr, 0x666600);
		}
		else if (30 < r && r <= 40) {
			bufferedImage.setRGB(px_scr, py_scr, 0x777700);
		}
		else if (40 < r && r <= 50) {
			bufferedImage.setRGB(px_scr, py_scr, 0x888800);
		}
		else if (50 < r && r <= 100) {
			bufferedImage.setRGB(px_scr, py_scr, 0x999900);
		}
		else if (100 < r && r <= 150) {
			bufferedImage.setRGB(px_scr, py_scr, 0xaaaa00);
		}
		else if (150 < r && r <= 150) {
			bufferedImage.setRGB(px_scr, py_scr, 0xbbbb00);
		}
		else if (200 < r && r <= 150) {
			bufferedImage.setRGB(px_scr, py_scr, 0xcccc00);
		}
		else if (300 < r && r <= 350) {
			bufferedImage.setRGB(px_scr, py_scr, 0xdddd00);
		}
		else {
			bufferedImage.setRGB(px_scr, py_scr, 0xeeee00);
		}
	}

	public static int z_check(Complex c) {
		Complex z0 = new Complex(0.0, 0.0);

		Complex z_prev = z0;
		Complex z_i = null;

		int steps = 0;

		Double d = null;

		final int maxIterations = 1200;

		for (int i = 0; i < maxIterations; i++) {
			z_i = z_iter(z_prev, c);
			z_prev = z_i;

			d = z_prev.getReal();

			if (d.isInfinite() || d.isNaN()) {
				steps = i;
				break;
			}
		}

		 System.out.println("s: " + steps + ", " + z_i.getReal() + ", " + z_i.getImaginary());
		// return (steps == 0);

		return steps;
	}

	public static Complex z_iter(Complex z, Complex constant) {
//		formula is Z = Z^2 + C
//		return z.multiply(z).add(c);

//		formula project num 16 : F(Z) = C*e^(-Z) + C^2
		Complex minusZ = z.multiply(-1);
		Complex exponentRaisedToTheMinusZ = minusZ.exp();
		Complex zSquared = z.multiply(z);

		return exponentRaisedToTheMinusZ.multiply(constant).add(zSquared);
	}
}
