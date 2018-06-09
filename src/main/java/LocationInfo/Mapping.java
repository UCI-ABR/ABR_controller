package LocationInfo;

/**
 * Created by Joseph on 7/15/2017.
 *  This module is used to map the environment & keep track of obstacles in it.
 */

public class Mapping extends CompassFunctions {
    //Initializes entire map to be unexplored & have obstacles on the borders
    private void initializeMap() {
        for(int y=0; y<map_size; y++) {
            for (int x=0; x<map_size; x++) {
                map[y][x] = unexplored_area;
            }
        }

        for(int i=0; i<map_size; i++) {

            //map[i][3] = obstacle;  //<-----REMOVE after testing
            //map[i][4] = obstacle;  //<-----REMOVE after testing


            map[i][0] = obstacle;
            map[0][i] = obstacle;
            map[i][map_size - 1] = obstacle;
            map[map_size - 1][i] = obstacle;
        }
    }

    //Sets light area to be bottom-left corner cells of map  <---CHANGE: light area later
    private void setLightArea() {
        for(int y=1; y<2; y++) {
            for (int x=1; x<2; x++) {
                map[y][x] = light_area;
            }
        }
    }

    //Initializes map by marking might area & unexplored areas
    void createMap() {
        initializeMap();
        setLightArea();
    }

    //Updates map w/ locations of free spaces & obstacles
    void updateMap() {
        int x = (int) measuredStateMatrix[0];
        int y = (int) measuredStateMatrix[1];

        //Log.i("kf: map position","{" + x + ", " + y + "}");

        //encountered obstacle
        if(irLeft > ProximityThreshold || irCenter > ProximityThreshold || irRight > ProximityThreshold) {
            //facing north
            if (compass >= 315 || compass < 45) {
                map[y][x] = obstacle;
            }
            //facing east
            else if (compass >= 45 && compass < 135) {
                map[y][x] = obstacle;
            }
            //facing south
            else if (compass >= 135 && compass < 225) {
                map[y][x] = obstacle;
            }
            //facing west
            else if (compass >= 225 && compass < 315) {
                map[y][x] = obstacle;
            }
        } else {    //Encountered Free Space
            //facing north
            if (compass >= 315 || compass < 45) {
                map[y][x] = free_space;
            }
            //facing east
            else if (compass >= 45 && compass < 135) {
                map[y][x] = free_space;
            }
            //facing south
            else if (compass >= 135 && compass < 225) {
                map[y][x] = free_space;
            }
            //facing west
            else if (compass >= 225 && compass < 315) {
                map[y][x] = free_space;
            }
        }
    }

    public double getCoordX() { return (int) measuredStateMatrix[0];}
    public double getCoordY() { return (int) measuredStateMatrix[1]; }

    public int getMapCellValue(int x, int y) { return map[y][x]; }

    //Returns true if facing a map cell with a known obstacle in it
    public boolean mappedObstacle() {
        int x = (int) measuredStateMatrix[0];
        int y = (int) measuredStateMatrix[1];
        int mapReader;
        boolean detector = false;
        int nextCell = 0;

        //facing north
        if (compass >= 315 || compass < 45) {
            y = (int) measuredStateMatrix[1] + nextCell;
            mapReader = getMapCellValue(x,y);

            if (mapReader == obstacle) {detector = true;}
        }
        //facing east
        else if (compass >= 45 && compass < 135) {
            x = (int) measuredStateMatrix[0] + nextCell;
            mapReader = getMapCellValue(x,y);

            if (mapReader == obstacle) {detector = true;}
        }
        //facing south
        else if (compass >= 135 && compass < 225) {
            y = (int) measuredStateMatrix[1] - nextCell;
            mapReader = getMapCellValue(x,y);

            if (mapReader == obstacle) {detector = true;}
        }
        //facing west
        else if (compass >= 225 && compass < 315) {
            x = (int)  measuredStateMatrix[0] - nextCell;
            mapReader = getMapCellValue(x,y);

            if (mapReader == obstacle) {detector = true;}
        }

        return detector;
    }
}
