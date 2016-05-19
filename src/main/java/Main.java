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

	public static final int RECURSION_DEPTH = 1;

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
		System.out.println("It tooks: " + (end - start) + "ns - (" + (end - start) / 100000000 + "s)");
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
				up();
				nextMove(actualDepth + 1, UP);
			}
		}
		if (previousMove != RIGHT) {
			for (int i = 0; i < 4; i++) {
				right();
				nextMove(actualDepth + 1, RIGHT);
			}
		}
		if (previousMove != FRONT) {
			for (int i = 0; i < 4; i++) {
				doMove(FRONT);
				nextMove(actualDepth + 1, FRONT);
			}
		}
		if (previousMove != LEFT) {
			for (int i = 0; i < 4; i++) {
				doMove(LEFT);
				nextMove(actualDepth + 1, LEFT);
			}
		}
		if (previousMove != BACK) {
			for (int i = 0; i < 4; i++) {
				doMove(BACK);
				nextMove(actualDepth + 1, BACK);
			}
		}
		if (previousMove != DOWN) {
			for (int i = 0; i < 4; i++) {
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
		byte[] rightFaceCorners = { topCorners[0], topCorners[3], downCorners[3], downCorners[0] };
		byte[] rightFaceCornersOrientations = { topCornersOrientations[0], topCornersOrientations[3], downCornersOrientations[3], downCornersOrientations[0] };

		short[] rightFaceEdges = { topEdges[3], middleEdges[3], downEdges[3], middleEdges[0] };
		byte[] rightFaceEdgesOrientations = { topEdgesOrientations[3], middleEdgesOrientations[3], downEdgesOrientations[3], middleEdgesOrientations[0] };

		rotate(rightFaceCorners, 1);
		rotate(rightFaceCornersOrientations, 1);
		rotate(rightFaceEdges, 1);
		rotate(rightFaceEdgesOrientations, 1);

		topCorners[0] = rightFaceCorners[0];
		topCorners[3] = rightFaceCorners[1];
		downCorners[3] = rightFaceCorners[2];
		downCorners[0] = rightFaceCorners[3];
		topCornersOrientations[0] = (byte) ((rightFaceCornersOrientations[0] + 2) % 3);
		topCornersOrientations[3] = (byte) ((rightFaceCornersOrientations[1] + 1) % 3);
		downCornersOrientations[3] = (byte) ((rightFaceCornersOrientations[2] + 2) % 3);
		downCornersOrientations[0] = (byte) ((rightFaceCornersOrientations[3] + 1) % 3);

		topEdges[3] = rightFaceEdges[0];
		middleEdges[3] = rightFaceEdges[1];
		downEdges[3] = rightFaceEdges[2];
		middleEdges[0] = rightFaceEdges[3];
		//this down is not working
		topEdgesOrientations[3] = (byte) ((rightFaceEdgesOrientations[0] + 1) % 2);
		middleEdgesOrientations[3] = (byte) ((rightFaceEdgesOrientations[1] + 1) % 2);
		downEdgesOrientations[3] = (byte) ((rightFaceEdgesOrientations[2] + 1) % 2);
		middleEdgesOrientations[0] = (byte) ((rightFaceEdgesOrientations[3] + 1) % 2);

		printCubeStatus("RIGHT");

	}

	private static void doMove(byte move) {
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
