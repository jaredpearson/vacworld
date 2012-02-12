package jaredpearson;

public class LoggerFactory {
	public static Log getLogger(Class<?> clazz) {
		return new Log(){
			@Override
			public void debug(String message) {
				System.out.println(message);
			}
		};
	}
}
