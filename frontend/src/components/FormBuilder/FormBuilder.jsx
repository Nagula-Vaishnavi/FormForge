import { useState } from 'react';
import FormField from '../FormField/FormField';
import FormPreview from '../FormPreview/FormPreview';

const FormBuilder = ({ onSave, initialFields = [] }) => {
  const [fields, setFields] = useState(initialFields);
  const [formTitle, setFormTitle] = useState('');
  const [isPreviewMode, setIsPreviewMode] = useState(false);

  const addField = (type) => {
    const newField = {
      id: Date.now(),
      type,
      label: '',
      required: false,
      options: type === 'select' || type === 'radio' || type === 'checkbox' ? [''] : [],
    };
    setFields([...fields, newField]);
  };

  const updateField = (id, updates) => {
    setFields(fields.map(field => 
      field.id === id ? { ...field, ...updates } : field
    ));
  };

  const removeField = (id) => {
    setFields(fields.filter(field => field.id !== id));
  };

  const handleSave = () => {
    onSave({ title: formTitle, fields });
  };

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div className="flex gap-2">
          <button
            onClick={() => setIsPreviewMode(false)}
            className={`px-4 py-2 rounded-lg transition font-medium ${
              !isPreviewMode
                ? 'bg-primary-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            Edit
          </button>
          <button
            onClick={() => setIsPreviewMode(true)}
            className={`px-4 py-2 rounded-lg transition font-medium ${
              isPreviewMode
                ? 'bg-primary-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
            }`}
          >
            Preview
          </button>
        </div>
        {!isPreviewMode && fields.length > 0 && (
          <button
            onClick={handleSave}
            className="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition font-medium"
          >
            Save Form
          </button>
        )}
      </div>

      {isPreviewMode ? (
        <FormPreview fields={fields} formTitle={formTitle} />
      ) : (
        <>
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Form Title
            </label>
            <input
              type="text"
              value={formTitle}
              onChange={(e) => setFormTitle(e.target.value)}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              placeholder="Enter form title"
            />
          </div>

          <div className="flex gap-2 flex-wrap">
            <button
              onClick={() => addField('text')}
              className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition"
            >
              + Text Field
            </button>
            <button
              onClick={() => addField('email')}
              className="px-4 py-2 bg-green-500 text-white rounded-lg hover:bg-green-600 transition"
            >
              + Email Field
            </button>
            <button
              onClick={() => addField('textarea')}
              className="px-4 py-2 bg-purple-500 text-white rounded-lg hover:bg-purple-600 transition"
            >
              + Text Area
            </button>
            <button
              onClick={() => addField('select')}
              className="px-4 py-2 bg-orange-500 text-white rounded-lg hover:bg-orange-600 transition"
            >
              + Dropdown
            </button>
            <button
              onClick={() => addField('radio')}
              className="px-4 py-2 bg-pink-500 text-white rounded-lg hover:bg-pink-600 transition"
            >
              + Radio
            </button>
            <button
              onClick={() => addField('checkbox')}
              className="px-4 py-2 bg-teal-500 text-white rounded-lg hover:bg-teal-600 transition"
            >
              + Checkbox
            </button>
            <button
              onClick={() => addField('date')}
              className="px-4 py-2 bg-indigo-500 text-white rounded-lg hover:bg-indigo-600 transition"
            >
              + Date
            </button>
            <button
              onClick={() => addField('number')}
              className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600 transition"
            >
              + Number
            </button>
          </div>

          <div className="space-y-4">
            {fields.map((field) => (
              <FormField
                key={field.id}
                field={field}
                onUpdate={(updates) => updateField(field.id, updates)}
                onRemove={() => removeField(field.id)}
              />
            ))}
          </div>
        </>
      )}
    </div>
  );
};

export default FormBuilder;
