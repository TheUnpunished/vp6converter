package xyz.pepegamodteam.vp6converter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class main {
    public static void main(String[] args) throws IOException {
        File baseDir = new File(System.getProperty("user.dir"));
        File[] vp6s = baseDir.listFiles((dir, name) -> name.endsWith(".vp6"));
        for(File vp6: vp6s) {
            BufferedInputStream is = new BufferedInputStream(Files.newInputStream(vp6.toPath()));
            File fos = new File(vp6.getAbsolutePath().substring(0, vp6.getAbsolutePath().length() - 4) + "_converted.vp6");
            BufferedOutputStream os = new BufferedOutputStream(Files.newOutputStream(fos.toPath()));
            do {
                byte[] buf = new byte[4];
                is.read(buf);
                String headerName = new String(buf, StandardCharsets.UTF_8);
                switch (headerName) {
                    case "SCHl": {
                        headerName = "SHEN";
                        break;
                    }
                    case "SCDl": {
                        headerName = "SDEN";
                        break;
                    }
                    case "SCCl": {
                        headerName = "SCEN";
                        break;
                    }
                }
                os.write(headerName.getBytes(StandardCharsets.UTF_8));
                is.read(buf);
                os.write(buf);
                ByteBuffer buffer = ByteBuffer.wrap(buf);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buf = new byte[buffer.getInt() - 8];
                is.read(buf);
                os.write(buf);
            } while (is.available() > 0);
            os.flush();
            os.close();
            is.close();
        }
    }
}
