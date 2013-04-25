import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class ImagePanel extends JComponent {
    private Image image;
    public ImagePanel(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
class GUI extends JFrame implements ActionListener {
	String path1=null,path2=null;
	JTextField fileName,typeName;
	JButton browse, submit, help, about;
	BufferedImage img = null;
	JRadioButton opt1 = new JRadioButton("Lossless Compression");
	JRadioButton opt2 = new JRadioButton("Lossy Compression"); 
	JTextField maxsize;

	GUI() {
		this(600, 600);
	}

	GUI(int width, int height) {
		this.setSize(width, height);
		this.setTitle("Image Compression using Wavelet Transforms");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.getContentPane().add(new Component("sample.jpeg"));
		try {
			img = ImageIO.read(new File("/home/regulus/rbowroad.jpg"));
		} catch (IOException e) {}
		this.setContentPane(new ImagePanel(img));
		createUI();
	}

	public void actionPerformed(ActionEvent e) {

		JFileChooser chooser = new JFileChooser();
		if (e.getSource() == help) {
			JOptionPane.showMessageDialog(null, "This project requires an image file as input. \n 1. Use the browse button to choose this input file\n 2. Enter the maximum size that the image might be compressed in case of Lossy transforms and the SNR.\n 3. Choose whether Lossy or Lossless compression is to be used. \n 4. Click the compress button to give the compressed image bit and view it in image viewer provided. ");
		}
		if (e.getSource() == about) {
			JOptionPane.showMessageDialog(null, "The project was made in partial fulfillment of Software Engineering course.\n Project Advisor : Prof. Joy Prabhakaran and Prof. Jayprakash L\n Created by : \n 1. Haripriya V --- haripriya.v@iiitb.org\n 2. Krishna Prem --- krishna.prem@iiitb.org\n 3. Rinitha Raj --- rinitha.raj@iiitb.org\n 4. Vikas Verma --- vikas.verma@iiitb.org\n Special Credits : Carlos Rueda");
		}
		if (e.getSource() == browse) {

			FileNameExtensionFilter filter = new FileNameExtensionFilter("imge files",
					"jpg","jpeg","pgm","gif","raw");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				path1=chooser.getSelectedFile().getPath();
				path2=chooser.getSelectedFile().getName();
				fileName.setText(path1);
			}
		}
		if (e.getSource() == submit) {
			boolean flag = validation();
			if (flag) {
				String temp1="./shellnew.sh"+ " " + path1;
				//JOptionPane.showMessageDialog(null, "Ok:  " + temp1);
		        Process ls=null,ls1=null;
		        BufferedReader input=null;
		        String line=null;
		        int val=0;
		        if(opt2.isSelected())
		        {
		        	try {
		        		val=Integer.parseInt(typeName.getText());
		        			if(val>40)
		        				ls= Runtime.getRuntime().exec(new String[]{"sh","shell1.sh",path1,path2,maxsize.getText()});
		        			else 
		        				ls= Runtime.getRuntime().exec(new String[]{"sh","shell3.sh",path1,path2,maxsize.getText()});
		                   input = new BufferedReader(new InputStreamReader(ls.getInputStream()));

		                } catch (IOException e1) {
		                    e1.printStackTrace();  
		                    System.exit(1);
		                }
		                
		               
		               try {
		                       while( (line=input.readLine())!=null)
		                        System.out.println(line);

		                } catch (IOException e1) {
		                    e1.printStackTrace();  
		                    System.exit(0);
		                }    
				}
		        else 
		        {
		        	try{
		        			val=Integer.parseInt(typeName.getText());
		                   ls1= Runtime.getRuntime().exec(new String[]{"sh","shell2.sh",path1,path2});
		                   input = new BufferedReader(new InputStreamReader(ls1.getInputStream()));

		        	}catch(Exception eio){}
		        	}
		 
			}
		}
	}


	void refresh() {
		fileName.setText("");

	}
	
	void createUI() {
		
		this.setLayout(null);
		opt1.setSelected(true);
		ButtonGroup bg = new ButtonGroup();
		bg.add(opt1);
		bg.add(opt2);
		fileName = new JTextField();
		typeName = new JTextField();
		maxsize = new JTextField();
		JLabel cratio = new JLabel(" Expected Compression Ratio :");
		JLabel ctype = new JLabel(" Image compression type :");
		JLabel csize = new JLabel(" Lossy transform max bytes(leave 0 if lossless) :");
		JLabel file = new JLabel("File :");
		file.setForeground(Color.white);
		ctype.setForeground(Color.white);
		cratio.setForeground(Color.white);
		csize.setForeground(Color.white);
		Font fnt = new Font("Serit",Font.BOLD,14);
		file.setFont(fnt);
		cratio.setFont(fnt);
		ctype.setFont(fnt);
		csize.setFont(fnt);
		browse = new JButton("Browse");
		submit = new JButton("Compress");
		help = new JButton("Help");
		about = new JButton("About");
		file.setLocation(50, 50); // x and y
		ctype.setLocation(50, 150);
		cratio.setLocation(50, 250);
		csize.setLocation(50, 350);
		fileName.setLocation(100, 50);
		typeName.setLocation(100,300);
		maxsize.setLocation(100,400);
		browse.setLocation(450, 50);
		submit.setLocation(230, 450);
		help.setLocation(360, 450);
		about.setLocation(100, 450);
		opt1.setLocation(300,150);
		opt2.setLocation(300,180);

		file.setSize(100, 50); // width and height
		cratio.setSize(300, 50);
		ctype.setSize(300, 50);
		csize.setSize(400, 50);
		fileName.setSize(300, 30);
		typeName.setSize(300, 30);
		maxsize.setSize(300, 30);
		browse.setSize(120, 30);
		submit.setSize(120, 30);
		help.setSize(120, 30);
		about.setSize(120, 30);
		opt1.setSize(200,50);
		opt2.setSize(200,50);
		maxsize.setText("0");
		typeName.setText("0");
			
		browse.addActionListener(this);
		submit.addActionListener(this);
		help.addActionListener(this);
		about.addActionListener(this);
		
		this.add(ctype);
		this.add(cratio);
		this.add(csize);
		this.add(file);
		this.add(fileName);
		this.add(typeName);
		this.add(browse);
		this.add(submit);
		this.add(opt1);
		this.add(opt2);
		this.add(maxsize);
		this.add(help);
		this.add(about);

	}

	boolean validation() {
		if ((fileName.getText().equals(""))) {
			JOptionPane.showMessageDialog(null,
					"You Can't Leave the FIELDS Blank");
			return false;
		} else {
			String name = fileName.getText();
			int len = name.length();
			if (len < 5) {
				JOptionPane.showMessageDialog(null, "Length");
				return false;
			} else {
				String extension = name.substring(len - 3, len);
				if (extension.equals("jpg")||extension.equals("jpeg")||extension.equals("png")||extension.equals("pgm")) {
					JOptionPane.showMessageDialog(null, "Ok:  " + extension);
					return true;
				} else {
					JOptionPane.showMessageDialog(null, "No");
					return false;
				}
			}
		}
	}
}

class UserInteraction {
	void start() {
		GUI ui = new GUI();
		ui.setVisible(true);

	}
}

class segui {
	public static void main(String[] args) {
		UserInteraction u = new UserInteraction();
		u.start();

	}
}