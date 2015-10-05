/**
 * @author Tony Pinkston
 */
package discover.vdis.marking.army;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import discover.Discover;

public class ArmyTracking {

    private static final Logger logger = Discover.getLogger();
    
    private static final String DIVISION = "EM_USARMY_DIV.CSV";
    private static final String BRIGADE = "EM_USARMY_BDE.CSV";
    private static final String COMPANY = "EM_USARMY_CO.CSV";
    private static final String PLATOON = "EM_USARMY_PLT.CSV";
    private static final String SECTION = "EM_USARMY_SEC.CSV";
    private static final String SQUAD = "EM_USARMY_SQU.CSV";
    private static final String TEAM = "EM_USARMY_TM.CSV";
    
    /** Divisions, brigades, companies, platoons, sections, squads, teams. */ 
    private static final Echelons<AbstractEchelon> echelons;
    
    /** High level units (battalions). */
    private static final HashMap<ArmyDivision, List<ArmyBattalion>> battalions;

    static {
    
        battalions = new HashMap<ArmyDivision, List<ArmyBattalion>>();
        echelons = new Echelons<AbstractEchelon>();
    }
    
    /**
     * Loads echelon data from files.
     */
    public static void load() {
        
        long start = System.currentTimeMillis();
        int total = 0;

        total += loadDivisions();
        total += loadBrigades();
        total += loadBattalions();
        total += loadCompanies();
        total += loadPlatoons();
        total += loadSections();
        total += loadSquads();
        total += loadTeams();
        
        long duration = (System.currentTimeMillis() - start);
        
        System.out.print("Loaded " + total + " VDIS echelons in ");
        System.out.println(duration + " milliseconds");
        
        if (logger.isLoggable(Level.FINER)) {
            
            StringBuffer buffer = new StringBuffer("\n");
            
            for(Class<? extends AbstractEchelon> key : echelons.keySet()) {
                
                List<AbstractEchelon> list = getList(key);
                
                buffer.append("\n");
                buffer.append(key.getName());
                buffer.append("\n");
                
                for(AbstractEchelon value : list) {
                    
                    buffer.append("    ");
                    buffer.append(value.value);
                    buffer.append(": ");
                    buffer.append(value.name);
                    buffer.append(", \"");
                    buffer.append(value.description);
                    buffer.append("\"\n");
                }
            }

            for(ArmyDivision division : battalions.keySet()) {
                
                buffer.append("\n");
                buffer.append(ArmyBattalion.class.getName());
                buffer.append(" for ArmyDivision \"");
                buffer.append(division.description);
                buffer.append("\"\n");
                
                List<ArmyBattalion> list = battalions.get(division);
                
                for(ArmyBattalion value : list) {
                    
                    buffer.append("    ");
                    buffer.append(value.value);
                    buffer.append(": ");
                    buffer.append(value.name);
                    buffer.append(", \"");
                    buffer.append(value.description);
                    buffer.append(", \"");
                    buffer.append(value.getDivision().name);
                    buffer.append(", \"");
                    buffer.append(value.getBrigade().name);
                    buffer.append("\"\n");
                }
            }
            
            logger.finer(buffer.toString());
        }
    }
    
    /**
     * @param echelon - Class that extends AbstractEchelon
     * 
     * @return List of echelon enumeration values.
     */
    public static List<AbstractEchelon> getList(
        Class<? extends AbstractEchelon> echelon) {

        List<AbstractEchelon> values = echelons.get(echelon);
        
        if (values == null) {
            
            return null;
        }
        else {

            return Collections.unmodifiableList(values);
        }
    }
    
    public static AbstractEchelon[] getValues(
        Class<? extends AbstractEchelon> echelon) {
        
        List<AbstractEchelon> values = getList(echelon);
        
        if (values == null) {
            
            return null;
        }
        else {

            return values.toArray(new AbstractEchelon[values.size()]);
        }
    }

    /**
     * @param division - ArmyDivision
     * 
     * @return List of echelon enumeration values.
     */
    public static List<ArmyBattalion> getBattalions(ArmyDivision division) {
        
        List<ArmyBattalion> values = battalions.get(division);
        
        if (values == null) {
            
            return null;
        }
        else {

            return Collections.unmodifiableList(values);
        }
    }
    
    /**
     * @param echelon - Class that extends AbstractEchelon
     * @param value - Enumeration value
     * 
     * @return Echelon value.
     */
    public static <T extends AbstractEchelon> T getValue(
        Class<T> echelon, 
        int value) {

        List<AbstractEchelon> list = getList(echelon);
        
        if (list == null) {
            
            logger.warning("No echelon values for " + echelon.getName());
        }
        else for(AbstractEchelon item : list) {
            
            if (item.value == value) {
                
                return echelon.cast(item);
            }
        }
        
        logger.warning(
            "No echelon value for " + value +
            ": " + echelon.getSimpleName());
        
        return null;
    }
    
    /**
     * @param division - ArmyDivision
     * @param value - Enumeration value
     * 
     * @return ArmyBattalion value.
     */
    public static ArmyBattalion getValue(ArmyDivision division, int value) {
        
        List<ArmyBattalion> list = getBattalions(division);
        
        if (list == null) {
            
            logger.warning("No echelon values for " + division.name);
        }
        else for(ArmyBattalion item : list) {
            
            if (item.value == value) {
                
                return item;
            }
        }
        
        return null;
    }
    
    private static int loadDivisions() {
        
        List<String[]> rows = parse(DIVISION);

        if (rows != null) {
            
            List<AbstractEchelon> list = new ArrayList<AbstractEchelon>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                
                String tokens[] = rows.get(i);

                try {
                    
                    list.add(new ArmyDivision(
                        getInt(tokens, 0),
                        tokens[1],
                        tokens[2]));
                }
                catch(NumberFormatException exception) {

                    logger.severe(
                        "NumberFormatException!" +
                        "\n  file: " + DIVISION +
                        "\n  line: " + (i + 2) +
                        "\n  error: " + exception.getMessage());
                }
            }
            
            echelons.add(ArmyDivision.class, list);
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
    
    private static int loadBrigades() {
        
        List<String[]> rows = parse(BRIGADE);

        if (rows != null) {

            List<AbstractEchelon> list = new ArrayList<AbstractEchelon>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                
                String tokens[] = rows.get(i);

                try {
                    
                    list.add(new ArmyBrigade(
                        getInt(tokens, 0),
                        tokens[1],
                        tokens[2]));
                }
                catch(NumberFormatException exception) {

                    logger.severe(
                        "NumberFormatException!" +
                        "\n  file: " + BRIGADE +
                        "\n  line: " + (i + 2) +
                        "\n  error: " + exception.getMessage());
                }
            }
            
            echelons.add(ArmyBrigade.class, list);
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
    
    private static int loadBattalions() {
        
        List<AbstractEchelon> list = getList(ArmyDivision.class);
        
        if (list != null) {
            
            for(AbstractEchelon item : list) {
                
                loadBattalions((ArmyDivision)item);
            }
        }
        
        return ((list == null) ? 0 : list.size());
   }
    
    private static int loadBattalions(ArmyDivision division) {

        String name = ("EM_USARMY_BN_" + division.value + "DIV.CSV");
        List<String[]> rows = parse(name);

        if (rows != null) {

            List<ArmyBattalion> list = new ArrayList<ArmyBattalion>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                
                String tokens[] = rows.get(i);

                try {
                    
                    list.add(new ArmyBattalion(
                        getInt(tokens, 0),
                        tokens[1],
                        tokens[2],
                        getInt(tokens, 3),
                        getInt(tokens, 4),
                        tokens[5],
                        division,
                        getValue(ArmyBrigade.class, getInt(tokens, 7))));
                }
                catch(NumberFormatException exception) {

                    logger.severe(
                        "NumberFormatException!" +
                        "\n  file: " + name +
                        "\n  line: " + (i + 2) +
                        "\n  error: " + exception.getMessage());
                }
            }
            
            if (!list.isEmpty()) {
                
                battalions.put(division, list);
            }
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
   
    private static int loadCompanies() {
        
        List<String[]> rows = parse(COMPANY);

        if (rows != null) {
            
            List<AbstractEchelon> list = new ArrayList<AbstractEchelon>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                
                String tokens[] = rows.get(i);

                try {
                    
                    list.add(new ArmyCompany(
                        getInt(tokens, 0),
                        tokens[1],
                        tokens[2],
                        getInt(tokens, 3),
                        tokens[4]));
                }
                catch(NumberFormatException exception) {

                    logger.severe(
                        "NumberFormatException!" +
                        "\n  file: " + COMPANY +
                        "\n  line: " + (i + 2) +
                        "\n  error: " + exception.getMessage());
                }
            }
            
            echelons.add(ArmyCompany.class, list);
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
    
    private static int loadPlatoons() {
        
        List<String[]> rows = parse(PLATOON);

        if (rows != null) {
            
            List<AbstractEchelon> list = new ArrayList<AbstractEchelon>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                 
                 String tokens[] = rows.get(i);

                 try {
                     
                     list.add(new ArmyPlatoon(
                         getInt(tokens, 0),
                         tokens[1],
                         tokens[2],
                         getInt(tokens, 3),
                         tokens[4]));
                 }
                 catch(NumberFormatException exception) {

                     logger.severe(
                         "NumberFormatException!" +
                         "\n  file: " + PLATOON +
                         "\n  line: " + (i + 2) +
                         "\n  error: " + exception.getMessage());
                 }
             }
            
             echelons.add(ArmyPlatoon.class, list);
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
    
    private static int loadSections() {
        
        List<String[]> rows = parse(SECTION);

        if (rows != null) {
            
            List<AbstractEchelon> list = new ArrayList<AbstractEchelon>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                
                String tokens[] = rows.get(i);

                try {
                    
                    list.add(new ArmySection(
                        getInt(tokens, 0),
                        tokens[1],
                        tokens[2],
                        tokens[3]));
                }
                catch(NumberFormatException exception) {

                    logger.severe(
                        "NumberFormatException!" +
                        "\n  file: " + SECTION +
                        "\n  line: " + (i + 2) +
                        "\n  error: " + exception.getMessage());
                }
            }
            
            echelons.add(ArmySection.class, list);
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
    
    private static int loadSquads() {
        
        List<String[]> rows = parse(SQUAD);

        if (rows != null) {
            
            List<AbstractEchelon> list = new ArrayList<AbstractEchelon>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                
                String tokens[] = rows.get(i);

                try {
                    
                    list.add(new ArmySquad(
                        getInt(tokens, 0),
                        tokens[1],
                        tokens[2],
                        tokens[3]));
                }
                catch(NumberFormatException exception) {

                    logger.severe(
                        "NumberFormatException!" +
                        "\n  file: " + SQUAD +
                        "\n  line: " + (i + 2) +
                        "\n  error: " + exception.getMessage());
                }
            }
            
            echelons.add(ArmySquad.class, list);
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
    
    private static int loadTeams() {
        
        List<String[]> rows = parse(TEAM);

        if (rows != null) {

            List<AbstractEchelon> list = new ArrayList<AbstractEchelon>();
            
            for(int i = 0, size = rows.size(); i < size; ++i) {
                
                String tokens[] = rows.get(i);

                try {
                    
                    list.add(new ArmyTeam(
                        getInt(tokens, 0),
                        tokens[1],
                        tokens[2],
                        tokens[3]));
                }
                catch(NumberFormatException exception) {

                    logger.severe(
                        "NumberFormatException!" +
                        "\n  file: " + TEAM +
                        "\n  line: " + (i + 2) +
                        "\n  error: " + exception.getMessage());
                }
            }
            
            echelons.add(ArmyTeam.class, list);
        }
        
        return ((rows == null) ? 0 : rows.size());
    }
    
    private static List<String[]> parse(String name) {
        
        BufferedReader reader = getReader(name);
        
        if (reader != null) {
            
            try {

                // First line is the header, ignore contents just 
                // get number of tokens...
                String tokens[] = getTokens(reader.readLine());
                
                int line = 1;
                
                if (tokens != null) {
                    
                    final int columns = tokens.length;
                    List<String[]> rows = new ArrayList<String[]>();
                    
                    while(tokens != null) {
                    
                        tokens = getTokens(reader.readLine());
                        ++line;
                        
                        if (tokens != null) {
                            
                            if (tokens.length == columns) {
                                
                                rows.add(tokens);
                            }
                            else {
                                
                                logger.warning(
                                    "Invalid number of tokens!" +
                                    "\n  file: " + name +
                                    "\n  line: " + line +
                                    "\n  tokens: " + Arrays.toString(tokens));
                            }
                        }
                    }
                    
                    return rows;
                }
                
                reader.close();
            }
            catch(Exception exception) {
                
                logger.log(Level.SEVERE, "Caught exception!", exception);
            }
        }
        
        return null;
    }
    
    private static BufferedReader getReader(String name) {
        
        InputStream stream = ArmyTracking.class.getResourceAsStream(name);
        
        if (stream == null) {
            
            return null;
        }
        else {
            
            return new BufferedReader(new InputStreamReader(stream));
        }
    }
    
    private static int getInt(
        String array[],
        int i) throws NumberFormatException {
        
        return Integer.parseInt(array[i]);
    }
    
    private static String[] getTokens(String line) {
        
        if (line == null) {
            
            return null;
        }
        else {
            
            StringTokenizer tokenizer = new StringTokenizer(line, ",");
            
            String array[] = new String[tokenizer.countTokens()];
            
            for(int i = 0; i < array.length; ++i) {
                
                array[i] = tokenizer.nextToken();
            }
            
            return array;
        }
    }
    
    static class Echelons<E> {

        private final HashMap<Class<? extends E>, List<E>> map;
        
        public Echelons() {
            
            this.map = new HashMap<Class<? extends E>, List<E>>();
        }
        
        public Set<Class<? extends E>> keySet() {
            
            return this.map.keySet();
        }
        
        public void add(Class<? extends E> key, List<E> list) {
            
            this.map.put(key, list);
        }
        
        public List<E> get(Class<? extends E> key) {
            
            // Intentionally throws NPE if null...
            key.getClass();
            
            return this.map.get(key);
        }
    }
}
