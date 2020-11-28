import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args)
    {
        // Contains the main GUI that you see when you start the program.
        ///////////////////////////////////////////////////////////////////////////////////////
        JFrame frame = new JFrame("CSE360 Final Project");
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);
        panel.setBackground(Color.lightGray);
        panel.setLayout(new BorderLayout());

        JFileChooser files = new JFileChooser();
        JFileChooser files2 = new JFileChooser();
        files.setDialogTitle("Please choose a csv file");
        files2.setDialogTitle("Please choose a csv file");
        FileNameExtensionFilter type = new FileNameExtensionFilter("*.csv", "csv");
        files.setFileFilter(type);
        files2.setFileFilter(type);

        JMenuBar bar = new JMenuBar();
        frame.setJMenuBar(bar);

        JMenu file = new JMenu("File");
        JMenuItem item1 = new JMenuItem("Load a Roster");
        JMenuItem item2 = new JMenuItem("Add Attendance");
        JMenuItem item3 = new JMenuItem("Save");
        JMenuItem item4 = new JMenuItem("Plot Data");
        file.add(item1);
        file.add(item2);
        file.add(item3);
        file.add(item4);

        JMenu about = new JMenu("About");
        JMenuItem info = new JMenuItem("About");
        about.add(info);

        bar.add(file);
        bar.add(about);

        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                files.showOpenDialog(frame);
                //Object[][] info = getInfo(files);
                try
                {
                    File f1 = files.getSelectedFile();
                    FileReader fr = new FileReader(f1);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    String[] arr;
                    String delimiter = ",";
                    /*while((line = br.readLine()) != null)
                    {
                        arr = line.split(delimiter);
                        for(int i = 0; i < arr.length; i++)
                        {
                            System.out.print(arr[i] + "\n");
                        }
                    }*/

                    line = br.readLine();
                    arr = line.split(delimiter);
                    String[] columnNames = {"ID", "First Name", "Last Name", "Program", "Level", "ASURITE"};
                    Object names = new Object[1][6];
                    Object[][] info = {{arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]}};
                    JTable table = new JTable(info, columnNames);
                    table.repaint();
                    table.revalidate();
                    table.setGridColor(Color.BLACK);
                    JScrollPane scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scroller.setBackground(Color.lightGray);
                    scroller.setForeground(Color.lightGray);
                    scroller.repaint();
                    scroller.revalidate();
                    panel.add(scroller, BorderLayout.CENTER);
                    panel.repaint();
                    panel.revalidate();
                    frame.repaint();
                    frame.revalidate();
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        });
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame options = new JFrame("Select a Date");

                Panel dateChooser = new Panel();
                dateChooser.setBackground(Color.lightGray);
                dateChooser.setLayout(new GridLayout(3, 2));

                JLabel label1 = new JLabel("Month", SwingConstants.CENTER);
                JLabel label2 = new JLabel("Day", SwingConstants.CENTER);

                dateChooser.add(label1);
                dateChooser.add(label2);

                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                JComboBox combo1 = new JComboBox(months);
                String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
                JComboBox combo2 = new JComboBox(days);

                dateChooser.add(combo1);
                dateChooser.add(combo2);

                JPanel empty = new JPanel();
                empty.setBackground(Color.lightGray);
                dateChooser.add(empty);

                JButton confirmation = new JButton("Confirm");
                confirmation.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(combo1.getSelectedItem() == "Feb" && (combo2.getSelectedItem() == "29" || combo2.getSelectedItem() == "30" || combo2.getSelectedItem() == "31"))
                        {
                            JOptionPane.showMessageDialog(null, "Please choose a valid date!");
                        }
                        else if(combo1.getSelectedItem() == "Apr" && combo2.getSelectedItem() == "31")
                        {
                            JOptionPane.showMessageDialog(null, "Please choose a valid date!");
                        }
                        else if(combo1.getSelectedItem() == "Jun" && combo2.getSelectedItem() == "31")
                        {
                            JOptionPane.showMessageDialog(null, "Please choose a valid date!");
                        }
                        else if(combo1.getSelectedItem() == "Sep" && combo2.getSelectedItem() == "31")
                        {
                            JOptionPane.showMessageDialog(null, "Please choose a valid date!");
                        }
                        else if(combo1.getSelectedItem() == "Nov" && combo2.getSelectedItem() == "31")
                        {
                            JOptionPane.showMessageDialog(null, "Please choose a valid date!");
                        }
                        else
                        {
                            //final String chosenMonth = String.valueOf(combo1.getSelectedItem());
                            //final String chosenDay = String.valueOf(combo2.getSelectedItem());
                            JOptionPane.showMessageDialog(null, "You are adding attendance for " + combo1.getSelectedItem() + ". " + combo2.getSelectedItem());
                            options.dispose();
                            files2.showOpenDialog(frame);
                        }
                    }
                });
                dateChooser.add(confirmation);


                options.setLayout(new BorderLayout());
                options.add(dateChooser, BorderLayout.CENTER);
                options.pack();
                options.setVisible(true);
                options.setSize(250,150);
                options.setResizable(false);
            }
        });
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        item4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "This program was created by Jose Lugo.");
            }
        });

        frame.setResizable(false);
        frame.pack();
        frame.setSize(800,800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*static Object[][] getInfo(JFileChooser f)
    {
        try
        {
            File f1 = f.getSelectedFile();
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            String line;
            String[] arr;
            String delimiter = ",";
            while((line = br.readLine()) != null)
            {
                arr = line.split(delimiter);
                for(int i = 0; i < arr.length; i++)
                {
                    System.out.print(arr[i] + "\n");
                }
                System.out.println();
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }*/
}
