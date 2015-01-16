package com.courseraproject.mutibo;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.courseraproject.mutibo.model.AuthType;
import com.courseraproject.mutibo.model.User;
import com.courseraproject.mutibo.model.UserRepository;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@RestController
public class UserController {
	@Autowired
	private UserRepository userRepository;

	private static abstract class AuthChecker {
		protected String token;
		protected JsonObject data;
		protected UserRepository userRepository;
		protected User user;

		protected abstract AuthType getAuthType();

		protected abstract String getEndpointUrl();

		protected abstract String getTokenParamName();

		public AuthChecker(UserRepository repository, String token) {
			this.token = token;
			this.userRepository = repository;
		}

		public boolean isValidAuth() {
			data = getExternalData();
			if (data.isJsonObject()) {
				String externalId = getExternalIdFromData();
				String userName = getUserNameFromData();
				user = userRepository.findByExternalIdAndAuthType(externalId,
						getAuthType());
				if (user == null) {
					user = (User) User.create(userName, getAuthType(),
							DigestUtils.shaHex(token), externalId, "USER");
					userRepository.save(user);
				}
				return true;
			} else {
				return false;
			}
		}

		public abstract String getExternalIdFromData();

		public abstract String getUserNameFromData();

		public JsonObject getExternalData() {
			return Utils.getJSONData(Utils.getJSONString(getEndpointUrl() + "?"
					+ getTokenParamName() + "=" + token));
		}

		public User getUser() {
			return user;
		}
	}

	private static class FacebookAuthChecker extends AuthChecker {

		public FacebookAuthChecker(UserRepository repository, String token) {
			super(repository, token);
		}

		@Override
		protected String getEndpointUrl() {
			return "https://graph.facebook.com/me";
		}

		@Override
		protected String getTokenParamName() {
			return "access_token";
		}

		@Override
		public String getExternalIdFromData() {
			return data.get("id").getAsString();
		}

		@Override
		public String getUserNameFromData() {
			// not all Facebook accounts have an email, in this case we generate
			// a login name from external ID
			String name = null;
			JsonElement email = data.get("email");
			if(email != null) {
				name = email.getAsString();
			} else {
				name = String.valueOf(getAuthType()) + "_"
						+ data.get("id").getAsString();
			}
			return name;
		}

		@Override
		protected AuthType getAuthType() {
			return AuthType.FACEBOOK;
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public User registerUser(@RequestParam("app_token") String appToken,
			@RequestParam("auth_type") String authTypeStr,
			final HttpServletResponse response) {
		AuthChecker checker;
		AuthType authType = AuthType.valueOf(authTypeStr);
		// Only Facebook so far, wish I had more time for other social media!
		switch (authType) {
		default:
			checker = new FacebookAuthChecker(userRepository, appToken);
			if (checker.isValidAuth()) {
				response.setStatus(201);
				return checker.getUser();
			} else {
				throw new BadRequestException();
			}
		}
	}

}
