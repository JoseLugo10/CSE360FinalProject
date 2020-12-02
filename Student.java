/**
 * The student class allows for the creation of a Student object which contains many
 * parameters, based off the csv files that the user inputs.
 *
 * @author  Jose lugo
 * @version 1.0
 * @since   2020-11-24
 */

public class Student {

    /**
     * These private variables will be used to help organize the data that we input from the csv file that is
     * selected from the user. Each variable represents an attribute of a student, which is used to make a class roster.
     */
    private String idNumber;
    private String firstName;
    private String lastName;
    private String program;
    private String level;
    private String asurite;

    /**
     * Will set the idNumber for a Student object
     * @param id - the value we are using to set the idNumber variable
     */
    public void setIdNumber(String id)
    {
        idNumber = id;
    }

    /**
     * Will return the idNumber of a Student object
     * @return idNumber
     */
    public String getIdNumber()
    {
        return idNumber;
    }

    /**
     * Will set the firstName for a Student object
     * @param first - the value we are using to set the firstName variable
     */
    public void setFirstName(String first)
    {
        firstName = first;
    }

    /**
     * Will return the firstName of a Student object
     * @return firstName
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Will set the lastName for a Student object
     * @param last - the value we are using to set the lastName variable
     */
    public void setLastName(String last)
    {
        lastName = last;
    }

    /**
     * Will return the lastName of a Student object
     * @return lastName
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Will set the program for a Student object
     * @param pro - the value we are using to set the program variable
     */
    public void setProgram(String pro)
    {
        program = pro;
    }

    /**
     * Will return the program of a Student object
     * @return program
     */
    public String getProgram()
    {
        return program;
    }

    /**
     * Will set the level for a Student object
     * @param lev - the value we are using to set the level variable
     */
    public void setLevel(String lev)
    {
        level = lev;
    }

    /**
     * Will return the level of a Student object
     * @return level
     */
    public String getLevel()
    {
        return level;
    }

    /**
     * Will set the asurite for a Student object
     * @param asu - the value we are using to set the asurite variable
     */
    public void setASURITE(String asu)
    {
        asurite = asu;
    }

    /**
     * Will return the asurite of a Student object
     * @return asurite
     */
    public String getASURITE()
    {
        return asurite;
    }
}
