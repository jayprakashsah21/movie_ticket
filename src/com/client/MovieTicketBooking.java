package com.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class MovieTicketBooking {

	private static int[] tickets = new int[50];

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ExecutorService executor = Executors.newFixedThreadPool(8);
		ReentrantLock rl = new ReentrantLock();
		BookTickets bk1 = new BookTickets(tickets, rl);
		BookTickets bk2 = new BookTickets(tickets, rl);
		executor.submit(bk1);
		executor.submit(bk2);
	}

}

class BookTickets implements Runnable {

	private int tickets[];
	private ReentrantLock rl;

	BookTickets(int tickets[], ReentrantLock rl) {
		this.tickets = tickets;
		this.rl = rl;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (rl.tryLock()) {

				try {
					int ticketNumber = bookTicket();
					if (ticketNumber != 0) {
						printTicketNumber(ticketNumber);
					} else {
						System.out.println("No Seats arre available");
					}
				} finally {
					rl.unlock();
				}
				// cancelTicket(ticketNumber);
			}
		}

	}

	private void printTicketNumber(int ticketNumber) {
		// TODO Auto-generated method stub
		System.out.println(
				"The booked ticket number is:" + ticketNumber + "by Thread:" + Thread.currentThread().getName());

	}

	private int bookTicket() {
		// TODO Auto-generated method stub
		for (int i = 0; i < tickets.length; i++) {

			if (tickets[i] == 0) {
				tickets[i] = 1;
				return i + 1;
			}
		}
		return -1;
	}

	private boolean cancelTicket(int i) {
		// TODO Auto-generated method stub

		if (tickets[i - 1] == 1) {
			tickets[i - 1] = 0;
			System.out.println("The cancelled ticket number is:" + i + "by Thread:" + Thread.currentThread().getName());
			return true;
		}

		return false;
	}

}
