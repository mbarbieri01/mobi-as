package myTest;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@Stateful
@SessionScoped
public class A {

	@Inject
	private SessionObj sessionObj;
	
	public void printSomething() {
		System.out.println("auth = "+sessionObj.getAuth());
	}
}
