package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.ArrayDeque;
import java.util.Deque;

public class KitchenHatchImpl implements KitchenHatch{
	private int maxDishes;
	private Deque<Order> orderDeque;
	private Deque<Dish> dishDeque=new ArrayDeque<>();

	private int cooksCurrentlyWorking=0;

	public int getCooksCurrentlyWorking() {
		return cooksCurrentlyWorking;
	}

	public KitchenHatchImpl(int maxDishes, Deque<Order> orderDeque) {
		this.maxDishes = maxDishes;
		this.orderDeque = orderDeque;
	}

	@Override
	public int getMaxDishes() {
		return maxDishes;
	}

	@Override
	public Order dequeueOrder(long timeout) {
		synchronized (this) {
			if(orderDeque.size()>=1) {
				cooksCurrentlyWorking++;
				return orderDeque.pop();
			}
		}
		return null;
	}

	@Override
	public int getOrderCount() {
		synchronized (this) {
			return orderDeque.size();
		}
	}

	@Override
	public Dish dequeueDish(long timeout){
		synchronized (dishDeque) {
			while(dishDeque.size() ==0){
				try {
					dishDeque.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Dish pop = dishDeque.pop();
			System.out.println(pop.getMealName() + "Taken out!");
			dishDeque.notifyAll();
			return pop;
		}
	}

	@Override
	public void enqueueDish(Dish m) {
		synchronized (dishDeque){
			while(dishDeque.size() == maxDishes){
				try {
					dishDeque.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			dishDeque.add(m);
			cooksCurrentlyWorking--;
			dishDeque.notifyAll();
		}
	}

	@Override
	public int getDishesCount() {
		synchronized (this) {
			return dishDeque.size();
		}
	}
}
