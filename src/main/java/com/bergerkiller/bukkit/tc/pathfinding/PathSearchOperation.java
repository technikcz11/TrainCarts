package com.bergerkiller.bukkit.tc.pathfinding;

public class PathSearchOperation {
    /**
     * Destination node being reached
     */
    private final PathNode destination;
    /**
     * Maximum distance allowed for a valid search result. Avoids navigating
     * paths that are longer than other paths that are already found.
     */
    private double maxTotalDistance = Double.MAX_VALUE;

    public PathSearchOperation(PathNode destination) {
        this.destination = destination;
    }

    public PathNode getDestination() {
        return destination;
    }

    public double getMaxTotalDistance() {
        return maxTotalDistance;
    }

    /**
     * Notifies that a search resulted in a solution, and wants to check whether
     * the total distance is lower than any previous result (or no result).
     *
     * @param startDistance Distance before this part of the result was found
     * @param result        Part of the search path solution
     * @return input result if accepted (or not found, unchanged), DUMMY_NOT_FOUND if not accepted
     */
    public PathSearchResult acceptResult(double startDistance, PathSearchResult result) {
        if (!result.isFound()) {
            return result;
        }
        double total = startDistance + result.getDistance();
        if (total < maxTotalDistance) {
            maxTotalDistance = total;
            return result;
        } else {
            return PathSearchResult.DUMMY_NOT_FOUND;
        }
    }
}
