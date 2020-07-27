import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;

import org.apache.commons.cli.*;

public class Main {
	public static int width = 1280;
	public static int height = 960;

	public static int threadCount = 1;

	public static double minReal = -2.0;
	public static double maxReal = 2.0;
	public static double minImaginary = -2.0;
	public static double maxImaginary = 2.0;

	public static String outputFileName = "zad17.png";

	public static boolean quietOutput = false;

	public static int maxPointIterations = 500;

	public static void main(String[] args) {
		long startTimestamp = Calendar.getInstance().getTimeInMillis();

		try {
			parseConsoleInput(args);
		}
		catch (ParseException parseException) {
			parseException.printStackTrace();
			System.exit(1);
		}

		ArrayList<AsynchronousStaticSlave> asynchronousStaticSlaves = new ArrayList<>(threadCount);
		ArrayList<FutureTask<HashMap<Pixel, Integer>>> futureTasks = new ArrayList<>(threadCount);
		ArrayList<HashMap<Pixel, Integer>> calculatedPixels = new ArrayList<>(threadCount);

		for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
			AsynchronousStaticSlave asynchronousStaticSlave = new AsynchronousStaticSlave(threadIndex);

			FutureTask<HashMap<Pixel, Integer>> futureTask = new FutureTask<>(asynchronousStaticSlave);

			Thread thread = new Thread(futureTask);

//			futureTask.run();
			thread.start();
			futureTasks.add(futureTask);
		}

		try {
			for (var futureTask : futureTasks) {
				HashMap<Pixel, Integer> calculatedPixel = futureTask.get();

				calculatedPixels.add(calculatedPixel);
			}
		}
		catch (InterruptedException | ExecutionException interruptedException) {
			interruptedException.printStackTrace();
		}

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.fillRect(0, 0, width - 1, height - 1);

		for (var calculatedPixel : calculatedPixels) {
			calculatedPixel.forEach((pixel, color) -> {
				bufferedImage.setRGB(pixel.getX(), pixel.getY(), color);
			});
		}

		graphics2D.setColor(Color.GRAY);
		graphics2D.drawRect(0, 0, width - 2, height - 2);

		try {
			ImageIO.write(bufferedImage, "PNG", new File(outputFileName));
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}

		long finishTimestamp = Calendar.getInstance().getTimeInMillis();
		long overallTime = finishTimestamp - startTimestamp;

		if (quietOutput) {
			System.out.println(overallTime);
		}
		else {
			System.out.printf("Threads used in current run: %d%n", threadCount);
			System.out.printf("Total execution time for current run (millis): %d%n", overallTime);
		}
	}

	private static void parseConsoleInput(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption("s", true, "output image dimensions e.g. 640x480");
		options.addOption("r", true, "complex plane bounds e.g. -2.0:2.0:-1.0:1.0");
		options.addOption("t", true, "number of tasks(threads)");
		options.addOption("o", true, "name of the output file");
		options.addOption("q", false, "quiet running of program, without additional logs");

		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = parser.parse(options, args);

		if (commandLine.hasOption("s")) {
			String[] imageDimensions = commandLine.getOptionValue("s").split("x");
			width = Integer.parseInt(imageDimensions[0]);
			height = Integer.parseInt(imageDimensions[1]);
		}

		if (commandLine.hasOption("r")) {
			String[] complexPlaneBounds = commandLine.getOptionValue("r").split(":");
		 	minReal = Double.parseDouble(complexPlaneBounds[0]);
		 	maxReal = Double.parseDouble(complexPlaneBounds[1]);
		 	minImaginary = Double.parseDouble(complexPlaneBounds[2]);
		 	maxImaginary = Double.parseDouble(complexPlaneBounds[3]);
		}

		if (commandLine.hasOption("t")) {
			threadCount = Integer.parseInt(commandLine.getOptionValue("t"));
		}

		if (commandLine.hasOption("o")) {
			outputFileName = commandLine.getOptionValue("o");
		}

		if (commandLine.hasOption("q")) {
			quietOutput = true;
		}
	}
}
