import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static int frameCount = 0;

    public static void main(String[] args) throws IOException {
        VideoProcessor processor = new VideoProcessor("./ffmpeg/ffmpeg.exe", "./ffmpeg/ffprobe.exe");
        processor.videoToFrames("./input.gif");

        System.out.print("[sofvfx] Processing frames...");
        File directory = new File("./frames/");
        File[] frames = directory.listFiles();

        assert frames != null;
        for (File frame : frames) {
            if (frame.isFile()) {
                File file = new File(frame.getPath());
                BufferedImage img = ImageIO.read(file);
                System.out.print("[sofvfx] Processing " + file.getName() + "\r");
                textToImage(EffectsProcessor.frameToText(img, 6), img.getHeight(), img.getWidth());
            }
        }
        System.out.print("[sofvfx] Finished processing frames.\r\n");

        processor.framesToVideo("./output.gif");
    }

    public static void textToImage(ArrayList<String> lines, int height, int width) throws IOException {
        int scaling = 2;
        BufferedImage textImage = new BufferedImage(width * scaling, height * scaling, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = textImage.getGraphics();

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, height * scaling, width * scaling);

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("", Font.PLAIN, 8 * scaling));
        for (int i = 0; i < lines.size(); i++) {
            graphics.drawString(lines.get(i), 0, (8 * scaling) * i);
        }
        frameCount++;
        ImageIO.write(textImage, "png", new File("./processed/" + ("000".substring(0, (3 - Integer.toString(frameCount).length())) + frameCount) + ".png"));
    }

}

