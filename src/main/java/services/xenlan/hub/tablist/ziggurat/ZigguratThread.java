package services.xenlan.hub.tablist.ziggurat;

public class ZigguratThread extends Thread {

    private Ziggurat ziggurat;

    public ZigguratThread(Ziggurat ziggurat) {
        this.ziggurat = ziggurat;
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                tick();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            try {
                sleep(ziggurat.getTicks() * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tick() {
        ziggurat.getTablists().values().forEach(ZigguratTablist::update);
    }
}
