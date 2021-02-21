import java.io.File;

public class file {


    public static void main(String[] args) {
        File file = new File("/img/bak");

        file.mkdirs();
        System.out.println(1);
    }

}

