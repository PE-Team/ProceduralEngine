package pe.util.console;

public class ConsoleCommand {
	
	private String name, description;
	private String[] arguments;
	
	public ConsoleCommand(String name, String description){
		this.name = name;
		this.arguments = new String[0];
		this.description = description;
	}
	
	public ConsoleCommand(String name, String description, String... arguments){
		this.name = name;
		this.arguments = arguments;
		this.description = description;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String[] getArgs(){
		return this.arguments;
	}
	
	public int getArgsLength(){
		return this.arguments.length;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public boolean equals(Object obj){
		if(this.name.equals(((ConsoleCommand) obj).name)) return true;
		return false;
	}
}
