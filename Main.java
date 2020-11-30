import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

        bar.add(file);
        bar.add(about);
        //////////////////////////////////////////////////////////////////////////////////////////////////////

        ArrayList<ArrayList<String>> roster = new ArrayList<ArrayList<String>>();
        String[] columnNames = {"ID", "First Name", "Last Name", "Program", "Level", "ASURITE"};
        final Object[][][] allData = new Object[1][1][1];

        ArrayList<ArrayList<ArrayList<String>>> attendanceDays = new ArrayList<ArrayList<ArrayList<String>>>();
        int addedDays = 0;

        DefaultTableModel model = new DefaultTableModel(allData[0], columnNames);

        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                files.showOpenDialog(frame);
                try
                {
                    File f1 = files.getSelectedFile();
                    FileReader fr = new FileReader(f1);
                    BufferedReader br = new BufferedReader(fr);
                    String line;
                    String[] arr;
                    String delimiter = ",";
                    int amountOfStudents = 0;
                    while((line = br.readLine()) != null)
                    {
                        amountOfStudents++;
                        ArrayList<String> stu = new ArrayList<String>();
                        arr = line.split(delimiter);
                        for(int i = 0; i < arr.length; i++)
                        {
                            stu.add(arr[i]);
                        }
                        roster.add(stu);
                    }

                    allData[0] = new Object[amountOfStudents][6];

                    int stuNum = 0;
                    int attribute = 0;
                    for(ArrayList<String> student : roster)
                    {
                        for(String person : student)
                        {
                            allData[0][stuNum][attribute] = person;
                            attribute++;
                        }
                        stuNum++;
                        attribute = 0;
                    }

                    JTable table = new JTable(model)
                    {
                        public boolean isCellEditable(int rows, int columns)
                        {
                            return false;
                        }
                    };
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    table.getColumnModel().getColumn(0).setPreferredWidth(100);
                    table.getColumnModel().getColumn(3).setPreferredWidth(175);
                    table.getColumnModel().getColumn(4).setPreferredWidth(125);

                    table.setGridColor(Color.BLACK);
                    JScrollPane scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    scroller.setBackground(Color.lightGray);
                    panel.add(scroller, BorderLayout.CENTER);
                    frame.add(panel);
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
                            JOptionPane.showMessageDialog(null, "You are adding attendance for " + combo1.getSelectedItem() + ". " + combo2.getSelectedItem());
                            options.dispose();
                            files2.showOpenDialog(frame);

                            try
                            {
                                File f2 = files2.getSelectedFile();
                                FileReader fr2 = new FileReader(f2);
                                BufferedReader br2 = new BufferedReader(fr2);

                                ArrayList<ArrayList<String>> newDay = new ArrayList<ArrayList<String>>();

                                String line;
                                String[] arr;
                                String delimiter = ",";
                                while((line = br2.readLine()) != null)
                                {
                                    ArrayList<String> studentLog = new ArrayList<String>();
                                    arr = line.split(delimiter);
                                    for(int i = 0; i < arr.length; i++)
                                    {
                                        studentLog.add(arr[i]);
                                    }
                                    newDay.add(studentLog);
                                }
                                attendanceDays.add(newDay);

                                model.addColumn(combo1.getSelectedItem() + ". " + combo2.getSelectedItem());
                                JTable table = new JTable(model);
                                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table.getColumnModel().getColumn(0).setPreferredWidth(100);
                                table.getColumnModel().getColumn(3).setPreferredWidth(175);
                                table.getColumnModel().getColumn(4).setPreferredWidth(125);
                                table.setGridColor(Color.BLACK);
                                JScrollPane scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                scroller.setBackground(Color.lightGray);
                                scroller.setForeground(Color.lightGray);
                                panel.add(scroller, BorderLayout.CENTER);
                                frame.add(panel);
                                frame.repaint();
                                frame.revalidate();

                            }
                            catch(IOException ioe)
                            {
                                ioe.printStackTrace();
                            }
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

        about.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                JOptionPane.showMessageDialog(frame, "This program was created by Jose Lugo.");
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        frame.setResizable(false);
        frame.pack();
        frame.setSize(800,800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
