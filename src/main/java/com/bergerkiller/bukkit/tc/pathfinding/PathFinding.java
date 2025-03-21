package com.bergerkiller.bukkit.tc.pathfinding;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PathFinding {
    private static final class PathKey {
        private final PathNode from;
        private final PathNode to;

        public PathKey(PathNode node, PathNode destination) {
            this.from = node;
            this.to = destination;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }

        @Override
        public boolean equals(Object o) {
            PathKey other = (PathKey) o;
            return from == other.from && to == other.to;
        }
    }

    private static final Map<PathKey, PathSearchResult> cachedResults = new HashMap<>();

    // Used while calculating paths to avoid infinite recursion
    private final Map<PathNode, Double> visitedNodes = new HashMap<>();

    private PathFinding() {}

    public static PathSearchResult findBestPath(PathNode start, PathNode destination) {
        PathKey key = new PathKey(start, destination);

        if(cachedResults.containsKey(key)) {
            return cachedResults.get(key);
        }

        PathSearchResult result = PathFinding
                .find(start, destination)
                .orElseGet(() -> PathSearchResult.missing(start, destination));

        cachedResults.put(key, result);
        return result;
    }

    private static Optional<PathSearchResult> find(PathNode start, PathNode destination) {
        return new PathFinding().findBestPath(
                new PathSearchOperation(destination),
                start,
                0.0,
                0
        );
    }

    private Optional<PathSearchResult> findBestPath(PathSearchOperation search, PathNode currentNode, double startDistance, int depth) {
        Double previousRun = visitedNodes.get(currentNode);

        if(depth > 25) {
            return Optional.empty();
        }

        // Shortcut to quit early
        if (startDistance > search.getMaxTotalDistance()) {
            return Optional.empty();
        }

        // Either we recursively hit ourselves and the last search wasn't finished yet,
        // in which case it is set to DUMMY_NOT_FOUND. Or we had completed the search before and
        // already have the remainder of the trip. Either way, stop searching.
        if (previousRun != null && startDistance > previousRun) {
            return Optional.empty();
        }

        visitedNodes.put(currentNode, startDistance);

        // If destination == this, return instantly with 0 distance
        if (currentNode == search.getDestination()) {
            return Optional.of(search.acceptResult(startDistance, PathSearchResult.self(currentNode)));
        }

        // First time hitting this node. Initiate a new search of its neighbours.
        // Seed the initial result before it is known as not-found to avoid infinite recursion.
        // Before we proceed, see if a path to this same destination was already cached.
        // If so, we can simply try to use that

        Optional<PathSearchResult> result = findCachedSearchResult(currentNode, search.getDestination());
        if (result.isPresent()) {
            return Optional.of(search.acceptResult(startDistance, result.get()));
        }

        PathSearchResult bestResult = null;

        // Ask all neighbouring nodes for the same destination, recursively
        for(PathConnection neighbour : currentNode.getNeighbours()) {
            PathSearchResult neighResult = findBestPath(search, neighbour.destination, startDistance + neighbour.distance, depth + 1)
                    .orElse(PathSearchResult.DUMMY_NOT_FOUND);

            if (neighResult.isFound()) {
                bestResult = PathSearchResult.chain(
                        currentNode,
                        search.getDestination(),
                        neighbour,
                        neighResult
                );
            }
        }

        return Optional.ofNullable(bestResult);
    }

    private Optional<PathSearchResult> findCachedSearchResult(PathNode startNode, PathNode destination) {
        PathKey key = new PathKey(startNode, destination);
        return Optional.ofNullable(cachedResults.get(key));
    }
}
