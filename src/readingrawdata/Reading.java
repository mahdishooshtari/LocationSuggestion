/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package readingrawdata;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Mahdi_Shooshtari
 */
class Reading {
    JFrame frame;
    JTextField textField;
    String path;
    String cordinates = "";
    private int[] xPoints;
    private int[] yPoints;
    
    public Reading() {
        frame = new JFrame("Reading Raw Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        JPanel panel = new JPanel();
        path = "D:\\university\\MSc\\Semester2\\694- Mobility Data Management\\GeoLife\\Data\\000\\Trajectory\\";
        textField = new JTextField(path+"20081023025304.plt");
        
        
        JButton button = new JButton("Show!");
        
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                path = textField.getText();
                try {
                    try {
                        cordinates = Drawing(path);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(Reading.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParseException ex) {
                        Logger.getLogger(Reading.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Reading.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        
        textField.setSize(200, 50);
        
        
        
        panel.add(textField);
        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    public String Drawing(String path) throws IOException, MalformedURLException, ParseException{
        String cordinates = "";
        Charset charset = Charset.forName("UTF-8");
        int lineCounter = 0;
        
        PrintWriter writer_x = new PrintWriter("output_x.txt", "UTF-8");
        PrintWriter writer_y = new PrintWriter("output_y.txt", "UTF-8");        

        double last_x = 0.0;
        double last_y = 0.0;
        double distance = 12.0;
        double minDistance = 24;
        double maxDistance = -1;
        for (String line : Files.readAllLines(Paths.get(path),charset)){
//            System.out.println(line);
            
            if (lineCounter >= 6){
                String[] tempString=line.split(",");
                String tempX = tempString[0]; //Latitude
//                xPoints[lineCounter-6] = Integer.parseInt(tempX);
                String tempY = tempString[1];//Longtitude
//                yPoints[lineCounter-6] = Integer.parseInt(tempY);
                //tempString[2] Always is 0
                String tempZ = tempString[3];//Altitude
                String tempNumberOfDays = tempString[4];
                String date = tempString[5];
                String time = tempString[6];

                if (lineCounter==6){
                    last_x = Double.parseDouble(tempX);
                    last_y = Double.parseDouble(tempY);
                }else{
                    distance = Math.sqrt(Math.pow((Double.parseDouble(tempX)-last_x), 2) + Math.pow((Double.parseDouble(tempY)-last_y), 2));
                    if (distance < minDistance){
                        minDistance = distance;
                    }
                    if (distance > maxDistance){
                        maxDistance = distance;
                    }
                }
                writer_x.println(tempX);
                writer_y.println(tempY);
//                RetreiveGPS rGPS = new RetreiveGPS(tempX,tempY);
            }
            lineCounter ++;
        }
        
        System.out.println("minDistance = "+minDistance);
        System.out.println("maxDistance = "+maxDistance);
        writer_x.close();
        writer_y.close();
        //Graphics2D.drawPolyline(xPoints, yPoints);
        
        return cordinates;
    }
   
}


