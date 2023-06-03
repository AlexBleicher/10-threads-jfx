package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

public class Cook implements Runnable {

	private String name;
	private ProgressReporter progressReporter;
	private KitchenHatch kitchenHatch;

	public Cook(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch) {
		this.name = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch = kitchenHatch;
	}

	@Override
	public void run() {
		while (kitchenHatch.getOrderCount() > 0) {
			Order o = kitchenHatch.dequeueOrder();
			System.out.println(kitchenHatch.getDishesCount());

			Dish orderedDish = new Dish(o.getMealName());
			kitchenHatch.enqueueDish(orderedDish);
			try {
				Thread.sleep(orderedDish.getCookingTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			progressReporter.updateProgress();
		}
		progressReporter.notifyCookLeaving();
	}
}
