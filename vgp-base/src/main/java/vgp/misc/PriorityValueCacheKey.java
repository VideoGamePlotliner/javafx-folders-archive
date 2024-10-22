package vgp.misc;

import java.util.Objects;

/**
 * @author (to be added)
 * @version 2.3
 * @since 2.2
 */
final class PriorityValueCacheKey {

    private final String stateName;
    private final int numberOfRepsForState;

    PriorityValueCacheKey(String stateName, int numberOfRepsForState) {
        this.stateName = stateName;
        this.numberOfRepsForState = numberOfRepsForState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateName, numberOfRepsForState);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PriorityValueCacheKey)) {
            return false;
        }
        PriorityValueCacheKey other = (PriorityValueCacheKey) obj;
        return this.numberOfRepsForState == other.numberOfRepsForState
                && Objects.equals(this.stateName, other.stateName);
    }
}