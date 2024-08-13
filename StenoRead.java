import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.Random;

//Nathan Christe
//6.20.24
//This program takes an image with a hidden integer placed into the LSBs of the RGB channels and outputs the hidden integer
public class StenoRead {
	public StenoRead(String inFileName) {
		BufferedImage inI = null;
		try {
			inI = ImageIO.read(new File(inFileName)); // Load input image
			int W = inI.getWidth(); // get the width of the image
			int H = inI.getHeight(); // get the height of the image

			boolean start_hide = false;
			int bitSet = 0;// Used to tell which set of 3 bits are being read
			int hiddenNumber = 0;
			for (int y = 0; y < H; y++) {
				for (int x = 0; x < W; x++) {
					int rgb = inI.getRGB(x, y);
					int RED = (rgb >> 16) & 255;
					int GREEN = (rgb >> 8) & 255;
					int BLUE = (rgb) & 255;

					if ((rgb % 2 != 0) && !start_hide) {
						start_hide = true;
					} else if (start_hide) {
						// Sets the RED BLUE and GREEN to their LSB
						RED = RED % 2;
						BLUE = BLUE % 2;
						GREEN = GREEN % 2;

						int group = RED + (GREEN << 1) + (BLUE << 2); // puts the three LSBs into one set of 3 bits

						hiddenNumber += (group) << (3 * bitSet);// Places bits into correct positions
						bitSet++;
					}
				}
			}
			System.out.println("Hidden number is: " + hiddenNumber);

		} catch (IOException ee) {
			System.err.println(ee);
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		new StenoRead("out.png");
	}
}
