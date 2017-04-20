import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.
    private final double leftMostLon;
    private final double topMostLat;
    private final double rightMostLon;
    private final double bottomMostLat;
    private final double[] lonDPPs;
    private final double[] lonPerPic;
    private final double[] latPerPic;
    private final QuadTree tree;
    String directory;
    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        leftMostLon = MapServer.ROOT_ULLON;
        topMostLat = MapServer.ROOT_ULLAT;
        rightMostLon = MapServer.ROOT_LRLON;
        bottomMostLat = MapServer.ROOT_LRLAT;
        lonDPPs = new double[8];
        lonPerPic = new double[8];
        latPerPic = new double[8];
        directory = imgRoot;
        for (int i = 0; i < 8; i++) {
            lonDPPs[i] = (rightMostLon - leftMostLon) / (Math.pow(2.0, i) * 256);
            lonPerPic[i] = lonDPPs[i] * 256;
            latPerPic[i] = (topMostLat - bottomMostLat) / Math.pow(2.0, i);
        }
        tree = new QuadTree(leftMostLon, topMostLat, rightMostLon, bottomMostLat, 0, imgRoot, 0);
    }

    private class QuadTree {
        QuadTree topLeft;
        QuadTree topRight;
        QuadTree bottomLeft;
        QuadTree bottomRight;
        double ullon;
        double ullat;
        double lrlon;
        double lrlat;
        int image;
        String file;
        int depth;

        QuadTree(double ulLon, double ulLat, double lrLon, double lrLat, int level, String directory, int img) {
            depth = level;
            ullon = ulLon;
            ullat = ulLat;
            lrlon = lrLon;
            lrlat = lrLat;
            image = img;
            if (img == 0) {
                file = directory + "root.png";
            } else {
                file = directory + img + ".png";
            }
            if (depth == 7) {
                topLeft = null;
                topRight = null;
                bottomLeft = null;
                bottomRight = null;
            } else {
                double midLon = (ullon + lrlon) / 2;
                double midLat = (ullat + lrlat) / 2;
                topLeft = new QuadTree(ullon, ullat, midLon, midLat, level + 1, directory, img * 10 + 1);
                topRight = new QuadTree(midLon, ullat, lrlon, midLat, level + 1, directory, img * 10 + 2);
                bottomLeft = new QuadTree(ullon, midLat, midLon, lrlat, level + 1, directory, img * 10 + 3);
                bottomRight = new QuadTree(midLon, midLat, lrlon, lrlat, level + 1, directory, img * 10 + 4);
            }
        }

        QuadTree findStart(double ulLon, double ulLat, int depth) {
            if(depth == 0) {
                return this;
            }
            if (ulLon < (ullon + lrlon) / 2) {
                if (ulLat > (ullat + lrlat) / 2) {
                    return topLeft.findStart(ulLon, ulLat, depth - 1);
                } else {
                    return bottomLeft.findStart(ulLon, ulLat, depth - 1);
                }
            } else {
                if (ulLat > (ullat + lrlat) / 2) {
                    return topRight.findStart(ulLon, ulLat, depth - 1);
                } else {
                    return bottomRight.findStart(ulLon, ulLat, depth - 1);
                }
            }
        }
    }
    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     * </p>
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     *                    Can also be interpreted as the length of the numbers in the image
     *                    string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     *                    forget to set this to true! <br>
     * @see #REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        double ullat = params.get("ullat");
        double lrlat = params.get("lrlat");
        double ullon = params.get("ullon");
        double lrlon = params.get("lrlon");
        double lonDDP = (lrlon - ullon) / params.get("w");
        if (ullon > rightMostLon || lrlon < leftMostLon) {
            results.put("query_success", false);
            return results;
        } else if (ullat < bottomMostLat || lrlat > topMostLat) {
            results.put("query_success", false);
            return results;
        }
        if (ullon < leftMostLon) {
            ullon = leftMostLon;
        }
        if (ullat > topMostLat) {
            ullat = topMostLat;
        }
        int level = 0;
        for (int i = 0; i < 8; i++) {
            if (lonDPPs[i] <= lonDDP) {
                level = i;
                break;
            } else if (i == 7) {
                level = 7;
            }
        }
        results.put("depth", level);
        QuadTree start = tree.findStart(ullon, ullat, level);
        double left = start.ullon;
        double top = start.ullat;
        results.put("raster_ul_lon", left);
        results.put("raster_ul_lat", top);
        double lonPP = lonPerPic[level];
        double latPP = latPerPic[level];
        Double l = (lrlon - left) / lonPP;
        if (l % 1 != 0) {
            l += 1;
        }
        int length = l.intValue();
        results.put("raster_lr_lon", start.ullon + length * lonPP);
        Double h = (top - lrlat) / latPP;
        if (h % 1 != 0) {
            h += 1;
        }
        int height = h.intValue();
        results.put("raster_lr_lat", top - height * latPP);
        String[][] raster = new String[height][length];
        for (int i = 0; i < raster.length; i++) {
            double curr = left;
            for (int j = 0; j < raster[0].length; j++) {
                QuadTree node = tree.findStart(curr, top, level);
                raster[i][j] = node.file;
                curr = node.lrlon;
                if (j == length - 1) {
                    top = node.lrlat;
                }
            }
        }
        results.put("render_grid", raster);
        results.put("query_success", true);
        return results;
    }
}
