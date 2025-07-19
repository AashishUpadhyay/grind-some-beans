package com.practice.problems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class LongestPalindromicPathInGraph {
    private static class MemoKey {
        int nd1, nd2, seen;
        
        public MemoKey(int nd1, int nd2, int seen) {
            this.nd1 = nd1;
            this.nd2 = nd2;
            this.seen = seen;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MemoKey)) return false;
            MemoKey other = (MemoKey) o;
            return nd1 == other.nd1 && nd2 == other.nd2 && seen == other.seen;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(nd1, nd2, seen);
        }
    }

    private HashMap<Integer, HashSet<Integer>> _graph;
    private String _label;
    private HashMap<MemoKey, Integer> _memo;

    public int maxLen(int n, int[][] edges, String label) {
        int rv = 1;
        if (n == 1) {
            return rv;
        }

        this._label = label;
        this._graph = new HashMap<Integer, HashSet<Integer>>();
        this._memo = new HashMap<MemoKey, Integer>();
        
        for (int i = 0; i < edges.length; i++) {
            if (!this._graph.containsKey(edges[i][0])) {
                this._graph.put(edges[i][0], new HashSet<Integer>());
            }
            if (!this._graph.containsKey(edges[i][1])) {
                this._graph.put(edges[i][1], new HashSet<Integer>());
            }
            this._graph.get(edges[i][0]).add(edges[i][1]);
            this._graph.get(edges[i][1]).add(edges[i][0]);
        }
        
        HashSet<Integer> visited = new HashSet<Integer>();
        for (int i = 0; i < n; i++) {
            rv = Math.max(rv, dfs(i, i, (1 << i)));
            visited.add(i);

            if (this._graph.containsKey(i)) {
                for (Integer j : this._graph.get(i)) {
                    if (!visited.contains(j)) {
                        rv = Math.max(rv, dfs(i, j, (1 << i) | (1 << j)));
                    }
                }
            }
        }
        return rv;
    }

    private int dfs(int nd1, int nd2, int seen) {
        // Create a MemoKey for memoization
        MemoKey key = new MemoKey(nd1, nd2, seen);
        
        // Check if we have already computed this state
        if (_memo.containsKey(key)) {
            return _memo.get(key);
        }

        int rv = 0;
        if (this._label.charAt(nd1) != this._label.charAt(nd2)) {
            _memo.put(key, 0);
            return 0;
        }

        if (nd2 < nd1) {
            return dfs(nd2, nd1, seen);
        }

        int curr_len = 0;
        if (nd1 == nd2) {
            curr_len = 1;
        } else {
            curr_len = 2;
        }

        if (!(this._graph.containsKey(nd1) && this._graph.containsKey(nd2))) {
            _memo.put(key, Math.max(rv, curr_len));
            return Math.max(rv, curr_len);
        }
        
        HashSet<Integer> nd1nbours = this._graph.get(nd1);
        HashSet<Integer> nd2nbours = this._graph.get(nd2);

        for (Integer nd1nbr : nd1nbours) {
            if ((seen & (1 << nd1nbr)) != 0)  // Fixed the bit check
                continue;
            
            for (Integer nd2nbr : nd2nbours) {
                if ((seen & (1 << nd2nbr)) != 0)  // Fixed the bit check
                    continue;

                if ((nd1nbr == nd2nbr)) {
                    if (nd1 != nd2)
                        rv = Math.max(rv, curr_len);
                } else {
                    rv = Math.max(rv, curr_len + dfs(nd1nbr, nd2nbr, seen | (1 << nd1nbr) | (1 << nd2nbr)));
                }
            }
        }
        
        // Store the result before returning
        _memo.put(key, Math.max(rv, curr_len));  // Fixed to include curr_len in final result
        return Math.max(rv, curr_len);  // Fixed to include curr_len in return
    }
} 