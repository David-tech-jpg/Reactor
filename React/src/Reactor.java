import java.util.Scanner;

public class Reactor {
    Integer temp = 20;
    Integer targetTemp = 90;
    Boolean enabled = true;

    public static void main(String[] args) throws InterruptedException {
        final Reactor reactor = new Reactor();
        final boolean enabled = true;

        Runnable uran = new Runnable() {
            public void run() {
                while (reactor.enabled) {
                    synchronized (reactor) {
                        reactor.temp++;
                    }
                    try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        };
        
        Runnable absorb = new Runnable() {
            public void run() {
                while (reactor.enabled || reactor.temp > 0) {
                    synchronized (reactor) {
                        if (reactor.temp > reactor.targetTemp) {
                            int diff = Math.abs(reactor.targetTemp - reactor.temp);
                            if (diff < 10) reactor.temp -= 2;
                            else if (diff > 10 && diff < 20) reactor.temp -= 3;
                            else if (diff > 20) reactor.temp -= 4;
                        }
                    }
                    try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        };

        Runnable monitor = new Runnable() {
            public void run() {
                while (enabled || reactor.temp > -1) {
                    System.out.println("t:" + reactor.temp);
                    if(reactor.temp>=100){
                        System.out.println("BANG!!!");
                        System.exit(0);
                    } else if(reactor.temp<=0){
                        System.out.println("STOP");
                        System.exit(0);
                    }
                    try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            }
        };

        new Thread(uran).start();
        new Thread(absorb).start();
        new Thread(monitor).start();


        while (reactor.enabled) {
            Scanner s = new Scanner(System.in);
            if (s.hasNextLine()) {
                String line = s.nextLine();
                if (line.equals("stop")) reactor.enabled = false;
                else reactor.targetTemp = Integer.valueOf(line);
            }
            Thread.sleep(100);
        }
    }

}