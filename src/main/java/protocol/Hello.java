package protocol;

import java.util.function.Consumer;

public class Hello {
    private final Runnable runnable;
    public Hello() {
        this.runnable = new Test();
    }

    public void test(){
        runnable.run();
    }


}

