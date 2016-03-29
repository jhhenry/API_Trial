package org.henry.jmockit;

import mockit.Expectations;
import mockit.Mocked;

import org.testng.annotations.Test;

public class FirstTest {
	
  @Test
  public void f(@Mocked final Collaborator coll) {
	  new Expectations() {
		  {
			  
		  }
	  };
  }
}
