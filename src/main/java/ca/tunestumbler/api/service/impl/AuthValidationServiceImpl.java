package ca.tunestumbler.api.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.tunestumbler.api.exceptions.AuthValidationServiceException;
import ca.tunestumbler.api.io.entity.AuthValidationEntity;
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
		if (userRepository.findByEmail(user.getEmail()) != null) {
			throw new AuthValidationServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
		}

		String stateId = sharedUtils.generateStateId(50);

		// If stateId already exists, generate a new one
		for (int i = 5; i > 0; i--) {
			if (authValidationRepository.findByStateId(stateId) != null) {
				if (i <= 0) {
					throw new AuthValidationServiceException(
							ErrorMessages.COULD_NOT_GENERATE_UNIQUE_STATE.getErrorMessage());
				}
				stateId = sharedUtils.generateStateId(50);
			} else {
				break;
			}
		}

		AuthValidationEntity authValidationEntity = new AuthValidationEntity();
		BeanUtils.copyProperties(user, authValidationEntity);

		authValidationEntity.setStateId(stateId);
		authValidationEntity.setLastModified(sharedUtils.getCurrentTime());

		AuthValidationEntity storedAuthValidation = authValidationRepository.save(authValidationEntity);

		AuthValidationDTO newAuthValidationDTO = new AuthValidationDTO();
		BeanUtils.copyProperties(storedAuthValidation, newAuthValidationDTO);

		return newAuthValidationDTO;
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
	public AuthValidationDTO updateState(String stateId, String code, AuthValidationDTO authState) {
		AuthValidationDTO authValiationToUpdate = new AuthValidationDTO();

		AuthValidationEntity authValidationEntity = authValidationRepository.findByStateIdAndValidated(stateId, false);

		if (authValidationEntity == null) {
			throw new AuthValidationServiceException(ErrorMessages.BAD_REQUEST.getErrorMessage());
		}

		authState.setValidated(true);
		authState.setCode(code);
		authState.setLastModified(sharedUtils.getCurrentTime());

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
