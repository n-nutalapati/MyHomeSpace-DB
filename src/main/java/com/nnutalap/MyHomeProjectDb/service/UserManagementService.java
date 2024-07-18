package com.nnutalap.MyHomeProjectDb.service;

import com.nnutalap.MyHomeProjectDb.dto.ReqRes;
import com.nnutalap.MyHomeProjectDb.models.MyUsers;
import com.nnutalap.MyHomeProjectDb.repository.MhpUsersDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

	@Autowired
	private MhpUsersDbRepo userRepo;

	@Autowired
	private JWTUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MongoSequenceGenerator seqGenerator;

	public ReqRes register(ReqRes registrationRequest) {
		ReqRes resp = new ReqRes();

		try {

			MyUsers myUser = new MyUsers();
			myUser.setId(seqGenerator.getSeqUserId(MyUsers.SEQUENCE_NAME));
			myUser.setEmail(registrationRequest.getEmail());
			myUser.setName(registrationRequest.getName());
			myUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
			myUser.setRole(registrationRequest.getRole());

			MyUsers myUsersResult = userRepo.save(myUser);

			if (myUsersResult.getId() > 0 ) {
				resp.setMyUsers(myUsersResult);
				resp.setMessage("User Saved Successfully");
				resp.setStatusCode(200);
			}
		} catch (Exception e) {
			resp.setStatusCode(500);
			resp.setError(e.getMessage());
			return resp;
		}

		return resp;
	}

	public ReqRes login(ReqRes loginRequest) {
		ReqRes response = new ReqRes();
		try {

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			var user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow();
			var jwt = jwtUtils.generateToken(user);
			var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

			response.setStatusCode(200);
			response.setToken(jwt);
			response.setId(user.getId());
			response.setRole(user.getRole());
			response.setRefreshToken(refreshToken);
			response.setExpirationTime("24Hrs");
			response.setMessage("Successfully Logged In");

		} catch (Exception e) {

			response.setStatusCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
		return response;
	}

	public ReqRes refreshToken(ReqRes refreshTokenRequest) {
		ReqRes response = new ReqRes();

		try {

			String MyEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
			MyUsers users = userRepo.findByEmail(MyEmail).orElseThrow();
			if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
				var jwt = jwtUtils.generateToken(users);
				response.setStatusCode(200);
				response.setToken(jwt);
				response.setRefreshToken(refreshTokenRequest.getToken());
				response.setExpirationTime("24Hr");
				response.setMessage("Successfully Refreshed Token");
			}
			response.setStatusCode(200);
			return response;
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	public ReqRes getAllUsers() {
		ReqRes reqRes = new ReqRes();

		try {

			List<MyUsers> result = userRepo.findAll();
			if (!result.isEmpty()) {
				reqRes.setMyUsersList(result);
				reqRes.setStatusCode(200);
				reqRes.setMessage("Successful");
			} else {
				reqRes.setStatusCode(404);
				reqRes.setMessage("No users found");
			}
			return reqRes;
		} catch (Exception e) {
			reqRes.setStatusCode(500);
			reqRes.setMessage("Error Occured: " + e.getMessage());
			return reqRes;
		}
	}
	
	 public ReqRes getUsersById(Integer id) {
	        ReqRes reqRes = new ReqRes();
	        try {
	            MyUsers usersById = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
	            reqRes.setMyUsers(usersById);
	            reqRes.setStatusCode(200);
	            reqRes.setMessage("Users with id '" + id + "' found successfully");
	        } catch (Exception e) {
	            reqRes.setStatusCode(500);
	            reqRes.setMessage("Error occurred: " + e.getMessage());
	        }
	        return reqRes;
	    }


	    public ReqRes deleteUser(Integer userId) {
	        ReqRes reqRes = new ReqRes();
	        try {
	            Optional<MyUsers> userOptional = userRepo.findById(userId);
	            if (userOptional.isPresent()) {
	                userRepo.deleteById(userId);
	                reqRes.setStatusCode(200);
	                reqRes.setMessage("User deleted successfully");
	            } else {
	                reqRes.setStatusCode(404);
	                reqRes.setMessage("User not found for deletion");
	            }
	        } catch (Exception e) {
	            reqRes.setStatusCode(500);
	            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
	        }
	        return reqRes;
	    }

	    public ReqRes updateUser(Integer userId, MyUsers updatedUser) {
	        ReqRes reqRes = new ReqRes();
	        try {
	            Optional<MyUsers> userOptional = userRepo.findById(userId);
	            if (userOptional.isPresent()) {
	                MyUsers existingUser = userOptional.get();
	                existingUser.setEmail(updatedUser.getEmail());
	                existingUser.setName(updatedUser.getName());
	                existingUser.setRole(updatedUser.getRole());

	                // Check if password is present in the request
	                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
	                    // Encode the password and update it
	                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
	                }

	                MyUsers savedUser = userRepo.save(existingUser);
	                reqRes.setMyUsers(savedUser);
	                reqRes.setStatusCode(200);
	                reqRes.setMessage("User updated successfully");
	            } else {
	                reqRes.setStatusCode(404);
	                reqRes.setMessage("User not found for update");
	            }
	        } catch (Exception e) {
	            reqRes.setStatusCode(500);
	            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
	        }
	        return reqRes;
	    }


	    public ReqRes getMyInfo(String email){
	        ReqRes reqRes = new ReqRes();
	        try {
	            Optional<MyUsers> userOptional = userRepo.findByEmail(email);
	            if (userOptional.isPresent()) {
	                reqRes.setMyUsers(userOptional.get());
	                reqRes.setStatusCode(200);
	                reqRes.setMessage("successful");
	            } else {
	                reqRes.setStatusCode(404);
	                reqRes.setMessage("User not found");
	            }

	        }catch (Exception e){
	            reqRes.setStatusCode(500);
	            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
	        }
	        return reqRes;

	    }
}
