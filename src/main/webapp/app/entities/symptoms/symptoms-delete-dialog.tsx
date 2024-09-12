import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './symptoms.reducer';

export const SymptomsDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const symptomsEntity = useAppSelector(state => state.symptoms.entity);
  const updateSuccess = useAppSelector(state => state.symptoms.updateSuccess);

  const handleClose = () => {
    navigate('/symptoms');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(symptomsEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="symptomsDeleteDialogHeading">
        Potwierdź usunięcie
      </ModalHeader>
      <ModalBody id="fitApp.symptoms.delete.question">Czy na pewno chcesz usunąć Symptoms {symptomsEntity.id}?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Anuluj
        </Button>
        <Button id="jhi-confirm-delete-symptoms" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Usuń
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SymptomsDeleteDialog;
