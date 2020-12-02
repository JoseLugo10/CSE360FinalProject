/**
 * This program allows you to to load in a class roster using csv files, add attendance for a
 * specific date, save that data onto another csv file, and allows you to plot the attendance for
 * that class roster. The program uses various java swing, awt, and the jfreechart libraries to implement
 * this software.
 *
 * @author  Jose lugo
 * @version 1.0
 * @since   2020-11-24
 */

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Main extends JFrame {

    /**
     * These objects will be used for the first window we will see when the program is ran.
     */
    JPanel mainPanel;
    JMenuBar menuBar;
    JMenu file;
    JMenu about;
    JMenuItem loadARoster;
    JMenuItem addAttendance;
    JMenuItem save;
    JMenuItem plot;
    JMenuItem aboutTheCreator;

    /**
     * These objects are used for the parts of the program that involve the use of csv files.
     */
    JFileChooser rosterFile;
    JFileChooser attendanceFile;
    FileNameExtensionFilter CSV;

    /**
     * These variables will help keep track of the roster of students, and the amount of students as well.
     */
    ArrayList<Student> roster;
    int numberOfStudents;

    /**
     * These variable are used for storing data from the user inputted csv files. they are also used in assisting with the
     * creation of the JTable used in the program.
     */
    ArrayList<String> columns;
    String[] categories;
    Object[][] information;
    int numberOfCategories;

    /**
     * These variables are used in the creation of a JTable used to store and output the data.
     */
    JTable table;
    DefaultTableModel model;
    JScrollPane scroller;

    /**
     * trackedDays is an ArrayList of an ArrayList of ClassAttendance objects, which is used to keep a log of all the days
     * attendance was tracked for. totalAttendance is an ArrayList of ClassAttendance objects which is used for one individual day.
     * actualStudents is used to keep track of students who are found in the attendance file, but are actually in the class roster.
     * The rest of the variables are used to keep the number of days attendance was counted for, the amount of students in one attendance
     * file, and an ArrayList of strings to keep track of the dates that attendance was done for.
     */
    ArrayList<ArrayList<ClassAttendance>> trackedDays;
    ArrayList<ClassAttendance> totalAttendance;
    ArrayList<ArrayList<String>> actualStudents;
    int numberOfDays;
    int numberOfLoggedStudents;
    ArrayList<String> dates;

    /**
     * These objects are used in the implementation of the date picker, which is used to select a date for the attendance csv file
     * that the user inputs.
     */
    JFrame options;
    JPanel dateChooser;
    JLabel month;
    JLabel day;
    JComboBox combo1;
    JComboBox combo2;
    JPanel empty;
    JButton confirmation;

    /**
     * The Main constructor will implements all of the objects created, and use it in the development of the actual software.
     */
    public Main()
    {
        /**
         * The frame and the panel for the GUI are created
         */
        setTitle("CSE365 Final Project");
        setLayout(new BorderLayout());
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        /**
         * The menu bar along with the menus and the menuitems are initialized, and added so that the user will
         * be able to select the to perform certain functions.
         */
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

        /**
         * The roster and columns ArrayLists are initialized, and will be used throughout the program.
         */
        roster = new ArrayList<Student>();
        columns = new ArrayList<String>();

        /**
         * A categories String array is initialized and contains the categories representing attributes of a student
         * added to the roster.
         */
        numberOfCategories = 6;
        categories = new String[]{"ID", "First Name", "Last Name", "Program", "Level", "ASURITE"};
        columns.add(categories[0]);
        columns.add(categories[1]);
        columns.add(categories[2]);
        columns.add(categories[3]);
        columns.add(categories[4]);
        columns.add(categories[5]);

        /**
         * The actualStudents and the trackedDays variables are initialized, which will be used all throughout the program.
         */
        actualStudents = new ArrayList<ArrayList<String>>();
        trackedDays = new ArrayList<ArrayList<ClassAttendance>>();

        /**
         * numberOfDays and dates are initialized. This will be very useful throuout the program in updating the JTable, and
         * making the plot.
         */
        numberOfDays = 0;
        dates = new ArrayList<String>();

        /**
         * The FileNameExtensionFilter CSV will make it so that when a user is asked to select a csv file, they cannot select
         * any other file. This will help to avoid error in file selection.
         */
        CSV = new FileNameExtensionFilter("*.csv", "csv");

        /**
         * If the 'Load a Roster' menuitem is clicked, then an action event occurs where the user will be taken to a file chooser menu, and
         * they are able to select a csv file to use as their class roster.
         */
        loadARoster.addActionListener(new ActionListener() {
            /**
             * The program will now exeute the file opener based off the user clicking the 'Load a Roster" menuitem.
             * @param e - ActionEvent
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                /**
                 * We initialize the rosterFile object, and we ask the user to select a csv file for the
                 * roster. We also set the file filter so that only csv files can be selected.
                 */
                rosterFile = new JFileChooser();
                rosterFile.setDialogTitle("Please choose a CSV file");
                rosterFile.setFileFilter(CSV);
                rosterFile.showOpenDialog(mainPanel);

                /**
                 * The try and catch block is used in case of a IOexception error
                 */
                try
                {
                    /**
                     * A File, FileReader, and BufferedReader object are created in order for the program to be able
                     * to read the user inputted file.
                     */
                    File f1 = rosterFile.getSelectedFile();
                    FileReader fr1 = new FileReader(f1);
                    BufferedReader br1 = new BufferedReader(fr1);

                    /**
                     * These Strings and String array are used to help seperate the data stored in the csv file. The
                     * delimiter will split the data so that we can put the data into our data structure.
                     */
                    String line;
                    String[] array;
                    String delimiter = ",";
                    numberOfStudents = 0;

                    /**
                     * A while loop is used until the end of the file has been reached, to read the file and store the data
                     * into our data structure.
                     */
                    while((line = br1.readLine()) != null)
                    {
                        /**
                         * A Student object is created, and its variables will be initialized to the values read from the
                         * csv file.
                         */
                        Student stu = new Student();

                        /**
                         * We split the read line into an array, and each of those array values are set to the corresponding
                         * values of the Student object we created.
                         */
                        array = line.split(delimiter);
                        stu.setIdNumber(array[0]);
                        stu.setFirstName(array[1]);
                        stu.setLastName(array[2]);
                        stu.setProgram(array[3]);
                        stu.setLevel(array[4]);
                        stu.setASURITE(array[5]);

                        /**
                         * We increment the numberOfStudents so that we know the full roster size, and we add this student to the
                         * ArrayList of Student objects called roster.
                         */
                        numberOfStudents++;
                        roster.add(stu);
                    }

                    /**
                     * We create a new 2D Object array, equal to the numberOfStudents, and the numberOfCategories.
                     */
                    information = new Object[numberOfStudents][numberOfCategories];

                    /**
                     * We now iterate through the ArrayList roster so that we can store the values of each student
                     * into the information 2D Object array. We also use the variable stuNum so that we can iterate through
                     * all of the students in the roster.
                     */
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

                    /**
                     * We now initialize the TableModel, the JTable, and the JScrollPane to create the JTable that
                     * will contain the data of the roster. We then add it to the mainPanel and add it to the JFrame.
                     */
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
                    add(mainPanel, BorderLayout.CENTER);
                    repaint();
                    revalidate();

                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        });

        /**
         * When the user clicks on the 'Add Attendance' menuitem, the user is sent to a menu to select a csv file containing
         * a class roster.
         */
        addAttendance.addActionListener(new ActionListener() {
            /**
             * The JFileChooser will prompt the user to pick a csv file to display on a JTable.
             * @param e - ActionEvent
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                /**
                 * These objects are initialized to create a window that will prompt the user to select a date for
                 * the attendance.
                 */
                options = new JFrame("Please select a date");
                options.setLayout(new BorderLayout());
                dateChooser = new JPanel();
                dateChooser.setLayout(new GridLayout(3, 2));
                month = new JLabel("Month", SwingConstants.CENTER);
                day = new JLabel("Day", SwingConstants.CENTER);
                dateChooser.add(month);
                dateChooser.add(day);

                /**
                 * Since we are using combo boxes to select the date, we must fill them with information. We make String arrays
                 * containing months, and days in a calender.
                 */
                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
                combo1 = new JComboBox(months);
                String[] days = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
                combo2 = new JComboBox(days);

                /**
                 * These objects are added into a panel which will use a grid layout to better organize the
                 * components.
                 */
                dateChooser.add(combo1);
                dateChooser.add(combo2);
                empty = new JPanel();
                dateChooser.add(empty);

                /**
                 * A JButton is created, which is used to confirm a date selection for the attendance.
                 */
                confirmation = new JButton("Confirm");
                dateChooser.add(confirmation);

                /**
                 * When the 'Confirm' button is pressed, that means that the user wants to add attendance for that
                 * particular date that they chose.
                 */
                confirmation.addActionListener(new ActionListener() {
                    /**
                     * Now that the user has selected a date, the program must make sure that that date is a real date on
                     * the calender.
                     * @param e - ActionEvent
                     */
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        /**
                         * Multiple if statements are used to check the validity of the date the user has chosen. If they have chosen an invalid
                         * date, they will be prompted to pick a real date.
                         */
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
                            /**
                             * Now that a valid date has been chosen, we increase the numberOfCategories, and add the selected
                             * date into our columns ArrayList, and our dates ArrayList
                             */
                            numberOfCategories++;
                            String date = (combo1.getSelectedItem() + ". " + combo2.getSelectedItem());
                            columns.add(date);
                            dates.add(date);

                            /**
                             * Since we added a new column by adding attendance, we must make sure that our String array containing the
                             * headers is up to date.
                             */
                            String[] newColumns = new String[numberOfCategories];
                            for(int i = 0; i < numberOfCategories; i++)
                            {
                                newColumns[i] = columns.get(i);
                            }

                            /**
                             * An ArrayList of Strings in created which will contain the attendance for the particular date
                             * that the user has chosen.
                             */
                            ArrayList<String> studentsForDay = new ArrayList<String>();

                            /**
                             * A dialog box displays to the user the date that they are adding attendance to.
                             */
                            JOptionPane.showMessageDialog(null, "You are adding attendance for " + date);
                            options.dispose();

                            /**
                             * We intialize the JFileChosser, set the filer, and prompt the user to select a csv file.
                             */
                            attendanceFile = new JFileChooser();
                            attendanceFile.setDialogTitle("Please select a CSV file");
                            attendanceFile.setFileFilter(CSV);
                            attendanceFile.showOpenDialog(mainPanel);

                            /**
                             * A try and catch block is used in order to avoid any errors when selecting a file.
                             */
                            try
                            {
                                /**
                                 * A the components used to read from a file are created.
                                 */
                                File f2 = attendanceFile.getSelectedFile();
                                FileReader fr2 = new FileReader(f2);
                                BufferedReader br2 = new BufferedReader(fr2);

                                /**
                                 * Similar to the 'Add a Roster' function, we read the csv file the same way. The only difference
                                 * this time is that instead of adding the data to a Student object, we add them to a ClassAttendance object.
                                 *
                                 */
                                String line;
                                String[] array;
                                String delimiter = ",";

                                /**
                                 * The numberOfLoggedStudents variable will help us in keeping track of the amount of students that have logged into
                                 * class for this particular date.
                                 */
                                numberOfLoggedStudents = 0;
                                totalAttendance = new ArrayList<ClassAttendance>();
                                while((line = br2.readLine()) != null)
                                {
                                    /**
                                     * As mentioned before, we are creating ClassAttendance objects instead of Student
                                     * objects to store the data. We then add each ClassAttendance object into an ArrayList
                                     * called totalAttendance.
                                     */
                                    ClassAttendance present = new ClassAttendance();
                                    array = line.split(delimiter);
                                    present.setASURITE(array[0]);
                                    present.setMinutes(Integer.parseInt(array[1]));
                                    totalAttendance.add(present);
                                    numberOfLoggedStudents++;
                                }

                                /**
                                 * Now that we have logged the attendance for a single day, we add the ArrayList into another
                                 * ArrayList called trackedDays. We also increase the number of days that have been logged.
                                 */
                                trackedDays.add(totalAttendance);
                                numberOfDays++;

                                /**
                                 * We reinitialize our 2D Object array information, because since we added attendance, we need to have
                                 * a new column to represent that data in the JTable.
                                 */
                                information = new Object[numberOfStudents][numberOfCategories];

                                /**
                                 * This for loop acts similar to the one we used in the 'Add a Roster' function, except now we have to include
                                 * the data of the attendance that the user logged in.
                                 */
                                int stuNum = 0;
                                for(Student student : roster)
                                {
                                    information[stuNum][0] = student.getIdNumber();
                                    information[stuNum][1] = student.getFirstName();
                                    information[stuNum][2] = student.getLastName();
                                    information[stuNum][3] = student.getProgram();
                                    information[stuNum][4] = student.getLevel();
                                    information[stuNum][5] = student.getASURITE();

                                    /**
                                     * Since the information array has more data that need to be inputted, we now have to iterate through each day that has
                                     * been added, and add that data into the information array.
                                     */
                                    for(int i = 6; i < 6 + trackedDays.size(); i++)
                                    {
                                        /**
                                         * These for loops will go through all of the students that were in class for this particular day, and will keep track
                                         * if their name has appeared more than once in this attendance file. It also excludes any students that came to class, but are not
                                         * in the roster.
                                         */
                                        int amountOfTimes = 0;
                                        int minutes = 0;
                                        for(int j = 0; j < (trackedDays.get(i - 6)).size(); j++)
                                        {
                                            /**
                                             * If the user is in the roster and they came to class, then the amount of minutes that they were in are set to
                                             * the minutes variable. This will also keep track if this student has shown up more than once.
                                             */
                                            if(trackedDays.get(i - 6).get(j).getASURITE().equals(roster.get(stuNum).getASURITE()) && amountOfTimes > 0)
                                            {
                                                int extra = trackedDays.get(i - 6).get(j).getMinutes();
                                                minutes = minutes + extra;
                                            }
                                            else if(trackedDays.get(i - 6).get(j).getASURITE().equals(roster.get(stuNum).getASURITE()) && amountOfTimes == 0)
                                            {
                                                amountOfTimes++;
                                                minutes = trackedDays.get(i - 6).get(j).getMinutes();
                                            }
                                        }

                                        /**
                                         * If a student has logged in minutes for this attendance, then the values for information are
                                         * initialized. Else, it will default to 0.
                                         */
                                        if(minutes > 0)
                                        {
                                            information[stuNum][i] = String.valueOf(minutes);
                                        }
                                        else
                                        {
                                            information[stuNum][i] = "0";
                                        }
                                    }
                                    stuNum++;
                                }

                                /**
                                 * These for loops will be used to find students who logged into class, but are no in the roster. It does this by
                                 * checking to see if the ASURITE field mathces with any of the ASURITE fields in the roster.
                                 */
                                int unrecognized = 0;
                                for(int i = 0; i < trackedDays.get(numberOfDays - 1).size(); i++)
                                {
                                    /**
                                     * The integers recognized will be used to determine if a particular student that logged in is in the roster.
                                     */
                                    int recognized = 0;
                                    for(int j = 0; j < numberOfStudents; j++)
                                    {
                                        /**
                                         * If the student is found, then recognized is set to 1, and the loop will break.
                                         */
                                        if(trackedDays.get(numberOfDays - 1).get(i).getASURITE().equals(information[j][5]))
                                        {
                                            recognized = 1;
                                            break;
                                        }
                                    }

                                    /**
                                     * If the student is not found in the roster, then the unrecognized value is incremented.
                                     */
                                    if(recognized == 0)
                                    {
                                        unrecognized++;
                                    }
                                }

                                /**
                                 * Now that our information array is filled with the right data, we can add this data into
                                 * the ArrayList that will contain students that are in the roster.
                                 */
                                for(int i = 0; i < numberOfStudents; i++)
                                {
                                    studentsForDay.add(String.valueOf(information[i][5 + numberOfDays]));
                                }

                                actualStudents.add(studentsForDay);

                                /**
                                 * We reinitialize the TableModel and JTable so that they contain the data of the attendance that
                                 * we put from the csv file.
                                 */
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
                                add(mainPanel, BorderLayout.CENTER);
                                repaint();
                                revalidate();

                                /**
                                 * A dialog message appears showing the amount of students that attendance was logged for, and displaying any student that
                                 * was not in the roster.
                                 */
                                JOptionPane.showMessageDialog(mainPanel, "Data loaded in for "+ numberOfStudents +" in the roster.\n" + unrecognized + " additional attendee was found:");

                            }
                            catch(IOException ioe)
                            {
                                ioe.printStackTrace();
                            }
                        }
                    }
                });

                /**
                 * All of the components for the date chooser menu are made visible.
                 */
                options.add(dateChooser, BorderLayout.CENTER);
                options.pack();
                options.setVisible(true);
                options.setSize(250, 150);
                options.setResizable(false);
            }
        });

        /**
         * When the user clicks on the 'Save' menuitem, they will be allowed to save the data that is inside
         * their JTable onto a new csv file, that even contains the headers.
         */
        save.addActionListener(new ActionListener() {
            /**
             * The program will not ask the user for anything else when they save their program, they will only
             * get a message saying that their file has been saved into a file called filledTable.csv.
             * @param e - ActionEvent
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                /**
                 * A try catch block is used in order to avoid any possible errors.
                 */
                try
                {
                    /**
                     * A File, FileWriter, and a BufferedWriter are all created so that we can write into a csv file.
                     */
                    File csvFile = new File("filledTable.csv");
                    FileWriter fw = new FileWriter(csvFile);
                    BufferedWriter bw = new BufferedWriter(fw);

                    /**
                     * Since this file will contain the headers of the JTable, we want to make sure that we write these to
                     * the file first. We iterate through all of the columns that we have, and write them, of course not forgetting to write
                     * commas to them as well. Except for the last column.
                     */
                    for(int i = 0; i < numberOfCategories; i++)
                    {
                        if(i == numberOfCategories - 1)
                        {
                            bw.write(columns.get(i));
                        }
                        else
                        {
                            bw.write(columns.get(i)  + ",");
                        }
                    }
                    bw.write("\n");

                    /**
                     * Now that we have written down all of the headers, we can write down the data for all of the students. This
                     * is the same process as the columns, although this time we are using the information 2D Object array
                     * to get our data, since the array contains the most recent data.
                     */
                    for(int i = 0; i < numberOfStudents; i++)
                    {
                        for(int j = 0; j < numberOfCategories; j++)
                        {
                            if(j == numberOfCategories - 1)
                            {
                                bw.write(String.valueOf(information[i][j]));
                            }
                            else
                            {
                                bw.write(String.valueOf(information[i][j]) + ",");
                            }
                        }
                        bw.write("\n");
                    }

                    /**
                     * We close the BufferedWriter and display a message to the user.
                     */
                    bw.close();
                    JOptionPane.showMessageDialog(mainPanel, "Your data has been saved to filledTable.csv");
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }

            }
        });

        /**
         * This action event will allow the user to create a plot based off the data of the roster attendance.
         */
        plot.addActionListener(new ActionListener() {
            /**
             * The ActionEvent will create the scatter plot instantly, and display it onto a window.
             * @param e - ActionEvent
             */
            @Override
            public void actionPerformed(ActionEvent e) {

                /**
                 * We create an XYDataset called dataset, and initialize it by using the createDataset function. It will
                 * already contain the information about the attendance of the students.
                 */
                XYDataset dataset = createDataset();

                /**
                 * We create a JFreeChart object which will have the name of the scatter plot, the axis names, and the dataset.
                 */
                JFreeChart plotChart = ChartFactory.createScatterPlot("Number of Students in Percentile (Out of 75 Minutes)", "Percentile", "Number of Students", dataset);

                /**
                 * The rest of these variables are used to make sure that the scatter plot looks nice and neat.
                 */
                XYPlot xyz = (XYPlot)plotChart.getPlot();
                xyz.setBackgroundPaint(new Color(255,228,196));
                ChartPanel pane = new ChartPanel(plotChart);
                setContentPane(pane);

                /**
                 * The scatter plot is put into a JFrame, so that the user can easily exit out of the plot when they are done
                 * using it.
                 */
                pane.setSize(800, 400);
                JFrame chartFrame = new JFrame();
                chartFrame.setLayout(new BorderLayout());
                chartFrame.add(pane, BorderLayout.CENTER);
                chartFrame.pack();
                chartFrame.setVisible(true);
                chartFrame.setSize(800, 400);
                chartFrame.setResizable(false);
            }
        });

        /**
         * This simple action event pops up a JOptionPane that shows information about the creators of this program. i.e.
         * it says this was created by me :)
         */
        aboutTheCreator.addActionListener(new ActionListener() {
            /**
             * The message is displayed using the showMessageDialog function, and the message can be easily
             * exited out.
             * @param e - ActionEvent
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainPanel,"This program was created by Jose Lugo");
            }
        });
    }

    /**
     * This function will create an collection of XY plots and return them. These are used in the implementation of the
     * scatter plot chart that is displayed when the user clicks plot on the program. The scatter plot is created using the
     * data from the actualStudents ArrayList, which contains the minutes that each student was in class.
     * @return dataset
     */
    private XYDataset createDataset()
    {
        /**
         * The dataset object will contain the plots for every single day that attendance was inputted in the program.
         */
        XYSeriesCollection dataset = new XYSeriesCollection();

        /**
         * This for loop will iterate for the amount of days that attendance was logged for.
         */
        for(int i = 0; i < numberOfDays; i++)
        {
            /**
             * An XYSeries variable called series is created which contain all the plot points of a particular day
             */
            XYSeries series = new XYSeries(dates.get(i));

            /**
             * All of these integer variables are used to keep track of the percent of time students spent in class. Since the
             * plot that we are creating has an x-axis that starts from 0% and ends at 100%, we have variables that go up 5% each. Now
             * these variables are incremented depending on how long a student spends in class. For example, if a student spent 75 minutes
             * in class, then the oneHundredPercent variable is incremented by one since they were in class for 75/75 minutes.
             */
            int zeroPercent = 0;
            int fivePercent = 0;
            int tenPercent = 0;
            int fifteenPercent = 0;
            int twentyPercent = 0;
            int twentyFivePercent = 0;
            int thirtyPercent = 0;
            int thirtyFivePercent = 0;
            int fourtyPercent = 0;
            int fourtyFivePercent = 0;
            int fiftyPercent = 0;
            int fiftyFivePercent = 0;
            int sixtyPercent = 0;
            int sixtyFivePercent = 0;
            int seventyPercent = 0;
            int seventyFivePercent = 0;
            int eightyPercent = 0;
            int eightyFivePercent = 0;
            int ninetyPercent = 0;
            int ninetyFivePercent = 0;
            int oneHundredPercent = 0;

            /**
             * Now we can iterate through every student in the roster to find out how much time they spent in class on a
             * particular day.
             */
            for(int j = 0; j < numberOfStudents; j++)
            {
                /**
                 * We parse the amount of time a student spent in class into an integer, and then we multiply it by
                 * 100, and divide by 75. This gives a value floored into an integer, equivalent to the percent that
                 * they spent in class.
                 */
                int stuMins = Integer.parseInt(String.valueOf(actualStudents.get(i).get(j)));
                int percentile = (stuMins * 100) / 75;

                /**
                 * All of these if statements will check to see that percent the student spent in class. If the appropriate one
                 * is found, then that percent value is incremented. Since we are counting by fives, as long as a student is within
                 * a percent range such as 75-70, they will default to the lower bound of those values. i.e. if the percentile is equal to
                 * 33, then they are part of the 30 percent club.
                 */
                if(percentile == 0 || percentile < 5)
                {
                    zeroPercent++;
                }
                if(percentile >= 100)
                {
                    oneHundredPercent++;
                }
                if(percentile < 100 && percentile >= 95)
                {
                    ninetyFivePercent++;
                }
                if(percentile < 95 && percentile >= 90)
                {
                    ninetyPercent++;
                }
                if(percentile < 90 && percentile >= 85)
                {
                    eightyFivePercent++;
                }
                if(percentile < 85 && percentile >= 80)
                {
                    eightyPercent++;
                }
                if(percentile < 80 && percentile >= 75)
                {
                    seventyFivePercent++;
                }
                if(percentile < 75 && percentile >= 70)
                {
                    seventyPercent++;
                }
                if(percentile < 70 && percentile >= 65)
                {
                    sixtyFivePercent++;
                }
                if(percentile < 65 && percentile >= 60)
                {
                    sixtyPercent++;
                }
                if(percentile < 60 && percentile >= 55)
                {
                    fiftyFivePercent++;
                }
                if(percentile < 55 && percentile >= 50)
                {
                    fiftyPercent++;
                }
                if(percentile < 50 && percentile >= 45)
                {
                    fourtyFivePercent++;
                }
                if(percentile < 45 && percentile >= 40)
                {
                    fourtyPercent++;
                }
                if(percentile < 40 && percentile >= 35)
                {
                    thirtyFivePercent++;
                }
                if(percentile < 35 && percentile >= 30)
                {
                    thirtyPercent++;
                }
                if(percentile < 30 && percentile >= 25)
                {
                    twentyFivePercent++;
                }
                if(percentile < 25 && percentile >= 20)
                {
                    twentyPercent++;
                }
                if(percentile < 20 && percentile >= 15)
                {
                    fifteenPercent++;
                }
                if(percentile < 15 && percentile >= 10)
                {
                    tenPercent++;
                }
                if(percentile < 10 && percentile >= 5)
                {
                    fivePercent++;
                }
            }

            /**
             * Now that we hav figured out all of the percentages, we can start to add plots to the chart. These if statements
             * will check to see if they are greater thn 0. If they are, then the chart will plot the percentage on the x-axis, and
             * the amount of students who fit in that percentile in the y-axis.
             */
            if(zeroPercent > 0)
            {
                series.add(0, zeroPercent);
            }
            if(fivePercent > 0)
            {
                series.add(5, fivePercent);
            }
            if(tenPercent > 0)
            {
                series.add(10, tenPercent);
            }
            if(fifteenPercent > 0)
            {
                series.add(15, fifteenPercent);
            }
            if(twentyPercent > 0)
            {
                series.add(20, twentyPercent);
            }
            if(twentyFivePercent > 0)
            {
                series.add(25, twentyFivePercent);
            }
            if(thirtyPercent > 0)
            {
                series.add(30, thirtyPercent);
            }
            if(thirtyFivePercent > 0)
            {
                series.add(35, thirtyFivePercent);
            }
            if(fourtyPercent > 0)
            {
                series.add(40, fourtyPercent);
            }
            if(fourtyFivePercent > 0)
            {
                series.add(45, fourtyFivePercent);
            }
            if(fiftyPercent > 0)
            {
                series.add(50, fiftyPercent);
            }
            if(fiftyFivePercent > 0)
            {
                series.add(55, fiftyFivePercent);
            }
            if(sixtyPercent > 0)
            {
                series.add(60, sixtyPercent);
            }
            if(sixtyFivePercent > 0)
            {
                series.add(65, sixtyFivePercent);
            }
            if(seventyPercent > 0)
            {
                series.add(70, seventyPercent);
            }
            if(seventyFivePercent > 0)
            {
                series.add(75, seventyFivePercent);
            }
            if(eightyPercent > 0)
            {
                series.add(80, eightyPercent);
            }
            if(eightyFivePercent > 0)
            {
                series.add(85, eightyFivePercent);
            }
            if(ninetyPercent > 0)
            {
                series.add(90, ninetyPercent);
            }
            if(ninetyFivePercent > 0)
            {
                series.add(95, ninetyFivePercent);
            }
            if(oneHundredPercent > 0)
            {
                series.add(100, oneHundredPercent);
            }

            /**
             * We add the series for a particular day into the dataset.
             */
            dataset.addSeries(series);
        }

        /**
         * Finally, we return the dataset containing the plot points of every logged day.
         */
        return dataset;
    }


    /**
     * The main method will create a Main object, and therefore will be allowed to run the program when the code is ran.
     * @param args - used in creating the main function of a Java program
     */
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
