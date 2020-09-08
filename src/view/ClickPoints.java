package view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author Ger Saris
 * @version 1.0
 * @date 12-03-2017
 */
public class ClickPoints {

    private HashMap<Point, String> points;
    private ArrayList<Integer> xvalues;
    private ArrayList<Integer> yvalues;

    private HashMap<Point, String> pointsWithoutMP;
    private ArrayList<Integer> xValuesWithoutMP;
    private ArrayList<Integer> yValuesWithoutMP;

    public ClickPoints() {
        points = new HashMap<>();
        xvalues = new ArrayList<>();
        yvalues = new ArrayList<>();

        pointsWithoutMP = new HashMap<>();
        xValuesWithoutMP = new ArrayList<>();
        yValuesWithoutMP = new ArrayList<>();
    }


    public String getKey(int clickX, int clickY) {
        String ret = "?";

        int foundX = 0;
        int foundY = 0;

        Collections.sort(xvalues);
        Collections.sort(yvalues);

        int lower;
        int higher;
        int i = 0;

        // find nearest X
        lower = -1000;
        higher = +1000;
        for (Integer o : xvalues) {
            i = o;

            if (i <= clickX && i > lower) {
                lower = i;
            }

            if (i >= clickX && i < higher) {
                higher = i;
            }
        }

        if ((clickX - lower) < (higher - clickX)) {
            foundX = lower;
        } else {
            foundX = higher;
        }

        // find nearest Y
        lower = -1000;
        higher = +1000;
        for (Integer o : yvalues) {
            i = o;

            if (i <= clickY && i > lower) {
                lower = i;
            }

            if (i >= clickY && i < higher) {
                higher = i;
            }
        }

        if ((clickY - lower) < (higher - clickY)) {
            foundY = lower;
        } else {
            foundY = higher;
        }

        //lookup nearest point based on nearest foundX, foundY
        Point lookup = new Point(foundX, foundY);
        ret = points.get(lookup);
        return ret;
    }

    public String getKeyWithoutMP(int clickX, int clickY) {
        String ret = "?";

        int foundX = 0;
        int foundY = 0;

        Collections.sort(xValuesWithoutMP);
        Collections.sort(yValuesWithoutMP);

        int lower;
        int higher;
        int i = 0;

        // find nearest X
        lower = -1000;
        higher = +1000;
        for (Integer o : xValuesWithoutMP) {
            i = o;

            if (i <= clickX && i > lower) {
                lower = i;
            }

            if (i >= clickX && i < higher) {
                higher = i;
            }
        }

        if ((clickX - lower) < (higher - clickX)) {
            foundX = lower;
        } else {
            foundX = higher;
        }

        // find nearest Y
        lower = -1000;
        higher = +1000;
        for (Integer o : yValuesWithoutMP) {
            i = o;

            if (i <= clickY && i > lower) {
                lower = i;
            }

            if (i >= clickY && i < higher) {
                higher = i;
            }
        }

        if ((clickY - lower) < (higher - clickY)) {
            foundY = lower;
        } else {
            foundY = higher;
        }

        //lookup nearest point based on nearest foundX, foundY
        Point lookup = new Point(foundX, foundY);
        ret = pointsWithoutMP.get(lookup);
        return ret;
    }

    public void addPoint(int x, int y, String key) {
        Point newpoint = new Point(x, y);
        if (!points.containsKey(newpoint)) {
            points.put(newpoint, key);
            xvalues.add(x);
            yvalues.add(y);
        }
    }

    public void addPointWithoutMP(int x, int y, String key) {
        Point newPoint = new Point(x, y);
        if (!pointsWithoutMP.containsKey(newPoint)) {
            pointsWithoutMP.put(newPoint, key);
            xValuesWithoutMP.add(x);
            yValuesWithoutMP.add(y);
        }
    }

    public HashMap<Point, String> getPointsWithoutMP() {
        return pointsWithoutMP;
    }

    public HashMap<Point, String> getPoints() {
        return points;
    }
}

