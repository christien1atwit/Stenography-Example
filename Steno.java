import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.util.Random;

//Nathan Christe
//6.20.24
//This program takes an image and hides a number using Stenography.
public class Steno {
	public Steno(String inFileName, String outFileName, int hide) {
		BufferedImage inI = null;
		BufferedImage outI = null;
		Random rand = new Random();
		try {
			inI = ImageIO.read(new File(inFileName)); // Load input image
			int W = inI.getWidth(); // get the width of the image
			int H = inI.getHeight(); // get the height of the image

			// Create same type output image as the input (but has no content)
			outI = new BufferedImage(W, H, inI.getType());

			int hide_offset = rand.nextInt((H * W) / 5);// Selects a random offset to store the hidden number
			int current_pix = 0;
			boolean start_hide = false;
			for (int y = 0; y < H; y++) {
				for (int x = 0; x < W; x++) {
					int rgb = inI.getRGB(x, y);

					int RED = (rgb >> 16) & 255;
					int GREEN = (rgb >> 8) & 255;
					int BLUE = (rgb) & 255;

					int setEmpty = 1;

					// Sets the least signifigant bit of each channel to 0
					RED = ~(setEmpty | (~RED));
					BLUE = ~(setEmpty | (~BLUE));
					GREEN = ~(setEmpty | (~GREEN));

					int new_rgb = (RED << 16) + (GREEN << 8) + (BLUE) + (255 << 24);

					// Sets the LSB of the entire pixel to 1
					// Acts as a flag for the reader to start reading each color channel's LSB
					if (hide_offset == current_pix) {

						start_hide = true;
						new_rgb += 1;

					} else if (start_hide && hide > 0) {
						// Puts the bits of the hidden number into the RBG channels' LSB
						for (int i = 0; i < 3; i++) {
							boolean secIsZeroBit = (hide % 2 == 0);
							hide = hide >> 1;
							if (!secIsZeroBit) {
								new_rgb += (1 << ((2 - i) * 8));
							}
						}

					}

					outI.setRGB(x, y, new_rgb); // set the pixel of the output image to new_rgb
					current_pix++;
				}
			}

			ImageIO.write(outI, "png", new File(outFileName)); // write the image to the output file
		} catch (IOException ee) {
			System.err.println(ee);
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		new Steno("sample.png", "out.png", 134315);
	}
}
