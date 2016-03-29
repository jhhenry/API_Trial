package org.henry.jmockit;

public class Collaborator {
	private DependencyOne d1;
	
	public void doSomething()
	{
		d1.doOne();
	}
}
