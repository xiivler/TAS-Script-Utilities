package tas;

//Java program to create open or
//save dialog using JFileChooser
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;
class TAS_Script_Utilities extends JFrame implements ActionListener {

 static JTextField savePath;
 static JTextField loadPath;
 static JLabel message;
 
 private static File file;
 private static File destFile;
 
 private static String OS = System.getProperty("os.name");

 // a default constructor
 TAS_Script_Utilities()
 {
 }

 public static void main(String args[])
 {
	 
     // frame to contains GUI elements
     JFrame f = new JFrame("TAS-Script Utilities");

     // set the size of the frame
     f.setSize(700, 237);
     
     f.setResizable(false);

     f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

     JButton save = new JButton("Browse");
     JButton load = new JButton("Browse");
     JButton unnumber = new JButton("Remove line numbers");
     JButton number = new JButton("Number lines");
     JButton addEmpty = new JButton("Write out empty lines");
     JButton removeEmpty = new JButton("Remove empty lines");
     unnumber.setActionCommand("unnumber");
     number.setActionCommand("number");
     addEmpty.setActionCommand("addEmpty");
     removeEmpty.setActionCommand("removeEmpty");
     save.setActionCommand("save");
     load.setActionCommand("load");
     
     savePath = new JTextField(20);
     loadPath = new JTextField(20);
     
     message = new JLabel(" ");

     TAS_Script_Utilities f1 = new TAS_Script_Utilities();

     save.addActionListener(f1);
     load.addActionListener(f1);
     unnumber.addActionListener(f1);
     number.addActionListener(f1);
     addEmpty.addActionListener(f1);
     removeEmpty.addActionListener(f1);

     JPanel all = new JPanel();
     all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
     
     // make a panel to add the buttons and labels
     JPanel input = new JPanel();
     input.setBorder(new EmptyBorder(new Insets(20, 14, 10, 20)));
     input.setLayout(new BoxLayout(input, BoxLayout.X_AXIS));
     JLabel inPath = new JLabel("Input Path: ", JLabel.RIGHT);
     if (OS.indexOf("Mac") >= 0)
    	 inPath.setPreferredSize(new Dimension(90, 20));
     else
         inPath.setPreferredSize(new Dimension(80, 20));
     input.add(inPath);
     input.add(loadPath);
     input.add(load);
     
     JPanel output = new JPanel();
     output.setBorder(new EmptyBorder(new Insets(20, 14, 20, 20)));
     output.setLayout(new BoxLayout(output, BoxLayout.X_AXIS));
     JLabel outPath = new JLabel("Output Path: ", JLabel.RIGHT);
     if (OS.indexOf("Mac") >= 0)
    	 outPath.setPreferredSize(new Dimension(90, 20));
     else
         outPath.setPreferredSize(new Dimension(80, 20));
     output.add(outPath);
     output.add(savePath);
     output.add(save);
     
     JPanel generate = new JPanel();
     generate.add(unnumber);
     generate.add(number);
     generate.add(addEmpty);
     generate.add(removeEmpty);
     
     JPanel text = new JPanel();
     text.add(message);
     
     all.add(input);
     all.add(output);
     all.add(generate);
     all.add(Box.createRigidArea(new Dimension(0,5)));
     all.add(text);
     all.add(Box.createRigidArea(new Dimension(0,20)));

     f.add(all);
     
     f.setVisible(true);
 }
 
 public void actionPerformed(ActionEvent evt)
 {
     // if the user presses the save button show the save dialog
     String com = evt.getActionCommand();
     
     if (com.equals("save")) {
         // create an object of JFileChooser class
         JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
         j.setDialogTitle("Choose Output Location");
         
         if (savePath.getText().length() > 0)
  			j.setSelectedFile(new File(savePath.getText()));

         // invoke the showsSaveDialog function to show the save dialog
         int r = j.showSaveDialog(null);

         // if the user selects a file
         if (r == JFileChooser.APPROVE_OPTION)
         {
             savePath.setText(j.getSelectedFile().getAbsolutePath());
         }
     }

     // if the user presses the open dialog show the open dialog
     else if (com.equals("load")) {
         // create an object of JFileChooser class
         JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
         
        j.setDialogTitle("Choose Source File");
 		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt files", "txt");
 		j.setFileFilter(filter);
 		
 		if (loadPath.getText().length() > 0)
 			j.setSelectedFile(new File(loadPath.getText()));

         // invoke the showsOpenDialog function to show the save dialog
         int r = j.showOpenDialog(null);

         // if the user selects a file
         if (r == JFileChooser.APPROVE_OPTION)
         {
             // set the label to the path of the selected file
        	 String filepath = j.getSelectedFile().getAbsolutePath();
             loadPath.setText(filepath);
             savePath.setText(filepath);
         }
     }
     
     else
    	 run(com);
 }
 
 private static void run(String action) {
	
	long start = System.currentTimeMillis();
	
	File destination = null;
	
	Scanner scan = null;
	try {
		scan = 	new Scanner(new File(loadPath.getText()));
	}
	catch (FileNotFoundException e) {
		message.setText("Source file not found.");
		return;
	}
 	
 	PrintWriter print = null;
 	try {
 		destination = new File(savePath.getText() + "-temp");
		print = new PrintWriter(destination);
 	}
 	catch (FileNotFoundException e) {
 		message.setText("Output destination not found.");
 		scan.close();
 		return;
 	}
 	
 	if (action.equals("unnumber"))
 		unnumber(scan, print);
 	else if (action.equals("number"))
 		number(scan, print);
 	else if (action.equals("addEmpty"))
 		addEmptyLines(scan, print);
 	else if (action.equals("removeEmpty"))
 		removeEmptyLines(scan, print);


 	scan.close();
 	print.close();
 	
 	File existing = new File(savePath.getText());
 	existing.delete();
 	destination.renameTo(existing);
 	
 	DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
 	
 	long end = System.currentTimeMillis();
 	long time = end - start;
 	message.setText("File successfully generated in " + time + " ms at " + timeFormat.format(LocalTime.now()) + ".");
 	}
 
 	public static void unnumber(Scanner scan, PrintWriter print) {
 	 	while (scan.hasNextLine()) {
 	 		Scanner s = new Scanner(scan.nextLine());
 	 		if (s.hasNextInt())
 	 			s.nextInt();
 	 		print.println(s.nextLine().trim());
 	 	}
 	}
 	
 	public static void number(Scanner scan, PrintWriter print) {
 		int line = 1;
 	 	while (scan.hasNextLine()) {
 	 		Scanner s = new Scanner(scan.nextLine());
 	 		if (s.hasNextInt())
 	 			s.nextInt();
 	 		print.println(line + " " + s.nextLine().trim());
 	 		line++;
 	 	}
 	}
 	
 	public static void addEmptyLines(Scanner scan, PrintWriter print) {
 	 	int oldLine = 0;
 	 	int newLine = 1;
 		while (scan.hasNextLine()) {
 			String lineText = scan.nextLine();
 			Scanner s = new Scanner(lineText);
 	 		if (s.hasNextInt()) {
 	 			newLine = s.nextInt();
 	 		}
 	 		for (int i = oldLine + 1; i < newLine; i++)
 	 			print.println(i + " NONE 0;0 0;0");
 	 		print.println(lineText);
 	 		oldLine = newLine;
 	 	}
 	}
 	
 	public static void removeEmptyLines(Scanner scan, PrintWriter print) {
 	 	while (scan.hasNextLine()) {
 	 		String lineText = scan.nextLine();
 	 		Scanner s = new Scanner(lineText);
 	 		if (s.hasNextInt())
 	 			s.nextInt();
 	 		if (!s.nextLine().trim().equals("NONE 0;0 0;0"))
 	 			print.println(lineText);
 	 	}
 	}
}
