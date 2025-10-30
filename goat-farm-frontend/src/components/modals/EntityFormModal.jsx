import PropTypes from 'prop-types';
import { useEffect } from 'react';
import { Modal, Button, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';

function EntityFormModal({ title, fields, initialValues, validationSchema, onSubmit, show, onHide, submitLabel }) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors }
  } = useForm({
    defaultValues: initialValues,
    resolver: validationSchema ? yupResolver(validationSchema) : undefined
  });

  useEffect(() => {
    reset(initialValues);
  }, [initialValues, reset]);

  return (
    <Modal show={show} onHide={onHide} centered size="lg">
      <Form onSubmit={handleSubmit(onSubmit)}>
        <Modal.Header closeButton>
          <Modal.Title>{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="row g-3">
            {fields.map((field) => (
              <div key={field.name} className={field.col || 'col-md-6'}>
                <Form.Group>
                  <Form.Label>{field.label}</Form.Label>
                  {field.type === 'select' ? (
                    <Form.Select {...register(field.name)}>
                      <option value="">Select...</option>
                      {field.options?.map((option) => (
                        <option key={option.value} value={option.value}>
                          {option.label}
                        </option>
                      ))}
                    </Form.Select>
                  ) : field.type === 'textarea' ? (
                    <Form.Control as="textarea" rows={field.rows || 3} {...register(field.name)} />
                  ) : (
                    <Form.Control type={field.type || 'text'} step={field.step} {...register(field.name)} />
                  )}
                  {errors[field.name] && (
                    <Form.Text className="text-danger">{errors[field.name]?.message}</Form.Text>
                  )}
                </Form.Group>
              </div>
            ))}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>
            Cancel
          </Button>
          <Button type="submit" variant="primary">
            {submitLabel || 'Save'}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

EntityFormModal.propTypes = {
  title: PropTypes.string.isRequired,
  fields: PropTypes.arrayOf(PropTypes.shape({
    name: PropTypes.string.isRequired,
    label: PropTypes.string.isRequired,
    type: PropTypes.string,
    options: PropTypes.array,
    rows: PropTypes.number,
    col: PropTypes.string
  })).isRequired,
  initialValues: PropTypes.object,
  validationSchema: PropTypes.object,
  onSubmit: PropTypes.func.isRequired,
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
  submitLabel: PropTypes.string
};

EntityFormModal.defaultProps = {
  initialValues: {},
  validationSchema: null,
  submitLabel: 'Save'
};

export default EntityFormModal;
