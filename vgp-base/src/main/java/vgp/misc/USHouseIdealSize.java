package vgp.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import vgp.dispose.Disposable;

/**
 * A means to calculate an optimal number of members for the United States House
 * of Representatives.
 * 
 * @author (to be added)
 * @version 2.3
 * @since 2.2
 */
public final class USHouseIdealSize implements Disposable {

    /**
     * Each element in this set is the name of a different state.
     */
    private static final class StateNameSet extends TreeSet<String> {
    }

    /**
     * For each {@link Map.Entry} in this map, the entry's {@link Map.Entry#getKey()
     * key} is the name of a state and the entry's {@link Map.Entry#getValue()
     * value} is that state's apportionment population on 1 April 2020.
     */
    private static final class StatePopsMap extends TreeMap<String, Integer> {
    }

    /**
     * Keys are state names and each key's corresponding value is that state's
     * number of Representatives.
     */
    static final class StateToRepsMap extends TreeMap<String, Integer> {

        StateToRepsMap(StateToRepsMap calculateNumRepsForEachState) {
            super(calculateNumRepsForEachState);
        }

        StateToRepsMap() {
            super();
        }
    }

    private static final int TOTAL_NUMBER_OF_STATES = 50;

    private boolean disposed = false;

    private final StateNameSet stateNames = new StateNameSet();

    private final StatePopsMap statePopulations = new StatePopsMap();

    private final PriorityValueFunction priorityValueFunction = new PriorityValueFunction(this);

    private final SizeStateRepsFunction sizeStateRepsFunction = new SizeStateRepsFunction(this);

    /**
     * Zero-argument constructor.
     * 
     * @throws IOException
     */
    private USHouseIdealSize() throws IOException {
        super();

        /**
         * For each {@link Map.Entry} in this map, the entry's {@link Map.Entry#getKey()
         * key} is the name of a state and the entry's {@link Map.Entry#getValue()
         * value} is that state's number of Representatives due to the 2020 census.
         * <p>
         * Use this for testing.
         */
        final StateToRepsMap stateNumbersOfReps2020 = new StateToRepsMap();

        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("table1.txt")))) {

            String lineText = "";
            int lineNumber = 0;
            String stateName = null;
            int numberOfStates = 0;

            final int lineNumberToBeginAt = 6;
            final int numberOfColumns = 4;

            while (lineText != null && numberOfStates < TOTAL_NUMBER_OF_STATES) {
                lineText = reader.readLine();
                lineNumber++;

                if (lineNumber >= lineNumberToBeginAt) {
                    switch ((lineNumber - lineNumberToBeginAt) % numberOfColumns) {
                        case 0:
                            stateName = lineText;
                            stateNames.add(stateName);
                            break;
                        case 1:
                            statePopulations.put(stateName, Integer.parseInt(lineText));
                            break;
                        case 2:
                            stateNumbersOfReps2020.put(stateName, Integer.parseInt(lineText));
                            numberOfStates++;
                            break;
                        default:
                            break;
                    }
                }
            }
            if (stateNames.size() != TOTAL_NUMBER_OF_STATES) {
                throw new Error("stateNames.size() != " + TOTAL_NUMBER_OF_STATES);
            }
            if (statePopulations.size() != TOTAL_NUMBER_OF_STATES) {
                throw new Error("statePopulations.size() == " + TOTAL_NUMBER_OF_STATES);
            }
            if (!stateNames.equals(statePopulations.keySet())) {
                throw new Error("!stateNames.equals(statePopulations.keySet())");
            }
            if (!stateNumbersOfReps2020.equals(calculateNumRepsForEachState(435))) {
                throw new Error("!stateNumbersOfReps2020.equals(calculateNumRepsForEachState(435))");
            }
        } catch (Throwable e) {
            dispose();
            throw e;
        } finally {
            stateNumbersOfReps2020.clear();
        }
    }

    // https://constitution.congress.gov/constitution/
    // "The Number of Representatives shall not exceed one for every thirty
    // Thousand"
    private final int getMaxNumReps() {
        BigInteger totalPopulation = BigInteger.ZERO;
        for (Integer statePopulation : statePopulations.values()) {
            totalPopulation = totalPopulation.add(BigInteger.valueOf(statePopulation));
        }
        return totalPopulation.divide(BigInteger.valueOf(30000)).intValueExact();
    }

    private final double getMaxMinQuotientOfRatiosOfStatePopulationToNumReps(int sizeOfHouseOfRepresentatives) {
        final Collection<Double> ratios = new ArrayList<>();
        final StateToRepsMap numRepsForEachState = calculateNumRepsForEachState(sizeOfHouseOfRepresentatives);
        for (Entry<String, Integer> entry : numRepsForEachState.entrySet()) {
            final String stateName = entry.getKey();
            final double statePopulation_double = statePopulations.get(stateName);
            final double numReps_double = entry.getValue();
            ratios.add(statePopulation_double / numReps_double);
        }
        final double minOfRatios = Collections.min(ratios);
        final double maxOfRatios = Collections.max(ratios);
        ratios.clear();
        numRepsForEachState.clear();
        return maxOfRatios / minOfRatios;
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        disposed = true;
        stateNames.clear();
        statePopulations.clear();
        priorityValueFunction.dispose();
        sizeStateRepsFunction.dispose();
    }

    @Override
    public final boolean isDisposed() {
        return disposed;
    }

    // https://www.census.gov/topics/public-sector/congressional-apportionment/about/computing.html
    // Keys of return value are state names. Value for each key is number of
    // Representatives for that state.
    private final StateToRepsMap calculateNumRepsForEachState(final int sizeOfHouseOfRepresentatives) {
        verifyNotDisposed();

        if (sizeOfHouseOfRepresentatives < TOTAL_NUMBER_OF_STATES) {
            throw new IllegalArgumentException("sizeOfHouseOfRepresentatives < " + TOTAL_NUMBER_OF_STATES);
        }
        return sizeStateRepsFunction.calculateNumRepsForEachState(sizeOfHouseOfRepresentatives, TOTAL_NUMBER_OF_STATES, stateNames, priorityValueFunction);
    }

    @Override
    public String toString() {
        verifyNotDisposed();
        return "USHouseIdealSize [statePopulations=" + statePopulations + "]";
    }

    final int getStatePopulation(String stateName) {
        verifyNotDisposed();
        return statePopulations.get(stateName);
    }

    /**
     * The entry point for this class to calculate things from the command line.
     * 
     * @param args the string array passed from the command line
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final USHouseIdealSize usHouseIdealSize = new USHouseIdealSize();

        final int maxNumReps = usHouseIdealSize.getMaxNumReps();
        try {
            System.out.println(usHouseIdealSize);
            System.out.println();

            System.out.println("Size\tMaxMinQuotient\nNanoseconds");
            for (int sizeOfHouseOfRepresentatives = TOTAL_NUMBER_OF_STATES; sizeOfHouseOfRepresentatives < maxNumReps; sizeOfHouseOfRepresentatives++) {
                final long before = System.nanoTime();
                final double quotient = usHouseIdealSize.getMaxMinQuotientOfRatiosOfStatePopulationToNumReps(sizeOfHouseOfRepresentatives);
                final long after = System.nanoTime();
                final long timeItTook = after - before;
                System.out.println(String.format("%d\t%f\t%20d ns", sizeOfHouseOfRepresentatives, quotient, timeItTook));
            }
            System.out.println();
        } finally {
            usHouseIdealSize.dispose();
        }
    }
}
