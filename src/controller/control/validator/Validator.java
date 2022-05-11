package controller.control.validator;

public class Validator {
    public static final String PATTERN_LETTERS = "([A-Za-záéíóúüÁÉÍÓÚÜñÑ]{1,}(\\ [A-Za-záéíóúüÁÉÍÓÚÜñÑ]{1,})*)";
    public static final String PATTERN_HOURS = "([\\d]){1,2}";
    public static final String PATTERN_NUMBERS = "(([\\d]){1,})";
    public static final String PATTERN_NUMBERS_AND_LETTERS = "([A-Za-z-0-9áéíóúüÁÉÍÓÚÜñÑ]{1,}(\\ [A-Za-z0-9áéíóúüÁÉÍÓÚÜñÑ]{1,})*)";
    public static final String PATTERN_NAME = PATTERN_LETTERS;
    public static final String PATTERN_NUMBERS_AND_LETTER_WITH_STRANGE_SYMBOLS = "([A-Za-z0-9áéíóúüÁÉÍÓÚÜñÑ0\\-\\_\\,\\.\\(\\)\\[\\{\\}]{1,}(\\ [A-Za-z0-9áéíóúüÁÉÍÓÚÜñÑ\\-\\_\\,\\.\\(\\)\\{\\}]{1,})*)";
    //public static final String PATTERN_CURP = "/^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$/";
    //public static final String PATTERN_CURP = "/^([A-Z&]|[a-z&]{1})([AEIOU]|[aeiou]{1})([A-Z&]|[a-z&]{1})([A-Z&]|[a-z&]{1})([0-9]{2})(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])([HM]|[hm]{1})([AS|as|BC|bc|BS|bs|CC|cc|CS|cs|CH|ch|CL|cl|CM|cm|DF|df|DG|dg|GT|gt|GR|gr|HG|hg|JC|jc|MC|mc|MN|mn|MS|ms|NT|nt|NL|nl|OC|oc|PL|pl|QT|qt|QR|qr|SP|sp|SL|sl|SR|sr|TC|tc|TS|ts|TL|tl|VZ|vz|YN|yn|ZS|zs|NE|ne]{2})([^A|a|E|e|I|i|O|o|U|u]{1})([^A|a|E|e|I|i|O|o|U|u]{1})([^A|a|E|e|I|i|O|o|U|u]{1})([0-9]{2})$/";
    public static final String PATTERN_CURP = PATTERN_NUMBERS_AND_LETTERS;
    public static final String PATTERN_TELEPHONE = "([\\d]){8,10}";
    public static final String PATTERN_EMAIL = "[A-Za-z0-9\\-ñÑ_]{1,}(\\.([A-Za-z0-9\\-ñÑ_]{1,}))*@(([a-zA-Z]{2,})(\\.([a-z]{1,})){1,})";
    //public static final String PATTERN_RFC = "/^([A-ZÑ&]{3,4}) ?(?:- ?)?(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])) ?(?:- ?)?([A-Z\\d]{2})([A\\d])$/";
    public static final String PATTERN_RFC = PATTERN_NUMBERS_AND_LETTERS;
    public static final int LENGTH_GENERAL = 120;
    public static final int LENGTH_RFC = 13;
    public static final int LENGTH_TELEPHONE = 15;
    public static final int LENGTH_CURP = 18;
    public static final int LENGTH_EMAIL = 120;
    public static final int LENGTH_SMALL_TEXT = 12;
    public static final int LENGTH_HOUR = 2;
    public static final int MIN_YEARS_OLD = 18;
    public static final int LENGTH_LONG_TEXT = 1200;
    public static final int LENGTH_LONG_MEDIUM_TEXT = 600;
    public static final int LENGTH_LONG_SMALL_TEXT = 300;
    public static final int LENGTH_LONG_LONG_TEXT = 1600;


    public static boolean validateCURP() {
        return false;
    }

    /***
     * This method search for a match with a pattern
     * <p>
     * The purpose of this method it's try to match a text to a specified pattern
     * </p>
     * @param string the data string to be evaluated.
     * @param pattern the regex pattern to evaluate.
     * @return true if string match with pattern otherwise false.
     */
    public static boolean doesStringMatchPattern(String string, String pattern) {
        return string.matches(pattern);
    }

    /***
     * This method check if a string is larger than a specified limit.
     * <p>
     * The purpose of tihs method it's try to match a text to a specified amount of characters
     * </p>
     * @param string the data string to be evaluated.
     * @param limit the amount allowed characters.
     * @return true if string is larger than limit.
     */
    public static boolean isStringLargerThanLimitOrEmpty(String string, int limit) {
        return string.length() > limit || string.equals("");
    }


}
