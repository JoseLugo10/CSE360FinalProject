import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends JFrame {

    JPanel mainPanel;
    JMenuBar menuBar;
    JMenu file;
    JMenu about;
    JMenuItem loadARoster;
    JMenuItem addAttendance;
    JMenuItem save;
    JMenuItem plot;
    JMenuItem aboutTheCreator;

    JFileChooser rosterFile;
    JFileChooser attendanceFile;
    FileNameExtensionFilter CSV;

    ArrayList<Student> roster;
    int numberOfStudents;

    ArrayList<String> columns;
    String[] categories;
    Object[][] information;
    int numberOfCategories;

    JTable table;
    DefaultTableModel model;
    JScrollPane scroller;

    ArrayList<ArrayList<ClassAttendance>> trackedDays;
    ArrayList<ClassAttendance> totalAttendance;
    int numberOfDays;
    int numberOfLoggedStudents;

    JFrame options;
    JPanel dateChooser;
    JLabel month;
    JLabel day;
    JComboBox combo1;
    JComboBox combo2;
    JPanel empty;
    JButton confirmation;

    JFrame updated;
    JPanel attendanceFinished;
    JLabel amountLoaded;
    JLabel unknowns;

    public Main()
    {
        setTitle("CSE365 Final Project");
        setLayout(new BorderLayout());

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        add(mainPanel);

        menuBar = new JMenuBar();
        file = new JMenu("File");
        about = new JMenu("About");
        loadARoster = new JMenuItem("Load a Roster");
        addAttendance = new JMenuItem("Add Attendance");
        save = new JMenuItem("Save");
        plot = new JMenuItem("Plot");
        aboutTheCreator = new JMenuItem("About");
        file.add(loadARoster);
        file.add(addAttendance);
        file.add(save);
        file.add(plot);
        about.add(aboutTheCreator);
        menuBar.add(file);
        menuBar.add(about);
        setJMenuBar(menuBar);

        roster = new ArrayList<Student>();
        columns = new ArrayList<String>();

        numberOfCategories = 6;
        categories = new String[]{"ID", "First Name", "Last Name", "Program", "Level", "ASURITE"};
        columns.add(categories[0]);
        columns.add(categories[1]);
        columns.add(categories[2]);
        columns.add(categories[3]);
        columns.add(categories[4]);
        columns.add(categories[5]);

        trackedDays = new ArrayList<ArrayList<ClassAttendance>>();
        numberOfDays = 0;

        CSV = new FileNameExtensionFilter("*.csv", "csv");

        loadARoster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rosterFile = new JFileChooser();
                rosterFile.setDialogTitle("Please choose a CSV file");
                rosterFile.setFileFilter(CSV);

                rosterFile.showOpenDialog(mainPanel);

                try
                {
                    File f1 = rosterFile.getSelectedFile();
                    FileReader fr1 = new FileReader(f1);
                    BufferedReader br1 = new BufferedReader(fr1);

                    String line;
                    String[] array;
                    String delimiter = ",";
                    numberOfStudents = 0;
                    while((line = br1.readLine()) != null)
                    {
                        Student stu = new Student();
                        array = line.split(delimiter);
                        stu.setIdNumber(array[0]);
                        stu.setFirstName(array[1]);
                        stu.setLastName(array[2]);
                        stu.setProgram(array[3]);
                        stu.setLevel(array[4]);
                        stu.setASURITE(array[5]);
                        numberOfStudents++;
                        roster.add(stu);
                    }

                    information = new Object[numberOfStudents][numberOfCategories];

                    int stuNum = 0;
                    for(Student student : roster)
                    {
                        information[stuNum][0] = student.getIdNumber();
                        information[stuNum][1] = student.getFirstName();
                        information[stuNum][2] = student.getLastName();
                        information[stuNum][3] = student.getProgram();
                        information[stuNum][4] = student.getLevel();
                        information[stuNum][5] = student.getASURITE();
                        stuNum++;
                    }

                    model = new DefaultTableModel(information, categories);
                    table = new JTable();
                    table.setModel(model);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                    table.getColumnModel().getColumn(0).setPreferredWidth(100);
                    table.getColumnModel().getColumn(3).setPreferredWidth(175);
                    table.getColumnModel().getColumn(4).setPreferredWidth(125);
                    table.setGridColor(Color.BLACK);
                    scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    mainPanel.add(scroller, BorderLayout.CENTER);
                    mainPanel.repaint();
                    mainPanel.revalidate();

                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        });

        addAttendance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                options = new JFrame("Please select a date");
                options.setLayout(new BorderLayout());
                dateChooser = new JPanel();
                dateChooser.setLayout(new GridLayout(3, 2));
                month = new JLabel("Month", SwingConstants.CENTER);
                day = new JLabel("Day", SwingConstants.CENTER);
                dateChooser.add(month);
                dateChooser.add(day);

                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                combo1 = new JComboBox(months);
                String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
                combo2 = new JComboBox(days);
                dateChooser.add(combo1);
                dateChooser.add(combo2);

                empty = new JPanel();
                dateChooser.add(empty);

                confirmation = new JButton("Confirm");
                dateChooser.add(confirmation);

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
                            numberOfCategories++;
                            numberOfDays++;
                            String date = (combo1.getSelectedItem() + ". " + combo2.getSelectedItem());
                            columns.add(date);
                            String[] newColumns = new String[numberOfCategories];
                            for(int i = 0; i < numberOfCategories; i++)
                            {
                                newColumns[i] = columns.get(i);
                            }

                            JOptionPane.showMessageDialog(null, "You are adding attendance for " + date);
                            options.dispose();

                            attendanceFile = new JFileChooser();
                            attendanceFile.setDialogTitle("Please select a CSV file");
                            attendanceFile.setFileFilter(CSV);
                            attendanceFile.showOpenDialog(mainPanel);

                            try
                            {
                                File f2 = attendanceFile.getSelectedFile();
                                FileReader fr2 = new FileReader(f2);
                                BufferedReader br2 = new BufferedReader(fr2);

                                String line;
                                String[] array;
                                String delimiter = ",";
                                numberOfLoggedStudents = 0;
                                totalAttendance = new ArrayList<ClassAttendance>();
                                while((line = br2.readLine()) != null)
                                {
                                    ClassAttendance present = new ClassAttendance();
                                    array = line.split(delimiter);
                                    present.setASURITE(array[0]);
                                    present.setMinutes(Integer.parseInt(array[1]));
                                    totalAttendance.add(present);
                                    numberOfLoggedStudents++;
                                }

                                trackedDays.add(totalAttendance);

                                information = new Object[numberOfStudents][numberOfCategories];

                                int loggedStudents = 0;
                                int stuNum = 0;
                                String[] missingStudents = new String[numberOfStudents];
                                int miss = 0;
                                for(Student student : roster)
                                {
                                    information[stuNum][0] = student.getIdNumber();
                                    information[stuNum][1] = student.getFirstName();
                                    information[stuNum][2] = student.getLastName();
                                    information[stuNum][3] = student.getProgram();
                                    information[stuNum][4] = student.getLevel();
                                    information[stuNum][5] = student.getASURITE();
                                    for(int k = 6; k < 6 + numberOfDays; k++)
                                    {
                                        int amountOfTimes = 0;
                                        int minutes = 0;
                                        for(int i = 0; i < numberOfLoggedStudents; i++)
                                        {
                                            if(totalAttendance.get(i).getASURITE().equals(roster.get(stuNum).getASURITE()) && amountOfTimes > 0)
                                            {
                                                int extra = totalAttendance.get(i).getMinutes();
                                                minutes = minutes + extra;
                                            }
                                            else if(totalAttendance.get(i).getASURITE().equals(roster.get(stuNum).getASURITE()) && amountOfTimes == 0)
                                            {
                                                amountOfTimes++;
                                                minutes = totalAttendance.get(i).getMinutes();
                                            }
                                        }

                                        if(minutes > 0)
                                        {
                                            information[stuNum][k] = String.valueOf(minutes);
                                            loggedStudents++;
                                        }
                                        else
                                        {
                                            information[stuNum][k] = "0";
                                            //missingStudents[miss] = roster.get(stuNum).getASURITE();
                                            miss++;
                                        }
                                    }
                                    stuNum++;
                                }

                                /*int recognizedStudents = 0;
                                for(int i = 0; i < miss; i++)
                                {
                                    for(int j = 0; j < loggedStudents; j++)
                                    {
                                        if(totalAttendance.get(j).getASURITE().equals(missingStudents[i]))
                                        {
                                            recognizedStudents++;
                                            break;
                                        }
                                    }
                                }

                                int randomStudents = miss - recognizedStudents;*/


                                model = new DefaultTableModel(information, newColumns);
                                table = new JTable();

                                table.setModel(model);
                                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                table.getColumnModel().getColumn(0).setPreferredWidth(100);
                                table.getColumnModel().getColumn(3).setPreferredWidth(175);
                                table.getColumnModel().getColumn(4).setPreferredWidth(125);
                                table.setGridColor(Color.BLACK);
                                scroller = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                                mainPanel.add(scroller, BorderLayout.CENTER);
                                mainPanel.repaint();
                                mainPanel.revalidate();

                                updated = new JFrame("ATTENTION!!!");
                                updated.setLayout(new BorderLayout());
                                attendanceFinished = new JPanel();
                                attendanceFinished.setLayout(new GridLayout(2, 1));
                                amountLoaded = new JLabel("Data loaded for " + loggedStudents + " in the roster.", SwingConstants.CENTER);
                                unknowns = new JLabel(miss + " addition attendee was found:", SwingConstants.CENTER);
                                attendanceFinished.add(amountLoaded);
                                attendanceFinished.add(unknowns);
                                updated.add(attendanceFinished, BorderLayout.CENTER);
                                updated.pack();
                                updated.setVisible(true);
                                updated.setSize(200, 100);
                                updated.setResizable(false);


                            }
                            catch(IOException ioe)
                            {
                                ioe.printStackTrace();
                            }
                        }
                    }
                });

                options.add(dateChooser, BorderLayout.CENTER);
                options.pack();
                options.setVisible(true);
                options.setSize(250, 150);
                options.setResizable(false);
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        plot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        aboutTheCreator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel,"This program was created by Jose Lugo");
            }
        });
    }

    public static void main(String[] args)
    {
        Main main = new Main();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setBackground(Color.GRAY);
        main.setSize(800, 800);
        main.setVisible(true);
        main.setResizable(false);

    }

}
