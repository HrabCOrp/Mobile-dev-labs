package lab1_2;

public class CoordinateMH {
    private Direction direction;
    private int degrees, minutes, seconds;

    /* Default initialization with zeros. Latitude is the default direction */
    public CoordinateMH() {
        this.degrees = 0;
        this.minutes = 0;
        this.seconds = 0;
        this.direction = Direction.LATITUDE;
    }

    /* Initialization with defined values */
    public CoordinateMH(int degrees, int minutes, int seconds, Direction direction) {
        // checking bounds for degrees, minutes, and seconds
        boolean validLatitude = direction.name().equals("LATITUDE") && degrees >= -90 && degrees <= 90;
        boolean validLongitude = direction.name().equals("LONGITUDE") && degrees >= -180 && degrees <= 180;
        boolean validMinutesSeconds = minutes >= 0 && minutes < 60 && seconds >= 0 && seconds < 60;

        if ((validLongitude || validLatitude) && validMinutesSeconds) {
            this.degrees = degrees;
            this.minutes = minutes;
            this.seconds = seconds;
            this.direction = direction;
        } else {
            System.out.println("<Error> Invalid parameter for coordinate");
            System.exit(0);
        }
    }

    /** Important notes:
     - We use 'N', 'E' for positive (+) degrees and 'S', 'W' for negative (-) degrees;
     - Thus, incorrect formats are: -80°5′5"S and -80.55°S;
     - Correct formats are: 80°5′5"S and 80.55°S, or -80°5′5" and -80.55°
       (the latter 2 are not used).
     **/

    /* Get cardinal direction (N, W, S, E) */
    private String getZ() {
        switch (this.direction) {
            // zero and positive degrees are for North and East
            // negative degrees are for South and West
            case LATITUDE:
                return (degrees >= 0) ? "N" : "S";

            case LONGITUDE:
                return (degrees >= 0) ? "E" : "W";
        }
        return null;
    }

    /* Get coordinate in format of "xx°yy′zz″ Z" */
    public String getCoordinate() {
        String Z = getZ(); // cardinal direction

        // Make sure we have a positive value (N,W,S,E are used instead of "+/-")
        int deg = (this.degrees > 0) ? this.degrees : -this.degrees;

        return deg + "°" + this.minutes + "′" + this.seconds + "\"" + Z;
    }

    /* Get coordinate in format of "xx,xxx...° Z" */
    public String getDecimalCoordinate() {
        String Z = getZ(); // cardinal direction
        double decimalDegrees; // resulting degrees

        // converting to decimal format
        if (this.degrees < 0) {
            decimalDegrees = -(double)this.seconds / 3600.0 - (double) this.minutes + this.degrees;
            // we want positive value (N,W,S,E are used)
            decimalDegrees *= -1;
        } else {
            decimalDegrees = (double)this.seconds / 3600.0 + (double) this.minutes + this.degrees;
        }

        return decimalDegrees + "°" + Z;
    }

    /* Get average coordinate between:
     - current object
     - other object
     */
    public CoordinateMH getAverageCoordinate(CoordinateMH object) {
        int averageDegrees;
        int averageMinutes;
        int averageSeconds;

        // We need negative minutes and seconds (if degrees < 0) to properly calculate average values
        int minutes1 = (this.degrees > 0) ? this.minutes : -this.minutes;
        int seconds1 = (this.degrees > 0) ? this.seconds : -this.seconds;
        int minutes2 = (object.degrees > 0) ? object.minutes : -object.minutes;
        int seconds2 = (object.degrees > 0) ? object.seconds : -object.seconds;

        if (this.direction == object.direction) {
            averageDegrees = (this.degrees + object.degrees) / 2;
            averageMinutes = (minutes1 + minutes2) / 2;
            averageSeconds = (seconds1 + seconds2) / 2;

            return new CoordinateMH(averageDegrees, averageMinutes, averageSeconds, this.direction);
        }
        return null;
    }

    /* Get average coordinate between:
     - object1
     - object2
     */
    public static CoordinateMH getAverageCoordinateObj(CoordinateMH object1, CoordinateMH object2) {
        int averageDegrees;
        int averageMinutes;
        int averageSeconds;

        // We need negative minutes and seconds (if degrees are below 0) to properly calculate average values
        int minutes1 = (object1.degrees > 0) ? object1.minutes : -object1.minutes;
        int seconds1 = (object1.degrees > 0) ? object1.seconds : -object1.seconds;
        int minutes2 = (object2.degrees > 0) ? object2.minutes : -object2.minutes;
        int seconds2 = (object2.degrees > 0) ? object2.seconds : -object2.seconds;

        if (object1.direction == object2.direction) {
            averageDegrees = (object1.degrees + object2.degrees) / 2;
            averageMinutes = (minutes1 + minutes2) / 2;
            averageSeconds = (seconds1 + seconds2) / 2;

            return new CoordinateMH(averageDegrees, averageMinutes, averageSeconds, object1.direction);
        }
        return null;
    }

    public static void main(String[] args) {
        /* Default coordinate */
        CoordinateMH defaultCoordinate = new CoordinateMH();
        /* Set#1 */
        CoordinateMH width1 = new CoordinateMH(90, 10, 20, Direction.LATITUDE);
        CoordinateMH length1 = new CoordinateMH(23, 42, 59, Direction.LONGITUDE);
        /* Set#2 */
        CoordinateMH width2 = new CoordinateMH(-80, 6, 8, Direction.LATITUDE);
        CoordinateMH length2 = new CoordinateMH(-23, 42, 59, Direction.LONGITUDE);
        /* Get average (way#1) */
        CoordinateMH midWidth1 = width1.getAverageCoordinate(width2);
        CoordinateMH midLength1 = length2.getAverageCoordinate(length1);
        /* Get average (way#2) */
        CoordinateMH midWidth2 = getAverageCoordinateObj(width2, width1);
        CoordinateMH midLength2 = getAverageCoordinateObj(length1, length2);

        /* Printing results */
        System.out.println("--- Default coordinate ---");
        System.out.println(defaultCoordinate.getCoordinate() + " (dec: " + defaultCoordinate.getDecimalCoordinate() + ")");

        System.out.println("\n--- Set#1 (latitude and longitude) ---");
        System.out.println(width1.getCoordinate() + " (dec: "+ width1.getDecimalCoordinate() + ")");
        System.out.println(length1.getCoordinate() + " (dec: "+ length1.getDecimalCoordinate() + ")");

        System.out.println("\n--- Set#2 (latitude and longitude) ---");
        System.out.println(width2.getCoordinate() + " (dec: " + width2.getDecimalCoordinate() + ")");
        System.out.println(length2.getCoordinate() + " (dec: " + length2.getDecimalCoordinate() + ")");

        System.out.println("\n--< Average coordinates between 2 sets | Way#1  >--");
        System.out.println(midWidth1.getCoordinate() + " (dec: " + midWidth1.getDecimalCoordinate() + ")");
        System.out.println(midLength1.getCoordinate() + " (dec: " + midLength1.getDecimalCoordinate() + ")");

        System.out.println("\n--< Average coordinates between 2 sets | Way#2 >--");
        System.out.println(midWidth2.getCoordinate() + " (dec: " + midWidth2.getDecimalCoordinate() + ")");
        System.out.println(midLength2.getCoordinate() + " (dec: " + midLength2.getDecimalCoordinate() + ")");
    }
}
