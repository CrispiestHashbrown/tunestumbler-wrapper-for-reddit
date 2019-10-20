package ca.tunestumbler.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;

import ca.tunestumbler.api.exceptions.AuthValidationServiceException;
import ca.tunestumbler.api.exceptions.RecordAlreadyExistsException;
import ca.tunestumbler.api.io.entity.AuthValidationEntity;
import ca.tunestumbler.api.io.entity.UserEntity;
import ca.tunestumbler.api.io.repositories.AuthValidationRepository;
import ca.tunestumbler.api.io.repositories.UserRepository;
import ca.tunestumbler.api.service.AuthValidationService;
import ca.tunestumbler.api.shared.SharedUtils;
import ca.tunestumbler.api.shared.dto.AuthValidationDTO;
import ca.tunestumbler.api.shared.dto.UserDTO;
import ca.tunestumbler.api.ui.model.response.ErrorMessages;

@Service
public class AuthValidationServiceImpl implements AuthValidationService {

	@Autowired
	AuthValidationRepository authValidationRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	SharedUtils sharedUtils;

	@Override
	public AuthValidationDTO createAuthState(UserDTO user) {
		UserEntity userEntity = userRepository.findByUserId(user.getUserId());

		if (userEntity == null) {
			throw new RecordAlreadyExistsException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		String stateId = sharedUtils.generateStateId(50);
		String authorizationUrl = "https://www.reddit.com/api/v1/authorize" +
				"?client_id=VvztT4RO6UUmAA" +
				"&response_type=code" +
				"&state=" + stateId +
				"&redirect_uri=https://www.tunestumbler.com/" +
				"&duration=permanent" +
				"&scope=read,history,vote,save,account,subscribe,mysubreddits";
		
		AuthValidationEntity authValidationEntity = new AuthValidationEntity();

		authValidationEntity.setUserEntity(userEntity);
		authValidationEntity.setStateId(stateId);
		authValidationEntity.setUserId(userEntity.getUserId());
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());
		authValidationEntity.setAuthorizationUrl(authorizationUrl);

		AuthValidationEntity storedAuthValidation = authValidationRepository.save(authValidationEntity);

		AuthValidationDTO authValidationDTO = new AuthValidationDTO();
		BeanUtils.copyProperties(storedAuthValidation, authValidationDTO);

		return authValidationDTO;
	}

	@Override
	public AuthValidationDTO getAuthState(String stateId) {
		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		AuthValidationDTO existingAuthValidation = new AuthValidationDTO();
		BeanUtils.copyProperties(authValidationEntity, existingAuthValidation);
		return existingAuthValidation;
	}

	@Override
	public AuthValidationDTO updateState(String stateId, String code) {
		if (Strings.isNullOrEmpty(stateId) || Strings.isNullOrEmpty(code)) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		AuthValidationDTO authValiationToUpdate = new AuthValidationDTO();

		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		authValidationEntity.setValidated(true);
		authValidationEntity.setCode(code);
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());

		AuthValidationEntity updatedAuthValidation = authValidationRepository.save(authValidationEntity);
		BeanUtils.copyProperties(updatedAuthValidation, authValiationToUpdate);

		return authValiationToUpdate;
	}

	@Override
	public void deleteState(String stateId) {
		if (Strings.isNullOrEmpty(stateId)) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		AuthValidationEntity authValiationToDelete = authValidationRepository.findByStateId(stateId);

		if (authValiationToDelete == null) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		authValidationRepository.delete(authValiationToDelete);
	}

}
