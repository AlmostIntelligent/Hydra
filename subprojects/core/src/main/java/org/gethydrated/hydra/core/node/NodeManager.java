package org.gethydrated.hydra.core.node;

import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.api.node.Node;

/**
 * 
 * @author Christian Kulpa
 *
 */
public class NodeManager {

	private final Connector connector;
	
	private List<Node> nodes = new LinkedList<>();
	
	public NodeManager() {
		connector = new Connector();
	}
	
}
