package vgp.misc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import vgp.dispose.Disposable;

/**
 * https://www2.census.gov/programs-surveys/decennial/2020/data/apportionment/2020PriorityValues.pdf
 * 
 * @author (to be added)
 * @version 2.2
 * @since 2.2
 */
final class PriorityValueFunction implements Disposable {

    private USHouseIdealSize usHouseIdealSize;

    private final Map<PriorityValueCacheKey, Double> priorityValueCache = new LinkedHashMap<>();

    PriorityValueFunction(USHouseIdealSize usHouseIdealSize) {
        super();
        this.usHouseIdealSize = Objects.requireNonNull(usHouseIdealSize);
    }

    final double getPriorityValue(String stateName, int numberOfRepsForState) {
        verifyNotDisposed();
        Objects.requireNonNull(stateName);

        final PriorityValueCacheKey key = new PriorityValueCacheKey(stateName, numberOfRepsForState);

        if (!priorityValueCache.containsKey(key)) {

            final double numberOfRepsForState_double = numberOfRepsForState;

            final double statePopulation_double = usHouseIdealSize.getStatePopulation(stateName);

            final double priorityValue = statePopulation_double
                    / Math.sqrt(numberOfRepsForState_double * (numberOfRepsForState_double /*-*/ + 1.0));

            priorityValueCache.put(key, priorityValue);
        }
        return priorityValueCache.get(key);
    }

    @Override
    public void dispose() {
        if (isDisposed()) {
            return;
        }
        usHouseIdealSize = null;
        priorityValueCache.clear();
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