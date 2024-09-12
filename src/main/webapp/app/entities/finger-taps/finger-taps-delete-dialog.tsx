import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './finger-taps.reducer';

export const FingerTapsDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const fingerTapsEntity = useAppSelector(state => state.fingerTaps.entity);
  const updateSuccess = useAppSelector(state => state.fingerTaps.updateSuccess);

  const handleClose = () => {
    navigate('/finger-taps');
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(fingerTapsEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="fingerTapsDeleteDialogHeading">
        Potwierdź usunięcie
      </ModalHeader>
      <ModalBody id="fitApp.fingerTaps.delete.question">Czy na pewno chcesz usunąć Finger Taps {fingerTapsEntity.id}?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Anuluj
        </Button>
        <Button id="jhi-confirm-delete-fingerTaps" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Usuń
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default FingerTapsDeleteDialog;
