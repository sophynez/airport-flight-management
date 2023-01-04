package airport_flight_management;

import java.util.Comparator;

public class RoutageComparator implements Comparator<Routing> {

	@Override
	public int compare(Routing o1, Routing o2) {
		if (o1.getDistence() < o2.getDistence())
            return -1;
        else if (o1.getDistence() > o2.getDistence())
            return 1;
                        return 0;
	}

}