import { useState } from 'react';

const FormPreview = ({ fields, formTitle }) => {
  const [submissionData, setSubmissionData] = useState({});

  const handleFieldChange = (fieldId, value) => {
    setSubmissionData(prev => ({
      ...prev,
      [fieldId]: value
    }));
  };

  const renderField = (field) => {
    const commonProps = {
      id: field.id,
      value: submissionData[field.id] || '',
      onChange: (e) => handleFieldChange(field.id, e.target.value),
      required: field.required,
      disabled: true,
      className: "w-full px-4 py-2 border border-gray-300 rounded-lg bg-gray-50 focus:ring-2 focus:ring-primary-500 focus:border-transparent cursor-not-allowed"
    };

    switch (field.type) {
      case 'text':
        return (
          <input
            type="text"
            {...commonProps}
            placeholder={field.placeholder || 'Enter text'}
          />
        );

      case 'email':
        return (
          <input
            type="email"
            {...commonProps}
            placeholder={field.placeholder || 'Enter email'}
          />
        );

      case 'number':
        return (
          <input
            type="number"
            {...commonProps}
            placeholder={field.placeholder || 'Enter number'}
          />
        );

      case 'textarea':
        return (
          <textarea
            {...commonProps}
            placeholder={field.placeholder || 'Enter text'}
            rows={4}
          />
        );

      case 'select':
        return (
          <select {...commonProps}>
            <option value="">Select an option</option>
            {field.options?.filter(opt => opt.trim()).map((option, index) => (
              <option key={index} value={option}>
                {option}
              </option>
            ))}
          </select>
        );

      case 'radio':
        return (
          <div className="space-y-2">
            {field.options?.filter(opt => opt.trim()).map((option, index) => (
              <label key={index} className="flex items-center space-x-2">
                <input
                  type="radio"
                  name={field.id}
                  value={option}
                  checked={submissionData[field.id] === option}
                  onChange={(e) => handleFieldChange(field.id, e.target.value)}
                  disabled
                  className="text-primary-600 focus:ring-primary-500 cursor-not-allowed"
                />
                <span>{option}</span>
              </label>
            ))}
          </div>
        );

      case 'checkbox':
        return (
          <div className="space-y-2">
            {field.options?.filter(opt => opt.trim()).map((option, index) => {
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
                    disabled
                    className="text-primary-600 focus:ring-primary-500 cursor-not-allowed"
                  />
                  <span>{option}</span>
                </label>
              );
            })}
          </div>
        );

      case 'date':
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

  return (
    <div className="space-y-6">
      <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mb-6">
        <div className="flex items-center">
          <svg className="w-5 h-5 text-blue-600 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
          </svg>
          <span className="text-blue-800 font-medium">Preview Mode - Form is read-only</span>
        </div>
      </div>

      <div className="bg-white rounded-xl shadow-sm p-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">{formTitle || 'Untitled Form'}</h1>
          <p className="text-gray-600">This is how your form will appear to users</p>
        </div>

        <form className="space-y-6">
          {fields.length === 0 ? (
            <div className="text-center py-12 text-gray-500">
              <svg className="w-16 h-16 text-gray-300 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              <p>No fields added yet. Switch to Edit mode to add fields.</p>
            </div>
          ) : (
            fields.map((field) => (
              <div key={field.id}>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  {field.label || 'Untitled Field'}
                  {field.required && <span className="text-red-500 ml-1">*</span>}
                </label>
                {renderField(field)}
              </div>
            ))
          )}

          {fields.length > 0 && (
            <button
              type="button"
              disabled
              className="w-full py-3 bg-gray-400 text-white rounded-lg font-medium cursor-not-allowed"
            >
              Submit (Preview Only)
            </button>
          )}
        </form>
      </div>
    </div>
  );
};

export default FormPreview;
