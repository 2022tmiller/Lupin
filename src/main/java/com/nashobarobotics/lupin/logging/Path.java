package com.nashobarobotics.lupin.logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Path implements Cloneable {
    private static final Path empty = new Path();
    private List<String> path;

    public Path(String... path) {
        this.path = Arrays.asList(path);
    }

    public Path(ArrayList<String> path) {
        this.path = path;
    }

    public static Path fromString(String s) {
        return new Path(s.split("/"));
    }

    public static Path empty() {
        return empty;
    }

    public String toString() {
        return String.join("/", path);
    }

    public Path append(String segment) {
        Path p = this.clone();
        p.path.add(segment);
        return p;
    }

    public Path append(Path addition) {
        Path p = this.clone();
        p.path.addAll(addition.path);
        return p;
    }

    public Path parent() {
        Path p = this.clone();
        p.path.remove(p.path.size() - 1);
        return p;
    }

    public List<String> asList() {
        return path;
    }

    public int size() {
        return path.size();
    }
    
    public Path clone() {
        return new Path(new ArrayList<>(path));
    }
}
