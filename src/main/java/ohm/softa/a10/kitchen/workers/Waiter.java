package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;

import java.util.Random;

public class Waiter implements Runnable {

	private String name;
	private ProgressReporter progressReporter;
	private KitchenHatch kitchenHatch;

	public Waiter(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch) {
		this.name = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch = kitchenHatch;
	}

	@Override
	public void run() {
		Random r = new Random();
		Dish dishToServe;
		do {
			System.out.println("Hello?");
			dishToServe = kitchenHatch.dequeueDish();
			try {
				Thread.sleep(r.nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			progressReporter.updateProgress();
		} while (kitchenHatch.getOrderCount() > 0 || dishToServe != null);
		progressReporter.notifyWaiterLeaving();
	}
}
