package pe.engine.error;

import java.io.PrintStream;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.APIUtil;

public class ErrorHandler extends GLFWErrorCallback implements DisposableResource{
	
	private Map<Integer, String> ERROR_CODES = APIUtil.apiClassTokens((field, value) -> 0x10000 < value && value < 0x20000, null, GLFW.class);
	private PrintStream stream;
	
	public ErrorHandler(){
		this.stream = System.err;
		
		Resources.add(this);
	}
	
	public ErrorHandler(PrintStream stream){
		this.stream = stream;
		
		Resources.add(this);
	}

	public void invoke(int error, long description) {
		String msg = getDescription(description);
		
		stream.printf("[LWJGL] %s error\n", ERROR_CODES.get(error));
		stream.println("\tDescription : " + msg);
		stream.println("\tStacktrace  :");
		StackTraceElement[] stack = Thread.currentThread().getStackTrace();
		for ( int i = 4; i < stack.length; i++ ) {
			stream.print("\t\t");
			stream.println(stack[i].toString());
		}
	}
	
	public void dispose(){
		free();
	}
}
