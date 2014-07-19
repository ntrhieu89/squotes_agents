package squotes.main;

import squotes.jobs.AutoPostJob;

public class MainProgram {
	public static void main(String args[]) {
		// start the auto-post thread
		Thread autoPost = new Thread(new AutoPostJob());
		autoPost.start();
	}
}
