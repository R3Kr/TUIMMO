package protocol;

import com.esotericsoftware.kryo.Kryo;

public class KryoFactory {
    public static Kryo register(Kryo kryo){
        kryo.register(Hello.class);
        kryo.register(Test.class);
        return kryo;
    }
}
