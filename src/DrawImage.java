import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.javatuples.Pair;

import dto.Match.Event;
import dto.Match.Participant;

public class DrawImage extends JPanel {

    private BufferedImage canvas;

    public DrawImage(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        fillCanvas(Color.BLACK);
    }

    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(canvas, null, null);
    }


    public void fillCanvas(Color c) {
        int color = c.getRGB();
        for (int x = 0; x < canvas.getWidth(); x++) {
            for (int y = 0; y < canvas.getHeight(); y++) {
                canvas.setRGB(x, y, color);
            }
        }
        repaint();
    }


    public void drawLine(Color c, int x1, int y1, int x2, int y2) {
        // Implement line drawing
        repaint();
    }

    public void drawPicture(int x1, int y1, Pair<Integer,Integer> mapMaxCoords, Pair<Double,Double> picutreMaxCoords,List<Coords> theData) {
    	int color;
        for(Coords aCoord: theData)
        {
        	for(Event e: aCoord.filteredEvents)
        	{
        		Pair<Integer,Integer> localPair = new Pair<Integer,Integer>(e.getPosition().getX(),e.getPosition().getY());
        		color = getColorLaners(aCoord.participantData,e,2);//Color.GRAY.getRGB();
        		localPair = resizePair(mapMaxCoords,picutreMaxCoords,localPair,true);
            	if(!(localPair.getValue0()<0||localPair.getValue1()<0))
            	{
            		try {
						canvas.setRGB(localPair.getValue0(), localPair.getValue1(), color);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
        	}

        }
        repaint();
    }


	private int getColorLaners(List<Participant> participantData, Event e, int colorType) {
		//MID, MIDDLE, TOP, JUNGLE, BOT, BOTTOM
		String condition = "DEFAULT";
		int color = 0;
		switch(colorType){
		case 0:
		case 1:
			if(colorType==0)
				condition = participantData.get(e.getVictimId()-1).getTimeline().getLane();
			else
			{
				if(e.getKillerId()==0)
					return Color.WHITE.getRGB();
				condition = participantData.get(e.getKillerId()-1).getTimeline().getLane();
			}
			switch(condition){
			case "TOP":
				color = Color.RED.getRGB();
				break;
			case "MID":
			case "MIDDLE":
				color = Color.BLUE.getRGB();
				break;
			case "BOT":
			case "BOTTOM":
				String role = participantData.get(e.getVictimId()-1).getTimeline().getRole();
				if(role.equals("DUO_CARRY"))
					color = Color.ORANGE.getRGB();
				else if(role.equals("DUO_SUPPORT"))
					color = Color.PINK.getRGB();
				else
					color = Color.WHITE.getRGB();
				break;
			case "JUNGLE":
				color = Color.GREEN.getRGB();
				break;
			default:
				color = Color.GRAY.getRGB();
				break;
			}
			break;
		case 2:
			color = Color.WHITE.getRGB();
			return color;
		default:
			condition = "DEFAULT";
		}
		
		return color;
	}

	private Pair<Integer, Integer> resizePair(
			Pair<Integer, Integer> mapMaxCoords,
			Pair<Double, Double> picutreMaxCoords,
			Pair<Integer, Integer> localPair,
			boolean invert) {
    	int x = (int) (localPair.getValue0()*(picutreMaxCoords.getValue0()/mapMaxCoords.getValue0()));
    	int y = (int) (localPair.getValue1()*(picutreMaxCoords.getValue1()/mapMaxCoords.getValue1()));
		if(invert)
			y = (int) (picutreMaxCoords.getValue1() - y);
		return new Pair<Integer,Integer>(x,y);
	}

    
    
	public void drawOval(Color c, int x1, int y1, int width, int height) {
        // Implement oval drawing
        repaint();
    }
    public void saveToFile(String fileName) throws IOException{
        File outputfile = new File(fileName);
        ImageIO.write(canvas, "png", outputfile);
    }


    public static void main(String[] args) {
        int width = 640;
        int height = 480;
        JFrame frame = new JFrame("Direct draw demo");

        DrawImage panel = new DrawImage(width, height);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}