package vgp.misc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

import vgp.dispose.Disposable;
import vgp.misc.USHouseIdealSize.StateToRepsMap;

/**
 * @author (to be added)
 * @version 2.3
 * @since 2.3
 */
final class SizeStateRepsFunction implements Disposable {

    private USHouseIdealSize usHouseIdealSize;

    /**
     * Keys are sizes of the House of Representatives.
     */
    private final Map<Integer, StateToRepsMap> sizeStateRepsCache = new LinkedHashMap<>();

    SizeStateRepsFunction(USHouseIdealSize usHouseIdealSize) {
        super();
        this.usHouseIdealSize = Objects.requireNonNull(usHouseIdealSize);
    }

    // Keys of parameter are state names. Value for each key is number of
    // Representatives for that state.
    private final String getNameOfStateWithHighestPriorityValue(final StateToRepsMap numRepsForEachState,
            final TreeSet<String> stateNames, final PriorityValueFunction priorityValueFunction) {
        verifyNotDisposed();
        Objects.requireNonNull(numRepsForEachState);

        String nameOfStateWithHighestPriorityValue = null;
        double maximumPriorityValue = Double.NEGATIVE_INFINITY;

        for (String stateName : stateNames) {

            final int numReps = numRepsForEachState.get(stateName);

            final double priorityValue = priorityValueFunction.getPriorityValue(stateName, numReps);

            if (nameOfStateWithHighestPriorityValue == null) {
                maximumPriorityValue = priorityValue;
                nameOfStateWithHighestPriorityValue = stateName;
            }

            if (Double.compare(maximumPriorityValue, priorityValue) < 0) {
                maximumPriorityValue = priorityValue;
                nameOfStateWithHighestPriorityValue = stateName;
            }
        }

        return Objects.requireNonNull(nameOfStateWithHighestPriorityValue);
    }

    // https://www.census.gov/topics/public-sector/congressional-apportionment/about/computing.html
    // Keys of return value are state names. Value for each key is number of
    // Representatives for that state. Return value is copied from cache.
    final StateToRepsMap calculateNumRepsForEachState(final int sizeOfHouseOfRepresentatives,
            final int TOTAL_NUMBER_OF_STATES, final TreeSet<String> stateNames,
            final PriorityValueFunction priorityValueFunction) {
        verifyNotDisposed();

        if (TOTAL_NUMBER_OF_STATES == 0) {
            throw new IllegalArgumentException("Zero states");
        }

        if (TOTAL_NUMBER_OF_STATES < 0) {
            throw new IllegalArgumentException("Negative number of states");
        }

        if (sizeOfHouseOfRepresentatives < TOTAL_NUMBER_OF_STATES) {
            throw new IllegalArgumentException("sizeOfHouseOfRepresentatives < " + TOTAL_NUMBER_OF_STATES);
        }

        Objects.requireNonNull(stateNames);
        if (stateNames.size() != TOTAL_NUMBER_OF_STATES) {
            throw new IllegalArgumentException("stateNames.size() != " + TOTAL_NUMBER_OF_STATES);
        }

        Objects.requireNonNull(priorityValueFunction);
        priorityValueFunction.verifyNotDisposed();

        if (!sizeStateRepsCache.containsKey(sizeOfHouseOfRepresentatives)) {

            final StateToRepsMap numRepsForEachState = new StateToRepsMap();

            if (sizeOfHouseOfRepresentatives == TOTAL_NUMBER_OF_STATES) {
                for (String stateName : stateNames) {
                    numRepsForEachState.put(stateName, 1);
                }
            } else {
                StateToRepsMap previousNumRepsForEachState = calculateNumRepsForEachState(
                        sizeOfHouseOfRepresentatives - 1, TOTAL_NUMBER_OF_STATES, stateNames, priorityValueFunction);

                final String nameOfStateWithHighestPriorityValue = getNameOfStateWithHighestPriorityValue(
                        previousNumRepsForEachState, stateNames, priorityValueFunction);

                Objects.requireNonNull(nameOfStateWithHighestPriorityValue);

                final int oldNumber = previousNumRepsForEachState.get(nameOfStateWithHighestPriorityValue);

                numRepsForEachState.putAll(previousNumRepsForEachState);
                numRepsForEachState.put(nameOfStateWithHighestPriorityValue, oldNumber + 1);
            }

            int totalNumReps = 0;
            for (String stateName : stateNames) {
                totalNumReps += numRepsForEachState.get(stateName);
            }

            if (totalNumReps != sizeOfHouseOfRepresentatives) {
                throw new Error(String.format("totalNumReps is %d but sizeOfHouseOfRepresentatives is %d", totalNumReps,
                        sizeOfHouseOfRepresentatives));
            }

            sizeStateRepsCache.put(sizeOfHouseOfRepresentatives, numRepsForEachState);
        }

        // We must copy the return value of get()
        return new StateToRepsMap(sizeStateRepsCache.get(sizeOfHouseOfRepresentatives));
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        usHouseIdealSize = null;
        sizeStateRepsCache.values().forEach(StateToRepsMap::clear);
        sizeStateRepsCache.clear();
    }

    @Override
    public final boolean isDisposed() {
        return usHouseIdealSize == null;
    }

    @Override
    public void verifyNotDisposed() {
        Disposable.super.verifyNotDisposed();
        usHouseIdealSize.verifyNotDisposed();
    }
}
