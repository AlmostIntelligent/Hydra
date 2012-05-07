package org.gethydrated.hydra.core.config;

import java.io.PrintStream;
import java.util.List;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class ConfigList extends ConfigurationItem{

	public ConfigList(String _name) {
		super(_name);
		// TODO Auto-generated constructor stub
	}

	protected String name;
	protected List<ConfigurationItem> childs;
	
	public List<ConfigurationItem> getChilds(){
		return childs;
	}
	
	public ConfigurationItem getChild(String name) throws ConfigItemNotFoundException{
		
		for (ConfigurationItem c : childs)
			if (c.getName().equals(name))
				return c;
		throw new ConfigItemNotFoundException(name);
	}

	@Override
	public Boolean hasValue() {
		return false;
	}

	@Override
	public Boolean hasChildren() {
		return childs.size() != 0;
	}

	@Override
	public void saveToStream(PrintStream stream, int indent) {
		for (int i = 0; i < indent;i++)
			stream.print("\t");
		stream.print("<"+name+">");
		for(ConfigurationItem itm: childs){
			itm.saveToStream(stream, indent+1);
		}
		stream.println("</"+name+">");
	}
		
}
