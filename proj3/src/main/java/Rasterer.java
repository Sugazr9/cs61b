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
    private final double[] LonDPPs;
    private final double[] LonPerPic;
    /** imgRoot is the name of the directory containing the images.
     *  You may not actually need this for your class. */
    public Rasterer(String imgRoot) {
        leftMostLon = -122.2998046875;
        topMostLat = 37.892195547244356;
        rightMostLon = -122.2119140625;
        bottomMostLat = 37.82280243352756;
        LonDPPs = new double[8];
        LonPerPic = new double[8];
        for (int i = 1; i < 8; i++) {
            LonDPPs[i] = (rightMostLon - leftMostLon) / (Math.pow(2, i) * 256);
            LonPerPic[i] = LonDPPs[i] * 256;
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>Has dimensions of at least w by h, where w and h are the user viewport width
     *         and height.</li>
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
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double LonDDP = (params.get("lrlon") - params.get("ullon")) / params.get("w");
        int level = 1;
        for (int i = 1; i < 8; i++) {
            if(LonDPPs[i] < LonDDP) {
                level = i;
                break;
            } else if (i == 7) {
                level = 7;
            }
        }
        results.put("depth", level);
        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
                           + "your browser.");
        return results;
    }

    private String getRight(String image) {
        if (image.matches("*[24]")) {
            return "";
        }
        int curr = Integer.getInteger(image);
        int last = curr % 4;
        switch (last) {
            case 2: {
                return getRight(String.valueOf(curr / 10)) + "1";
            }
            case 4: {
                return getRight(String.valueOf(curr / 10)) + "3";
            }
            default: {
                return String.valueOf(curr + 1);
            }
        }
    }

    private String getBottom(String image) {
        if (image.matches("*[34]")) {
            return "";
        }
        int curr = Integer.getInteger(image);
        int last = curr % 4;
        switch (last) {
            case 3: {
                return getBottom(String.valueOf(curr / 10)) + "1";
            }
            case 4: {
                return getBottom(String.valueOf(curr / 10)) + "2";
            }
            default: {
                return String.valueOf(curr + 2);
            }
        }
    }
}
