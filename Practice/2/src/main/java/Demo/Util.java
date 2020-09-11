package demo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.apache.commons.io.IOUtils;

public class Util {

	public static Optional<String> getContent(String path) {
		try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {

			return is == null
					? Optional.empty()
					: Optional.of(IOUtils.toString(is, StandardCharsets.UTF_8));

		} catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
}
