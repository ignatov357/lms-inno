package testutils;

public class CPUStressTest {

    public static void main(String[] args){
        for (int i = 0; i < 4; i++) {
            new Thread( ()->{while(true){}}).start();
        }
    }

}
