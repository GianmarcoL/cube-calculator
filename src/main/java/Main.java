/**
 * Copyright (c) 2016 Gianmarco Laggia, Alberto D'Este, Marco Falcier
 * <p>
 * Created on 19/05/16
 */

import java.util.Arrays;

/**
 * @author Gianmarco Laggia
 * @since 0.1
 */
public class Main {

	public static final int RECURSION_DEPTH = 4;

	/**
	 * AVAILABLE MOVES
	 */
	public static final byte UP    = 0;
	public static final byte RIGHT = 1;
	public static final byte FRONT = 2;
	public static final byte DOWN  = 3;
	public static final byte LEFT  = 4;
	public static final byte BACK  = 5;

	/**
	 * DIRECTION TIPE
	 */
	public static final byte CW     = 1;
	public static final byte DOUBLE = 2;
	public static final byte CCW    = 3;

	public static long totalMoves = 0;

	/**
	 * CUBE STATUS
	 */
	private static byte[]  topCorners;
	private static byte[]  topCornersOrientations;
	private static short[] topEdges;
	private static byte[]  topEdgesOrientations;

	private static short[] middleEdges;
	private static byte[]  middleEdgesOrientations;

	private static byte[]  downCorners;
	private static byte[]  downCornersOrientations;
	private static short[] downEdges;
	private static byte[]  downEdgesOrientations;

	public static void main(String[] args) {
		initializeCubeSolved();
		printCubeStatus("");
		long start = System.nanoTime();
		nextMove(0, (byte) 7);
		System.out.println(totalMoves);
		long end = System.nanoTime();
		System.out.println("It tooks: " + (end - start) + "ns - (" + (end - start) / 1000000000 + "s)");
		System.out.println("That is something like "+ ((float)(end - start) / totalMoves) / 1000 + "µs per move");
	}

	private static void initializeCubeSolved() {

		//CORNERS
		topCorners = new byte[] { 0, 1, 2, 3 };
		topCornersOrientations = new byte[] { 0, 0, 0, 0 };
		downCorners = new byte[] { 4, 5, 6, 7 };
		downCornersOrientations = new byte[] { 0, 0, 0, 0 };

		//EDGES
		topEdges = new short[] { 0, 1, 2, 3 };
		topEdgesOrientations = new byte[] { 0, 0, 0, 0 };
		middleEdges = new short[] { 4, 5, 6, 7 };
		middleEdgesOrientations = new byte[] { 0, 0, 0, 0 };
		downEdges = new short[] { 8, 9, 10, 11 };
		downEdgesOrientations = new byte[] { 0, 0, 0, 0 };
	}

	private static void printCubeStatus(String move) {
		String status = "\n" + move + "--------------------------------------\nP:" + Arrays.toString(topCorners) + Arrays.toString(topEdges) + Arrays
				.toString(middleEdges) + Arrays.toString(downCorners) + Arrays.toString(downEdges) + "\nR:" + Arrays.toString(topCornersOrientations) + Arrays
				.toString(topEdgesOrientations) + Arrays.toString(middleEdgesOrientations) + Arrays.toString(downCornersOrientations) + Arrays
				.toString(downEdgesOrientations);
		System.out.println(status);

	}

	private static void nextMove(int actualDepth, byte previousMove) {
		if (actualDepth == RECURSION_DEPTH) {
			return;
		}
		if (previousMove != UP) {
			for (int i = 0; i < 4; i++) {
				countMove();
				up();
				nextMove(actualDepth + 1, UP);
			}
		}
		if (previousMove != RIGHT) {
			for (int i = 0; i < 4; i++) {
				countMove();
				right();
				nextMove(actualDepth + 1, RIGHT);
			}
		}
		if (previousMove != FRONT) {
			for (int i = 0; i < 4; i++) {
				countMove();
				front();
				nextMove(actualDepth + 1, FRONT);
			}
		}
		if (previousMove != LEFT) {
			for (int i = 0; i < 4; i++) {
				countMove();
				left();
				nextMove(actualDepth + 1, LEFT);
			}
		}
		if (previousMove != BACK) {
			for (int i = 0; i < 4; i++) {
				countMove();
				back();
				nextMove(actualDepth + 1, BACK);
			}
		}
		if (previousMove != DOWN) {
			for (int i = 0; i < 4; i++) {
				countMove();
				down();
				nextMove(actualDepth + 1, DOWN);
			}
		}
	}

	private static void up() {
		rotate(topCorners, 1);
		rotate(topCornersOrientations, 1);
		rotate(topEdges, 1);
		rotate(topEdgesOrientations, 1);
		printCubeStatus("UP  ");
	}

	private static void down() {
		rotate(downCorners, 3);
		rotate(downCornersOrientations, 3);
		rotate(downEdges, 3);
		rotate(downEdgesOrientations, 3);
		printCubeStatus("DOWN");
	}

	private static void right() {
		byte[] rightCorners = { topCorners[0], topCorners[3], downCorners[3], downCorners[0] };
		byte[] rightCornersOrientations = { topCornersOrientations[0], topCornersOrientations[3], downCornersOrientations[3], downCornersOrientations[0] };

		short[] rightEdges = { topEdges[3], middleEdges[3], downEdges[3], middleEdges[0] };
		byte[] rightEdgesOrientations = { topEdgesOrientations[3], middleEdgesOrientations[3], downEdgesOrientations[3], middleEdgesOrientations[0] };

		rotate(rightCorners, 1);
		rotate(rightCornersOrientations, 1);
		rotate(rightEdges, 1);
		rotate(rightEdgesOrientations, 1);

		topCorners[0] = rightCorners[0];
		topCorners[3] = rightCorners[1];
		downCorners[3] = rightCorners[2];
		downCorners[0] = rightCorners[3];
		topCornersOrientations[0] = (byte) ((rightCornersOrientations[0] + 2) % 3);
		topCornersOrientations[3] = (byte) ((rightCornersOrientations[1] + 1) % 3);
		downCornersOrientations[3] = (byte) ((rightCornersOrientations[2] + 2) % 3);
		downCornersOrientations[0] = (byte) ((rightCornersOrientations[3] + 1) % 3);

		topEdges[3] = rightEdges[0];
		middleEdges[3] = rightEdges[1];
		downEdges[3] = rightEdges[2];
		middleEdges[0] = rightEdges[3];
		topEdgesOrientations[3] = (byte) ((rightEdgesOrientations[0] + 1) % 2);
		middleEdgesOrientations[3] = (byte) ((rightEdgesOrientations[1] + 1) % 2);
		downEdgesOrientations[3] = (byte) ((rightEdgesOrientations[2] + 1) % 2);
		middleEdgesOrientations[0] = (byte) ((rightEdgesOrientations[3] + 1) % 2);

		printCubeStatus("RIGHT");

	}

	private static void left() {
		byte[] leftCorners = { topCorners[2], topCorners[1], downCorners[1], downCorners[2] };
		byte[] leftCornersOrientations = { topCornersOrientations[2], topCornersOrientations[1], downCornersOrientations[1], downCornersOrientations[2] };

		short[] leftEdges = { topEdges[1], middleEdges[1], downEdges[1], middleEdges[2] };
		byte[] leftEdgesOrientations = { topEdgesOrientations[1], middleEdgesOrientations[1], downEdgesOrientations[1], middleEdgesOrientations[2] };

		rotate(leftCorners, 1);
		rotate(leftCornersOrientations, 1);
		rotate(leftEdges, 1);
		rotate(leftEdgesOrientations, 1);

		topCorners[2] = leftCorners[0];
		topCorners[1] = leftCorners[1];
		downCorners[1] = leftCorners[2];
		downCorners[2] = leftCorners[3];
		topCornersOrientations[2] = (byte) ((leftCornersOrientations[0] + 2) % 3);
		topCornersOrientations[1] = (byte) ((leftCornersOrientations[1] + 1) % 3);
		downCornersOrientations[1] = (byte) ((leftCornersOrientations[2] + 2) % 3);
		downCornersOrientations[2] = (byte) ((leftCornersOrientations[3] + 1) % 3);

		topEdges[1] = leftEdges[0];
		middleEdges[1] = leftEdges[1];
		downEdges[1] = leftEdges[2];
		middleEdges[2] = leftEdges[3];
		topEdgesOrientations[1] = (byte) ((leftEdgesOrientations[0] + 1) % 2);
		middleEdgesOrientations[1] = (byte) ((leftEdgesOrientations[1] + 1) % 2);
		downEdgesOrientations[1] = (byte) ((leftEdgesOrientations[2] + 1) % 2);
		middleEdgesOrientations[2] = (byte) ((leftEdgesOrientations[3] + 1) % 2);

		printCubeStatus("LEFT");

	}

	private static void front() {
		byte[] frontCorners = { topCorners[1], topCorners[0], downCorners[0], downCorners[1] };
		byte[] frontCornersOrientations = { topCornersOrientations[1], topCornersOrientations[0], downCornersOrientations[0], downCornersOrientations[1] };

		short[] frontEdges = { topEdges[0], middleEdges[0], downEdges[0], middleEdges[1] };
		byte[] frontEdgesOrientations = { topEdgesOrientations[0], middleEdgesOrientations[0], downEdgesOrientations[0], middleEdgesOrientations[1] };

		rotate(frontCorners, 1);
		rotate(frontCornersOrientations, 1);
		rotate(frontEdges, 1);
		rotate(frontEdgesOrientations, 1);

		topCorners[1] = frontCorners[0];
		topCorners[0] = frontCorners[1];
		downCorners[0] = frontCorners[2];
		downCorners[1] = frontCorners[3];
		topCornersOrientations[1] = (byte) ((frontCornersOrientations[0] + 2) % 3);
		topCornersOrientations[0] = (byte) ((frontCornersOrientations[1] + 1) % 3);
		downCornersOrientations[0] = (byte) ((frontCornersOrientations[2] + 2) % 3);
		downCornersOrientations[1] = (byte) ((frontCornersOrientations[3] + 1) % 3);

		topEdges[0] = frontEdges[0];
		middleEdges[0] = frontEdges[1];
		downEdges[0] = frontEdges[2];
		middleEdges[1] = frontEdges[3];
		topEdgesOrientations[0] = (byte) ((frontEdgesOrientations[0] + 2) % 2);
		middleEdgesOrientations[0] = (byte) ((frontEdgesOrientations[1] + 2) % 2);
		downEdgesOrientations[0] = (byte) ((frontEdgesOrientations[2] + 2) % 2);
		middleEdgesOrientations[1] = (byte) ((frontEdgesOrientations[3] + 2) % 2);

		printCubeStatus("FRONT");

	}

	private static void back() {
		byte[] backCorners = { topCorners[3], topCorners[2], downCorners[2], downCorners[3] };
		byte[] backCornersOrientations = { topCornersOrientations[3], topCornersOrientations[2], downCornersOrientations[2], downCornersOrientations[3] };

		short[] backEdges = { topEdges[2], middleEdges[2], downEdges[2], middleEdges[3] };
		byte[] backEdgesOrientations = { topEdgesOrientations[2], middleEdgesOrientations[2], downEdgesOrientations[2], middleEdgesOrientations[3] };

		rotate(backCorners, 1);
		rotate(backCornersOrientations, 1);
		rotate(backEdges, 1);
		rotate(backEdgesOrientations, 1);

		topCorners[3] = backCorners[0];
		topCorners[2] = backCorners[1];
		downCorners[2] = backCorners[2];
		downCorners[3] = backCorners[3];
		topCornersOrientations[3] = (byte) ((backCornersOrientations[0] + 2) % 3);
		topCornersOrientations[2] = (byte) ((backCornersOrientations[1] + 1) % 3);
		downCornersOrientations[2] = (byte) ((backCornersOrientations[2] + 2) % 3);
		downCornersOrientations[3] = (byte) ((backCornersOrientations[3] + 1) % 3);

		topEdges[2] = backEdges[0];
		middleEdges[2] = backEdges[1];
		downEdges[2] = backEdges[2];
		middleEdges[3] = backEdges[3];
		topEdgesOrientations[2] = (byte) ((backEdgesOrientations[0] + 2) % 2);
		middleEdgesOrientations[2] = (byte) ((backEdgesOrientations[1] + 2) % 2);
		downEdgesOrientations[2] = (byte) ((backEdgesOrientations[2] + 2) % 2);
		middleEdgesOrientations[3] = (byte) ((backEdgesOrientations[3] + 2) % 2);

		printCubeStatus("BACK");

	}

	private static void countMove() {
		totalMoves++;
	}

	public static void rotate(short[] arr, int order) {
		if (arr == null || arr.length == 0 || order < 0) {
			throw new IllegalArgumentException("Illegal argument!");
		}

		if (order > arr.length) {
			order = order % arr.length;
		}

		//length of first part
		int a = arr.length - order;

		reverse(arr, 0, a - 1);
		reverse(arr, a, arr.length - 1);
		reverse(arr, 0, arr.length - 1);

	}

	public static void reverse(short[] arr, int left, int right) {
		if (arr == null || arr.length == 1)
			return;

		while (left < right) {
			short temp = arr[left];
			arr[left] = arr[right];
			arr[right] = temp;
			left++;
			right--;
		}
	}

	public static void rotate(byte[] arr, int order) {
		if (arr == null || arr.length == 0 || order < 0) {
			throw new IllegalArgumentException("Illegal argument!");
		}

		if (order > arr.length) {
			order = order % arr.length;
		}

		//length of first part
		int a = arr.length - order;

		reverse(arr, 0, a - 1);
		reverse(arr, a, arr.length - 1);
		reverse(arr, 0, arr.length - 1);

	}

	public static void reverse(byte[] arr, int left, int right) {
		if (arr == null || arr.length == 1)
			return;

		while (left < right) {
			byte temp = arr[left];
			arr[left] = arr[right];
			arr[right] = temp;
			left++;
			right--;
		}
	}
}
