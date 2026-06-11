import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { api } from '../../services/api';

const PublicForm = () => {
  const { publicUrl } = useParams();
  const [formData, setFormData] = useState(null);
  const [fields, setFields] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  const [submissionData, setSubmissionData] = useState({});
  const [submitted, setSubmitted] = useState(false);

  useEffect(() => {
    fetchPublicForm();
  }, [publicUrl]);

  const fetchPublicForm = async () => {
    try {
      setLoading(true);
      const response = await api.get(`/public/forms/${publicUrl}`);
      setFormData(response.data.form);
      setFields(response.data.fields);
      setError(null);
    } catch (err) {
      console.error('Error fetching public form:', err);
      if (err.response?.status === 404) {
        setError('This form is not available or has been unpublished.');
      } else {
        setError('Failed to load the form. Please try again later.');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleFieldChange = (fieldId, value) => {
    setSubmissionData(prev => ({
      ...prev,
      [fieldId]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);

    try {
      await api.post(`/public/forms/${publicUrl}/submit`, {
        fieldValues: submissionData
      });
      setSubmitted(true);
    } catch (err) {
      console.error('Error submitting form:', err);
      alert('Failed to submit form. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const renderField = (field) => {
    const commonProps = {
      id: field.id,
      value: submissionData[field.id] || '',
      onChange: (e) => handleFieldChange(field.id, e.target.value),
      required: field.required,
      className: "w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
    };

    const options = field.options ? JSON.parse(field.options) : [];

    switch (field.fieldType) {
      case 'TEXT':
        return (
          <input
            type="text"
            {...commonProps}
            placeholder={field.placeholder || ''}
          />
        );

      case 'EMAIL':
        return (
          <input
            type="email"
            {...commonProps}
            placeholder={field.placeholder || ''}
          />
        );

      case 'NUMBER':
        return (
          <input
            type="number"
            {...commonProps}
            placeholder={field.placeholder || ''}
          />
        );

      case 'TEXTAREA':
        return (
          <textarea
            {...commonProps}
            placeholder={field.placeholder || ''}
            rows={4}
          />
        );

      case 'DROPDOWN':
        return (
          <select {...commonProps}>
            <option value="">Select an option</option>
            {options.map((option, index) => (
              <option key={index} value={option}>
                {option}
              </option>
            ))}
          </select>
        );

      case 'RADIO':
        return (
          <div className="space-y-2">
            {options.map((option, index) => (
              <label key={index} className="flex items-center space-x-2">
                <input
                  type="radio"
                  name={field.id}
                  value={option}
                  checked={submissionData[field.id] === option}
                  onChange={(e) => handleFieldChange(field.id, e.target.value)}
                  required={field.required}
                  className="text-primary-600 focus:ring-primary-500"
                />
                <span>{option}</span>
              </label>
            ))}
          </div>
        );

      case 'CHECKBOX':
        return (
          <div className="space-y-2">
            {options.map((option, index) => {
              const currentValues = submissionData[field.id] || [];
              return (
                <label key={index} className="flex items-center space-x-2">
                  <input
                    type="checkbox"
                    value={option}
                    checked={currentValues.includes(option)}
                    onChange={(e) => {
                      const newValues = e.target.checked
                        ? [...currentValues, option]
                        : currentValues.filter(v => v !== option);
                      handleFieldChange(field.id, newValues);
                    }}
                    required={field.required && currentValues.length === 0}
                    className="text-primary-600 focus:ring-primary-500"
                  />
                  <span>{option}</span>
                </label>
              );
            })}
          </div>
        );

      case 'DATE':
        return (
          <input
            type="date"
            {...commonProps}
          />
        );

      default:
        return <div className="text-gray-500">Unsupported field type</div>;
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="bg-white rounded-xl shadow-sm p-8 max-w-md text-center">
          <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <h3 className="text-xl font-semibold text-gray-900 mb-2">Form Not Available</h3>
          <p className="text-gray-600">{error}</p>
        </div>
      </div>
    );
  }

  if (submitted) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="bg-white rounded-xl shadow-sm p-8 max-w-md text-center">
          <svg className="w-16 h-16 text-green-500 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
          </svg>
          <h3 className="text-xl font-semibold text-gray-900 mb-2">Thank You!</h3>
          <p className="text-gray-600">Your response has been submitted successfully.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4">
      <div className="max-w-2xl mx-auto">
        <div className="bg-white rounded-xl shadow-sm p-8">
          <div className="mb-8">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{formData?.title}</h1>
            {formData?.description && (
              <p className="text-gray-600">{formData.description}</p>
            )}
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            {fields.map((field) => (
              <div key={field.id}>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  {field.label}
                  {field.required && <span className="text-red-500 ml-1">*</span>}
                </label>
                {renderField(field)}
              </div>
            ))}

            <button
              type="submit"
              disabled={submitting}
              className="w-full py-3 bg-primary-600 text-white rounded-lg hover:bg-primary-700 transition font-medium disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {submitting ? 'Submitting...' : 'Submit'}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default PublicForm;
