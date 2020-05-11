import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;

import org.apache.commons.cli.*;

public class Main {
	public static int width = 640;
	public static int height = 480;

	public static int threadCount = 1;

	public static double minReal = -2.0;
	public static double maxReal = 2.0;
	public static double minImaginary = -2.0;
	public static double maxImaginary = 2.0;

	public static String outputFileName = "test.png";

	public static boolean quietOutput = false;

	public static void main(String[] args) {
		long startTimestamp = Calendar.getInstance().getTimeInMillis();

		try {
			parseConsoleInput(args);
		}
		catch (ParseException parseException) {
			parseException.printStackTrace();
			System.exit(1);
		}


		ArrayList<FutureTask<BufferedImage>> futureTasks = new ArrayList<>(threadCount);
		ArrayList<Thread> threads = new ArrayList<>(threadCount);
		ArrayList<BufferedImage> threadBufferedImages = new ArrayList<>(threadCount);

		final int numberOfRowsPerThread = height / threadCount;
		final int rowsRemained = height % threadCount;

		int threadsIndex = 1;
		int threadStartingRow = 0;

		// if the rows are not divided equally between threads
		while (threadsIndex <= rowsRemained) {
			int currentThreadNumberOfRows = numberOfRowsPerThread + 1;

			FractalPartThread fractalPartThread = new FractalPartThread(threadsIndex, threadStartingRow, currentThreadNumberOfRows);
			FutureTask<BufferedImage> futureTask = new FutureTask<>(fractalPartThread);
			Thread thread = new Thread(futureTask);

			futureTasks.add(futureTask);
			thread.start();

			threadStartingRow += numberOfRowsPerThread +  1;

			threadsIndex++;
		}

		while (threadsIndex <= threadCount) {
			FractalPartThread fractalPartThread = new FractalPartThread(threadsIndex, threadStartingRow, numberOfRowsPerThread);
			FutureTask<BufferedImage> futureTask = new FutureTask<>(fractalPartThread);
			Thread thread = new Thread(futureTask);

			futureTasks.add(futureTask);
			thread.start();

			threadStartingRow += numberOfRowsPerThread;

			threadsIndex++;
		}

		// wait for all threads to finish their jobs
		try {
			for (FutureTask<BufferedImage> futureTask : futureTasks) {
				BufferedImage threadBufferedImage = futureTask.get();
				threadBufferedImages.add(threadBufferedImage);
			}
		}
		catch (InterruptedException | ExecutionException interruptedException) {
			interruptedException.printStackTrace();
		}

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setColor(Color.WHITE);
		graphics2D.fillRect(0, 0, width - 1, height - 1);

//		TODO : make it work when rows are unevenly spread
		int imageIndex = 1;
		int imageStartingRow = 0;
		for (BufferedImage threadBufferedImage : threadBufferedImages) {
			for (int yPixel = imageStartingRow; yPixel < numberOfRowsPerThread * imageIndex; yPixel++) {
				for (int xPixel = 0; xPixel < Main.width; xPixel++) {
					 int pixelColor = threadBufferedImage.getRGB(xPixel, yPixel);
					 bufferedImage.setRGB(xPixel, yPixel, pixelColor);
				}
			}

			imageStartingRow += numberOfRowsPerThread;
			imageIndex++;
		}


		graphics2D.setColor(Color.GRAY);
		graphics2D.drawRect(0, 0, width - 2, height - 2);

		try {
			ImageIO.write(bufferedImage, "PNG", new File(outputFileName));
			// ImageIO.write(bufferedImage, "PNG", new File("image-outputs/blackWhite.png"));
			// ImageIO.write(bufferedImage, "PNG", new File("image-outputs/colored-test.png"));
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}

//		or maybe stop timer before image write?
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
