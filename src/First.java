import processing.core.PApplet;
import megamu.mesh.*;

import java.awt.geom.Point2D;

public class First extends PApplet {
    /* main method of sketch */

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"First"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    public void settings() {
        size(800, 800);
    }

    /* sketch initial setup */
    public void setup() {
        background(255);
        drawVoronoi();


    }

    /* put something on the stage */
    public void draw() {


    }

    public void drawPoints(float[][] pts, float size) {
        int l = pts.length;
        for (int i = 0; i < l; i++) {
            ellipse(pts[i][0], pts[i][1], size, size);
        }
    }

    public void drawPoints(Point2D.Float[] pts, float size) {
        int l = pts.length;
        for (int i = 0; i < l; i++) {
            ellipse((float) (pts[i].getX()), (float) (pts[i].getY()), size, size);
        }
    }


    float[][] getArrayFromPts(Point2D.Float[] pts) {
        int l = pts.length;
        float[][] array = new float[l][2];
        for (int i = 0; i < l; i++) {
            array[i][0] = (float) (pts[i].getX());
            array[i][1] = (float) (pts[i].getY());
        }
        return array;
    }

    Point2D.Float[] getPtsfromArray(float[][] array) {
        Point2D.Float[] pts = new Point2D.Float[array.length];
        int l = pts.length;
        for (int i = 0; i < l; i++) {
            pts[i] = new Point2D.Float(array[i][0], array[i][1]);
        }
        return pts;
    }

    float[][] scaleAbout(float k, float xx,float yy,float[][]pts){
        float [][] result = new float[pts.length][2];
        for(int i=0;i<pts.length;i++){
            result[i][0] = k*(pts[i][0] - xx)+xx;
            result[i][1] = k*(pts[i][1] - yy)+yy;
        }
        return result;
    }


    void drawVoronoi() {

        int numPts = 40;
        Point2D.Float[] points = new Point2D.Float[numPts];
        for (int i = 0; i < numPts; i++) {
            points[i] = new Point2D.Float(random(0, 800), random(0, 800));
        }


        Voronoi myVoronoi = new Voronoi(getArrayFromPts(points));

        //Draw Pts
        noStroke();
        fill(255, 0, 0);
        drawPoints(points, 10);

        //float[][] myEdges = myVoronoi.getEdges();

        //int numEdges = myEdges.length;
        //int col = 0;
        //int colStep = 200 / numEdges;


        //Uncomment to draw Edges
  /*
  for (int i=0; i<numEdges; i++)
   {
   float startX = myEdges[i][0];
   float startY = myEdges[i][1];
   float endX = myEdges[i][2];
   float endY = myEdges[i][3];
   //stroke(col);
   //strokeWeight(i+1);

   line( startX, startY, endX, endY );
   col += colStep;
   println(col);
   }
   */

        MPolygon[] myRegions = myVoronoi.getRegions();
        int l = myRegions.length;
        for (int i = 0; i < l; i++) {
            float[][] coords = myRegions[i].getCoords();
            int indexA, indexB;
            float[] res;
            int cl = coords.length;

            Point2D.Float pt = polygonCenterOfMass(getPtsfromArray(coords));
            float[] centroid = new float[2];
            centroid[0] = (float)(pt.getX());
            centroid[1] = (float)(pt.getY());
            //println("x:" + centroid[0] + " y:" + centroid[1]);
            noStroke();
            fill(128, 0, 255);
            ellipse(centroid[0], centroid[1], 10, 10);


            float[][] r = new float[5][2];

            for (int j = 0; j < cl; j++) {
                indexA = (j + 1) % cl;
                indexB = (j + 2) % cl;
                r[0][0] = coords[j][0];
                r[2][0] = coords[indexA][0];
                r[4][0] = coords[indexB][0];
                r[1][0] = (float) ((r[0][0] + r[2][0]) / 2.0);
                r[3][0] = (float) ((r[2][0] + r[4][0]) / 2.0);
                r[0][1] = coords[j][1];
                r[2][1] = coords[indexA][1];
                r[4][1] = coords[indexB][1];
                r[1][1] = (float) ((r[0][1] + r[2][1]) / 2.0);
                r[3][1] = (float) ((r[2][1] + r[4][1]) / 2.0);

                stroke(0);
                noFill();
                bezier(r[1][0], r[1][1], r[2][0], r[2][1], r[2][0], r[2][1], r[3][0], r[3][1]);

                float[][]r2 = scaleAbout(0.80f,(float)(pt.getX()),(float)(pt.getY()),r);

                stroke(255,128,0);
                noFill();
                bezier(r2[1][0], r2[1][1], r2[2][0], r2[2][1], r2[2][0], r2[2][1], r2[3][0], r2[3][1]);

            }
        }
    }

    //void draw() {
    // Remove comments to print PDF

    //beginRecord(PDF,"voronoi.pdf");
    //background(255);
    //drawVoronoi();
    //endRecord();
    //exit();
    //}

    float[] scaleAbout(float xx, float yy, float x2, float y2, float f) {
        float[] moo = new float[2];
        xx = xx - x2;
        yy = yy - y2;
        xx *= f;
        yy *= f;
        xx += x2;
        yy += y2;
        moo[0] = xx;
        moo[1] = yy;
        return moo;
    }

    Point2D.Float getPoint(float polygon[][], int n) {
        final Point2D.Float pt = new Point2D.Float(polygon[n][0], polygon[n][1]);
        return pt;
    }


    public static double PolygonArea(Point2D.Float[] polygon, int N) {

        int i, j;
        double area = 0;

        for (i = 0; i < N; i++) {
            j = (i + 1) % N;
            area += polygon[i].getX() * polygon[j].getY();
            area -= polygon[i].getY() * polygon[j].getX();
        }

        area /= 2.0;
        return (Math.abs(area));
    }

    public static Point2D.Float polygonCenterOfMass(Point2D.Float[] pg) {

        if (pg == null)
            return null;

        int N = pg.length;
        Point2D.Float[] polygon = new Point2D.Float[N];

        for (int q = 0; q < N; q++)
            polygon[q] = new Point2D.Float((float)(pg[q].getX()), (float)(pg[q].getY()));

        double cx = 0, cy = 0;
        double A = PolygonArea(polygon, N);
        int i, j;

        double factor = 0;
        for (i = 0; i < N; i++) {
            j = (i + 1) % N;
            factor = (polygon[i].getX() * polygon[j].getY() - polygon[j].getX() * polygon[i].getY());
            cx += (polygon[i].getX() + polygon[j].getX()) * factor;
            cy += (polygon[i].getY() + polygon[j].getY()) * factor;
        }
        factor = 1.0 / (6.0 * A);
        cx *= factor;
        cy *= factor;
        return new Point2D.Float((int) Math.abs(Math.round(cx)), (int) Math.abs(Math
                .round(cy)));
    }
}
