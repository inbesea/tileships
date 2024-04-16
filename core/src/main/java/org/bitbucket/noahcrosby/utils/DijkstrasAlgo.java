package org.bitbucket.noahcrosby.utils;

import org.bitbucket.noahcrosby.shipGame.levelData.MapNode;

import java.util.*;

public class DijkstrasAlgo {

        public static Map<MapNode, Integer> shortestPath(MapNode start, MapNode end, Map<MapNode, List<MapNode>> graph) {
            Map<MapNode, Integer> dist = new HashMap<>();
            Map<MapNode, MapNode> prev = new HashMap<>();
            PriorityQueue<MapNode> pq = new PriorityQueue<>(Comparator.comparingInt(o -> dist.getOrDefault(o, Integer.MAX_VALUE)));

            dist.put(start, 0);
            pq.add(start);

            while (!pq.isEmpty()) {
                MapNode curr = pq.poll();
                if (curr.equals(end)) {
                    break;
                }
                if (dist.get(curr) == Integer.MAX_VALUE) {
                    break;
                }

                for (MapNode n : graph.getOrDefault(curr, Collections.emptyList())) {
                    int candidate = dist.getOrDefault(curr, 0) + n.getWeight();
                    if (candidate < dist.getOrDefault(n, Integer.MAX_VALUE)) {
                        dist.put(n, candidate);
                        prev.put(n, curr);
                        pq.add(n);
                    }
                }
            }

            if (!prev.containsKey(end)) {
                return Collections.emptyMap();
            }

            Map<MapNode, Integer> shortestPath = new HashMap<>();
            for (MapNode n = end; n != null; n = prev.get(n)) {
                shortestPath.put(n, dist.get(n));
            }

            return shortestPath;
        }
}
