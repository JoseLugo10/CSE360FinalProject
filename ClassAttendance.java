/**
 * The ClassAttendance class allows for the creation of a ClassAttendance object, which can hold
 * information about the attendance of a roster loaded in from a csv file.
 *
 * @author  Jose lugo
 * @version 1.0
 * @since   2020-11-24
 */

public class ClassAttendance {

    /**
     * The private variables asurite and minutes will be used to help organize the
     * data of the attendance csv file that will be used in the main program.
     */
    private String asurite;
    private int minutes;

    /**
     * Allows for the asurite field of a ClassAttendance object to
     * be set.
     * @param asu
     * @return none
     */
    public void setASURITE(String asu)
    {
        asurite = asu;
    }

    /**
     * Returns the string for the asurite of a ClassAttendance object
     * @return asurite
     */
    public String getASURITE()
    {
        return asurite;
    }

    /**
     * Allows for the minutes field of a ClassAttendance object to
     * be set.
     * @param min
     * @return none
     */
    public void setMinutes(int min)
    {
        minutes = min;
    }

    /**
     * Returns the integer for the amount of minutes of a ClassAttendance object.
     * @return minutes
     */
    public int getMinutes()
    {
        return minutes;
    }
}
