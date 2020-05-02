package net.yaht.pf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

import org.apache.commons.math3.complex.Complex;

/**
 * @author lisp
 * <p>
 * NOTE: please refer to this Java libraries used in source.
 * <p>
 * http://commons.apache.org/proper/commons-math/userguide/complex.html
 * http://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferedImage.html
 */


public class RunMe {
	public static void main(String[] args) {
		System.out.println("eho");

		int width = 641;
		int height = 481;

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D g2d = bi.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		double ty = 1.0 / (480.0 * 1.2);
		// double ty = 1.0;

		PrintWriter out = new PrintWriter(System.out);

		for (int i = 0; i < (int) (480.0 * 1.2); i++) {

			// double py = 8.0 - 16.0 * ty;
			double py = 2.0 - 4.0 * ty;
			// double py = 0.0 - 5.0 * ty;
			// int py_scr = (int) (Math.abs((py - 8.0)) * (480.0 / 16.0));
			int py_scr = (int) (Math.abs((py - 2.0)) * (480.0 / 4.0));
			// int py_scr = (int) (Math.abs((py)) * (480.0 / 5.0));

			double tx = 1.0 / (640.0 * 1.2);
			for (int j = 0; j < (int) (640.0 * 1.2); j++) {

				// double px = -8.0 + 16.0 * tx;
				double px = -2.0 + 4.0 * tx;
				// double px = 1.5 + 1.0 * tx;
				// int px_scr = (int) ((px + 8.0) * (640.0 / 16.0));
				int px_scr = (int) ((px + 2.0) * (640.0 / 4.0));
				// int px_scr = (int) ((px - 1.5) * (640.0 / 1.0));

				int r = z_check(new Complex(px, py));

//				debug info about points
//				out.printf("(%.9f, %.9f) to (%3d, %3d) => %d\n", px, py, px_scr, py_scr, r);
				out.printf("%d\n", r);

				// bi.setRGB(x, y, rgb);

				boolean blackWhiteOutput = true;
				if (blackWhiteOutput) {
					blackAndWhiteOutput(bi, py_scr, px_scr, r);
				}
				else {
					coloredOutput(bi, py_scr, px_scr, r);
				}

				tx += 1.0 / (640.0 * 1.2);
			}

			ty += 1.0 / (480.0 * 1.2);
		}

		g2d.setColor(Color.GRAY);
		g2d.drawRect(0, 0, width - 2, height - 2);

		try {
//			ImageIO.write(bi, "PNG", new File("SeeMe-2x2.png"));
//			ImageIO.write(bi, "PNG", new File("colored-test.png"));
			ImageIO.write(bi, "PNG", new File("bw-test.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		out.printf("done.\n");

		out.flush();
		out.close();
	}

	private static void blackAndWhiteOutput(BufferedImage bi, int py_scr, int px_scr, int r) {
		if (r == 0) { // inside ...
			bi.setRGB(px_scr, py_scr, 0x000000);
		}
		else {
			bi.setRGB(px_scr, py_scr, 0xffffff);
		}
	}

	private static void coloredOutput(BufferedImage bi, int py_scr, int px_scr, int r) {
		if (r == 0) { // inside ...
			bi.setRGB(px_scr, py_scr, 0x00ff00);
		}
		else if (r <= 10) { // outside ... (rapid move)
			bi.setRGB(px_scr, py_scr, 0xFFFFFF);
			// close to inside cases ...
			// } else if (10 < r && r <= 50) {
			// bi.setRGB(px_scr, py_scr, 0x0033EE);
		}
		else if (r == 11) {
			bi.setRGB(px_scr, py_scr, 0x0000ff);
		}
		else if (r == 12) {
			bi.setRGB(px_scr, py_scr, 0x0000ee);
		}
		else if (r == 13) {
			bi.setRGB(px_scr, py_scr, 0x0000dd);
		}
		else if (r == 14) {
			bi.setRGB(px_scr, py_scr, 0x0000cc);
		}
		else if (r == 15) {
			bi.setRGB(px_scr, py_scr, 0x0000bb);
		}
		else if (r == 16) {
			bi.setRGB(px_scr, py_scr, 0x0000aa);
		}
		else if (r == 17) {
			bi.setRGB(px_scr, py_scr, 0x000099);
		}
		else if (r == 18) {
			bi.setRGB(px_scr, py_scr, 0x000088);
		}
		else if (r == 19) {
			bi.setRGB(px_scr, py_scr, 0x000077);
		}
		else if (r == 20) {
			bi.setRGB(px_scr, py_scr, 0x000066);
		}
		else if (20 < r && r <= 30) {
			bi.setRGB(px_scr, py_scr, 0x666600);
		}
		else if (30 < r && r <= 40) {
			bi.setRGB(px_scr, py_scr, 0x777700);
		}
		else if (40 < r && r <= 50) {
			bi.setRGB(px_scr, py_scr, 0x888800);
		}
		else if (50 < r && r <= 100) {
			bi.setRGB(px_scr, py_scr, 0x999900);
		}
		else if (100 < r && r <= 150) {
			bi.setRGB(px_scr, py_scr, 0xaaaa00);
		}
		else if (150 < r && r <= 150) {
			bi.setRGB(px_scr, py_scr, 0xbbbb00);
		}
		else if (200 < r && r <= 150) {
			bi.setRGB(px_scr, py_scr, 0xcccc00);
		}
		else if (350 < r && r <= 300) {
			bi.setRGB(px_scr, py_scr, 0xdddd00);
		}
		else {
			bi.setRGB(px_scr, py_scr, 0xeeee00);
		}
	}

	public static int z_check(Complex c) {
		Complex z0 = new Complex(0.0, 0.0);

		Complex z_prev = z0;
		Complex z_i = null;

		int steps = 0;

		Double d = null;

		for (int i = 0; i < 640; i++) {
			z_i = z_iter(z_prev, c);
			z_prev = z_i;

			d = new Double(z_prev.getReal());

			if (d.isInfinite() || d.isNaN()) {
				steps = i;
				break;
			}
		}

		// System.out.println("s: " + steps + ", " + z_i.getReal() + ", " + z_i.getImaginary());
		// return (steps == 0);

		return steps;
	}

	public static Complex z_iter(Complex z, Complex c) {
		return z.multiply(z).add(c);
	}
}
