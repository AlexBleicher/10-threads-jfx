package ohm.softa.a10.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.kitchen.KitchenHatchImpl;
import ohm.softa.a10.kitchen.workers.Cook;
import ohm.softa.a10.kitchen.workers.Waiter;
import ohm.softa.a10.model.Order;
import ohm.softa.a10.util.NameGenerator;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.ResourceBundle;

import static ohm.softa.a10.KitchenHatchConstants.*;

public class MainController implements Initializable {

	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;
	private final NameGenerator nameGenerator;

	@FXML
	private ProgressIndicator waitersBusyIndicator;

	@FXML
	private ProgressIndicator cooksBusyIndicator;

	@FXML
	private ProgressBar kitchenHatchProgress;

	@FXML
	private ProgressBar orderQueueProgress;

	public MainController() {
		nameGenerator = new NameGenerator();

		ArrayDeque<Order> orders = new ArrayDeque<>();
		for(int i=0; i<ORDER_COUNT; i++){
			orders.add(new Order(nameGenerator.getRandomDish()));
		}
		//TODO assign an instance of your implementation of the KitchenHatch interface
		this.kitchenHatch = new KitchenHatchImpl(10, orders);
		this.progressReporter = new ProgressReporter(kitchenHatch, COOKS_COUNT, WAITERS_COUNT, ORDER_COUNT, KITCHEN_HATCH_SIZE);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderQueueProgress.progressProperty().bindBidirectional(this.progressReporter.orderQueueProgressProperty());
		kitchenHatchProgress.progressProperty().bindBidirectional(this.progressReporter.kitchenHatchProgressProperty());
		waitersBusyIndicator.progressProperty().bindBidirectional(this.progressReporter.waitersBusyProperty());
		cooksBusyIndicator.progressProperty().bind(this.progressReporter.cooksBusyProperty());

		/* TODO create the cooks and waiters, pass the kitchen hatch and the reporter instance and start them */
		Cook cook = new Cook("Hans", progressReporter, kitchenHatch);
		Thread t1 = new Thread(cook);
		t1.start();
		Waiter waiter1 = new Waiter("Jimmy", progressReporter, kitchenHatch);
		Thread t2 = new Thread(waiter1);
		t2.start();
		Waiter waiter2 = new Waiter("Lazy Joe", progressReporter, kitchenHatch);
		Thread t3 = new Thread(waiter2);
		t3.start();
	}
}
