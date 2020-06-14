import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ConcurrentLinkedQueue;
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

	public static String outputFileName = "zad17.png";

	public static boolean quietOutput = false;

	public static int maxPointIterations = 500;

	public static boolean dynamicLoadBalancing = false;

	public static void main(String[] args) {
		long startTimestamp = Calendar.getInstance().getTimeInMillis();

		try {
			parseConsoleInput(args);
		}
		catch (ParseException parseException) {
			parseException.printStackTrace();
			System.exit(1);
		}

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		setColorAndFillRectangle(bufferedImage, Color.WHITE);

		if (dynamicLoadBalancing) {
			if (!quietOutput) {
				System.out.printf("dynamic load balancing used%n");
			}
			dynamicLoadBalancing(bufferedImage);
		}
		else {
			if (!quietOutput) {
				System.out.printf("static load balancing used%n");
			}
			staticLoadBalancing(bufferedImage);
		}

		try {
			ImageIO.write(bufferedImage, "PNG", new File(outputFileName));
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
			System.exit(4);
		}
		
		long finishTimestamp = Calendar.getInstance().getTimeInMillis();
		long overallTime = finishTimestamp - startTimestamp;

		System.out.printf("Total execution time for current run (millis): %d%n", overallTime);

		if (!quietOutput) {
			System.out.printf("Threads used in current run: %d%n", threadCount);
		}
	}

	private static void parseConsoleInput(String[] args) throws ParseException {
		Options options = new Options();

		options.addOption("s", true, "output image dimensions e.g. 640x480");
		options.addOption("r", true, "complex plane bounds e.g. -2.0:2.0:-1.0:1.0");
		options.addOption("t", true, "number of tasks(threads)");
		options.addOption("o", true, "name of the output file");
		options.addOption("q", false, "quiet running of program, without additional logs");
		options.addOption("i", true, "number of iterations");
		options.addOption("d", false, "if present, dynamic load balancing is used");
		options.addOption("h", false, "print information about the program");

		CommandLineParser parser = new DefaultParser();
		CommandLine commandLine = parser.parse(options, args);

		if (commandLine.hasOption("h")) {
			printHelp(options);
			System.exit(1);
		}

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

		if (commandLine.hasOption("i")) {
			maxPointIterations = Integer.parseInt(commandLine.getOptionValue("i"));
		}

		if (commandLine.hasOption("d")) {
			dynamicLoadBalancing = true;
		}
	}

	private static void printHelp(Options options) {
		String header = "program calculating fractal image\n";
		String footer = "author : Ivan Chuchulski\n";
		HelpFormatter helpFormatter = new HelpFormatter();

		helpFormatter.printHelp("runMe [option]...", header, options, footer);
	}

	private static void setColorAndFillRectangle(BufferedImage bufferedImage, Color color) {
		Graphics2D graphics2D = bufferedImage.createGraphics();

		graphics2D.setColor(color);

		graphics2D.fillRect(0, 0, Main.width - 1, Main.height - 1);
	}

	private static void dynamicLoadBalancing(BufferedImage bufferedImage) {
		ArrayList<DynamicSlaveThread> fractalThreads = new ArrayList<>(threadCount);
		ConcurrentLinkedQueue<RowTask> tasks = new ConcurrentLinkedQueue<>();

		for (int row = 0; row < Main.height; row++) {
			tasks.add(new RowTask(row));
		}

		for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
			DynamicSlaveThread dynamicSlaveThread = new DynamicSlaveThread(threadIndex, bufferedImage, tasks);

			fractalThreads.add(dynamicSlaveThread);

			dynamicSlaveThread.start();
		}

		try {
			for (DynamicSlaveThread dynamicSlaveThread : fractalThreads) {
				dynamicSlaveThread.join();
			}
		}
		catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
			System.exit(2);
		}
	}

	private static void staticLoadBalancing(BufferedImage bufferedImage) {
		ArrayList<StaticSlaveThread> staticSlaves = new ArrayList<>(threadCount);

		for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
			StaticSlaveThread staticSlaveThread = new StaticSlaveThread(threadIndex, bufferedImage);

			staticSlaves.add(staticSlaveThread);

			staticSlaveThread.start();
		}

		try {
			for (StaticSlaveThread staticSlave : staticSlaves) {
				staticSlave.join();
			}
		}
		catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
			System.exit(3);
		}
	}

}
