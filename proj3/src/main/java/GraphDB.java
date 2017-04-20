import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    HashMap<Long, Node> nodes = new HashMap<>();
    HashMap<Long, ArrayList<Edge>> edges = new HashMap<>();
    Trie names = new Trie("");

    private class Trie {
        String value;
        ArrayList<Node> nodes;
        ArrayList<Trie> children;
        int valLength;

        Trie(String curr) {
            value = curr;
            valLength = curr.length();
            nodes = new ArrayList<>();
            children = new ArrayList<>();
        }

        void addNode(Node curr, String name) {
            if (name.length() == value.length()) {
                nodes.add(curr);
                return;
            }
            Trie next = null;
            String upNext = value + name.charAt(valLength);
            for (Trie check : children) {
                if (check.value.equals(upNext)) {
                    next = check;
                    break;
                }
            }
            if (next == null) {
                next = new Trie(upNext);
                children.add(next);
            }
            next.addNode(curr, name);
        }

        Trie findStart(String word) {
            if (word.equals(value)) {
                return this;
            }
            String upNext = word.substring(0, valLength + 1);
            for (Trie check : children) {
                if (check.value.equals(upNext)) {
                    return check.findStart(word);
                }
            }
            return null;
        }

        void addNodes(LinkedList<String> master) {
            for (Node n : nodes) {
                master.add(n.name);
            }
            if (children.size() == 0) {
                return;
            }
            for (Trie child : children) {
                child.addNodes(master);
            }
        }
    }
    private class Node {
        double lat;
        double lon;
        String name;
        long id;

        Node(long iden, double latitude, double longitude) {
            id = iden;
            lat = latitude;
            lon = longitude;
        }
    }
    private class Edge {
        long p1;
        long p2;
        int speedLimit;
        String name;

        Edge(long first, long second, String id, int limit) {
            p1 = first;
            p2 = second;
            speedLimit = limit;
            name = id;
        }

        long getOtherVert(long v) {
            if (v == p1) {
                return p2;
            }
            return p1;
        }

    }
    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputFile, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Object[] vert = edges.keySet().toArray();
        for (Object v : vert) {
            ArrayList edge = edges.get(v);
            if (edge.size() < 1) {
                edges.remove(v);
                nodes.remove(v);
            }
        }
    }

    /** Returns an iterable of all vertex IDs in the graph. */
    Iterable<Long> vertices() {
        return nodes.keySet();
    }

    /** Returns ids of all vertices adjacent to v. */
    Iterable<Long> adjacent(long v) {
        ArrayList<Edge> next = this.edges.get(v);
        ArrayList<Long> adjs = new ArrayList<>();
        for (int i = 0; i < next.size(); i++) {
            adjs.add(next.get(i).getOtherVert(v));
        }
        return adjs;
    }

    /** Returns the Euclidean distance between vertices v and w, where Euclidean distance
     *  is defined as sqrt( (lonV - lonV)^2 + (latV - latV)^2 ). */
    double distance(long v, long w) {
        return Math.sqrt(Math.pow(lon(v) - lon(w), 2) + Math.pow(lat(v) - lat(w), 2));
    }

    /** Returns the vertex id closest to the given longitude and latitude. */
    long closest(double lon, double lat) {
        long result = 0;
        double d = 10000;
        for (long v : vertices()) {
            double curr = Math.sqrt(Math.pow(lon(v) - lon, 2) + Math.pow(lat(v) - lat, 2));
            if (curr < d) {
                d = curr;
                result = v;
            }
        }
        return result;
    }

    /** Longitude of vertex v. */
    double lon(long v) {
        Node vertex = nodes.get(v);
        return vertex.lon;
    }

    /** Latitude of vertex v. */
    double lat(long v) {
        Node vertex = nodes.get(v);
        return vertex.lat;
    }

    void addNode(long id, double lon, double lat) {
        nodes.put(id, new Node(id, lat, lon));
        edges.put(id, new ArrayList<>());
    }

    void addEdge(long first, long second, String name, int speed) {
        Edge e = new Edge(first, second, name, speed);
        edges.get(first).add(e);
        edges.get(second).add(e);
    }

    void updateNode(long id, String name) {
        Node vertex = nodes.get(id);
        vertex.name = name;
        names.addNode(vertex, cleanString(name));
    }

    LinkedList<Map<String, Object>> search(String locationName) {
        String clean = cleanString(locationName);
        Trie needed = names.findStart(clean);
        LinkedList<Map<String, Object>> result = new LinkedList<>();
        if (needed == null) {
            return result;
        }
        for (Node n : needed.nodes) {
            HashMap<String, Object> info = new HashMap<>();
            result.add(info);
            info.put("lat", n.lat);
            info.put("lon", n.lon);
            info.put("name", n.name);
            info.put("id", n.id);
        }
        return result;
    }

    LinkedList<String> prefixSearch(String prefix) {
        String clean = cleanString(prefix);
        Trie needed = names.findStart(clean);
        LinkedList<String> result = new LinkedList<>();
        if (needed == null) {
            return result;
        }
        needed.addNodes(result);
        return result;
    }
}
