package ca.tunestumbler.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.exceptions.AuthValidationServiceException;
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
		String stateId = sharedUtils.generateStateId(50);

		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		AuthValidationEntity authValidationEntity = new AuthValidationEntity();

		authValidationEntity.setUserEntity(userEntity);
		authValidationEntity.setStateId(stateId);
		authValidationEntity.setUserId(userEntity.getUserId());
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());

		AuthValidationEntity storedAuthValidation = authValidationRepository.save(authValidationEntity);

		AuthValidationDTO authValidationDTO = new AuthValidationDTO();
		BeanUtils.copyProperties(storedAuthValidation, authValidationDTO);

		return authValidationDTO;
	}

	@Override
	public AuthValidationDTO getAuthState(String stateId) {
		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		AuthValidationDTO existingAuthValidation = new AuthValidationDTO();
		BeanUtils.copyProperties(authValidationEntity, existingAuthValidation);
		return existingAuthValidation;
	}

	@Override
	public AuthValidationDTO updateState(String stateId, String code) {
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
		AuthValidationEntity authValiationToDelete = authValidationRepository.findByStateId(stateId);

		if (authValiationToDelete == null) {
			throw new AuthValidationServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		}

		authValidationRepository.delete(authValiationToDelete);
	}

}
