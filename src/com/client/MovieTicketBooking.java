package com.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MovieTicketBooking {

	private final static int NUMBER_OF_TICKETS=10;
	private static int[] tickets = new int[NUMBER_OF_TICKETS];

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ExecutorService executor = Executors.newFixedThreadPool(8);
	//	ReentrantLock rl = new ReentrantLock();

		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
		Lock readLock = readWriteLock.readLock();
		Lock writeLock = readWriteLock.writeLock();

		BookTickets bk1 = new BookTickets(tickets, readLock, writeLock);
		BookTickets bk2 = new BookTickets(tickets, readLock, writeLock);

		executor.submit(bk1);
		executor.submit(bk2);
	}

}

class BookTickets implements Runnable {

	private int tickets[];
//	private ReentrantLock rl;
	private Lock readLock;
	private Lock wrteLock;

	BookTickets(int tickets[], Lock readLock, Lock wrteLock) {
		this.tickets = tickets;
		this.readLock = readLock;
		this.wrteLock = wrteLock;
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
			int ticketNumber = -1;
			try {
				wrteLock.lock();
				ticketNumber = bookTicket();
			} finally {
				wrteLock.unlock();
			}
			try {
				readLock.lock();
				if (ticketNumber != 0) {
					printTicketNumber(ticketNumber);
				} else {
					System.out.println("No Seats arre available");
				}
			} finally {
				readLock.unlock();
			}
			//can be called based on the user input with read and write lock
			// cancelTicket(ticketNumber);
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
