package brood.com.medcrawler;

/**
 * Created by nactus on 9/17/15.
 */
public class KeyProcessor {

    public static enum Mode {
        ALPHANUMERIC, NUMERIC
    }

    public static String generateRandomString(Mode mode) throws Exception {

        StringBuffer buffer = new StringBuffer();
        String characters = "";
        final int mylength = 8;  // this needs to stay at 8 !!

        /* Choose the type of string to be generated */
        switch(mode){

            case ALPHANUMERIC:
                characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
                break;

            case NUMERIC:
                characters = "1234567890";
                break;
        }

        // This feeds the randomizer the total number of possible permutations
        int charactersLength = characters.length();

        // Gen the key
        for (int i = 0; i < mylength; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }

        // Add the "-" between the first 4 characters
        //String tempReturn = new String(new StringBuilder(buffer.toString()).
        //        insert(buffer.toString().length()-4, "-"));

        // Spit it out
        return buffer.toString();
    }
}
