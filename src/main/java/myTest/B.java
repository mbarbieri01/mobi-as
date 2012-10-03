package myTest;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

@Stateful
@SessionScoped
public class B {
	
	@Inject
	private SessionObj sessionObj;
	
	public void authenticate(Boolean auth) {
		sessionObj.setAuth(auth);
	}
}
