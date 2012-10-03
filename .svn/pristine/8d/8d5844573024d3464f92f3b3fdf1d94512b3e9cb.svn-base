package myTest;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;

@Stateful
@SessionScoped
public class SessionObj {
	
	private Boolean auth;
	
	@PostConstruct
	public void initialize() {
		auth = false;
	}

	public Boolean getAuth() {
		return auth;
	}

	public void setAuth(Boolean auth) {
		this.auth = auth;
	}
	
	
}
