package myTest;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

@SessionScoped
@ManagedBean(name = "myManagedBean")
public class MyManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SessionObj sessionObj;

	private Boolean auth;

	public String login() {
		sessionObj.setAuth(true);
		return "";
	}

	public String logout() {
		sessionObj.setAuth(false);
		return "";
	}

	public String test() {
		System.out.println("login = " + sessionObj.getAuth());
		return "logged";
	}

	public String getAuth() {
		auth = sessionObj.getAuth();
		return "";
	}

}
